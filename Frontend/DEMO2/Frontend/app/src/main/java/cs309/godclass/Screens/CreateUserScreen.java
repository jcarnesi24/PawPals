package cs309.godclass.Screens;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;



import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import cs309.godclass.Logic.CreateUserLogic;
import cs309.godclass.Network.ServerRequest;
import cs309.godclass.Objects.Profile;
import cs309.godclass.R;

/**
 * This is the screen that is displayed to the user when they want to create an account.
 */
public class CreateUserScreen extends AppCompatActivity implements IView, AdapterView.OnItemSelectedListener{
        Button submitBtn;
        TextView enterFirstname;
        TextView enterLastname;
        TextView enterEmail;
        TextView enterUsername;
        TextView enterPassword;
        TextView enterAddress;
        private TextView CreateUserErrorTextView;
        private Spinner spinnerDrop;
        private String valueFromSpinner;


    /**
     * On create for the create user screen. Sets all of the textviews and buttons. It also
     * initializes a drop down menu to choose what user.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.create_user_screen);


            enterFirstname = findViewById(R.id.EditFirstname);
            enterLastname = findViewById(R.id.EditLastname);
            enterEmail = findViewById(R.id.EditEmail);
            enterUsername = findViewById(R.id.EditUsername);
            enterPassword = findViewById(R.id.EditPassword);
            enterAddress = findViewById(R.id.EditAddress);
            CreateUserErrorTextView = findViewById(R.id.CreateUserErrorMessageField);

            submitBtn = findViewById(R.id.submitButton);

            spinnerDrop = findViewById(R.id.spinner);

            spinnerDrop.setOnItemSelectedListener(this);

            String[] userOptions = getResources().getStringArray(R.array.user_options);
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,userOptions);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDrop.setAdapter(adapter);

            ServerRequest serverRequest = new ServerRequest();
            final CreateUserLogic logic = new CreateUserLogic(this, serverRequest, this);

        /**
         * Submit button logic calls the createUserLogic to add the fields in order to create a new user
         */
        submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String firstname = enterFirstname.getText().toString();
                        String lastname = enterLastname.getText().toString();
                        String email = enterEmail.getText().toString();
                        String username = enterUsername.getText().toString();
                        String password = enterPassword.getText().toString();
                        String user = valueFromSpinner;
                        String address = enterAddress.getText().toString();
                        logic.CreateUser(firstname,lastname,email,username,password,user,address, "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

    /**
     * Shows error text view
     * @param s
     */
    @Override
    public void showText(String s) {
        CreateUserErrorTextView.setText(s);
        CreateUserErrorTextView.setVisibility(View.VISIBLE);
    }

    /**
     * Displays text for a short period.
     * @param s
     */
    @Override
    public void toastText(String s) {

    }

    /**
     * set Profile method
     * @param profile
     */
    @Override
    public void setProfile(Profile profile) {

    }

    /**
     * Logic for what to do when an item is selected from drop menu
     * @param parent The AdapterView where the selection happened
     * @param view The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinner) {
            valueFromSpinner = parent.getItemAtPosition(position).toString();
        }
    }

    /**
     * Logic for what happens when no items are selected
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if(parent.getId() == R.id.spinner) {
            valueFromSpinner = parent.toString();
        }
    }
}
