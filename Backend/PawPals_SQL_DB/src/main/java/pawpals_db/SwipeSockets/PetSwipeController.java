package pawpals_db.SwipeSockets;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pawpals_db.Chat.Conversation;
import pawpals_db.Buyers.Buyer;
import pawpals_db.Chat.ConversationRepository;
import pawpals_db.Pets.Pet;
import pawpals_db.Pets.PetEncoder;
import pawpals_db.Pets.PetRepository;
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
 * Handles the swipe queue WebSocket. Finds and sends dogs to the user's swipe queue.
 * Swipes in the affirmative open a chat with the owner of the dog.
 * Swipes in the negative simply provide a new dog.
 */
@ServerEndpoint(value = "/swipe/pets/{username}", encoders = PetEncoder.class) // This directly follows "localhost:8080"
@Component
public class PetSwipeController {

    private static  Map <Session, String> sessionUsernameMap = new Hashtable<>();

    private static Map <String, Session> usernameSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(PetSwipeController.class);

    private static BasicUserRepository basicUserRepository;
    @Autowired
    public void setBasicUserRepository(BasicUserRepository bup) { basicUserRepository = bup; }

    private static PetRepository petRepository;
    @Autowired
    public void setPetRepository(PetRepository pr) { petRepository = pr; }

    private static ConversationRepository conversationRepository;
    @Autowired
    public void setConvoRepository(ConversationRepository cr) { conversationRepository = cr; }

    private static BasicUserTransactionalService BUTtS;
    @Autowired
    public void setBUTS(BasicUserTransactionalService buts) { BUTtS = buts; }

    // Steps to swiping:
    // 1. Client sends GET request with exeId and userId in body. - "/pets/queue"
    // 2. Server returns a List of Pets to start queue
    // 3. The client swipes "key" true/false and sends it and "dog".
    // 4. Server replies with another pet.
    // 5. Repeat ad nauseam.
    // Url will include the user ID
    // Receive "Key" T/F and a Pet (including its ID)
    // For now, nothing will be done with these. Might be useful to have a list of dogs they
    // have viewed to avoid repeats.
    // Reply with another dog object

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
     * the user ID, the last swiped Pet's ID, and a 1/0 (true/false) that represents a
     * swipe yes or no. Based on this, may open a chat with the Pet's owner (found by dog ID)
     * and will return a new Pet for the queue.
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
        String pid = "";
        ++i;
        while (data.charAt(i) != ' ') {
            pid += data.charAt(i++);
        }
        char key = data.charAt(++i);
        int userId = valueOf(uid);
        int petId = valueOf(pid);

        BasicUser buyer = BUTtS.getBasicUserWithConvos(userId);
        if (key == '1') { // The buyer swiped right, so create a new conversation.
            // TODO: Make sure it doesn't make duplicate conversations
            BasicUser seller = BUTtS.getBasicUserWithConvos(petRepository.findById(petId).getOwner().getAccount().getId());
            // The buyer's conversation with the seller.
            Conversation buyerConvo = new Conversation(seller.getUsername(), seller.getId(), "Seller", buyer);
            conversationRepository.save(buyerConvo);
            buyer.addConversation(buyerConvo);
            basicUserRepository.save(buyer);
            // The seller's conversation with the buyer.
            Conversation sellerConvo = new Conversation(buyer.getUsername(), buyer.getId(), "Buyer", seller);
            conversationRepository.save(sellerConvo);
            seller.addConversation(sellerConvo);
            basicUserRepository.save(seller);
        }
        // else, take no additional action
        
        // log the action
        String username = sessionUsernameMap.get(session);
        logger.info("[onSwipe] " + data);
        //logger.info("[onSwipe] " + buyer.getId() + ": " + dog.getId() + ": " + key);
        
        // Get next pet and send
        sendSwipeReply(buyer);
    }

    /**
     * Sends the Pet object back to the user, b. B's dogs are excluded
     * from being returned.
     *
     * @param b - The user in the session.
     */
    private void sendSwipeReply(BasicUser b) {
        Pet dog = queuePet(b);

        try {
            logger.info("[PETSENT]" + dog.toString());
            usernameSessionMap.get(b.getUsername()).getBasicRemote().sendText(dog.toString());
        } catch (IOException e) {
            logger.info("[DM Exception] " + e.getMessage());
        } catch (Exception e) {
            logger.info("[Other Exception] " + e.getMessage() + dog.toString());

        }
    }

    /**
     * Finds a new available Pet to be added to a user's swiping queue.
     *
     * @param basicUser - the user who will receive the Pet.
     * @return - The Pet to be sent to the user.
     */
    public static Pet queuePet(BasicUser basicUser) {
        long tableSize = petRepository.count(); // there seems to be an exists() method...
        Random rand = new Random(); // To find a random pet.
        int upperBound = (int)tableSize;
        int petId; // ID of the next pet.
        Pet upNext = null;
        // If not a seller or is but has no dogs, any dog is valid.
        Buyer buyer = basicUser.getBuyerProfile();

        if (!basicUser.isSeller() || basicUser.getSellerProfile().getDawgs().isEmpty()) {
            while (!passesFilters(buyer, upNext)) {
                upNext = petRepository.findById(rand.nextInt(upperBound) + 1);
                // Adds 1 because the upper bound is EXCLUSIVE and 0 is null.
            }
        }

        else { // must exclude their own dogs
            ArrayList<Integer> skipIds= new ArrayList<>();
            List<Pet> pets = basicUser.getSellerProfile().getDawgs();
            // Compile a list of invalid IDs
            // TODO: Fix and make sure this actually excludes their own dogs.
            for (Pet dog : pets) {
                skipIds.add(dog.getId());
            }
            // Find a valid Pet to send.
            while (!passesFilters(buyer, upNext)) {
                petId = rand.nextInt(upperBound) + 1;
                // Ensure a user won't be sent their own dog.
                for (Integer pid : skipIds) {
                    if (Integer.valueOf(petId).equals(pid)) {
                        break;
                    }
                }
                upNext = petRepository.findById(petId);
            }
        }
        // In either case, the last Pet assumed by upNext is the right one.
        return upNext;
    }

    private static boolean passesFilters(Buyer buyer, Pet pet) {
        if (pet == null) {
            return false;
        }

        if (buyer.filtersByAge() && (pet.getAge() < buyer.getMinAge() || pet.getAge() > buyer.getMaxAge())) {
            return false;
        }

        if (buyer.filtersByBreed() && !buyer.getBreedPreference().equals(pet.getBreed())) {
            return false;
        }

        if (buyer.wantsIndoorPet() && !pet.isIndoorPet()) {
            return false;
        }

        if (buyer.wantsPottyTrainedPet() && !pet.isPottyTrained()) {
            return false;
        }

        return true;
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

        // do error handling here
        logger.info("[onError]" + username + ": " + throwable.getMessage());
    }


}
