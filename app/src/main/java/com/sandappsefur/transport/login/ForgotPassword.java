package com.sandappsefur.transport.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sandappsefur.transport.R;
import com.sandappsefur.transport.home.Home;
import com.sandappsefur.transport.home.HomeD;

import Main.GSONRequest;
import Main.VolleySingleton;
import Main.appSetting;
import Main.signup.signinadapter;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }

    public void btnSigninfp(View view) {

        Intent i = new Intent(this, login.class);
        startActivity(i);

    }

    public void btnSendfgtPswrd(View view){

        try {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wailt...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.show();

            String email = ((EditText) findViewById(R.id.idusernamefp)).getText().toString();

            signinadapter u2 = new signinadapter(email, null, appSetting.userid, appSetting.userloginid, appSetting.usertypeid);
            final Gson gson = new Gson();
            final String jsonString = gson.toJson(u2);


            String url = appSetting.volleyUrl + "forgotpwdapp_serv?jsonString=" + jsonString;


            Response.Listener rl = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
                    System.out.println(response);
                    if (response.equals("1")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "mail send Successful", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("2")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Not matching email", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("3")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Your mail not working", Toast.LENGTH_SHORT).show();
                    }else if (response.equals("4")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                    }

                }
            };

            Response.ErrorListener re = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    if (error instanceof NetworkError) {
                    } else if (error instanceof ServerError) {
                    } else if (error instanceof AuthFailureError) {
                    } else if (error instanceof ParseError) {
                    } else if (error instanceof NoConnectionError) {
                    } else if (error instanceof TimeoutError) {
                        Toast.makeText(getApplicationContext(),
                                "Oops. Timeout error!",
                                Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(getApplicationContext(), "Fail to Connect With Server", Toast.LENGTH_SHORT).show();
                    System.out.println("---------------------------------------" + error);
                }
            };


            GSONRequest request = new GSONRequest(1, url, rl, re);
            request.json = jsonString;

            VolleySingleton.getVolleySingleton(getApplicationContext()).getRequestQueue().add(request);


        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
