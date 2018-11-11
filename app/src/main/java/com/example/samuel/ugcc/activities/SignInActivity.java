package com.example.samuel.ugcc.activities;

/**
 * Created by Samuel on 10-Mar-18.
 */
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.samuel.ugcc.NetworkUtil;
import com.example.samuel.ugcc.R;
import com.example.samuel.ugcc.helper.MySingleton;
import com.example.samuel.ugcc.helper.SessionHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity{
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_USERID = "userId";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";
    private EditText etUserId;
    private EditText etPassword;
    private String userId;
    private String password;
    private ProgressDialog pDialog;
    private String login_url =  NetworkUtil.URL + "ugccc/api/login.php";
    private SessionHandler session;
    ImageView sback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());

        if(session.isLoggedIn()){
            loadDashboard();
        }
        setContentView(R.layout.activity_signin);

        sback = (ImageView)findViewById(R.id.sinb);
        sback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(SignInActivity.this,WelcomeActivity.class);
                startActivity(it);
            }
        });

        etUserId = (EditText) findViewById(R.id.etUserId);
        etPassword = (EditText) findViewById(R.id.etPassword);

//        Button register = findViewById(R.id.btnRegister);
        Button login = (Button) findViewById(R.id.btnSignin);

        //Launch Registration screen when Register Button is clicked
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(i);
//                finish();
//            }
//        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                userId = etUserId.getText().toString().toLowerCase().trim();
                password = etPassword.getText().toString().trim();
                if (validateInputs()) {
                    login();
                }
            }
        });
    }

    /**
     * Launch Dashboard Activity on Successful Login
     */
    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();

    }

    /**
     * Display Progress bar while Logging in
     */

    private void displayLoader() {
        pDialog = new ProgressDialog(SignInActivity.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void login() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USERID, userId);
            request.put(KEY_PASSWORD, password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, login_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got logged in successfully

                            if (response.getInt(KEY_STATUS) == 0) {
                                session.loginUser(userId,response.getString(KEY_FULL_NAME));
                                loadDashboard();

                            }else{
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    /**
     * Validates inputs and shows error if any
     * @return
     */
    private boolean validateInputs() {
        if(KEY_EMPTY.equals(userId)){
            etUserId.setError("UserId cannot be empty");
            etUserId.requestFocus();
            return false;
        }
        if(KEY_EMPTY.equals(password)){
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }
}
//    ImageView sback;sback = (ImageView)findViewById(R.id.sinb);
//    TextView signin;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signin);
//        sback = (ImageView)findViewById(R.id.sinb);
//        sback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(SignInActivity.this,WelcomeActivity.class);
//                startActivity(it);
//            }
//        });
//
//        signin = (TextView) findViewById(R.id.signin);
//        signin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//}
