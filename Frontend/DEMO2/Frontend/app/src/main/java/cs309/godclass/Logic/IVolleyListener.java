package cs309.godclass.Logic;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Interface for the logic classes used for modular design of helper methods
 */
public interface IVolleyListener {

    /**
     * Static URL for the round trip connections to the server
     */

    // This url is for the remote server.

     public static final String url = "http://coms-309-065.class.las.iastate.edu:8080";

    //public static final String url = "";

//     public String url = "https://7ccc856f-f6d7-4095-9842-70590220701a.mock.pstmn.io";
    // Testing on LOCALHOST uses the IP 10.0.2.2 instead of 127.0.0.1
   // public String url = "http://10.0.2.2:8080";

    /**
     * Method that is called upon a successful round trip
     * @param s
     *  JSON object recieved from the server
     * @throws JSONException
     */
    public void onSuccess(JSONObject s) throws JSONException;

    /**
     * Method that is called upon an error during a round trip
     * @param s
     *  JSON object revieved from the server
     */
    public void onError(String s);
}
