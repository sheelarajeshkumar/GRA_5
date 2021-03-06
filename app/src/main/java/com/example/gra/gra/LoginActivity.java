package com.example.gra.gra;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bLogin = (Button) findViewById(R.id.bLogin);

        final TextView registerLink = (TextView) findViewById(R.id.tvRegisterHere);

        registerLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        bLogin.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String mobile_number = etMobileNumber.getText().toString();
                final String password = etPassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success){
                                collectVariables(jsonObject);
                                decideTransitionActivity();

                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry",null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(mobile_number,password,responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }

    private void decideTransitionActivity() {
        String userType = MyGlobalVariable.getUserType();

        if(userType.equals("0")){
            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
            LoginActivity.this.startActivity(intent);
        }
        else if(userType.equals("1")){
            Intent intent = new Intent(LoginActivity.this,govtOffcHomeActivity.class);
            LoginActivity.this.startActivity(intent);
        }
    }

    void collectVariables(JSONObject jsonObject){
        try {
            String aadhar_number = jsonObject.getString("aadhar_number");
            String userType = jsonObject.getString("userType");
            String email = jsonObject.getString("email");
            String mobileNumber = jsonObject.getString("mobile_number");
            String password = jsonObject.getString("password");

            MyGlobalVariable.setMyAadharNumber(aadhar_number);
            MyGlobalVariable.setUserType(userType);
            MyGlobalVariable.setEmail(email);
            MyGlobalVariable.setMobileNumber(mobileNumber);
            MyGlobalVariable.setPassword(password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
