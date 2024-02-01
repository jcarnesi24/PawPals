package cs309.godclass.Screens;
import static java.lang.Integer.valueOf;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import de.hdodenhof.circleimageview.CircleImageView;


import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;

import org.json.JSONException;

import cs309.godclass.Logic.ProfileLogic;
import cs309.godclass.Network.ServerRequest;
import cs309.godclass.Objects.Profile;
import cs309.godclass.R;

/**
 * This is the buyer profile screen this is the visual representation of what the user sees when entering the app as a buyer.
 */
public class BuyerProfileScreen extends AppCompatActivity implements View.OnClickListener, IView {

    Button logoutBtn;
    Button settingsButton;
    Button backButton;
    Button messageButton;
    Button toSwipingBtn;
    TextView nameTextView;
    TextView emailTextView;
    TextView addressTextView;
    ImageButton imageButton;
    CircleImageView imageView;
    Button saveFiltersButton;
    Button SitterSwipeButton;
    Switch breedFilterSwitch;
    Switch ageFilterSwitch;
    Switch wantPottyTrained;
    Switch wantInsideDog;
    TextView minAge;
    TextView maxAge;
    TextView breedFilter;



    private static final int PICK_IMAGE_REQUEST = 1;

    private Profile profile;

    /**
     * Holds value of id
     */
    public String id;

    /**
     * On create method for the profile screen sets textviews and buttons
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyer_profile_screen);


        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView2);
        addressTextView = findViewById(R.id.addressTextView2);

        logoutBtn = findViewById(R.id.logoutButton);
        saveFiltersButton = findViewById(R.id.saveFiltersButton);
        backButton = findViewById(R.id.backBtn);
        messageButton = findViewById(R.id.messageBtn1);

        breedFilterSwitch = findViewById(R.id.selectBreedFilter);
        ageFilterSwitch = findViewById(R.id.selectAgeFilterSwitch);
        wantPottyTrained = findViewById(R.id.selectPottyTrainedSwitch);
        wantInsideDog = findViewById(R.id.selectInsideDogSwitch);

        minAge = findViewById(R.id.editTextMinAge);
        maxAge = findViewById(R.id.editTextMaxAge);
        breedFilter = findViewById(R.id.editTextBreedFilter);


        toSwipingBtn = findViewById(R.id.ToSwipingButton);
        settingsButton = findViewById(R.id.buttonSettingsBuyer);

        imageButton = findViewById(R.id.chooseImageButton);
        imageView = findViewById(R.id.imageViewBuyer);
        SitterSwipeButton = findViewById(R.id.toSwipingButtonSitter);

        imageButton.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        backButton.setOnClickListener(this);
        messageButton.setOnClickListener(this);
        toSwipingBtn.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        saveFiltersButton.setOnClickListener(this);
        SitterSwipeButton.setOnClickListener(this);

       profile = (Profile) getIntent().getSerializableExtra("profile");

        if(profile.getHide().equals("hideBack")){
            backButton.setVisibility(View.INVISIBLE);
        }
        if(!(profile.getBuyerProfile().getBuyerProfilePic().equals("null"))){
            imageButton.setVisibility(View.INVISIBLE);
            String stringUrl = profile.getBuyerProfile().getBuyerProfilePic();
            Uri imageUrl = Uri.parse(stringUrl);
            Glide.with(this).load(imageUrl).into(imageView);
        }
        breedFilterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    breedFilter.setVisibility(View.VISIBLE);
                    breedFilter.setText(profile.getBuyerProfile().getBreedPreference());
                } else {
                    breedFilter.setVisibility(View.INVISIBLE);
                }
            }
        });
        ageFilterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    minAge.setVisibility(View.VISIBLE);
                    maxAge.setVisibility(View.VISIBLE);
                    minAge.setText("");
                    maxAge.setText("");
                } else {
                    minAge.setVisibility(View.INVISIBLE);
                    maxAge.setVisibility(View.INVISIBLE);
                }
            }
        });


        setProfile(profile);
    }

    /**
     * On click logic for all buttons
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()){


            case R.id.logoutButton:
                intent = new Intent(BuyerProfileScreen.this, RegistrationScreen.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
                break;
            case R.id.ToSwipingButton:
                intent = new Intent(BuyerProfileScreen.this, SwipingScreen.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
                break;
            case R.id.backBtn:
                intent = new Intent(BuyerProfileScreen.this, SelectionScreen.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
                break;
            case R.id.messageBtn1:
                intent = new Intent(BuyerProfileScreen.this, MessageListScreen.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
                break;
            case R.id.buttonSettingsBuyer:
                intent = new Intent(BuyerProfileScreen.this, SettingsScreen.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
                break;
            case R.id.chooseImageButton:
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select an image"), PICK_IMAGE_REQUEST);
                break;
            case R.id.toSwipingButtonSitter:
                intent = new Intent(BuyerProfileScreen.this, SitterSwipeScreen.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
                break;
            case R.id.saveFiltersButton:
                try {
                    if (wantInsideDog.isChecked()) {
                        profile.getBuyerProfile().wantInsideDog();
                    } else {
                        profile.getBuyerProfile().noWantInsideDog();
                    }

                    if (wantPottyTrained.isChecked()) {
                        profile.getBuyerProfile().wantPottyTrained();
                    } else {
                        profile.getBuyerProfile().noWantPottyTrained();
                    }

                    if (ageFilterSwitch.isChecked()) {
                        profile.getBuyerProfile().setAgePreference(valueOf(minAge.getText().toString()), valueOf(maxAge.getText().toString()));
                    } else {
                        profile.getBuyerProfile().clearAgePreference();
                    }

                    if (breedFilterSwitch.isChecked()) {
                        profile.getBuyerProfile().setBreedPreference(breedFilter.getText().toString());

                    } else {
                        profile.getBuyerProfile().clearBreedPreference();
                    }


                    ServerRequest serverRequest = new ServerRequest();
                    ProfileLogic logic = new ProfileLogic(this, serverRequest);
                    logic.updateFilters(profile);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            String profilePicUrl = imageUri.toString();

            // Use an image loading library like Glide or Picasso to set the image to the ImageView
            Glide.with(this).load(imageUri).into(imageView);
            profile.getBuyerProfile().setBuyerProfilePic(profilePicUrl);
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
     * Sets the text for the following fields
     * @param s
     */
    @Override
    public void showText(String s) {
        nameTextView.setText(s);
        emailTextView.setText(s);
        addressTextView.setText(s);
    }

    /**
     * Displays text for a short period of time
     * @param s
     */
    @Override
    public void toastText(String s) {
//        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    /**
     * Sets the text views by calling from Profile class
     * @param profile
     */
    @Override
    public void setProfile(Profile profile) {
        nameTextView.setText(profile.getFirstname() + " " + profile.getLastname());
        emailTextView.setText(profile.getEmail());
        addressTextView.setText(profile.getAddress());
        wantInsideDog.setChecked(profile.getBuyerProfile().isWantInsideDog());
        wantPottyTrained.setChecked(profile.getBuyerProfile().isWantPottyTrained());
        breedFilterSwitch.setChecked(profile.getBuyerProfile().isWantBreed());
        ageFilterSwitch.setChecked(profile.getBuyerProfile().isWantAge());

        if (profile.getBuyerProfile().isWantAge()) {
            minAge.setVisibility(View.VISIBLE);
            maxAge.setVisibility(View.VISIBLE);
            String test = "";
            minAge.setText(test + profile.getBuyerProfile().getMinAge());
            maxAge.setText(test + profile.getBuyerProfile().getMaxAge());
        } else {
            minAge.setVisibility(View.INVISIBLE);
            maxAge.setVisibility(View.INVISIBLE);
        }

        if (profile.getBuyerProfile().isWantBreed()) {
            breedFilter.setVisibility(View.VISIBLE);
            breedFilter.setText(profile.getBuyerProfile().getBreedPreference());
        } else {
            breedFilter.setVisibility(View.INVISIBLE);
        }
    }
}
