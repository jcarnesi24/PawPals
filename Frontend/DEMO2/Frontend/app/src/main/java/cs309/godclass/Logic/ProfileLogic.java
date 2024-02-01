package cs309.godclass.Logic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cs309.godclass.Network.ServerRequest;
import cs309.godclass.Objects.Buyer;
import cs309.godclass.Objects.Dog;
import cs309.godclass.Objects.Message;
import cs309.godclass.Objects.Sitter;
import cs309.godclass.Screens.IView;
import cs309.godclass.Network.IServerRequest;
import cs309.godclass.Objects.Profile;

/**
 * Logic page and helper methods used after login for profile use
 */
public class ProfileLogic implements IVolleyListener{

    /**
     * Server request object for server communication
     */
    IServerRequest serverRequest;

    /**
     * View connected with this logic page
     */
    IView r;
    String path;

    Profile profile;

    /**
     * Logic contructor for profile logic
     * @param r
     *  View that the logic was created in
     * @param serverRequest
     *  Server request that it initiated with the logic class
     */
    public ProfileLogic(IView r, IServerRequest serverRequest) {
        this.r = r;
        this.serverRequest = serverRequest;
        serverRequest.addVolleyListener(this);
    }

    /**
     * Sends a PUT request in a JSOSN object created from the params
     * @param profile
     *  new profile
     * @throws JSONException
     */
    public void updateProfile(Profile profile) throws JSONException{
        JSONObject currProf = new JSONObject();

        currProf.put("firstname", profile.getFirstname());
        currProf.put("lastname", profile.getLastname());
        currProf.put("emailAddress", profile.getEmail());
        currProf.put("username", profile.getUsername());
        currProf.put("password", profile.getPassword());
        currProf.put("userAddress", profile.getAddress());
        currProf.put("userBio", profile.getBio());



        //currProf.put("dogs", profile.getDogs());
        path = "/users/" + profile.getId();

        serverRequest.sendToServer(url + path, currProf, "PUT");

    }

    public void deleteProfile(String id) throws JSONException{
        path = "/users/" + id;
        serverRequest.sendToServer(url + path, null, "DELETE");
    }

    /**
     * This method is a put request to the server that updates the user. This is sent whenever the current user
     * wants to become a new user or delete one of their previous users.
     * @param profile
     * @throws JSONException
     */
    public void updateUser(Profile profile) throws JSONException{
        JSONObject currProf = new JSONObject();

        currProf.put("buyer", profile.getBuyer());
        currProf.put("seller", profile.getSeller());
        currProf.put("sitter", profile.getSitter());

        path = "/users/" + profile.getId() + "/profiles";

        serverRequest.sendToServer(url + path, currProf, "PUT");

    }

    public void sendProfilePic(String profilePic, String currUser, String id) throws JSONException {
        JSONObject newUserObj = new JSONObject();
        newUserObj.put("profilePic", profilePic);
        path = "/users/" + id;

        //String url2 = "https://7ccc856f-f6d7-4095-9842-70590220701a.mock.pstmn.io";

        if(currUser.equals("buyer")){
            path += "/buyers/pic";
        }else if(currUser.equals("seller")){
            path += "/sellers";
        }else if(currUser.equals("sitter")){
            path += "/sitters";
        }

      //  path +=  "/profilePic=" + profilePic;

        serverRequest.sendToServer(url + path, newUserObj, "PUT");
    }
    /**
     * Calls to server with dog to be added, edited to deleted
     * @param dog
     * @param owner
     * @param word
     * @throws JSONException
     */
    public void addDog(Dog dog, Profile owner, String word) throws JSONException {
        JSONObject currDog = new JSONObject();

        currDog.put("petName", dog.getName());
        currDog.put("breed", dog.getBreed());
        currDog.put("age", dog.getAge());
        currDog.put("id", dog.getId());
        currDog.put("petImage", dog.getDogImage());
        currDog.put("petBio", dog.getBio());
        currDog.put("indoorPet", dog.isInsideDog());
        currDog.put("pottyTrained", dog.isPottyTrained());

        path = "/users/" + owner.getId() + "/sellers/pets/" + dog.getId();

        if(word.equals("DELETE")) {
            currDog = null;
        }
        serverRequest.sendToServer(url + path, currDog, word);
    }

    /**
     * Initializes the queue for a given user ID by sending a GET request to retrieve pet information.
     *
     * @param id The user ID for which the queue is to be initialized.
     * @throws JSONException if there's an issue handling JSON objects
     */
    public void initQueue(int id) throws JSONException{
        JSONObject reqId = new JSONObject();

        reqId.put("userId", id);

        ArrayList<Integer> ids = new ArrayList<>();

        reqId.put("exeId", ids);

        path = "/pets/queue/" + id;

        serverRequest.sendToServer(url + path, null, "GET");

    }

    public void getConversation(int id) throws JSONException{

        path = "/users/messages/" + id;
        serverRequest.sendToServer(url + path, null, "GET");
    }

    public void initQueueSitter(int id) throws JSONException{

        path = "/sitters/queue/" + id;

        serverRequest.sendToServer(url + path, null, "GET");
    }

    public void updateFilters(Profile profile) throws JSONException {
        JSONObject updateBuyer = new JSONObject();
        Buyer buyer = profile.getBuyerProfile();

        updateBuyer.put("filterByAge", buyer.isWantAge());
        updateBuyer.put("filterByBreed", buyer.isWantBreed());
        updateBuyer.put("wantsIndoorPet", buyer.isWantInsideDog());
        updateBuyer.put("wantsPottyTrainedPet", buyer.isWantPottyTrained());
        updateBuyer.put("minAge", buyer.getMinAge());
        updateBuyer.put("maxAge", buyer.getMaxAge());
        updateBuyer.put("breedPreference", buyer.getBreedPreference());

        path = "/users/" + profile.getId() + "/buyers";
        serverRequest.sendToServer((url + path), updateBuyer, "PUT");
    }
    /**
     * Executed upon successful completion of the server request.
     * Creates Dog objects from the received JSON and associates them with the seller's profile.
     *
     * @param s The JSON object received from the server response.
     * @throws JSONException if there's an issue handling JSON objects
     */
    @Override
    public void onSuccess(JSONObject s) throws JSONException {

        if (path.contains("/pets/queue")) {

            Dog dog = new Dog(s.getString("petName"),
                    s.getString("breed"),
                    s.getString("age"),
                    s.getString("id"),
                    s.getString("petImage"),
                    s.getString("petBio"),
                    s.getBoolean("pottyTrained"),
                    s.getBoolean("indoorPet"));

            ArrayList<Dog> dogQueue = new ArrayList<>();

            dogQueue.add(dog);

            Profile profile = new Profile("test", "test", "test", "test", "Test", false, true, false, null, "0");
            profile.getSellerProfile().setDogs(dogQueue);

            r.setProfile(profile);
        }
        else if (path.contains("/users/messages")) {

            String id = s.getString("id");
            String otherUsername = s.getString("otherUsername");
            String otherUserType =  s.getString("otherUserType");

            Message message = new Message(otherUsername,id,otherUserType);

            Profile profile = new Profile("test", "test", "test", "test", "Test", false, false, false, new ArrayList<Message>(), "0");
            profile.addMessage(message);
            r.setProfile(profile);
        }
        else if (path.contains("/sitters/queue")) {

            int dogsSat = Integer.parseInt(s.getString("dogSat"));
            float rating = Float.parseFloat(s.getString("rating"));
            Sitter sitter = new Sitter
                    (s.getString("name"),
                            s.getString("email"),
                            s.getString("bio"),
                            s.getString("sitterImage"), rating,
                            s.getString("sitterId"), dogsSat);


            Profile profile = new Profile("test", "test", "test", "test", "Test", false, true, true, null, "0");
            profile.getSitterProfile().setSitterQueue(sitter);
            r.setProfile(profile);
        }
        else if (path.contains("/sellers/pets/")) {
            Dog dog = new Dog(s.getString("petName"),
                    s.getString("breed"),
                    s.getString("age"),
                    s.getString("id"),
                    s.getString("petImage"),
                    s.getString("petBio"),
                    s.getBoolean("pottyTrained"),
                    s.getBoolean("indoorPet"));

            Profile profile = new Profile(dog.getId(), "test", "test", "test", "Test", false, true, true, null, "0");
            r.setProfile(profile);
        }
    }

    /**
     * Executed when there is an error in the server request.
     *
     * @param errorMessage The error message describing the encountered issue.
     */
    @Override
    public void onError(String errorMessage) {
        r.toastText(errorMessage);
    }

    public void updateRating(String otherId, String profType, String rating) {
        path = "/users/" + otherId + "/" + profType + "s/" + rating;

        serverRequest.sendToServer(url + path, null, "POST");
    }
}
