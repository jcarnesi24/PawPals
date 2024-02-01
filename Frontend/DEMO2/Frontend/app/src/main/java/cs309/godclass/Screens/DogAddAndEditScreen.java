package cs309.godclass.Screens;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONException;

import cs309.godclass.Logic.ProfileLogic;
import cs309.godclass.Network.ServerRequest;
import cs309.godclass.Objects.Dog;
import cs309.godclass.Objects.Profile;
import cs309.godclass.R;

/**
 * This screen is what the user sees when they want to add and or edit a dog.
 */
public class DogAddAndEditScreen extends AppCompatActivity implements View.OnClickListener, IView {

    EditText editNameText;
    EditText editBreedText;
    EditText editAgeText;
    EditText editBioText;
    Button backButton;
    Button saveButton;
    Button deleteButton;
    Profile profile;

    ImageButton imageButton;
    ImageView imageView;

    Switch pottyTrained;
    Switch insideDog;

    boolean isPottyTrained;
    boolean isInsideDog;

    Boolean dogImg = false;

    private static final int PICK_IMAGE_REQUEST_DOG = 1;
    boolean newDog = false;
    int dogID;

    /**
     * Creates the screen with all of the text views and buttons. Also retrieves the profile and dogID from previous activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_edit_dog_screen);

        Intent intent = getIntent();
        profile = (Profile) intent.getSerializableExtra("profile");
        dogID = (int) intent.getSerializableExtra("dogID");


        if (profile.getSellerProfile().getDogs().get(dogID).getName().equals("")) {
            newDog = true;
        }

        editNameText = findViewById(R.id.editDogName);
        editBreedText = findViewById(R.id.editDogBreed);
        editAgeText = findViewById(R.id.editDogAge);
        editBioText = findViewById(R.id.editDogBioText);

        pottyTrained = findViewById(R.id.pottyTrainedSwitch);
        insideDog = findViewById(R.id.insideDogSwitch);

        backButton = findViewById(R.id.backToListButton);
        saveButton = findViewById(R.id.saveDogButton);
        deleteButton = findViewById(R.id.deleteDogButton);

        imageButton = findViewById(R.id.chooseImageButtonDog);
        imageView = findViewById(R.id.imageViewDogSwipe);

        imageButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        pottyTrained.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isPottyTrained = b;
            }
        });

        insideDog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isInsideDog = b;
            }
        });

        setDog(profile);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select an image"), PICK_IMAGE_REQUEST_DOG);
            }
        });
    }

    /**
     * On click method that handles what happens when each of the different buttons are pressed.
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        Intent intent;
        boolean feildIsEmpty = false;

        switch (view.getId()){

            case R.id.backToListButton:
                if (newDog) {
                    profile.getSellerProfile().getDogs().remove(dogID);
                }
                break;
            case R.id.saveDogButton:
                if (editNameText.getText().toString().isEmpty() ||
                        editBreedText.getText().toString().isEmpty() ||
                        editAgeText.getText().toString().isEmpty()){
                      // imageView.getDrawable() == null ){
                    toastText("No field can be empty!");
                    feildIsEmpty = true;

                } else {
                    profile.getSellerProfile().getDogs().get(dogID).setName(editNameText.getText().toString());
                    profile.getSellerProfile().getDogs().get(dogID).setAge(editAgeText.getText().toString());
                    profile.getSellerProfile().getDogs().get(dogID).setBreed(editBreedText.getText().toString());
                    profile.getSellerProfile().getDogs().get(dogID).setBio(editBioText.getText().toString());
                    profile.getSellerProfile().getDogs().get(dogID).setInsideDog(isInsideDog);
                    profile.getSellerProfile().getDogs().get(dogID).setPottyTrained(isPottyTrained);

                    ServerRequest serverRequest = new ServerRequest();
                    final ProfileLogic logic = new ProfileLogic(this, serverRequest);

                    try {
                        if (newDog) {
                            logic.addDog(profile.getSellerProfile().getDogs().get(dogID), profile, "POST");
                        } else {
                            logic.addDog(profile.getSellerProfile().getDogs().get(dogID), profile, "PUT");
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case R.id.deleteDogButton:


                ServerRequest serverRequest = new ServerRequest();
                final ProfileLogic logic = new ProfileLogic(this, serverRequest);

                try {
                    logic.addDog(profile.getSellerProfile().getDogs().get(dogID), profile, "DELETE");
                    profile.getSellerProfile().getDogs().remove(dogID);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                break;
        }
        if(!feildIsEmpty) {
            intent = new Intent(DogAddAndEditScreen.this, DogListScreen.class);
            intent.putExtra("profile", profile);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_DOG && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            String dogPicUrl = imageUri.toString();

            // Use an image loading library like Glide or Picasso to set the image to the ImageView
            Glide.with(this).load(imageUri).into(imageView);
            imageButton.setVisibility(View.INVISIBLE);
            profile.getSellerProfile().getDogs().get(dogID).setDogImage(dogPicUrl);
        }
        dogImg = true;
    }

    /**
     * Show text method
     * @param s
     */
    @Override
    public void showText(String s) {
    }

    /**
     * Show text method
     * @param s
     */
    @Override
    public void toastText(String s) {
      //  Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setProfile(Profile profile) {
        String newDogId = profile.getFirstname();
        for (Dog dog : this.profile.getSellerProfile().getDogs()) {
            if (dog.getId().equals("0")) {
                dog.setId(newDogId);
            }
        }
    }

    /**
     * Set profile method that gets the information from the profile and adds it to the screen.
     * @param profile
     */
    public void setDog(Profile profile) {
        editNameText.setText(profile.getSellerProfile().getDogs().get(dogID).getName());
        editAgeText.setText(profile.getSellerProfile().getDogs().get(dogID).getAge());
        editBreedText.setText(profile.getSellerProfile().getDogs().get(dogID).getBreed());
        editBioText.setText(profile.getSellerProfile().getDogs().get(dogID).getBio());
        if(profile.getSellerProfile().getDogs().get(dogID).getDogImage() != null) {
            Uri dogImage = Uri.parse(profile.getSellerProfile().getDogs().get(dogID).getDogImage());
            Glide.with(this).load(dogImage).into(imageView);
            imageButton.setVisibility(View.INVISIBLE);
        }
        isPottyTrained = profile.getSellerProfile().getDogs().get(dogID).isPottyTrained();
        pottyTrained.setChecked(isPottyTrained);
        isInsideDog = profile.getSellerProfile().getDogs().get(dogID).isInsideDog();
        insideDog.setChecked(isInsideDog);
    }
}
