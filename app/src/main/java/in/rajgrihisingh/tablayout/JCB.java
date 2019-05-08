package in.rajgrihisingh.tablayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import in.rajgrihisingh.R;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.model.VolleySingleton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class JCB extends Fragment {

    EditText total_working_hour,jcb_close,jcb_start;
    String total_working_hours,jcb_closes,jcb_starts;

    Button jcb_submit;
    String token;
    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.jcb_details, container, false);

        jcb_start=(EditText)rootView.findViewById(R.id.jcb_start);
        jcb_close=(EditText)rootView.findViewById(R.id.jcb_close);
        total_working_hour=(EditText)rootView.findViewById(R.id.total_working_hour);

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token=sp.getString("keyid", "");

        jcb_submit=(Button)rootView.findViewById(R.id.jcb_submit);
        jcb_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        return rootView;
    }

    private void attemptLogin() {

        jcb_starts = jcb_start.getText().toString();
        jcb_closes = jcb_close.getText().toString();
        total_working_hours = total_working_hour.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(jcb_starts)) {
            jcb_start.setError(getString(R.string.error_field_required));
            focusView = jcb_start;
            cancel = true;
        }
        if (TextUtils.isEmpty(jcb_closes)) {
            jcb_close.setError(getString(R.string.error_field_required));
            focusView = jcb_close;
            cancel = true;
        }
        if (TextUtils.isEmpty(total_working_hours)) {
            total_working_hour.setError(getString(R.string.error_field_required));
            focusView = total_working_hour;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        }else {
            Authenticate();
        }
    }

    public void Authenticate(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADD_JCB,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                Toast.makeText(getActivity(), obj.getString("message"),Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), "Error",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("jcb_start", jcb_starts);
                params.put("jcb_end", jcb_closes);
                params.put("total_hours", total_working_hours);
                params.put("token", token);

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

        jcb_start.setText("");
        jcb_close.setText("");
        total_working_hour.setText("");

    }
}
