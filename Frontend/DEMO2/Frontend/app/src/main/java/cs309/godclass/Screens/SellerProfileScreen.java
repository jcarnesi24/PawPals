package cs309.godclass.Screens;
import static java.lang.Short.valueOf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;

import cs309.godclass.Logic.ProfileLogic;
import cs309.godclass.Network.ServerRequest;
import cs309.godclass.Network.WebSocketListener;
import cs309.godclass.Network.WebSocketManager;
import cs309.godclass.Objects.Profile;
import cs309.godclass.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Represents the Seller Profile Screen displaying the seller's information and options.
 */
public class SellerProfileScreen extends AppCompatActivity implements View.OnClickListener, IView {

    Button dogButton;
    Button messageButton;
    Button settingsButton;
    Button backButton;
    Button logoutButton;
    TextView nameTextView;
    TextView emailTextView;
    TextView addressTextView;
    RatingBar ratingBar;
    Profile profile;

    ImageButton imageButton;
    CircleImageView imageView;

    private static final int PICK_IMAGE_REQUEST_SELLER = 1;

    // Local & Remote server URLs
    String webURL = "ws://10.0.2.2:8080/swipe";
    //String webURL = "ws://coms-309-065.class.las.iastate.edu:8080/swipe";

    /**
     * Initializes the SellerProfileScreen with text views and buttons.
     *
     * @param savedInstanceState If the activity is re-initialized after being shut down, the bundle
     *                         contains the most recent data; otherwise, it's null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_profile_screen);

        nameTextView = findViewById(R.id.nameTextView2);
        emailTextView = findViewById(R.id.emailTextView2);
        addressTextView = findViewById(R.id.addressTextView2);

        logoutButton = findViewById(R.id.logoutButton);
        messageButton = findViewById(R.id.sellerChatButton);
        backButton = findViewById(R.id.sellerBackButton);
        dogButton = findViewById(R.id.dogViewButton);
        settingsButton = findViewById(R.id.buttonSettingsSeller);

        imageButton = findViewById(R.id.chooseImageButtonSeller);
        imageView = findViewById(R.id.imageViewSeller);

        ratingBar = findViewById(R.id.ratingBar);


        imageButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        messageButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        dogButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);

        profile = (Profile) getIntent().getSerializableExtra("profile");



        if(profile.getHide().equals("hideBack")){
            backButton.setVisibility(View.INVISIBLE);
        }

        if(!(profile.getSellerProfile().getSellerProfilePic().equals("null"))){
            imageButton.setVisibility(View.INVISIBLE);
            String stringUrl = profile.getSellerProfile().getSellerProfilePic();
            Uri imageUrl = Uri.parse(stringUrl);
            Glide.with(this).load(imageUrl).into(imageView);
        }

        setProfile(profile);
    }

    /**
     * Handles the button click events within the Seller Profile Screen.
     *
     * @param view The view element that was clicked.
     */
    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()){

            case R.id.sellerBackButton:
                intent = new Intent(SellerProfileScreen.this, SelectionScreen.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
                break;
            case R.id.logoutButton:
                intent = new Intent(SellerProfileScreen.this, RegistrationScreen.class);
                startActivity(intent);
                break;
            case R.id.dogViewButton:
                intent = new Intent(SellerProfileScreen.this, DogListScreen.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
                break;

            case R.id.sellerChatButton:
                intent = new Intent(SellerProfileScreen.this, MessageListScreen.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
                break;
            case R.id.buttonSettingsSeller:
                intent = new Intent(SellerProfileScreen.this, SettingsScreen.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
                break;
            case R.id.chooseImageButtonSeller:
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select an image"), PICK_IMAGE_REQUEST_SELLER);

                break;

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_SELLER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            String profilePicUrl = imageUri.toString();

            // Use an image loading library like Glide or Picasso to set the image to the ImageView
            Glide.with(this).load(imageUri).into(imageView);
            profile.getSellerProfile().setSellerProfilePic(profilePicUrl);
            imageButton.setVisibility(View.INVISIBLE);
            ServerRequest serverRequest = new ServerRequest();
            ProfileLogic logic = new ProfileLogic(this,serverRequest);
            try {
                logic.sendProfilePic(profilePicUrl, profile.getCurrUser(), profile.getId());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Sets the displayed text in the name TextView.
     *
     * @param s The text to display in the name TextView.
     */
    @Override
    public void showText(String s) {
        nameTextView.setText(s);
    }

    /**
     * Displays a toast message for a short duration.
     *
     * @param s The text for the toast message.
     */
    @Override
    public void toastText(String s) {
      //  Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    /**
     * Sets the name, email, and address from the Profile class in their respective TextViews.
     *
     * @param profile The profile to extract name, email, and address information from.
     */
    @Override
    public void setProfile(Profile profile) {
        nameTextView.setText(profile.getName());
        emailTextView.setText(profile.getEmail());
        addressTextView.setText(profile.getAddress());
        ratingBar.setRating((float) profile.getSellerProfile().getRating());

    }

}
