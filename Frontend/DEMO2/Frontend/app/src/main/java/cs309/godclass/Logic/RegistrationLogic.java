package cs309.godclass.Logic;

import static java.lang.Float.parseFloat;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cs309.godclass.Objects.Dog;
import cs309.godclass.Objects.Message;
import cs309.godclass.Screens.IView;
import cs309.godclass.Network.IServerRequest;


import cs309.godclass.Objects.Profile;
import cs309.godclass.Screens.BuyerProfileScreen;
import cs309.godclass.Screens.SelectionScreen;
import cs309.godclass.Screens.SellerProfileScreen;
import cs309.godclass.Screens.SitterProfileScreen;


/**
 * Logic page used during login up through registration
 */
public class RegistrationLogic extends AppCompatActivity implements  IVolleyListener {
    IView r;
    /**
     * Server request created with logic page
     */
    IServerRequest serverRequest;
    /**
     * activity that is currently in use
     */
    AppCompatActivity activity;
    /**
     * boolean that stores the value of the log in state
     */
    private static boolean success;

    /**
     * String that hols the value of Id that is sent from backend
     */
    public String getId;

    /**
     * Variable that is added to the end of the base URL
     */
    public String path;

    /**
     * Hold all of the information for the current user.
     */
    public Profile profile;

    /**
     * Constructor for Registration Logic page
     * @param r
     *  View in which logic was created
     * @param serverRequest
     *  Request made with logic
     * @param activity
     *  Activity currently in play
     */
    public RegistrationLogic(IView r, IServerRequest serverRequest, AppCompatActivity activity) {
        this.r = r;
        this.serverRequest = serverRequest;
        this.activity = activity;
        serverRequest.addVolleyListener(this);
    }
    public RegistrationLogic(){

    }

    /**
     * Sends a POST request (Needs to be updated to GET) to the server to check if this user exists
     * @param name
     *  username of account
     * @param password
     *  password of account
     * @throws JSONException
     */
    public void registerUser(String name, String password) throws JSONException {
        JSONObject newUserObj = new JSONObject();
        newUserObj.put("username", name);
        newUserObj.put("password", password);
        path = "/users/login";

        serverRequest.sendToServer(url + path, newUserObj, "POST");
    }

    /**
     * Sends a get request to the server to get the profile information
     */
    public void getProfile() {
        path = "/users/" + getId;
        serverRequest.sendToServer(url + path, null, "GET");
    }

    /**
     * This is the logic for both registering and collecting the users base information
     * @param s
     *  JSON object recieved from the server
     * @throws JSONException
     */
    @Override
    public void onSuccess(JSONObject s) throws JSONException {
        String responseUrl = url + path;
        if(responseUrl.contains("/users/login")) {
            String getMessage = s.getString("message");

            if (getMessage.equals("Login successful")) {
                getId = s.getString("id");
                success = true;
                loggedIn();
            } else {
                r.showText("Incorrect Username or password");
                success = false;
            }
        } if(responseUrl.contains("/users/" + getId)){
            String username = s.getString("username");
            String email = s.getString("emailAddress");
            String password = s.getString("password");
            String firstname = s.getString("firstname");
            String lastname = s.getString("lastname");
            String address = s.getString("userAddress");
            String bio = s.getString("userBio");
            Boolean isBuyer = s.getBoolean("buyer");
            Boolean isSeller = s.getBoolean("seller");
            Boolean isSitter = s.getBoolean("sitter");


            JSONArray messageArray = s.getJSONArray("conversations");
            ArrayList<Message> messages = new ArrayList<>();
            if(messageArray.length() != 0) {

                for (int i = 0; i < messageArray.length(); i++) {
                    try {
                        JSONObject jsonObject = messageArray.getJSONObject(i);
                        Message newMessage = new Message(jsonObject.getString("otherUsername"),
                                jsonObject.getString("id"), jsonObject.getString("otherUserType"));

                        messages.add(newMessage);
                    } catch (JSONException e) {
                    }
                }
            }


            profile = new Profile(firstname, lastname, email, password, username, isBuyer, isSeller, isSitter, messages, getId);
            profile.setAddress(address);
            profile.setBio(bio);

            if (isSeller) {
                JSONObject sellerProf = s.getJSONObject("sellerProfile");

                 Double rating = sellerProf.getDouble("rating");

                 String sellerProfilePic = sellerProf.getString("sellerImage");

                 profile.getSellerProfile().setSellerProfilePic(sellerProfilePic);

                JSONArray dogArray = sellerProf.getJSONArray("dawgs");

                ArrayList<Dog> dogs = new ArrayList<Dog>();

                for (int i = 0; i < dogArray.length(); i++) {
                    try {
                        JSONObject jsonObject = dogArray.getJSONObject(i);
                        Dog dog = new Dog(jsonObject.getString("petName"),
                                jsonObject.getString("breed"),
                                jsonObject.getString("age"),
                                jsonObject.getString("id"),
                                jsonObject.getString("petImage"),
                                jsonObject.getString("petBio"),
                                jsonObject.getBoolean("pottyTrained"),
                                jsonObject.getBoolean("indoorPet"));
                        dogs.add(dog);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                profile.getSellerProfile().setDogs(dogs);
                profile.getSellerProfile().setRating(rating);

            }

            if (isBuyer) {
                JSONObject buyerProf = s.getJSONObject("buyerProfile");

                 String buyerProfilePic = buyerProf.getString("buyerImage");

                 profile.getBuyerProfile().setBuyerProfilePic(buyerProfilePic);

                boolean filterByAge = buyerProf.getBoolean("filterByAge");
                boolean filterByBreed = buyerProf.getBoolean("filterByBreed");
                boolean filterByInside = buyerProf.getBoolean("wantsIndoorPet");
                boolean filterByPotty = buyerProf.getBoolean("wantsPottyTrainedPet");

                if (filterByAge) {
                    int minAge = buyerProf.getInt("minAge");
                    int maxAge = buyerProf.getInt("maxAge");
                    profile.getBuyerProfile().setAgePreference(minAge, maxAge);
                }
                if (filterByBreed) {
                    profile.getBuyerProfile().setBreedPreference(buyerProf.getString("breedPreference"));
                }
                if(filterByInside) {
                    profile.getBuyerProfile().wantInsideDog();
                }
                if(filterByPotty) {
                    profile.getBuyerProfile().wantPottyTrained();
                }



            }

            if (isSitter) {
                JSONObject sitterProf = s.getJSONObject("sitterProfile");

                 String sitterProfilePic = sitterProf.getString("sitterImage");

                 profile.getSitterProfile().setSitterProfilePic(sitterProfilePic);

                String stringRating = sitterProf.getString("rating");
                float rating = parseFloat(stringRating);

                profile.getSitterProfile().setRating(rating);
            }


            switchScreen(profile);
        }
    }

    /**
     *
     * @param errorMessage
     *  JSON object revieved from the server
     */
    @Override
    public void onError(String errorMessage) {
        r.toastText(errorMessage);
        r.showText("Error with request, please try again");
    }

    /**
     * This method is called after the user is logged in.
     * It calls the getProfile() method above
     */
        public void loggedIn() {

        getProfile();
        }

    /**
     * This is the method that has the logic for determining what screen to go to
     * depending on what type of user (buyer, seller, sitter).
     * @param profile
     */
    public void switchScreen(Profile profile){
            boolean buyer = profile.getBuyer();
            boolean seller = profile.getSeller();
            boolean sitter = profile.getSitter();


        if(buyer && success && activity != null && (!seller) && (!sitter)){
            Intent intent = new Intent(activity, BuyerProfileScreen.class);
            profile.setHide("hideBack");
            intent.putExtra("profile", profile);
           profile.setUser("buyer");
            activity.startActivity(intent);
        }
        else if(seller && success && activity != null && (!buyer) && (!sitter)){
            Intent intent = new Intent(activity, SellerProfileScreen.class);
            profile.setHide("hideBack");
            intent.putExtra("profile", profile);
            profile.setUser("seller");
            activity.startActivity(intent);
        }
        else if(sitter && success && activity != null && (!seller) && (!buyer)){
            Intent intent = new Intent(activity, SitterProfileScreen.class);
            profile.setHide("hideBack");
            intent.putExtra("profile", profile);
            profile.setUser("sitter");
            activity.startActivity(intent);
        }
        else  if(buyer && sitter && success && activity != null && (!seller)){
            Intent intent = new Intent(activity, SelectionScreen.class);
            profile.setHide("seller");
            intent.putExtra("profile", profile);
            activity.startActivity(intent);
        }
        else if(buyer && seller && success && activity != null && (!sitter)){
            Intent intent = new Intent(activity, SelectionScreen.class);
            profile.setHide("sitter");
            intent.putExtra("profile", profile);
            activity.startActivity(intent);
        }
        else if(seller && sitter && success && activity != null && (!buyer)){
            Intent intent = new Intent(activity, SelectionScreen.class);
            profile.setHide("buyer");
            intent.putExtra("profile", profile);
            activity.startActivity(intent);
        }
        else if(buyer && sitter && seller && success && activity != null){
            Intent intent = new Intent(activity, SelectionScreen.class);
            profile.setHide("nothing");
            intent.putExtra("profile", profile);
            activity.startActivity(intent);
        }
    }

}