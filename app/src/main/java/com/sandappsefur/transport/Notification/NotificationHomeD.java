package com.sandappsefur.transport.Notification;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.sandappsefur.transport.Attendence.AdapterAttendanceDriverMemlst;
import com.sandappsefur.transport.R;
import com.sandappsefur.transport.StaffService.ProfileUserReq;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Main.GSONRequest;
import Main.VolleySingleton;
import Main.appSetting;
import Main.attendence.AttendenceU;
import Main.profile.usertable;
import Main.signup.signupnuser;
import Main.staffservice.profilereqLoad;

public class NotificationHomeD extends AppCompatActivity {

    SwipeRefreshLayout pullToRefresh;
    ProgressDialog progressDialog;

    RecyclerView recyclerView;
    AdapterNotificationHDatt adapter;
    List<signupnuser> ulist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_home_d);

        pullToRefresh = findViewById(R.id.pulltoRefreshNotificationDH);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                notificationBodayload(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Please Wailt...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();

        notificationBodayload();

    }

    public void notificationBodayload() {

        try {

//            Toast.makeText(getApplicationContext(), "Meth", Toast.LENGTH_SHORT).show();
            ulist = new ArrayList<>();
            recyclerView = findViewById(R.id.recycleviewNotificationDH);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


            signupnuser u2 = new signupnuser(appSetting.userid, null, null, appSetting.usertypeid);
            final Gson gson = new Gson();
            final String jsonString = gson.toJson(u2);

            String url = appSetting.volleyUrl + "notificationdHattload_serv";

            Response.Listener rl = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
                    progressDialog.dismiss();
                    if (response.equals("1")) {
                        Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("2")) {
//                        TextView tv = findViewById(R.id.nodataAttDriver);
//                        tv.setText("No Members");
                        Toast.makeText(getApplicationContext(), "No Members", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("3")) {
                        Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("4")) {
                        Toast.makeText(getApplicationContext(), "Duplicate Entry", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "awooo", Toast.LENGTH_SHORT).show();
                        try {

                            JSONArray servicelist = new JSONArray(response);
                            for (int i = 0; i < servicelist.length(); i++) {
                                JSONObject Objectmarks = servicelist.getJSONObject(i);
                                String dDate = Objectmarks.getString("dDate");
                                String morAttC = Objectmarks.getString("morAttC");
                                String aftAttC = Objectmarks.getString("aftAttC");

                                signupnuser userL = new signupnuser(dDate, morAttC, aftAttC, null);
                                ulist.add(userL);

                            }
                            adapter = new AdapterNotificationHDatt(getApplicationContext(), ulist);
                            recyclerView.setAdapter(adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            };

            Response.ErrorListener re = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    System.out.println(error.getMessage());
                }
            };


            GSONRequest request = new GSONRequest(1, url, rl, re);
            request.json = jsonString;
            VolleySingleton.getVolleySingleton(getApplicationContext()).getRequestQueue().add(request);


        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }

    }

}
