package cs309.godclass.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import cs309.godclass.Logic.RegistrationLogic;
import cs309.godclass.Objects.Profile;
import cs309.godclass.R;

/**
 * Represents the Selection Screen allowing users to choose their profile type.
 */
public class SelectionScreen extends AppCompatActivity implements View.OnClickListener{
    Button buyerButton;
    Button sellerButton;
    Button sitterButton;

    public Profile profile;

    public String hide;


    /**
     * Initializes the Selection Screen by setting up text views and buttons based on the user's profile type.
     *
     * @param savedInstanceState If the activity is re-initialized after being shut down, the bundle contains the most recent data; otherwise, it's null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_selection_screen);


        buyerButton = findViewById(R.id.buyerPage);
        sellerButton = findViewById(R.id.sellerPage);
        sitterButton = findViewById(R.id.sitterPage);

        buyerButton.setOnClickListener(this);
        sellerButton.setOnClickListener(this);
        sitterButton.setOnClickListener(this);
        profile = (Profile) getIntent().getSerializableExtra("profile");

        // hide = (String) getIntent().getSerializableExtra("hide");
        hide = profile.getHide();



        if(profile.getHide().equals("buyer")){
            buyerButton.setVisibility(View.INVISIBLE);
        }
        if(profile.getHide().equals("seller")){
            sellerButton.setVisibility(View.INVISIBLE);
        }
        if(profile.getHide().equals("sitter")){
            sitterButton.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * Handles the logic for each button click, redirecting to different profile type screens.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){

            case R.id.buyerPage:

                intent = new Intent(SelectionScreen.this, BuyerProfileScreen.class);
                intent.putExtra("profile", profile);
                profile.setUser("buyer");
                startActivity(intent);
                break;
            case R.id.sellerPage:
                intent = new Intent(SelectionScreen.this, SellerProfileScreen.class);
                intent.putExtra("profile", profile);
                profile.setUser("seller");
                startActivity(intent);
                break;
            case R.id.sitterPage:
                intent = new Intent(SelectionScreen.this, SitterProfileScreen.class);
                intent.putExtra("profile", profile);
                profile.setUser("sitter");
                startActivity(intent);
                break;
        }
    }
}






