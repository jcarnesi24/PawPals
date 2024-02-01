package cs309.godclass.Screens;
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

import org.json.JSONException;

import cs309.godclass.Logic.ProfileLogic;
import cs309.godclass.Network.ServerRequest;
import cs309.godclass.Objects.Profile;
import cs309.godclass.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Represents the Sitter Profile Screen where a sitter's information and options are displayed.
 */
public class SitterProfileScreen extends AppCompatActivity implements View.OnClickListener, IView {
    // Buttons and TextViews
    Button logoutBtn;
    Button settingsButton;
    Button sitterChatBtn;
    Button backButton;
    TextView bioTextView;
    TextView nameTextView;
    TextView emailTextView;
    TextView addressTextView;
    RatingBar ratingBar;
    private static final int PICK_IMAGE_REQUEST_SITTER = 1;

    ImageButton imageButton;
    CircleImageView imageView;
    Profile profile;


    /**
     * Called when the activity is starting.
     * Sets up the UI elements and buttons for the SitterProfileScreen.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *     this Bundle contains the data it most recently supplied in {@link #onSaveInstanceState}.
     *     Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sitter_profile_screen);


        bioTextView = findViewById(R.id.bioTxt);
        nameTextView = findViewById(R.id.nameTextView2);
        emailTextView = findViewById(R.id.emailTextView2);
        addressTextView = findViewById(R.id.addressTextView2);

        logoutBtn = findViewById(R.id.logoutButton);
        backButton = findViewById(R.id.SitterBackButton);
        sitterChatBtn = findViewById(R.id.SitterChatButton);
        settingsButton = findViewById(R.id.buttonSettingsSitter);

        imageButton = findViewById(R.id.chooseImageButtonSitter);
        imageView = findViewById(R.id.imageViewSitter);


        ratingBar = findViewById(R.id.ratingBar2);

        imageButton.setOnClickListener(this);

        logoutBtn.setOnClickListener(this);
        backButton.setOnClickListener(this);
        sitterChatBtn.setOnClickListener(this);
        settingsButton.setOnClickListener(this);

        profile = (Profile) getIntent().getSerializableExtra("profile");

        if(profile.getHide().equals("hideBack")){
            backButton.setVisibility(View.INVISIBLE);
        }
        if(!(profile.getSitterProfile().getSitterProfilePic().equals("null"))){
            imageButton.setVisibility(View.INVISIBLE);
            String stringUrl = profile.getSitterProfile().getSitterProfilePic();
            Uri imageUrl = Uri.parse(stringUrl);
            Glide.with(this).load(imageUrl).into(imageView);
        }

        setProfile(profile);
    }

    /**
     * Handles button clicks and their respective actions within the Sitter Profile Screen.
     *
     * @param view The view element that was clicked.
     */
    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()){

            case R.id.logoutButton:
                intent = new Intent(SitterProfileScreen.this, RegistrationScreen.class);
                startActivity(intent);
                break;
            case R.id.SitterBackButton:
                intent = new Intent(SitterProfileScreen.this, SelectionScreen.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
                break;
            case R.id.SitterChatButton:
                intent = new Intent(SitterProfileScreen.this, MessageListScreen.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
                break;
            case R.id.buttonSettingsSitter:
                intent = new Intent(SitterProfileScreen.this, SettingsScreen.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
                break;
            case R.id.chooseImageButtonSitter:
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select an image"), PICK_IMAGE_REQUEST_SITTER);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_SITTER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            String profilePicUrl = imageUri.toString();

            // Use an image loading library like Glide or Picasso to set the image to the ImageView
            Glide.with(this).load(imageUri).into(imageView);
            profile.getSitterProfile().setSitterProfilePic(profilePicUrl);
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
     * Displays the text for the bio, name, email, and address on the Sitter Profile Screen.
     *
     * @param s The text to display in the text views.
     */
    @Override
    public void showText(String s) {
        bioTextView.setText(s);
        nameTextView.setText(s);
        emailTextView.setText(s);
        addressTextView.setText(s);
    }

    /**
     * Displays a toast message for a short duration.
     *
     * @param s The text to display in the toast message.
     */
    @Override
    public void toastText(String s) {
      //  Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    /**
     * Sets the bio, name, and email for the Profile class and displays them in the Sitter Profile Screen.
     *
     * @param profile The Profile object containing bio, name, and email information.
     */
    @Override
    public void setProfile(Profile profile) {
        bioTextView.setText(profile.getBio());
        nameTextView.setText(profile.getName());
        emailTextView.setText(profile.getEmail());
        addressTextView.setText(profile.getAddress());
        ratingBar.setRating((float) profile.getSitterProfile().getRating());
    }
}
