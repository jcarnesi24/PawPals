package cs309.godclass.Logic;

import org.json.JSONException;
import org.json.JSONObject;

public interface IVolleyListener {
    public void onSuccess(JSONObject s) throws JSONException;
    public void onError(String s);
}
