package cs309.godclass.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cs309.godclass.Logic.IVolleyListener;
import cs309.godclass.Logic.ProfileLogic;
import cs309.godclass.Network.IServerRequest;
import cs309.godclass.Network.ServerRequest;
import cs309.godclass.Objects.Dog;
import cs309.godclass.Objects.Message;
import cs309.godclass.Objects.Profile;
import cs309.godclass.R;
/**
 * Represents the Message List Screen where users can view and interact with their messages.
 */
public class MessageListScreen extends AppCompatActivity implements IView, View.OnClickListener, IVolleyListener {
    Button backButton;
    EditText searchBar;
    LinearLayout messageList;

    Profile profile;

    String otherId;

    HashMap<Message, ArrayList<TextView>> messageMap;
    HashMap<Message, RatingBar> ratingMap;

    /**
     * Initializes the Message List Screen by setting up the layout, buttons, and handling interactions.
     *
     * @param savedInstanceState If the activity is re-initialized after being shut down, the bundle contains the most recent data; otherwise, it's null.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_list_screen);

        backButton = findViewById(R.id.backToButton);
        searchBar = findViewById(R.id.messageSearch);

        messageList = findViewById(R.id.MessagesListLinearLayout);

        messageMap = new HashMap<>();
        ratingMap = new HashMap<>();

        profile = (Profile) getIntent().getSerializableExtra("profile");
        otherId = (String) getIntent().getSerializableExtra("otherUser");


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

        backButton.setOnClickListener(this);

        setProfile(profile);

    }

    /**
     * Filters and displays messages based on a search query.
     *
     * @param query The search query text to filter messages.
     */
    private void filterContent(String query) {
        for (Message message : messageMap.keySet()) {
            ArrayList<TextView> MessageTextView = messageMap.get(message);
            RatingBar ratingBar = ratingMap.get(message);
            boolean shown = false;

            String nameText = MessageTextView.get(0).getText().toString();

            if (nameText.toLowerCase().contains(query.toLowerCase())) {
                shown = true;
            }


            // Show or hide all associated TextViews
            for (TextView textView : MessageTextView) {
                textView.setVisibility(shown ? View.VISIBLE : View.GONE);
            }
            ratingBar.setVisibility(shown ? View.VISIBLE : View.GONE);
        }

    }

    /**
     * Placeholder method for displaying text (not currently implemented).
     *
     * @param s The text to display.
     */
    @Override
    public void showText(String s) {

    }

    /**
     * Placeholder method for displaying text as a toast message (not currently implemented).
     *
     * @param s The text to display as a toast.
     */
    @Override
    public void toastText(String s) {

    }

    /**
     * Sets up the user profile by populating the message list.
     *
     * @param profile The user's profile containing messages to be displayed.
     */
    @Override
    public void setProfile(Profile profile) {
        int i = 0;
        for (Message message: profile.getMessages()) {
            ArrayList<TextView> messageTextViews = new ArrayList<TextView>();

            Button messageButton = new Button(this);

            messageButton.setText(message.getOtherUsername());
            messageButton.setTag(i);
            messageButton.setOnClickListener(this);
            messageList.addView(messageButton);
            messageTextViews.add(messageButton);

            RatingBar ratingBar = new RatingBar(this);
            // Set layout parameters for the RatingBar
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 10, 0, 0); // Adjust margins as needed
            ratingBar.setLayoutParams(params);

            if (message.getOtherUserType().equals("Seller") || message.getOtherUserType().equals("Sitter")) {
                messageList.addView(ratingBar);

                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                        if (b) {
                            updateRating(rating, message.getOtherUserType());
                        }
                    }
                });
            }

            messageMap.put(message, messageTextViews);
            ratingMap.put(message, ratingBar);

            i++;
        }
    }

    private void updateRating(double rating, String userType) {
        ServerRequest serverRequest = new ServerRequest();
        ProfileLogic logic = new ProfileLogic(this, serverRequest);


        logic.updateRating(otherId, userType, String.valueOf(rating));
    }


    /**
     * Handles click events for various buttons and interacts with user messages.
     *
     * @param view The view or button that was clicked.
     */
    @Override
    public void onClick(View view) {
        Intent intent;
        if (view.getId() == R.id.backToButton) {
            if (profile != null && profile.getCurrUser() != null) {
                if (profile.getCurrUser().equals("buyer")) {
                    intent = new Intent(MessageListScreen.this, BuyerProfileScreen.class);
                } else if (profile.getCurrUser().equals("seller")) {
                    intent = new Intent(MessageListScreen.this, SellerProfileScreen.class);
                } else {
                    intent = new Intent(MessageListScreen.this, SitterProfileScreen.class);
                }

                intent.putExtra("profile", profile);
                startActivity(intent);
            }
        }else {
            int position = (int) view.getTag(); // Get the position associated with the clicked button
            Message message = profile.getMessages().get(position); // Retrieve the corresponding message
           // String myId = profile.getId();
            otherId = message.getId();
//            ServerRequest serverRequest = new ServerRequest();
////            ProfileLogic logic = new ProfileLogic(this,serverRequest);
////
////            try {
////                logic.sendMessageInfo(myId, otherId);
////            } catch (JSONException e) {
////                throw new RuntimeException(e);
////            }

            intent = new Intent(MessageListScreen.this, ChatScreen.class);
            intent.putExtra("profile", profile);
            intent.putExtra("otherUser", otherId);
            startActivity(intent);

        }
    }

    /**
     * Method called when the server request is successful (not currently implemented).
     *
     * @param s The successful response.
     * @throws JSONException On handling JSON exceptions.
     */
        @Override
    public void onSuccess(JSONObject s) throws JSONException {

    }

    /**
     * Method called when there's an error in the server request (not currently implemented).
     *
     * @param s The error message.
     */
    @Override
    public void onError(String s) {

    }
}


