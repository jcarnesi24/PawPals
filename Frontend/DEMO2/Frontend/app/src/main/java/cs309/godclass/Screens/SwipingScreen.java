package cs309.godclass.Screens;

import static java.lang.Short.valueOf;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cs309.godclass.Logic.ProfileLogic;
import cs309.godclass.Network.ServerRequest;
import cs309.godclass.Network.WebSocketListener;
import cs309.godclass.Network.WebSocketManager;
import cs309.godclass.Objects.Dog;
import cs309.godclass.Objects.Profile;
import cs309.godclass.R;

import java.util.concurrent.TimeUnit;

/**
 * Represents the Swiping Screen where users can swipe through dog profiles.
 */
public class SwipingScreen extends AppCompatActivity implements View.OnClickListener, IView, WebSocketListener {

    // Buttons and TextViews
    Button backButton;
    Button yesButton;
    Button noButton;
    TextView nameTextView;
    TextView breedTextView;
    TextView ageTextView;
    TextView bioTextView;
    RatingBar sellerRatingBar;
    Switch isPottyTrained;
    Switch isInsideDog;

    ImageView imageView;
    Profile profile;
    ArrayList<Dog> dogQueue = new ArrayList<Dog>();
    Dog currDog;
    float rating = 0;


    // Local & Remote server URLs
    // String BASE_URL = "ws://10.0.2.2:8080/swipe/pets";
    String BASE_URL = "ws://coms-309-065.class.las.iastate.edu:8080/swipe/pets";
    /**
     * Called when the activity is starting.
     * Initializes the UI and requests dog profiles from the server.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profile = (Profile) getIntent().getSerializableExtra("profile");
        setContentView(R.layout.swiping_screen);

        // Initialize UI elements and buttons
        backButton = findViewById(R.id.swipingBackButton);
        yesButton = findViewById(R.id.NoButton);
        noButton = findViewById(R.id.YesButton);
        imageView = findViewById(R.id.imageViewDogSwipe);
        sellerRatingBar = findViewById(R.id.SellerRatingBar);
        bioTextView = findViewById(R.id.swipeDogBioTextView);
        isInsideDog= findViewById(R.id.viewInsideDog);
        isPottyTrained = findViewById(R.id.viewPottyTrained);

        backButton.setOnClickListener(this);
        yesButton.setOnClickListener(this);
        noButton.setOnClickListener(this);

        nameTextView = findViewById(R.id.nameSwiDogextView);
        breedTextView = findViewById(R.id.breedSwiDogextView);
        ageTextView = findViewById(R.id.swipeAge);


         //Request dog profiles from the server
        ServerRequest serverRequest = new ServerRequest();
        final ProfileLogic logic = new ProfileLogic(this, serverRequest);

        try {
            logic.initQueue(valueOf(profile.getId()));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Handles button clicks and their respective actions.
     *
     * @param view The view element that was clicked.
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Intent intent;
        String message;

        ServerRequest serverRequest = new ServerRequest();
        final ProfileLogic logic = new ProfileLogic(this, serverRequest);

        switch (view.getId()){

            case R.id.swipingBackButton:
                intent = new Intent(SwipingScreen.this, BuyerProfileScreen.class);
                intent.putExtra("profile", profile);

                WebSocketManager.getInstance().disconnectWebSocket();
                startActivity(intent);
                break;
            case R.id.YesButton:
                message = profile.getId() + " " + currDog.getId() + " 1";
                WebSocketManager.getInstance().sendMessage(message);

                try {
                    processSwipe();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                try {
                    logic.getConversation(valueOf(profile.getId()));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                break;
            case R.id.NoButton:
                message = profile.getId() + " " + currDog.getId() + " 0";
                WebSocketManager.getInstance().sendMessage(message);

                try {
                    processSwipe();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                break;


        }
    }

    @Override
    public void showText(String s) {

    }

    @Override
    public void toastText(String s) {
//        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setProfile(Profile profile) {
        if (profile.getSellerProfile() != null) {
            dogQueue.addAll(profile.getSellerProfile().getDogs());

            String serverUrl = BASE_URL + "/" + this.profile.getUsername();

            WebSocketManager.getInstance().connectWebSocket(serverUrl);
            WebSocketManager.getInstance().setWebSocketListener(SwipingScreen.this);

            try {
                processSwipe();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.profile.getMessages().add(profile.getMessages().get(0));
        }
    }

    /**
     * Executed when the WebSocket connection is established.
     *
     * @param handshakedata Information about the handshake.
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
//        String brh = "69";
//        ageTextView.setText(brh);
    }

    /**
     * Executed when a message is received over the WebSocket connection.
     *
     * @param message The received message from the WebSocket.
     */
    @Override
    public void onWebSocketMessage(String message) {

            int i = 0;
            String name = "";
            while(message.charAt(i) != ',') {
                name += message.charAt(i++);
            }

            i++;
            String id = "";
            while(message.charAt(i) != ',') {
                id += message.charAt(i++);
            }

            i++;
            String breed = "";
            while (message.charAt(i) != ',') {
                breed += message.charAt(i++);
            }

            i++;
            String age = "";
            while (message.charAt(i) != ',') {
                age += message.charAt(i++);
            }
            i++;
            String dogImage = "";
            while (message.charAt(i) != ',') {
                dogImage += message.charAt(i++);
            }
            i++;
            String sellerRating = "";
            while (message.charAt(i) != ',') {
                sellerRating += message.charAt(i++);
            }
            i++;
            String dogBio = "";
            while (message.charAt(i) != ',') {
                dogBio += message.charAt(i++);
            }
            i++;
            String isPottyTrained = "";
            while (message.charAt(i) != ',') {
                isPottyTrained += message.charAt(i++);
            }
            i++;
            String isOutsideDog = "";
            while (message.charAt(i) != ',') {
                isOutsideDog += message.charAt(i++);
            }



            rating = Float.parseFloat(sellerRating);

            Dog dog = new Dog(name, breed, age, id, dogImage, dogBio, isPottyTrained.equals("true"), isOutsideDog.equals("true"));

            dogQueue.add(dog);

    }

    /**
     * Executed when the WebSocket connection is closed.
     *
     * @param code    The status code explaining the reason for closure.
     * @param reason  The reason for closure.
     * @param remote  True if the closure was initiated by the remote end.
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            toastText( "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    /**
     * Executed when an error occurs in the WebSocket connection.
     *
     * @param ex The encountered exception.
     */
    @Override
    public void onWebSocketError(Exception ex) {
        toastText("WebSocket Exception " + ex.toString());
        ex.printStackTrace();
    }

    /**
     * Process the swiping action, updating the displayed dog profile.
     *
     * @throws InterruptedException if the thread is interrupted during the waiting process.
     */
    private void processSwipe() throws InterruptedException {


        if(!dogQueue.isEmpty()) {
            currDog = dogQueue.get(0);
            dogQueue.remove(0);

            nameTextView.setText(currDog.getName());
            breedTextView.setText(currDog.getBreed());
            ageTextView.setText(currDog.getAge());
            bioTextView.setText(currDog.getBio());
            isPottyTrained.setChecked(currDog.isPottyTrained());
            isInsideDog.setChecked(currDog.isInsideDog());
            String stringUrl = currDog.getDogImage();
            Uri imageUrl = Uri.parse(stringUrl);
            Glide.with(this)
                    .load(imageUrl)
                    .error(R.drawable.profile_pic_seth)
                    .into(imageView);

            sellerRatingBar.setRating(rating);

        } else {
            toastText("Waiting for Dog from server, Please wait");

            nameTextView.setText(" ");
            breedTextView.setText(" ");
            ageTextView.setText(" ");

            TimeUnit.SECONDS.sleep(3);

            processSwipe();
        }
    }
}
