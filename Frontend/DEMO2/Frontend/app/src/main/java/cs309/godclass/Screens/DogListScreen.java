package cs309.godclass.Screens;

import static java.lang.Boolean.valueOf;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.text.TextWatcher;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import java.util.ArrayList;
import java.util.HashMap;

import cs309.godclass.Objects.Dog;
import cs309.godclass.Objects.Profile;
import cs309.godclass.R;

/**
 * Page where sellers can view, search through, and navigate to all of their dogs
 */
public class DogListScreen extends AppCompatActivity implements View.OnClickListener, IView, AdapterView.OnItemSelectedListener {

    Button backButton;
    Button addDogButton;
    EditText searchBar;
    LinearLayout dogList;

    HashMap<Dog, ArrayList<TextView>> dogMap;
    HashMap<Dog, ImageView> dogImage;
    private static Uri defaultImageUriForTesting;


    Profile profile;
    Spinner spinnerDrop;
    String valueFromSpinner = "";

    /**
     * Create method that sets all of the buttons and text boxes. This also initializes the search bar.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dog_list_screen);

        backButton = findViewById(R.id.backToSellerButton);
        searchBar = findViewById(R.id.dogSearch);
        dogList = findViewById(R.id.dogLayout);
        addDogButton = findViewById(R.id.addDog);

        addDogButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        searchBar.setOnClickListener(this);

        dogMap = new HashMap<>();
        dogImage = new HashMap<>();

        spinnerDrop = findViewById(R.id.sortSelector);

        spinnerDrop.setOnItemSelectedListener(this);

        String[] userOptions = getResources().getStringArray(R.array.sort_options);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,userOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDrop.setAdapter(adapter);


        //Text change Listener for the Search Bar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterContent(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        profile = (Profile) getIntent().getSerializableExtra("profile");


        setScreen(profile);

    }


    /**
     * Method to handle what button is being pressed.
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent intent;

        // Detects the back button hits
        if ( view.getId() == R.id.backToSellerButton) {
            intent = new Intent(DogListScreen.this, SellerProfileScreen.class);
            intent.putExtra("profile", profile);
            startActivity(intent);
        } else if (view.getId() == R.id.dogSearch) {
            searchBar.setText("");
        } else if (view.getId() == R.id.addDog) {
            intent = new Intent(DogListScreen.this, DogAddAndEditScreen.class);
            profile.getSellerProfile().getDogs().add(new Dog());

            intent.putExtra("profile", profile);
            intent.putExtra("dogID", profile.getSellerProfile().getDogs().size() - 1);
            startActivity(intent);
        }else {
            //Every other button is attached to a dog, and will pull the unique tag value
            //from the button that is generated for the dog
            int dogID = (int) view.getTag();

            intent = new Intent(DogListScreen.this, DogAddAndEditScreen.class);
            intent.putExtra("profile", profile);
            intent.putExtra("dogID", dogID);
            startActivity(intent);

        }
    }


    /**
     * Helper Method for the search Bar changed list of items to fit the query passed to function
     * @param query
     *  Value of search bar to be searched for
     */
    private void filterContent(String query) {

        for (Dog dog : dogMap.keySet()) {
            ArrayList<TextView> dogTextViews = dogMap.get(dog);
            ImageView dogView = dogImage.get(dog);
            boolean shown = false;

            String compText = null;

            if (valueFromSpinner.contains("name")) {
                compText = dogTextViews.get(0).getText().toString().substring(6) ;
            } else if (valueFromSpinner.contains("breed")) {
                compText = dogTextViews.get(1).getText().toString().substring(7);
            } else if (valueFromSpinner.contains("age")) {
                compText = dogTextViews.get(2).getText().toString().substring(5);
            } else {
                ArrayList<String> allStrings = new ArrayList<>();

                allStrings.add(dogTextViews.get(0).getText().toString().substring(6));
                allStrings.add(dogTextViews.get(1).getText().toString().substring(7));
                allStrings.add(dogTextViews.get(2).getText().toString().substring(5));

                for (String string : allStrings) {
                    if (string.toLowerCase().contains(query.toLowerCase())) {
                        shown = true;
                        break; // If one TextView matches, show them all
                    }
                }
            }

            if (compText != null && compText.toLowerCase().contains(query.toLowerCase())) {
                shown = true;
            }

            // Show or hide all associated TextViews
            for (TextView textView : dogTextViews) {
                textView.setVisibility(shown ? View.VISIBLE : View.GONE);
            }

            dogView.setVisibility(shown ? View.VISIBLE : View.GONE);
        }

    }

    /**
     * Show Text method
     * @param s
     */
    @Override
    public void showText(String s) {

    }

    /**
     * Error and testing detection
     * @param s
     *  String to be sentthan
     */
    @Override
    public void toastText(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
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

    public static void setDefaultImageUriForTesting(Uri uri) {
        defaultImageUriForTesting = uri;
    }


    /**
     * Set profile method that iterates through the array list of dogs and makes text views and buttons accordingly.
     * @param profile
     */
    public void setScreen(Profile profile) {
        int i = 0;
        for (Dog dog: profile.getSellerProfile().getDogs()) {
            ArrayList<TextView> dogTextViews = new ArrayList<TextView>();

            TextView dogName = new TextView(this);
            String message = "Name: " + dog.getName();
            dogName.setText(message);
            dogList.addView(dogName);
            dogTextViews.add(dogName);

            TextView dogBreed = new TextView(this);
            message = "Breed: " + dog.getBreed();
            dogBreed.setText(message);
            dogList.addView(dogBreed);
            dogTextViews.add(dogBreed);

            TextView dogAge = new TextView(this);
            message = "Age: " + dog.getAge();
            dogAge.setText(message);
            dogList.addView(dogAge);
            dogTextViews.add(dogAge);

            if(defaultImageUriForTesting == null) {
                ImageView dogView = new ImageView(this);
                String dogImgString = dog.getDogImage();
                Uri dogImg = Uri.parse(dogImgString);

                Glide.with(this)
                        .load(dogImg)
                        .into(dogView);
                dogList.addView(dogView, 300, 300);
                dogImage.put(dog, dogView);
            }else{
                ImageView dogView = new ImageView(this);
                Uri dogImg = defaultImageUriForTesting;
                Glide.with(this)
                        .load(dogImg)
                        .into(dogView);
                dogList.addView(dogView, 300, 300);
                dogImage.put(dog, dogView);

            }

            Button dogButton = new Button(this);
            dogButton.setText("View This Dog");
            dogButton.setTag(i);
            dogButton.setOnClickListener(this);
            dogList.addView(dogButton);
            dogTextViews.add(dogButton);

            dogMap.put(dog, dogTextViews);


            i++;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.sortSelector) {
            valueFromSpinner = adapterView.getItemAtPosition(i).toString();
            filterContent(searchBar.getText().toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
