package cs309.godclass.Logic;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cs309.godclass.Screens.IView;

import cs309.godclass.Network.IServerRequest;
import cs309.godclass.Screens.RegistrationScreen;
public class CreateUserLogic extends AppCompatActivity implements IVolleyListener {
   // public static final String url = "https://c73a7bcc-caeb-4bcb-96d5-c31a6b6b6620.mock.pstmn.io/getInfo";
    IView r;
    IServerRequest serverRequest;
    AppCompatActivity activity;

    private static boolean success;

    /**
     * Constructor
     * @param r
     * @param serverRequest
     * @param activity
     */
    public CreateUserLogic(IView r,IServerRequest serverRequest, AppCompatActivity activity) {
        this.r = r;
        this.serverRequest = serverRequest;
        this.activity = activity;
        serverRequest.addVolleyListener(this);
    }

    /**
     * Creates a object with all of the required fields to send to backend
     * @param firstname
     * @param lastname
     * @param email
     * @param username
     * @param password
     * @param user
     * @throws JSONException
     */
    public void CreateUser(String firstname, String lastname, String email, String username, String password, String user, String address, String bio) throws JSONException {

        JSONObject newUserObj = new JSONObject();
        newUserObj.put("firstname", firstname);
        newUserObj.put("lastname", lastname);
        newUserObj.put("emailAddress", email);
        newUserObj.put("username", username);
        newUserObj.put("password", password);
        newUserObj.put("userAddress", address);
        newUserObj.put("userBio", bio);
        boolean sitter = false, buyer = false, seller = false;

        if (user.equals("Sitter")) {
            sitter = true;
        } else if (user.equals("Buyer")) {
            buyer = true;
        } else if (user.equals("Seller")) {
            seller = true;
        }

        newUserObj.put("sitter",sitter);
        newUserObj.put("buyer",buyer);
        newUserObj.put("seller",seller);
        String path = "/users";
        serverRequest.sendToServer(url + path, newUserObj, "POST");
    }

    /**
     * This is the on success message for when the backend sends their response back
     * to frontend. It creates the user if User Created is returned. Their are also
     * error responses for having the same email or username as someone else.
     * @param s
     *  JSON object recieved from the server
     * @throws JSONException
     */
    @Override
    public void onSuccess(JSONObject s) throws JSONException {
        String response = s.getString("message");
        if (response.equals("User Created")) {
            success = true;
            loggedIn();

        } else if(response.equals("Username taken")){
            r.showText("That username has already been taken");
            success = false;
        }else if(response.equals("Email taken")){
            r.showText("This email already has an account");
            success = false;
        }
        else {
            r.showText("Please fill in all areas");
            success = false;
        }
    }

    /**
     * Error response
     * @param errorMessage
     *  JSON object revieved from the server
     */
    @Override
    public void onError(String errorMessage) {
        r.toastText(errorMessage);
        r.showText("Error with request, please try again");
    }

    /**
     * After creating a new user you are sent to the registration screen
     */
    public void loggedIn() {
        if (success && activity != null) {
            Intent intent = new Intent(activity, RegistrationScreen.class);
            activity.startActivity(intent);
        }
    }
}

