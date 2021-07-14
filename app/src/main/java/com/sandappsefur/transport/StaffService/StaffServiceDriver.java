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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.nex3z.notificationbadge.NotificationBadge;
import com.sandappsefur.transport.R;
import com.sandappsefur.transport.StaffService.FragmentNewsfeed;
import com.sandappsefur.transport.StaffService.FragmentNewsfeedDriver;
import com.sandappsefur.transport.StaffService.FragmentSearch;
import com.sandappsefur.transport.StaffService.FragmentSearchDriver;
import com.sandappsefur.transport.home.HomeD;
import com.sandappsefur.transport.profile.profile;

import Main.GSONRequest;
import Main.VolleySingleton;
import Main.appSetting;
import Main.staffservice.reqSend;

public class StaffServiceDriver extends AppCompatActivity {

    int servicereq = 0;
    int serviceinvites = 0;
    int alertcount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_service_driver);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefreshstfserviceDriver);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                alertRefresh(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListner);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_driver, new FragmentNewsfeedDriver()).commit();

        alertRefresh();

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new FragmentNewsfeedDriver();
                    break;
                case R.id.nav_search:
                    selectedFragment = new FragmentSearchDriver();
                    break;

            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_driver, selectedFragment).commit();
            return true;
        }
    };

    //alert Refresh-------------------------------------------
    public void alertRefresh() {

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

                        NotificationBadge badge = findViewById(R.id.notificationBatchdriver);
                        badge.setBadgeBackgroundDrawable(getDrawable(R.drawable.nb_badge_bg));
                        badge.setAnimationDuration(100);
                        badge.clear();
                    } else if (response.equals("2")) {
                        Toast.makeText(getApplicationContext(), "More Users", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("4")) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    } else {
                        if (response != null) {
                            reqSend reqSnt = gson.fromJson(response, reqSend.class);
                            servicereq = reqSnt.getReqcount();

                            ((TextView) findViewById(R.id.shtcntlbl)).setText(String.valueOf( reqSnt.getAttendstat()));
                            ((TextView) findViewById(R.id.avausecntlbl)).setText(String.valueOf(reqSnt.getReportstat()));

                            if (servicereq > 0) {

                                alertcount = servicereq + serviceinvites;

                                NotificationBadge badge = findViewById(R.id.notificationBatchdriver);
                                badge.setBadgeBackgroundDrawable(getDrawable(R.drawable.nb_badge_bg));
                                badge.setAnimationDuration(100);
                                badge.setNumber(alertcount);

                            } else {
                                NotificationBadge badge = findViewById(R.id.notificationBatchdriver);
                                badge.setBadgeBackgroundDrawable(getDrawable(R.drawable.nb_badge_bg));
                                badge.setAnimationDuration(100);
                                badge.clear();
                            }


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
//alert Refresh-------------------------------------------

    public void btnstfservicebackHomeDriver(View view) {
        Intent i = new Intent(this, HomeD.class);
        startActivity(i);

    }

    public void notificationStaffServiceDriver(View view) {
//        ((LinearLayout)findViewById(R.id.linearlayoutuserprofile)).removeAllViews();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_driver, new FragmentNotificationStfService(), "frnoti").commit();

    }

}
