package pawpals_db.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

import javax.websocket.Session;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import pawpals_db.Buyers.Buyer;


import pawpals_db.Chat.Message;
import pawpals_db.Chat.ChatSocket;
import pawpals_db.Pets.Pet;
import pawpals_db.Pets.PetRepository;
import pawpals_db.Chat.MessageRepository;
import pawpals_db.Chat.Conversation;
//import pawpals_db.Users.BasicUser;

import pawpals_db.Buyers.BuyerRepository;
import pawpals_db.Pets.Pet;
import pawpals_db.Pets.PetRepository;
import pawpals_db.Sellers.Seller;
import pawpals_db.Sellers.SellerRepository;
import pawpals_db.Sitters.Sitter;
import pawpals_db.Sitters.SitterRepository;


/**
 * 
 * @author Jacob Carnesi, Zane Lenz
 * 
 */ 

@RestController
@Api(tags = "Basic User Controller", description = "Operations related to Basic Users")
public class BasicUserController {

    @Autowired
    BasicUserRepository basicUserRepository;

    @Autowired
    BuyerRepository buyerRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    SitterRepository sitterRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    MessageRepository msgRepository;

    @Autowired
    BasicUserTransactionalService BUTtS;

    private String success = "{\"message\":\"success :)\"}";
    private String failure = "{\"message\":\"failure :(\"}";

    @GetMapping(path = "/users")
    @ApiOperation(value = "Get all Users", notes = "Get a list of all users.")
    List<BasicUser> getAllUsers(){
        return basicUserRepository.findAll();
    }

    /**
     * Returns all messages in repository. We plan to sell this every quarter.
     *
     * @return - The list of all the messages.
     */
    @GetMapping(path = "/chatlogs")
    @ApiOperation(value = "Get all Messages", notes = "Get a list of all messages.")
    List<Message> getAllMessages() { return msgRepository.findAll(); }

    /**
     * For when the government finds out we are collecting and selling information illegally.
     * Leave no trace. Trust no one, question everything.
     */
    @DeleteMapping(path = "/chatlogs/nuke")
    @ApiOperation(value = "Delete All Chat Logs", notes = "Delete all messages.")
    void deleteChatLogs() { msgRepository.deleteAll(); }

    /**
     * Returns messages of specified user.
     *
     * @param username - the user's username.
     * @return - a list of all the user's messages.
     */
    @GetMapping(path = "/chatlogs/{username}")
    @ApiOperation(value = "Get Messages by User Name", notes = "Get messages of specified user.")
    List<Message> getMessagesByUserName(@ApiParam(value = "User's username", required = true) @PathVariable String username) {
        List<Message> allMsgs = getAllMessages();
        List<Message> userMsgs = new ArrayList<>();
        int i = 0;
        for (i = 0; i < allMsgs.size(); ++i) {
            Message msg = allMsgs.get(i);
            if (msg.getUserName().equals(username)) { //this username and basicUser username are separate things
                userMsgs.add(msg);                    //Fix that
            }
        }
        return userMsgs;
    }

    /**
     * Returns all messages in a conversation between two users.
     *
     * @param myId - the user's own ID.
     * @param otherId - The ID of the user they are talking to.
     * @return - The list of messages in the conversation.
     */
    @GetMapping(path = "chatlogs/{myId}/{otherId}")
    @ApiOperation(value = "Get Conversation", notes = "Get messages in a conversation between two users.")
    List<Message> getConversation(@ApiParam(value = "User's own ID", required = true) @PathVariable int myId,
                                  @ApiParam(value = "ID of the user they are talking to", required = true) @PathVariable int otherId) {
        List<Message> allMsgs = getAllMessages();
        List<Message> convoMsgs = new ArrayList<>();
        int i = 0;
        for (i = 0; i < allMsgs.size(); ++i) {
            Message msg = allMsgs.get(i);
            int user = msg.getSenderId();
            int otherUser = msg.getReceiverId();
            if ((otherUser == otherId || otherUser == myId) && (user == myId || user == otherId)) {
                convoMsgs.add(msg);
            }
        }
        return convoMsgs;
    }

    /**
     * Returns the necessary information to display a user's profile as specified by the
     * id path parameter.
     *
     * @param id - the id of the user to be displayed.
     * @return - The BasicUser object for the given user.
     */
    @GetMapping(path = "/users/{id}")
    @ApiOperation(value = "Get User by ID", notes = "Get user information by ID.")
    BasicUser getUserById( @ApiParam(value = "ID of the user to be displayed", required = true) @PathVariable int id) {
        return basicUserRepository.findById(id);
    }

    @GetMapping(path = "/users/messages/{id}")
    @ApiOperation(value = "Get a user's list of conversations", notes = "Return all the conversations that a user is part of")
    Conversation getLastUserConversation(@ApiParam(value = "ID of the user that owns the conversations.") @PathVariable int id) {
        BasicUser b = BUTtS.getBasicUserWithConvos(id);
        List<Conversation> convos = b.getConversations();
        if (convos.isEmpty()) {
            return null;
        }
        return convos.get(convos.size() - 1);
    }

    /**
     * Gets a user's profile types and adds them to a JSON token String.
     * Could also be used to provide other types of information.
     *
     * @param id - ID of the user profile to access.
     * @param infoType - specifies what information to provide.
     * @return - a String representing a JSON token of booleans.
     */
    @GetMapping(path = "/users/{id}/login")
    @ApiOperation(value = "Get Profile Types", notes = "Get user's profile types and add them to a JSON token String.")
    String getProfileTypes(@ApiParam(value = "ID of the user profile to access", required = true) @PathVariable int id,
                           @ApiParam(value = "Specifies what information to provide", required = false) @RequestParam(name = "infoType") String infoType) {
        if (infoType.equals("profiles")) {
            BasicUser u = basicUserRepository.findById(id);
            return "{\"isSeller\" : " + u.isSeller() + ",\n\"isBuyer\" : " + u.isBuyer() +
                    ",\n\"isSitter\" : " + u.isSitter() + "}";
        }
        else {
            return failure;
        }
    }
    

    /**
     * Creates a new user given the correct information.
     *
     * @param basicUser - A new user object.
     * @return - the newly created user so their info can be displayed in their profile?
     */
    @PostMapping(path = "/users")
    @ApiOperation(value = "Create User", notes = "Create a new user given the correct information.")
    String createUser(@ApiParam(value = "A new user object", required = true) @RequestBody BasicUser basicUser){
        // TODO: add password validation on either end.
        if (basicUser == null)
            return failure;
        if (basicUserRepository.findByUsername(basicUser.getUsername()) != null)
            return "{\"message\":\"Username taken\"}";
        if (basicUserRepository.findByEmailAddress(basicUser.getEmailAddress()) != null)
            return "{\"message\":\"Email taken\"}";
        else {
            // Initialize profiles based on booleans.
            // TODO: move linking the account into a constructor.
            basicUserRepository.save(basicUser);
            if (basicUser.isSeller()) {
                Seller s = new Seller(basicUser);
                basicUser.setSellerProfile(s);
                //s.setAccount(basicUser);
                sellerRepository.save(basicUser.getSellerProfile());
            }
            else if (basicUser.isBuyer()) {
                Buyer b = new Buyer(basicUser);
                basicUser.setBuyerProfile(b);
                //b.setAccount(basicUser);
                buyerRepository.save(basicUser.getBuyerProfile());
            } else if (basicUser.isSitter()) {
                Sitter s = new Sitter(basicUser);
                basicUser.setSitterProfile(s);
                //s.setAccount(basicUser);
                sitterRepository.save(basicUser.getSitterProfile());
            }
            basicUserRepository.save(basicUser);
            return "{\"message\":\"User Created\"}";
        }

    }

    /**
     * Intakes a JSON containing a username and password
     * Returns a JSON containing a string, telling the user if logic was successful
     *
     * @param credentials - The JSON containing username and password
     * @return - message containing logic results
     */
    @PostMapping(path = "/users/login")
    @ApiOperation(value = "Create User", notes = "Create a new user given the correct information.")
    String loginUser(@ApiParam(value = "User object with only username and password.", required = true) @RequestBody BasicUser credentials) {
        if (basicUserRepository.findByUsername(credentials.getUsername()) != null) {
            BasicUser user = basicUserRepository.findByUsername(credentials.getUsername());
            if (user.getPassword().equals(credentials.getPassword())) {
                return "{\"message\" : \"Login successful\",\n\"id\" : " + user.getId() +  "}";
            }
            // If they are an admin, include that?
            else {
                return "{\"message\" : \"Incorrect password\"}";
            }
        }
        else {
            return "{\"message\" : \"No users found\"}";
        }
    }
    // TODO: deauthenticate upon a PUT request to /users/{id}/logout

    /**
     * Using booleans for the different types of profile, adds ANY
     * new profile the user needs. Also, toggles the user's booleans
     * for each profile.
     *
     * @param id - the BasicUser account ID.
     * @param bools - the new booleans for each profile type.
     */
    @PutMapping(path = "/users/{id}/profiles")
    @ApiOperation(value = "Update User Profiles", notes = "Update user profiles based on provided booleans.")
    void updateProfiles(@ApiParam(value = "BasicUser account ID", required = true) @PathVariable int id,
                        @ApiParam(value = "New booleans for each profile type", required = true) @RequestBody BasicUser bools) {
        BasicUser user = basicUserRepository.findById(id);
        user.toggleSellerProfile(bools.isSeller());
        if (bools.isSeller() && user.getSellerProfile() == null) {
            Seller s = new Seller(user);
            user.setSellerProfile(s);
            sellerRepository.save(user.getSellerProfile());
        }
        user.toggleBuyerProfile(bools.isBuyer());
        if (bools.isBuyer() && user.getBuyerProfile() == null) {
            Buyer b = new Buyer(user);
            user.setBuyerProfile(b);
            buyerRepository.save(user.getBuyerProfile());
        }
        user.toggleSitterProfile(bools.isSitter());
        if (bools.isSitter() && user.getSitterProfile() == null) {
            Sitter s = new Sitter(user);
            user.setSitterProfile(s);
            sitterRepository.save(user.getSitterProfile());
        }

        basicUserRepository.save(user);
    }

    /**
     * Updates any part of the user's bio. The JSON request body must include all the relevant fields,
     * NOT just the variable that is being updated.
     *
     * @param id - The ID of the user that is being updated.
     * @param request - The BasicUser object with altered fields.
     * @return - The updated BasicUser.
     */
    @PutMapping(path = "/users/{id}")
    @ApiOperation(value = "Update User Information", notes = "Update any part of the user's bio.")
    BasicUser updateUser(@ApiParam(value = "ID of the user that is being updated", required = true) @PathVariable int id,
                         @ApiParam(value = "BasicUser object with altered fields", required = true) @RequestBody BasicUser request){
        BasicUser basicUser = basicUserRepository.findById(id);
        if(basicUser == null)
            return null;
        // update all fields to the new ones.
        basicUser.setUsername(request.getUsername());
        basicUser.setEmailAddress(request.getEmailAddress());
        basicUser.setUserBio(request.getUserBio());
        basicUser.setUserAddress(request.getUserAddress());
        basicUser.setFirstname(request.getFirstname());
        basicUser.setLastname(request.getLastname());
        basicUser.setPassword(request.getPassword());
        // Will NOT add new profiles.

        basicUserRepository.save(basicUser);
        return basicUserRepository.findById(id);
    }

    /**
     * Deletes the given user. Deletion includes deleting all the user's different
     * actor types and pets. The success message is a picture of the app mascot dog
     * looking at the user with big, sad eyes and saying something like "I'll miss you :("
     *
     * @param id - The ID of the BasicUser to be deleted.
     * @return - A success message that will make them feel bad for deleting their account.
     */
    // TODO: Make sure this deletes all their associated pets and account types.
    @DeleteMapping(path = "/users/{id}")
    @Transactional
    @ApiOperation(value = "Delete User", notes = "Delete the given user and associated data.")
    String deleteUser(@ApiParam(value = "ID of the BasicUser to be deleted", required = true) @PathVariable int id) {
        BasicUser u = basicUserRepository.findById(id);
//        if (u.isSeller() && u.getSellerProfile() != null) {
//            Seller s = u.getSellerProfile();
//            s.setAccount(null);
//            if (s.getDawgs() != null) {
//                for (Pet p : s.getDawgs()) {
//                    petRepository.deleteById(p.getId());
//                }
//            }
//            u.setSellerProfile(null);
//            sellerRepository.deleteById(s.getId());
//        }
//
//        if (u.isBuyer() && u.getBuyerProfile() != null) {
//            Buyer b = u.getBuyerProfile();
//            b.setAccount(null);
//            u.setBuyerProfile(null);
//            buyerRepository.deleteById(b.getId());
//        }
//
//        if (u.isSitter() && u.getSitterProfile() != null) {
//            Sitter s = u.getSitterProfile();
//            s.setAccount(null);
//            u.setSitterProfile(null);
//            sitterRepository.deleteById(s.getId());
//        }
        basicUserRepository.deleteById(id);
        return success;
    }

}
