package pawpals_db.Chat;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pawpals_db.Users.BasicUser;
import pawpals_db.Users.BasicUserRepository;

/**
 * Controls the WebSocket interactions for the chat feature.
 *
 * @author Zane Lenz
 */
@Controller      // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/chat/{myId}/{otherId}")  // this is Websocket url ///chat/{username}
public class ChatSocket {

    // cannot autowire static directly (instead we do it by the below
    // method
    private static MessageRepository msgRepo;

    private static BasicUserRepository userRepo;

    int senderId;
    int receiverId;

    String myUsername;

    String otherUsername;

    /*
     * Grabs the MessageRepository singleton from the Spring Application
     * Context.  This works because of the @Controller annotation on this
     * class and because the variable is declared as static.
     * There are other ways to set this. However, this approach is
     * easiest.
     */
    @Autowired
    public void setMessageRepository(MessageRepository repo) {
        msgRepo = repo;  // we are setting the static variable
    }

    @Autowired
    public void setBasicUserRepository(BasicUserRepository basicRepo) { userRepo = basicRepo; }

    // Store all socket session and their corresponding username.
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap2 = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(ChatSocket.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("myId") int myId, @PathParam("otherId") int otherId) //@PathParam("username") String username
            throws IOException {

        //senderId = userRepo.findByUsername(username).getId();
                senderId = myId;
                receiverId = otherId;
                String username = userRepo.findById(myId).getUsername();
                //String otherUsername = userRepo.findById(otherId).getUsername();
                Session otherSession = session;
                myUsername = userRepo.findById(myId).getUsername();
                otherUsername = userRepo.findById(otherId).getUsername();

        //logger.info("Entered into Open");

        // store connecting user information
        sessionUsernameMap.put(session, myUsername);
        usernameSessionMap.put(myUsername, session);
        usernameSessionMap2.put(otherUsername, otherSession);

                logger.info(sessionUsernameMap.get(session) + " Connected Successfully");

        //Send chat history to the newly connected user
        sendMessageToPArticularUser(myUsername, getChatHistory(senderId, receiverId));
       // sendMessageToPArticularUser(otherUsername, getChatHistory(senderId, receiverId));
        // broadcast that new user joined
        //String message = "User:" + username + " has Joined the pawpals_db.Chat";
        //broadcast(message); //MAYBE USE THIS FOR DISPLAYING CHATS?
    }


    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

        String userMsg = "@" + otherUsername + " " + message;
        // Handle new messages
        logger.info("Entered into Message: Got Message:" + userMsg);
        String username = sessionUsernameMap.get(session);

        // Direct message to a user using the format "@username <message>"
        if (userMsg.startsWith("@")) {
            //String destUsername = message.split(" ")[0].substring(1);
            //receiverId = userRepo.findByUsername(destUsername).getId();

            // send the message to the sender and receiver
            sendMessageToPArticularUser(otherUsername, "[DM] " + username + ": " + userMsg);
           // sendMessageToPArticularUser(username, "[DM] " + username + ": " + userMsg);
            logger.info(otherUsername + " Received Message");

            // Saving chat history to repository
            msgRepo.save(new Message(username, message, senderId, receiverId));
        }
        /* We could use this for admin messaging all users, but doesn't seem necessary for basic users
        else { // broadcast
            broadcast(username + ": " + message);
        } */
    }


    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into Close");

        // remove the user connection information
        String username = sessionUsernameMap.get(session);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);

        // broadcase that the user disconnected
        String message = username + " disconnected";
        broadcast(message);
    }


    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        logger.info("Entered into Error");
        throwable.printStackTrace();
    }


    private void sendMessageToPArticularUser(String username, String message) {
//        try {
//            usernameSessionMap.get(username).getBasicRemote().sendText(message);
//        }
//        catch (IOException e) {
//            logger.info("Exception: " + e.getMessage().toString());
//            e.printStackTrace();
//        }
        if(username == myUsername){
            try {
                usernameSessionMap.get(username).getBasicRemote().sendText(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            try {
                usernameSessionMap2.get(username).getBasicRemote().sendText(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            }
            catch (IOException e) {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }

        });

    }


    // Gets the pawpals_db.Chat history from the repository
    private String getChatHistory(int senderId, int receiverId) {
        List<Message> allMsgs = msgRepo.findAll();

        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (i = 0; i < allMsgs.size(); ++i) {
            Message msg = allMsgs.get(i);
            int user = msg.getSenderId();
            int otherUser = msg.getReceiverId();

            if ((otherUser == receiverId || otherUser == senderId) && (user == senderId || user == receiverId)) {
                sb.append(msg.getUserName() + ": " + msg.getContent() + "\n");
            }
        /*// convert the list to a string
        StringBuilder sb = new StringBuilder();
        if(messages != null && messages.size() != 0) {
            for (Message message : messages) {
                sb.append(message.getUserName() + ": " + message.getContent() + "\n");
            }*/
        }
        return sb.toString();
    }

} // end of Class
