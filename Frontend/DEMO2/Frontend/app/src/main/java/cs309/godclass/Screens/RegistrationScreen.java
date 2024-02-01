package cs309.godclass.Screens;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import cs309.godclass.AppController;
import cs309.godclass.Logic.RegistrationLogic;
import cs309.godclass.Network.ServerRequest;
import cs309.godclass.Objects.Profile;
import cs309.godclass.R;

/**
 * Represents the Registration Screen allowing users to register or sign in.
 */
public class RegistrationScreen extends AppCompatActivity implements IView {
    TextView nameTextView;
    //TextView emailTextView;
    TextView passwordTextView;
    public TextView registerErrorTextView;
    Button registerSubmitButton;

    Button signInBtn;


    Button nextScreenButton;

    /**
     * Initializes the Registration Screen by setting up text views and buttons for user registration and sign in.
     *
     * @param savedInstanceState If the activity is re-initialized after being shut down, the bundle contains the most recent data; otherwise, it's null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AppController();
        setContentView(R.layout.activity_registration_screen);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        nameTextView = findViewById(R.id.nameTextField);

        passwordTextView = findViewById(R.id.passwordTextField);
        registerErrorTextView = findViewById(R.id.registerErrorMessageField);
        registerSubmitButton = findViewById(R.id.registerSubmitButton);


        signInBtn = findViewById(R.id.signInButton);



        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            nameTextView.setAutofillHints(View.AUTOFILL_HINT_NAME);
            //emailTextView.setAutofillHints(View.AUTOFILL_HINT_USERNAME);
            passwordTextView.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);
        }

        ServerRequest serverRequest = new ServerRequest();
        final RegistrationLogic logic = new RegistrationLogic(this, serverRequest, this);
        /**
         * Submit button logic
         */
        registerSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String name = nameTextView.getText().toString();
                   // String email = emailTextView.getText().toString();
                    String password = passwordTextView.getText().toString();
                    logic.registerUser(name, password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        /**
         * sign in button logic
         */
        signInBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new  Intent(RegistrationScreen.this, CreateUserScreen.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Displays error text on the screen.
     *
     * @param s The error message to be displayed.
     */
    @Override
    public void showText(String s) {
        registerErrorTextView.setText(s);
        registerErrorTextView.setVisibility(View.VISIBLE);
    }

    /**
     * Displays text as a toast message for a short duration.
     *
     * @param s The message to display as a toast.
     */
    @Override
    public void toastText(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }


    /**
     * Placeholder method for setting the profile. Currently not implemented.
     *
     * @param profile The user profile to be set.
     */
    @Override
    public void setProfile(Profile profile) {

    }
}
