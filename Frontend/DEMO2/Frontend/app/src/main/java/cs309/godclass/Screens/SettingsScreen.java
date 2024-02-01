package cs309.godclass.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import cs309.godclass.Logic.ProfileLogic;
import cs309.godclass.Logic.RegistrationLogic;
import cs309.godclass.Network.ServerRequest;
import cs309.godclass.Objects.Profile;
import cs309.godclass.R;

/**
 * Represents the Profile Screen, allowing users to edit their profile information and adjust settings.
 */
public class SettingsScreen extends AppCompatActivity implements View.OnClickListener, IView {
    Profile profile;

    // Edit Profile UI elements
    EditText editBioText;
    EditText editFirstname;
    EditText editLastname;
    EditText editEmailText;
    EditText editAddressText;
    EditText editPasswordText;
    EditText editUsernameText;

    // Settings UI elements
    Button saveSettingsButton;
    Button backButton;
    Button deleteButton;
    Switch buyerSwitch;
    Switch sellerSwitch;
    Switch sitterSwitch;
    TextView changeUserText;
    TextView errorMessage;

    /**
     * Called when the activity is starting.
     * Sets up the UI elements for the Profile Screen and handles button clicks.
     *
     * @param savedInstanceState If the activity is re-initialized after being shut down, this Bundle contains the
     *                           most recent data. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_screen);

        editBioText = findViewById(R.id.editBioText);
        editAddressText = findViewById(R.id.editAddressText);
        editEmailText = findViewById(R.id.editEmailText);
        editUsernameText = findViewById(R.id.editUsernameText);
        editPasswordText = findViewById(R.id.editTextPassword);
        editFirstname = findViewById(R.id.editNameText);
        editLastname = findViewById(R.id.editNameText3);

        saveSettingsButton = findViewById(R.id.SaveButtonSettings);
        buyerSwitch = findViewById(R.id.switchBuyer);
        sellerSwitch = findViewById(R.id.switchSeller);
        sitterSwitch = findViewById(R.id.switchSitter);
        changeUserText = findViewById(R.id.changeUserText);
        errorMessage = findViewById(R.id.errorMessageSettings);
        backButton = findViewById(R.id.backButtonSettings);
        deleteButton = findViewById(R.id.deleteButtonSettings);

        // Set listeners
        saveSettingsButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);


        // Load profile data from intent
        profile = (Profile) getIntent().getSerializableExtra("profile");
        setProfile(profile);

        // Set initial switch states based on profile
        buyerSwitch.setChecked(profile.getBuyer());
        sellerSwitch.setChecked(profile.getSeller());
        sitterSwitch.setChecked(profile.getSitter());

        // Switch listeners
        buyerSwitch.setOnCheckedChangeListener(createSwitchListener("buyer"));
        sellerSwitch.setOnCheckedChangeListener(createSwitchListener("seller"));
        sitterSwitch.setOnCheckedChangeListener(createSwitchListener("sitter"));
    }

    /**
     * Handles button clicks and their respective actions within the Profile Screen.
     *
     * @param v The view element that was clicked.
     */
    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {

            case R.id.SaveButtonSettings:
                // Save profile changes
                ServerRequest serverRequest = new ServerRequest();
                ProfileLogic logic = new ProfileLogic(this, serverRequest);

                String oldUsername = profile.getUsername();
                String oldPassword = profile.getPassword();
                profile.setAddress(editAddressText.getText().toString());
                profile.setBio(editBioText.getText().toString());
                profile.setFirstname(editFirstname.getText().toString());
                profile.setLastname(editLastname.getText().toString());
                profile.setUsername(editUsernameText.getText().toString());
                profile.setPassword(editPasswordText.getText().toString());
                profile.setEmail(editEmailText.getText().toString());

                try {
                    logic.updateProfile(profile);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                ServerRequest settingsServerRequest = new ServerRequest();
                ProfileLogic settingsLogic = new ProfileLogic(this, settingsServerRequest);
                ServerRequest serverRequest2 = new ServerRequest();
                final RegistrationLogic logic2 = new RegistrationLogic(this, serverRequest2, this);

                if (profile.getBuyer() || profile.getSeller() || profile.getSitter()) {
                    try {
                        settingsLogic.updateUser(profile);
                        logic2.registerUser(oldUsername, oldPassword);
                        //logic2.getProfile();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    showText("You must select at least one user");
                }
                break;
            case R.id.backButtonSettings:
                if(profile.getCurrUser().equals("buyer")) {
                    intent = new Intent(SettingsScreen.this, BuyerProfileScreen.class);
                }else if(profile.getCurrUser().equals("seller")){
                    intent = new Intent(SettingsScreen.this, SellerProfileScreen.class);
                }else{
                    intent = new Intent(SettingsScreen.this, SitterProfileScreen.class);
                }
                intent.putExtra("profile", profile);
                startActivity(intent);
                break;
            case R.id.deleteButtonSettings:
                ServerRequest DeleteServerRequest = new ServerRequest();
                ProfileLogic DeleteServerLogic = new ProfileLogic(this, DeleteServerRequest);

                try {
                    DeleteServerLogic.deleteProfile(profile.getId());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                intent = new Intent(SettingsScreen.this, RegistrationScreen.class);
                startActivity(intent);
                break;


        }
    }

    /**
     * Dummy method - Does not perform any functionality in this context.
     *
     * @param s The text to display.
     */
    @Override
    public void showText(String s) {
        errorMessage.setText(s);
    }

    /**
     * Dummy method - Does not perform any functionality in this context.
     *
     * @param s The text to display.
     */
    @Override
    public void toastText(String s) {
        // Implement if needed
    }

    /**
     * Set the profile information in the text fields.
     *
     * @param profile The user's profile to display and edit.
     */
    @Override
    public void setProfile(Profile profile) {
        // Set profile data in both Edit Profile and Settings sections
        editBioText.setText(profile.getBio());
        editEmailText.setText(profile.getEmail());
        editAddressText.setText(profile.getAddress());
        editPasswordText.setText(profile.getPassword());
        editUsernameText.setText(profile.getUsername());
        editLastname.setText(profile.getLastname());
        editFirstname.setText(profile.getFirstname());
    }

    /**
     * Create a switch listener that updates the profile's hide attribute based on the switch state.
     *
     * @param userCategory The category of the user (buyer, seller, sitter).
     * @return The created switch listener.
     */
    private CompoundButton.OnCheckedChangeListener createSwitchListener(final String userCategory) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Your code to handle the switch state change goes here
                if (isChecked) {
                    switch (userCategory) {
                        case "buyer":
                            profile.setBuyer(true);
                            break;
                        case "seller":
                            profile.setSeller(true);
                            break;
                        case "sitter":
                            profile.setSitter(true);
                            break;
                    }
                } else {
                    profile.setHide(userCategory);
                    switch (userCategory) {
                        case "buyer":
                            profile.setBuyer(false);
                            break;
                        case "seller":
                            profile.setSeller(false);
                            break;
                        case "sitter":
                            profile.setSitter(false);
                            break;
                    }
                }
            }
        };
    }
}
