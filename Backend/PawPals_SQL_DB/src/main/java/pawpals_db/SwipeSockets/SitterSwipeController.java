
package pawpals_db.SwipeSockets;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pawpals_db.Chat.Conversation;
import pawpals_db.Buyers.Buyer;
import pawpals_db.Chat.ConversationRepository;
import pawpals_db.Sitters.Sitter;
import pawpals_db.Sitters.SitterRepository;
import pawpals_db.Users.BasicUser;
import pawpals_db.Users.BasicUserRepository;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

import org.slf4j.Logger;
import pawpals_db.Users.BasicUserTransactionalService;

import static java.lang.Integer.valueOf;

/**
 * Handles the sitter swipe queue WebSocket. Finds and sends sitters to the user's swipe queue.
 * Swipes in the affirmative open a chat with the sitter.
 * Swipes in the negative simply provide a new sitter.
 */
@ServerEndpoint(value = "/swipe/sitters/{username}")
@Component
public class SitterSwipeController {

    private static  Map <Session, String> sessionUsernameMap = new Hashtable<>();

    private static Map <String, Session> usernameSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(SitterSwipeController.class);

    private static BasicUserRepository basicUserRepository;
    @Autowired
    public void setBasicUserRepository(BasicUserRepository bup) { basicUserRepository = bup; }

    private static SitterRepository sitterRepository;
    @Autowired
    public void setSitterRepository (SitterRepository pr) { sitterRepository = pr; }

    private static ConversationRepository conversationRepository;
    @Autowired
    public void setConversationRepository(ConversationRepository cr) { conversationRepository = cr; }

    private static BasicUserTransactionalService BUTtS;
    @Autowired
    public void setBUTS(BasicUserTransactionalService buts) { BUTtS = buts; }

    /**
     * Upon a request from a client, opens a WebSocket connection with them and
     * stores the session with an associated username. Also sends the initial dog
     * to populate the queue for swiping.
     *
     * @param session
     * @param username
     * @throws IOException
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {

        logger.info("[onOpen] " + username);

        // Set hashmaps
        sessionUsernameMap.put(session, username);

        usernameSessionMap.put(username, session);

        BasicUser b = basicUserRepository.findByUsername(username);

        // Send initial dog to queue.
        sendSwipeReply(b);
    }

    /**
     * Handles each swipe the user does. Since it only seems to work with one parameter,
     * this expects a String containing the following data separated by whitespace:
     * the user ID, the last swiped Sitter's ID, and a 1/0 (true/false) that represents a
     * swipe yes or no. Based on this, may open a chat with the Sitter
     * and will return a new Sitter for the queue.
     *
     * @param session
     * @param data - the String of data.
     * @throws IOException
     */
    @OnMessage
    public void onSwipe(Session session, String data) throws IOException {
        // data = "userID dogID key", which I need to parse into 3 separate ints
        String uid = "";
        int i = 0;
        while (data.charAt(i) != ' ') {
            uid += data.charAt(i++);
        }
        String sid = "";
        ++i;
        while (data.charAt(i) != ' ') {
            sid += data.charAt(i++);
        }
        char key = data.charAt(++i);
        int userId = valueOf(uid);
        int sitterId = valueOf(sid);

        BasicUser buyer = BUTtS.getBasicUserWithConvos(userId);
        if (key == '1') { // The buyer swiped right, so create a new conversation.
            BasicUser sitter = BUTtS.getBasicUserWithConvos(sitterRepository.findById(sitterId).getAccount().getId());

            // TODO: make sure it doesn't make duplicate conversations
            // This probably means adding a new field to the Conversation class, subject.
            // Subject is the pet's or sitter's name.
            // Move into a helper method.

            // The buyer's conversation with the sitter.
            Conversation buyerConvo = new Conversation(sitter.getUsername(), sitter.getId(), "Sitter", buyer);
            conversationRepository.save(buyerConvo);
            buyer.addConversation(buyerConvo);
            basicUserRepository.save(buyer);

            // The sitter's conversation with the buyer.
            Conversation sitterConvo = new Conversation(buyer.getUsername(), buyer.getId(), "Buyer", sitter);
            conversationRepository.save(sitterConvo);
            sitter.addConversation(sitterConvo);
            basicUserRepository.save(sitter);
        }
        // else, take no additional action

        // log the action
        String username = sessionUsernameMap.get(session);
        logger.info("[onSwipe] " + data);
        //logger.info("[onSwipe] " + buyer.getId() + ": " + dog.getId() + ": " + key);

        // Get next sitter and send
        sendSwipeReply(buyer);
    }

    /**
     * Sends the Sitter object back to the user. Does not send a BasicUser's
     * own Sitter profile if they have one.
     *
     * @param b - The user in the session.
     */
    private void sendSwipeReply(BasicUser b) {

        Sitter s = queueSitter(b);

        try {
            logger.info("[SENT]" + s.toString());
            usernameSessionMap.get(b.getUsername()).getBasicRemote().sendText(s.toString());
        } catch (IOException e) {
            logger.info("[DM Exception] " + e.getMessage());
        } catch (Exception e) {
            logger.info("[Other Exception] " + e.getMessage() + s.toString());

        }
    }

    /**
     * Finds a new available Sitter to be added to a user's swiping queue.
     *
     * @param basicUser - the user who will receive the Sitter.
     * @return - The Sitter to be sent to the user.
     */
    public static Sitter queueSitter(BasicUser basicUser) {
        //long tableSize = sitterRepository.count(); // there seems to be an exists() method...
        Random rand = new Random(); // To find a random profile.
        int upperBound = (int)sitterRepository.count();
        //int sitterId; // ID of the next sitter.
        Sitter upNext = null;

        Buyer buyer = basicUser.getBuyerProfile();

        if (basicUser.isSitter()) { // exclude their own sitter profile
            int sid = basicUser.getSitterProfile().getId();
            while (upNext == null) {
                upNext = sitterRepository.findById(rand.nextInt(upperBound) + 1);
                if (upNext.getId() != sid) {
                    break;
                }
            }
        }

        else { // No need for additional check
            while (upNext == null) {
                upNext = sitterRepository.findById(rand.nextInt(upperBound) + 1);
            }
            }

        return upNext;
    }


    /**
     * Closes the given session.
     *
     * @param session - The session to be closed.
     * @throws IOException
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // server side log
        logger.info("[onClose] " + username);

        // remove user from memory mappings
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);

    }

    /**
     * If there is an error, it is handled here.
     *
     * @param session - the session where the error occurred.
     * @param throwable - the error or exception.
     */
    @OnError
    public void onError(Session session, Throwable throwable) {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // error handling
        logger.info("[onError]" + username + ": " + throwable.getMessage());
    }
}

