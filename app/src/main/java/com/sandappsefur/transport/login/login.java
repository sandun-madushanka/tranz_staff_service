package com.sandappsefur.transport.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.gson.Gson;
import com.sandappsefur.transport.R;
import com.sandappsefur.transport.home.Home;
import com.sandappsefur.transport.home.HomeD;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import Main.GSONRequest;
import Main.VolleySingleton;
import Main.appSetting;
import Main.signup.signinadapter;
import Main.signup.signupdriver;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }

    public void btnSignup(View v) {
        Intent i = new Intent(this, register.class);
        startActivity(i);
    }

    public void btnSignin(View v) {

//        Intent i = new Intent(this,Home.class);
//        startActivity(i);
//
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wailt...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();

        final String email = ((EditText) findViewById(R.id.idusername)).getText().toString();
        final String password = ((EditText) findViewById(R.id.idpassword)).getText().toString();

        signinadapter u2 = new signinadapter(email, password, appSetting.userid, appSetting.userloginid, appSetting.usertypeid);
        final Gson gson = new Gson();
        final String jsonString = gson.toJson(u2);



        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                try {

                    URL url = new URL(appSetting.volleyUrl + "signinapp_serv?usnm="+email+"&pwd="+password);
                    con = (HttpURLConnection)  url.openConnection();

                    con.setRequestMethod("GET");


                    if(con.getResponseCode() == 200){//meke HTTP_Ok wenuwata 200 dannath puluwn
                        System.out.println("ifffffffffffffffffff"+"---------------------------------");
                        InputStream is = con.getInputStream();

                        String response = "";
                        int data = 0;
                        while ((data=is.read()) !=-1){
                            response += (char) data;
                        }
                        System.out.println(response+"---------------------------------");
                        View parentLayout = findViewById(android.R.id.content);
                        if (response.equals("1")) {
                            progressDialog.dismiss();
                            System.out.println("11111111111111111---------------------------------");
                            Snackbar snackbar = Snackbar
                                    .make(parentLayout, "User Name or Password is Incorrect.Please Insert Correct Details", Snackbar.LENGTH_LONG);

                            snackbar.show();
                        } else if (response.equals("2")) {
                            progressDialog.dismiss();
                            System.out.println("222222222222222222---------------------------------");
                            Snackbar snackbar = Snackbar
                                    .make(parentLayout, "User is Blocked", Snackbar.LENGTH_LONG);

                            snackbar.show();
//                            Toast.makeText(getApplicationContext(), "User is Blocked", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("3")) {
                            progressDialog.dismiss();
                            System.out.println("333333333333333333333---------------------------------");
                            Snackbar snackbar = Snackbar
                                    .make(parentLayout, "Connection error", Snackbar.LENGTH_LONG);

                            snackbar.show();
//                            Toast.makeText(getApplicationContext(), "Connection error", Toast.LENGTH_SHORT).show();
                        } else {
                            System.out.println(response+"---------------------------------");
                            if (response != null) {
                                progressDialog.dismiss();
                                signinadapter signin = gson.fromJson(response, signinadapter.class);

                                appSetting.userid = signin.getUsrid();
                                appSetting.userloginid = signin.getUserloginid();
                                appSetting.usertypeid = signin.getUsrtypeid();
                                appSetting.loginStatus = "1";
//
//                            EditText et = findViewById(R.id.idusername);
//                            et.setText(signin.getUsrid());
//                            System.out.println(appSetting.userid + "," + appSetting.userloginid + "," +  appSetting.usertypeid);
//                            Toast.makeText(getApplicationContext(), appSetting.userid + "," + appSetting.userloginid + "," +  appSetting.usertypeid, Toast.LENGTH_SHORT).show();
//                            System.out.println("awaaaaaaaaaaaaaaaaaaaaaaaaa");

                                boolean ch = ((CheckBox) findViewById(R.id.remembermeid)).isChecked();

                                if (ch) {
                                    SharedPreferences sp = getSharedPreferences("settingslogin", MODE_PRIVATE);

                                    SharedPreferences.Editor e = sp.edit();

                                    e.putString("shuserid", appSetting.userid);
                                    e.putString("shuserloginid", appSetting.userloginid);
                                    e.putString("shusertypeid", appSetting.usertypeid);
                                    e.putString("shlogintype", appSetting.loginStatus);

                                    e.apply();
                                }

                                final Intent i2 = new Intent(getApplicationContext(), HomeD.class);
                                final Intent i3 = new Intent(getApplicationContext(), Home.class);

                                if (appSetting.usertypeid.equals("2")) {
                                    startActivity(i2);
                                } else if (appSetting.usertypeid.equals("3")) {
                                    startActivity(i3);
                                }

                            }

                        }

                    }else{
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar
                                .make(parentLayout, "Connection error", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "err", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    e.printStackTrace();
                    System.out.println("---------------------------------------" + e);
                }finally {
//                    Toast.makeText(getApplicationContext(), "ew", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    if(con!=null){
                        con.disconnect();

                    }
                }

            }
        });
        t.start();


//        try {
//
////            final ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
////            Sprite doubleBounce = new DoubleBounce();
////            progressBar.setIndeterminateDrawable(doubleBounce);
//
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setMessage("Please Wailt...");
//            progressDialog.setCancelable(false);
//            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
//            progressDialog.show();
//
//            String email = ((EditText) findViewById(R.id.idusername)).getText().toString();
//            String password = ((EditText) findViewById(R.id.idpassword)).getText().toString();
//
//            signinadapter u2 = new signinadapter(email, password, appSetting.userid, appSetting.userloginid, appSetting.usertypeid);
//            final Gson gson = new Gson();
//            final String jsonString = gson.toJson(u2);
//
//
//            String url = appSetting.volleyUrl + "signinapp_serv";
//
//            Response.Listener rl = new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
////                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
//                    if (response.equals("1")) {
//                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(), "User Name or Password is Incorrect.Please Insert Correct Details", Toast.LENGTH_SHORT).show();
//                    } else if (response.equals("2")) {
//                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(), "User is Blocked", Toast.LENGTH_SHORT).show();
//                    } else if (response.equals("3")) {
//                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(), "Connection error", Toast.LENGTH_SHORT).show();
//                    } else {
//
//                        if (response != null) {
//                            progressDialog.dismiss();
//                            signinadapter signin = gson.fromJson(response, signinadapter.class);
//
//                            appSetting.userid = signin.getUsrid();
//                            appSetting.userloginid = signin.getUserloginid();
//                            appSetting.usertypeid = signin.getUsrtypeid();
//                            appSetting.loginStatus = "1";
////
////                            EditText et = findViewById(R.id.idusername);
////                            et.setText(signin.getUsrid());
////                            System.out.println(appSetting.userid + "," + appSetting.userloginid + "," +  appSetting.usertypeid);
////                            Toast.makeText(getApplicationContext(), appSetting.userid + "," + appSetting.userloginid + "," +  appSetting.usertypeid, Toast.LENGTH_SHORT).show();
////                            System.out.println("awaaaaaaaaaaaaaaaaaaaaaaaaa");
//
//                            boolean ch = ((CheckBox) findViewById(R.id.remembermeid)).isChecked();
//
//                            if (ch) {
//                                SharedPreferences sp = getSharedPreferences("settingslogin", MODE_PRIVATE);
//
//                                SharedPreferences.Editor e = sp.edit();
//
//                                e.putString("shuserid", appSetting.userid);
//                                e.putString("shuserloginid", appSetting.userloginid);
//                                e.putString("shusertypeid", appSetting.usertypeid);
//                                e.putString("shlogintype", appSetting.loginStatus);
//
//                                e.apply();
//                            }
//
//                            final Intent i2 = new Intent(getApplicationContext(), HomeD.class);
//                            final Intent i3 = new Intent(getApplicationContext(), Home.class);
//
//                            if (appSetting.usertypeid.equals("2")) {
//                                startActivity(i2);
//                            } else if (appSetting.usertypeid.equals("3")) {
//                                startActivity(i3);
//                            }
//
//                        }
//
//
//                    }
//
//                }
//            };
//
//            Response.ErrorListener re = new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    progressDialog.dismiss();
//                    Toast.makeText(getApplicationContext(), "Fail to Connect With Server", Toast.LENGTH_SHORT).show();
//                    System.out.println("---------------------------------------" + error);
//                }
//            };
//
//
//            GSONRequest request = new GSONRequest(1, url, rl, re);
//            request.json = jsonString;
//            VolleySingleton.getVolleySingleton(getApplicationContext()).getRequestQueue().add(request);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }


    }

    public void btnfpSignin(View view) {

        Intent i = new Intent(this, ForgotPassword.class);
        startActivity(i);

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.ic_directions_vehicle);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        final String uname = ((EditText) findViewById(R.id.idusername)).getText().toString();
        final String pwd = ((EditText) findViewById(R.id.idpassword)).getText().toString();
        boolean ch = ((CheckBox) findViewById(R.id.remembermeid)).isChecked();

        if(ch){
            outState.putString("checkc", "1");
        }else{
            outState.putString("checkc", "0");
        }
        outState.putString("uname", uname);
        outState.putString("pwd", pwd);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        String uname = savedInstanceState.getString("uname");
        String pswd = savedInstanceState.getString("pwd");
        String chbx = savedInstanceState.getString("checkc");

        if(chbx.equals("1")){
            CheckBox ch =findViewById(R.id.remembermeid);
            ch.setChecked(true);
        }

        ((EditText) findViewById(R.id.idusername)).setText(uname);
        ((EditText) findViewById(R.id.idpassword)).setText(pswd);


        super.onRestoreInstanceState(savedInstanceState);
    }
}
