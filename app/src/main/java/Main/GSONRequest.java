package Main;

import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GSONRequest extends StringRequest {

    public String json;
    public GSONRequest(int method, String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.json = json;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> m = new HashMap<>();
        m.put("jssonString",json);
        return m;
    }
}
