package cs309.godclass.Screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;

import cs309.godclass.Network.WebSocketListener;
import cs309.godclass.Network.WebSocketManager;
import cs309.godclass.R;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import cs309.godclass.Objects.Profile;

/**
 * Class that shows the visual representation of the chat screen.
 */
public class ChatScreen extends AppCompatActivity implements WebSocketListener {
    Button sendButton;

    Button backButton;
    TextView message;
    TextView conversation;
    private Profile profile;
    String myId;
    String otherId;

    String path;


    // Local
   // String BASE_URL = "ws://10.0.2.2:8080/chat/";
    // Remote server
    String BASE_URL = "ws://coms-309-065.class.las.iastate.edu:8080/chat/";


    /**
     * Creates the chat screen and adds all of the buttons and texts.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_sreen);

        sendButton = findViewById(R.id.sendBtn);
        message = findViewById(R.id.MessageEditText);
        conversation = findViewById(R.id.ConversationText);
        backButton = findViewById(R.id.chatScreenBackBtn);

        profile = (Profile) getIntent().getSerializableExtra("profile");

        otherId = (String) getIntent().getSerializableExtra("otherUser");
        myId = profile.getId();

        path = myId + "/" +  otherId;

        //String serverUrl = BASE_URL + profile.getUsername();

        String serverUrl = BASE_URL + path;

        WebSocketManager.getInstance().connectWebSocket(serverUrl);

        WebSocketManager.getInstance().setWebSocketListener(ChatScreen.this);

        sendButton.setOnClickListener(v -> {
            try {
                WebSocketManager.getInstance().sendMessageText(message.getText().toString());
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage().toString());
            }
        });

        /**
         * On click listener for the back button. Sends user back to message list screen
         */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(ChatScreen.this, MessageListScreen.class);
                intent.putExtra("profile", profile);
                intent.putExtra("otherUser", otherId);
                startActivity(intent);
            }
        });

}

    /**
     * Method to display the message on the screen.
     * @param message The received WebSocket message.
     */
    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            String s = conversation.getText().toString();
            conversation.setText(s + "\n"+message);
        });
    }

    /**
     * Method that closes the web socket
     * @param code   The status code indicating the reason for closure.
     * @param reason A human-readable explanation for the closure.
     * @param remote Indicates whether the closure was initiated by the remote endpoint.
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = conversation.getText().toString();
            conversation.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    /**
     * Executes when the Web Socket is opened
     * @param handshakedata Information about the server handshake.
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    /**
     * Executes when the Web Socket has an error.
     * @param ex The exception that describes the error.
     */
    @Override
    public void onWebSocketError(Exception ex) {

    }
}
