package cs309.godclass.Network;

import org.json.JSONObject;

import cs309.godclass.Logic.IVolleyListener;

public interface IServerRequest {
    /**
     * Sends a request to the server with the nature of the request being determined by the params
     * @param url
     *  URL of the request
     * @param newUserObj
     *  Body of the reqeust method
     * @param methodType
     *  Type of request
     */
    public void sendToServer(String url, JSONObject newUserObj, String methodType);

    /**
     * updates the logic that is being used
     * @param logic
     *  New logic
     */
    public void addVolleyListener(IVolleyListener logic);
}
