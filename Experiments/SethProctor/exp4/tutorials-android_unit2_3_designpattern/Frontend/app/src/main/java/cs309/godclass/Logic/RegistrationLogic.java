package cs309.godclass.Logic;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import cs309.godclass.IView;
import cs309.godclass.Network.IServerRequest;
import cs309.godclass.Network.ServerRequest;
import cs309.godclass.R;
import cs309.godclass.RegistrationScreen;

public class RegistrationLogic implements IVolleyListener {

    IView r;
    IServerRequest serverRequest;
    private ProgressDialog pDialog;

    private TextView msgResponse;
    private String TAG = RegistrationLogic.class.getSimpleName();
    public RegistrationLogic(IView r, IServerRequest serverRequest) {
        this.r = r;
        this.serverRequest = serverRequest;
        serverRequest.addVolleyListener(this);
    }

    public void registerUser(String name, String password) throws JSONException {
       // final String url = "http://10.0.2.2:8080/";
        final String url = "https://7ccc856f-f6d7-4095-9842-70590220701a.mock.pstmn.io/name";
        JSONObject newUserObj = new JSONObject();
        newUserObj.put("name", name);
        //newUserObj.put("email", email);
        newUserObj.put("password", password);

        serverRequest.sendToServer(url, newUserObj, "POST");

    }

    @Override
    public void onSuccess(JSONObject s) throws JSONException {
        String getID = s.getString("ID");

        if (getID.equals("1")) {
            //startActivity(new Intent(getApplicationContext(), LoginScreen.class));
            r.showText("You are logged in!");
            //r.showText(correctName);

        } else {
            r.showText("Error with request, please try again!!!!!!!!!!!");
        }
    }

    @Override
    public void onError (String errorMessage) {
        r.toastText(errorMessage);
        r.showText("Error with request, please try again!!");
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())

            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }
    private void makeJsonObjReq() {
        final String url = "http://10.0.2.2:8080/";
        showProgressDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        msgResponse.setText(response.toString());
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideProgressDialog();
            }
        });
    }
}

