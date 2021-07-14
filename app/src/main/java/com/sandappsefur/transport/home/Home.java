package com.sandappsefur.transport.home;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.sandappsefur.transport.Attendence.AttendenceDriver;
import com.sandappsefur.transport.Attendence.AttendenceUser;
import com.sandappsefur.transport.Navigation.NavigationUser;
import com.sandappsefur.transport.R;
import com.sandappsefur.transport.StaffService.StaffService;
import com.sandappsefur.transport.settingsapp.Faq;
import com.sandappsefur.transport.settingsapp.Settings;
import com.sandappsefur.transport.fees.feesnUsers;
import com.sandappsefur.transport.profile.profile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Main.AlertReciver;
import Main.GSONRequest;
import Main.NotificationHelper;
import Main.VolleySingleton;
import Main.appSetting;
import Main.profile.usertable;
import Main.staffservice.profilereqLoad;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class Home extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,SensorListener {

    boolean con = false;
    Timer timer;

    SwipeRefreshLayout pullToRefresh;
    ProgressDialog progressDialog;

    private NotificationManagerCompat notificationManager;
    private NotificationHelper mnotificationHelper;

    private final String CHANNEL_ID = "personal notification";
    private final String CHANNEL_ID2 = "personal notification2";
    private final int NOTIFICATION_ID = 001;
    private final int NOTIFICATION_ID2 = 002;


    SensorManager sensorMgr;
    private static final int SHAKE_THRESHOLD = 5000;
    float x;
    float y;
    float z;
    long lastUpdate;
    private float last_x = -1.0f, last_y = -1.0f, last_z = -1.0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mnotificationHelper = new NotificationHelper(getApplicationContext());



//        -------------sensor------------------
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorMgr.registerListener(this,SensorManager.SENSOR_ACCELEROMETER,SensorManager.SENSOR_DELAY_GAME);

//        -------------sensor------------------


        mappermition();
        checkUserDetailsComplete();
        checkDriverVstatmartNotification();

        pullToRefresh = findViewById(R.id.swipetoRefreshUhome);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mappermition();
                checkUserDetailsComplete();

                checkDriverVstatmartNotification(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });

    }

    private void checkDriverVstatmartNotification() {

        try {

            usertable u2 = new usertable(appSetting.userid, appSetting.userloginid, appSetting.usertypeid, "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null");

            final Gson gson = new Gson();
            final String jsonString = gson.toJson(u2);

            String url = appSetting.volleyUrl + "checkDrivervstatChangeNotification_serv";

            Response.Listener rl = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
//                    progressDialog.dismiss();
                    if (response.equals("1")) {
                        Toast.makeText(getApplicationContext(), "Invalid User", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("2")) {
//                        Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                    } else {

                        if (response != null) {
                            int paymentst = 0;
                            int vs = 0;
                            try {
                                JSONObject atd = new JSONObject(response);

                                paymentst = Integer.parseInt(atd.getString("pmtHv"));
                                vs = Integer.parseInt(atd.getString("vStat"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            Toast.makeText(getApplicationContext(), paymentst + "," + vs, Toast.LENGTH_SHORT).show();


                            if (paymentst == 1) {
                                String tit1 = "Monthly Payment";
                                String message1 = "";

                                notificationbtn1(tit1, message1);
                            }

                            if (vs != 1) {
                                String tit1 = "Vehicle Breakdown";
                                String message1 = "";

                                notificationbtn2(tit1, message1);
                            }


                        }


                    }

                }
            };

            Response.ErrorListener re = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                }
            };


            GSONRequest request = new GSONRequest(1, url, rl, re);
            request.json = jsonString;
            VolleySingleton.getVolleySingleton(getApplicationContext()).getRequestQueue().add(request);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void profileUser(View view) {
        Intent i = new Intent(this, profile.class);
        startActivity(i);

    }

    public void feeUser(View view) {
        Intent i = new Intent(this, feesnUsers.class);
        startActivity(i);
    }

    public void staffService(View view) {
        Intent i = new Intent(this, StaffService.class);
        startActivity(i);

    }

    public void settingsUser(View view) {
        Intent i = new Intent(this, Settings.class);
        startActivity(i);

    }

    public void navigationUser(View view) {
        Intent i = new Intent(this, NavigationUser.class);
        startActivity(i);

    }

    public void attendenceUser(View view) {
        Intent i = new Intent(this, AttendenceUser.class);
        startActivity(i);

    }

    public void checkUserDetailsComplete() {
        if (con) {
//            timer.cancel();
        } else {
//            final int[] count = {5};
//            timer = new Timer();
//            timer.schedule(new TimerTask() {
//
//                @Override
//                public void run() {
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            final Toast toast = Toast.makeText(
//                                    getApplicationContext(), --count[0] + "",
//                                    Toast.LENGTH_SHORT);
//                            toast.show();
//
//
//                        }
//                    });
//                }
//            }, 0, 1000);

            try {

                usertable u2 = new usertable(appSetting.userid, appSetting.userloginid, appSetting.usertypeid, "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null");

                final Gson gson = new Gson();
                final String jsonString = gson.toJson(u2);

                String url = appSetting.volleyUrl + "uprofileload_serv";

                Response.Listener rl = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
                        if (response.equals("1")) {
                            Toast.makeText(getApplicationContext(), "Invalid User", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("2")) {
                            Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                        } else {

                            if (response != null) {
                                final usertable utbl = gson.fromJson(response, usertable.class);

                                if (utbl.getFname() == null || utbl.getOccupation() == null || utbl.getPickuploc() == null || utbl.getDroploc() == null || utbl.getCompany() == null || utbl.getUaddress() == null || utbl.getNic() == null || utbl.getMobile() == null || utbl.getDetails() == null) {

                                    View parentLayout = findViewById(android.R.id.content);
                                    Snackbar.make(parentLayout, R.string.snckMsg, Snackbar.LENGTH_INDEFINITE)
                                            .setAction(R.string.snckbtn, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent i = new Intent(getApplicationContext(), profile.class);
                                                    startActivity(i);
                                                }
                                            })
                                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                                            .show();

                                } else {
                                    con = true;
                                }


                            }


                        }

                    }
                };

                Response.ErrorListener re = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
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


    @Override
    public void onBackPressed() {
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

    public void notificationbtn1(String ttl, String msg) {

        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_directions_vehicle);
        builder.setContentTitle(ttl);
        builder.setContentText("You have Successfully paid your monthly Payment");
//        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
        builder.setColor(getResources().getColor(R.color.colorPrimary));
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);
        Intent i = new Intent(getApplicationContext(), feesnUsers.class);
        PendingIntent p = PendingIntent.getActivity(getApplicationContext(), 0, i, 0);
        builder.setContentIntent(p);

        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Personala Notification";
            String Description = "Include All The Personal Notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(Description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void notificationbtn2(String ttl, String msg) {

        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID2);
        builder.setSmallIcon(R.drawable.ic_directions_vehicle);
        builder.setContentTitle(ttl);
//        builder.setContentText(msg);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Sorry!, Your Staff service vehicle was breakdown.So Today it will late Some time. Sorry for the Delay.  So be patient until it fix. "));
        builder.setColor(getResources().getColor(R.color.colorPrimary));
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID2, builder.build());

    }

    private void createNotificationChannel2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Personala Notification2";
            String Description = "Include All The Personal Notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID2, name, importance);
            notificationChannel.setDescription(Description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @AfterPermissionGranted(123)
    public void mappermition() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(getApplicationContext(), perms)) {

        } else {
            EasyPermissions.requestPermissions(this, "App need permissions to Access your location, Camera and Storage", 123, perms);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

        }
    }

    @Override
    public void onSensorChanged(int sensor, float[] values) {
        if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                x = values[SensorManager.DATA_X];
                y = values[SensorManager.DATA_Y];
                z = values[SensorManager.DATA_Z];

                float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
//                    Log.d("sensor", "shake detected w/ speed: " + speed);
//                    Toast.makeText(this, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), Faq.class);
                    startActivity(i);
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(int sensor, int accuracy) {

    }



}

