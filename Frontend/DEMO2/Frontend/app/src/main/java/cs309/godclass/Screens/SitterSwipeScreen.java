package cs309.godclass.Screens;

import static java.lang.Short.valueOf;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cs309.godclass.Logic.ProfileLogic;
import cs309.godclass.Network.ServerRequest;
import cs309.godclass.Network.WebSocketListener;
import cs309.godclass.Network.WebSocketManager;
import cs309.godclass.Objects.Dog;
import cs309.godclass.Objects.Profile;
import cs309.godclass.Objects.Sitter;
import cs309.godclass.R;

public class SitterSwipeScreen extends AppCompatActivity implements View.OnClickListener, IView, WebSocketListener {
    Button backButton;
    Button yesButton;
    Button noButton;
    TextView nameTextView;
    TextView emailTextView;

    TextView dogsSat;
    TextView bioTextView;
    RatingBar sitterRatingBar;
    ImageView imageView;
    Profile profile;
    ArrayList<Sitter> sitterQueue = new ArrayList<Sitter>();
    //float rating = 0;

    Sitter currSitter;

    // Local & Remote server URLs
   // String BASE_URL = "ws://10.0.2.2:8080/swipe/sitters";
    String BASE_URL = "ws://coms-309-065.class.las.iastate.edu:8080/swipe/sitters";

    /**
     * Called when the activity is starting.
     * Initializes the UI and requests dog profiles from the server.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profile = (Profile) getIntent().getSerializableExtra("profile");
        setContentView(R.layout.sitter_swiping_screen);

        // Initialize UI elements and buttons
        backButton = findViewById(R.id.backButtonSitterSwipe);
        yesButton = findViewById(R.id.YesSitterSwipe);
        noButton = findViewById(R.id.NoSitterSwipe);
        imageView = findViewById(R.id.imageViewSitterSwipe);
        sitterRatingBar = findViewById(R.id.ratingBarSitterSwipe);
        bioTextView = findViewById(R.id.bioSitterSwipe);
        emailTextView = findViewById(R.id.emailSitterSwipe);
        dogsSat = findViewById(R.id.dogsSatSitterSwipe);
        nameTextView = findViewById(R.id.NameSitterSwipe);

        backButton.setOnClickListener(this);
        yesButton.setOnClickListener(this);
        noButton.setOnClickListener(this);

        //Request siiter profiles from the server
        ServerRequest serverRequest = new ServerRequest();
        final ProfileLogic logic = new ProfileLogic(this, serverRequest);

        try {
            logic.initQueueSitter(valueOf(profile.getId()));
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

            case R.id.backButtonSitterSwipe:
                intent = new Intent(SitterSwipeScreen.this, BuyerProfileScreen.class);
                intent.putExtra("profile", profile);

                WebSocketManager.getInstance().disconnectWebSocket();
                startActivity(intent);
                break;
            case R.id.YesSitterSwipe:
                message = profile.getId() + " " + currSitter.getId() + " 1";
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
            case R.id.NoSitterSwipe:
                message = profile.getId() + " " + currSitter.getId() + " 0";
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
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

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
        String bio = "";
        while(message.charAt(i) != ',') {
            bio += message.charAt(i++);
        }
        i++;
        String email = "";
        while(message.charAt(i) != ',') {
            email += message.charAt(i++);
        }
        i++;
        String sitterImage = "";
        while (message.charAt(i) != ',') {
            sitterImage += message.charAt(i++);
        }
        i++;
        String sitterRating = "";
        while (message.charAt(i) != ',') {
            sitterRating += message.charAt(i++);
        }
        i++;
        String numDogsSat = "";
        while (message.charAt(i) != ',') {
            numDogsSat += message.charAt(i++);
        }

        float rating = Float.parseFloat(sitterRating);
        int dogsSat = Integer.parseInt(numDogsSat);

        Sitter sitter = new Sitter(name,email,bio,sitterImage, rating, id ,dogsSat);

        sitterQueue.add(sitter);
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            toastText( "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    @Override
    public void onWebSocketError(Exception ex) {
        toastText("WebSocket Exception " + ex.toString());
        ex.printStackTrace();
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
            sitterQueue.add(profile.getSitterProfile().getSitterQueue());

            String serverUrl = BASE_URL + "/" + this.profile.getUsername();

            WebSocketManager.getInstance().connectWebSocket(serverUrl);
            WebSocketManager.getInstance().setWebSocketListener(SitterSwipeScreen.this);

            try {
                processSwipe();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.profile.getMessages().add(profile.getMessages().get(0));
        }
    }


    private void processSwipe() throws InterruptedException {


        if(!sitterQueue.isEmpty()) {
            currSitter = sitterQueue.get(0);
            sitterQueue.remove(0);

            nameTextView.setText(currSitter.getName());
            emailTextView.setText(currSitter.getEmail());
            String tst = "" + currSitter.getDogsSat();
            dogsSat.setText(tst);
            bioTextView.setText(currSitter.getBio());
            String stringUrl = currSitter.getSitterProfilePic();
            Uri imageUrl = Uri.parse(stringUrl);
            Glide.with(this)
                    .load(imageUrl)
                    .error(R.drawable.profile_pic_seth)
                    .into(imageView);

            sitterRatingBar.setRating(currSitter.getRating());

        } else {
            toastText("Waiting for Sitter from server, Please wait");

            nameTextView.setText(" ");
            emailTextView.setText(" ");
            bioTextView.setText(" ");
            dogsSat.setText(" ");

            TimeUnit.SECONDS.sleep(3);

            processSwipe();
        }
    }
}