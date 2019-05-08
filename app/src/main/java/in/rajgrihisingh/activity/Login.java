package in.rajgrihisingh.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import in.rajgrihisingh.model.ConnectivityReceiver;
import in.rajgrihisingh.pref.PrefManager;
import in.rajgrihisingh.pref.ProfilePrefManager;
import in.rajgrihisingh.pref.ProfileUser;
import org.json.JSONException;
import org.json.JSONObject;
import in.rajgrihisingh.R;
import java.util.HashMap;
import java.util.Map;
import in.rajgrihisingh.api.URLs;
import in.rajgrihisingh.model.VolleySingleton;
import in.rajgrihisingh.pref.SharedPrefManager;
import in.rajgrihisingh.pref.User;

public class Login extends AppCompatActivity  {

    private EditText UserView;
    private EditText mPasswordView;
    String username,password;

    private static final String SHARED_PREF_NAME = "Rajgrihisinghpref";
    private static final String KEY_ID = "keyid";
    private static final String KEY_BRANCH_ID= "branch_id";

    private PrefManager prefManager;
    String tokens,branch_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserView = (EditText) findViewById(R.id.user_id);
        mPasswordView = (EditText) findViewById(R.id.password);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);
        branch_id= prefs.getString(KEY_BRANCH_ID, null);

        /*try {
            Log.d("branch_id", branch_id);
        }catch (NullPointerException e){
            e.printStackTrace();
        }*/

     if (SharedPrefManager.getInstance(this).isLoggedIn()) {
          try {
              prefManager = new PrefManager(Login.this);
              if (branch_id.equals("1")) {
                  finish();
                  startActivity(new Intent(this, Ganpati.class));

              } else if (branch_id.equals("2")) {
                  finish();
                  startActivity(new Intent(this, RGandSons.class));

              } else if (branch_id.equals("3")) {
                  finish();
                  startActivity(new Intent(this, MainActivity.class));
              }
          }catch (NullPointerException e){
              e.printStackTrace();
          }
      }
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button SignInButton = (Button) findViewById(R.id.sign_in_button);
        SignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                    checkConnection();
            }
        });
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack ( boolean isConnected){
        String message;
        int color;
        if (isConnected) {
            attemptLogin();
        } else {
            message = "connect your internet.";
            color = Color.RED;
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
    }
    private void attemptLogin() {
        UserView.setError(null);
        mPasswordView.setError(null);

        username = UserView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(username)) {
            UserView.setError(getString(R.string.error_field_required));
            focusView = UserView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            Authenticate();
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public void Authenticate(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                           if (obj.getBoolean("status")) {

                                JSONObject userJson = obj.getJSONObject("user");
                                String token=userJson.getString("token");
                                String name=userJson.getString("name");
                                String branch_id=userJson.getString("branch_id");
                                String mobile=userJson.getString("mobile");
                                String email=userJson.getString("email");
                                JSONObject sites = userJson.getJSONObject("site");

                               String id=sites.getString("id");
                               String s_name=sites.getString("name");
                               String type=sites.getString("type");
                               String address=sites.getString("address");


                               String description=sites.getString("description");
                               String start_date=sites.getString("start_date");
                               String supervisor=sites.getString("supervisor");
                               String status=sites.getString("status");
                               String supervisor_name=sites.getString("supervisor_name");

                               ProfileUser profileUser = new ProfileUser(
                                       sites.getString("name"),
                                       sites.getString("address"),
                                       sites.getString("description"),
                                       sites.getString("supervisor_name")
                               );

                                User user = new User(
                                        userJson.getString("token"),
                                        userJson.getString("mobile"),
                                        userJson.getString("branch_id"),
                                        userJson.getString("name"),
                                        userJson.getString("email")
                                );
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                finish();

                               ProfilePrefManager.getInstance(getApplicationContext()).userProfile(profileUser);
                               finish();
                               if(branch_id.equals("1")) {
                                   Intent intent = new Intent(getApplicationContext(), Ganpati.class);
                                   intent.putExtra("name", name);
                                   intent.putExtra("token", token);
                                   intent.putExtra("email", email);
                                   startActivity(intent);
                                   finish();
                               }

                               if(branch_id.equals("2")) {
                                   Intent intent = new Intent(getApplicationContext(), RGandSons.class);
                                   intent.putExtra("name", name);
                                   intent.putExtra("token", token);
                                   intent.putExtra("email", email);
                                   startActivity(intent);
                                   finish();
                               }
                               if(branch_id.equals("3")) {
                                   Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                   intent.putExtra("name", name);
                                   intent.putExtra("token", token);
                                   intent.putExtra("email", email);
                                   startActivity(intent);
                                   finish();
                               }

                            } else if(!obj.getBoolean("status")) {

                                String error=obj.getString("error");
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

                            }else {
                               Toast.makeText(getApplicationContext(), "Connection error..", Toast.LENGTH_SHORT).show();
                           }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Add Sites", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    @Override
    public void onBackPressed() {
        backButtonHandler();
        return;
    }
    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);
        alertDialog.setTitle("Leave application?");
        alertDialog.setMessage("Are you sure you want to leave the application?");
        alertDialog.setIcon(R.mipmap.ic_launcher_round);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialog.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}

