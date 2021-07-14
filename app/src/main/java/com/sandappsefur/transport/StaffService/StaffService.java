package com.sandappsefur.transport.StaffService;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.nex3z.notificationbadge.NotificationBadge;
import com.sandappsefur.transport.R;
import com.sandappsefur.transport.home.Home;
import com.sandappsefur.transport.home.HomeD;

import Main.GSONRequest;
import Main.VolleySingleton;
import Main.appSetting;
import Main.staffservice.reqSend;

public class StaffService extends AppCompatActivity {
    int servicereq = 0;
    int serviceinvites = 0;
    int alertcount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_service);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefreshstfserviceUser);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                alertRefreshUser(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListner);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentNewsfeed()).commit();

        alertRefreshUser();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new FragmentNewsfeed();
                    break;
                case R.id.nav_search:
                    selectedFragment = new FragmentSearch();
                    break;

            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    public void alertRefreshUser() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        try {
            progressDialog.setMessage("Please Wailt...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.show();

            reqSend reqS = new reqSend(appSetting.userid, null, 0, 0, 0, appSetting.usertypeid, 0);
            final Gson gson = new Gson();
            final String jsonString = gson.toJson(reqS);

            String url = appSetting.volleyUrl + "serviceReqAlert_serv";

            Response.Listener rl = new Response.Listener<String>() {
                @SuppressLint("WrongViewCast")
                @Override
                public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
                    progressDialog.dismiss();


                    if (response.equals("1")) {

                        NotificationBadge badge = findViewById(R.id.notificationBatch);
                        badge.setBadgeBackgroundDrawable(getDrawable(R.drawable.nb_badge_bg));
                        badge.setAnimationDuration(100);
                        badge.clear();
                    } else {
                        if (response != null) {
                            reqSend reqSnt = gson.fromJson(response, reqSend.class);
                            servicereq = reqSnt.getReqcount();
                            alertcount = servicereq + serviceinvites;
                            NotificationBadge badge = findViewById(R.id.notificationBatch);
                            badge.setBadgeBackgroundDrawable(getDrawable(R.drawable.nb_badge_bg));
                            badge.setAnimationDuration(100);
                            badge.setNumber(alertcount);
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

    public void btnstfservicebackHome(View view) {
        Intent i = new Intent(this, Home.class);
        startActivity(i);
    }

    public void notificationStaffService(View view) {
//        ((LinearLayout)findViewById(R.id.linearlayoutuserprofile)).removeAllViews();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentNotificationStfService(), "frnoti").commit();

    }
}
