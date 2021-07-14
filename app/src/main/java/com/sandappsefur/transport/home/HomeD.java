package com.sandappsefur.transport.home;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sandappsefur.transport.Attendence.AttendenceDriver;
import com.sandappsefur.transport.Attendence.AttendenceUser;
import com.sandappsefur.transport.Navigation.NavigationDriver;
import com.sandappsefur.transport.Notification.NotificationHomeD;
import com.sandappsefur.transport.fees.PaymentsDriver;
import com.sandappsefur.transport.R;
import com.sandappsefur.transport.StaffService.StaffServiceDriver;
import com.sandappsefur.transport.profile.profile;
import com.sandappsefur.transport.profile.profileDriver;
import com.sandappsefur.transport.settingsapp.Faq;
import com.sandappsefur.transport.settingsapp.SettingsDriver;

import java.util.List;

import Main.GSONRequest;
import Main.NotificationHelper;
import Main.VolleySingleton;
import Main.appSetting;
import Main.profile.usertable;
import Main.staffservice.profilereqLoad;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeD extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,SensorListener {

    boolean con = false;

    SwipeRefreshLayout pullToRefresh;
    ProgressDialog progressDialog;

    private NotificationManagerCompat notificationManager;
    private NotificationHelper mnotificationHelper;

    private final String CHANNEL_ID ="personal notification";
    private final int NOTIFICATION_ID = 001;

//    private SensorManager sensorManager;
//    private Sensor lightSensor;
//    private SensorEventListener lightEventListener;


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
        setContentView(R.layout.activity_home_d);

        mnotificationHelper = new NotificationHelper(getApplicationContext());


//        -------------sensor------------------
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorMgr.registerListener(this,SensorManager.SENSOR_ACCELEROMETER,SensorManager.SENSOR_DELAY_GAME);

//        -------------sensor------------------



        mappermitiond();
//        sensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
//        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
//
//        if (lightSensor == null) {
//            Toast.makeText(getApplicationContext(), "The device has no light sensor !", Toast.LENGTH_SHORT).show();
//
//        }
//
//        lightEventListener = new SensorEventListener() {
//            @Override
//            public void onSensorChanged(SensorEvent sensorEvent) {
//                float value = sensorEvent.values[0];
//
//                if(value<5){
//                    int color = getResources().getColor(R.color.colorPrimary);
////                    findViewById(R.id.dhomelinearclr).setBackgroundColor(color);
//
//                }else {
//                    int color = Color.GREEN;
////                    findViewById(R.id.dhomelinearclr).setBackgroundColor(color);
//
//                }
//            }
//
//            @Override
//            public void onAccuracyChanged(Sensor sensor, int i) {
//
//            }
//        };

        pullToRefresh = findViewById(R.id.pulltoRefreshhomeD);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mappermitiond();
                checkDriverDetailsComplete();

                checkuserAttmartNotification(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });

//        progressDialog = new ProgressDialog(this);
//
//        progressDialog.setMessage("Please Wailt...");
//        progressDialog.setCancelable(false);
//        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
//        progressDialog.show();

        checkDriverDetailsComplete();

        checkuserAttmartNotification();

    }

    public void settingsDriver(View view){
        Intent i = new Intent(this,SettingsDriver.class);
        startActivity(i);

    }

    public void btnprofileDriver(View view){
        Intent i = new Intent(this,profileDriver.class);
        startActivity(i);
    }

    public void btnpstfServiceDriver(View view){
        Intent i = new Intent(this,StaffServiceDriver.class);
        startActivity(i);
    }

    public void btnPaymentsDriver(View view){
        Intent i = new Intent(this,PaymentsDriver.class);
        startActivity(i);
    }

    public void navigationDriver(View view){
        Intent i = new Intent(this,NavigationDriver.class);
        startActivity(i);
    }

    public void attendenceUser(View view) {
        Intent i = new Intent(this, AttendenceDriver.class);
        startActivity(i);
    }
    public void notificationDriver(View view) {
        Intent i = new Intent(this, NotificationHomeD.class);
        startActivity(i);
    }

    public void checkDriverDetailsComplete() {
        if (con) {
//            timer.cancel();
        } else {

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

                                if (utbl.getFname() == null || utbl.getOccupation() == null || utbl.getPickuploc() == null || utbl.getDroploc() == null || utbl.getCompany() == null || utbl.getUaddress() == null || utbl.getNic() == null || utbl.getMobile() == null || utbl.getDetails() == null  || utbl.getVehicleno() == null) {

                                    View parentLayout = findViewById(android.R.id.content);
                                    Snackbar.make(parentLayout, R.string.snckMsg, Snackbar.LENGTH_INDEFINITE)
                                            .setAction(R.string.snckbtn, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent i = new Intent(getApplicationContext(), profileDriver.class);
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

    public void checkuserAttmartNotification(){
        try {

            usertable u2 = new usertable(appSetting.userid, appSetting.userloginid, appSetting.usertypeid, "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null");

            final Gson gson = new Gson();
            final String jsonString = gson.toJson(u2);

            String url = appSetting.volleyUrl + "checkuserAttmartNotification_serv";

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
                            final profilereqLoad utbl = gson.fromJson(response, profilereqLoad.class);

                            String morac = utbl.getIduser();
                            String moracn =utbl.getUserLogin();
                            String nmmorac =utbl.getFname();
                            String aftac = utbl.getMobile();
                            String aftacn = utbl.getDetails();
                            String nmaftac = utbl.getVehicleno();

//                            Toast.makeText(getApplicationContext(), morac + "," + moracn + "," + nmmorac+","+aftac + "," + aftacn + "," + nmaftac, Toast.LENGTH_SHORT).show();

                            String tit = "Members Today Attendance";
                            String message = "There are "+morac+" members marks as Morning Attend" +
                                    ", "+aftac+" members marks as Afternoon Attend, "+moracn+" members marks as Morning not" +
                                    " Attend, "+aftacn+" members marks as Afternoon not Attend, "+nmmorac+" members not mark " +
                                    "Attendance for the Morning and "+nmaftac+" members not mark Attendance for the Afternoon.";

//                            Intent inte = new Intent(getApplicationContext(), AttendenceDriver.class);
//                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, inte, 0);

//                            NotificationCompat.Builder nb = mnotificationHelper.getChanne31Notification(tit, message,pendingIntent);
//                            mnotificationHelper.getManager().notify(1,nb.build());

//                            NotificationCompat.Builder builders =
//                                    new NotificationCompat.Builder(getApplicationContext())
//                                            .setSmallIcon(R.drawable.ic_directions_vehicle)
//                                            .setContentTitle(tit)
//                                            .setDefaults(Notification.DEFAULT_ALL)
//                                            .setPriority(NotificationManager.IMPORTANCE_HIGH)
//                                            .setColor(getResources().getColor(R.color.colorPrimary))
//                                            .setStyle(new NotificationCompat.BigTextStyle()
//                                                    .bigText(message))
//                                            .setContentText(message);
//
//                            Intent intent = new Intent(getApplicationContext(), AttendenceDriver.class);
//                            builders.addAction(R.drawable.ic_directions_vehicle, "View", PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT));
//
//                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                            mNotificationManager.notify(0, builders.build());

                            notificationbtn(message);
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

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1) {
//
//            Intent t = new Intent(getApplicationContext(), AttendenceDriver.class);
//            startActivity(t);
//        }
//    }

    public void notificationbtn(String msg) {

        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_directions_vehicle);
        builder.setContentTitle("Members Today Attendance");
//        builder.setContentText(msg);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
        builder.setColor(getResources().getColor(R.color.colorPrimary));
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);
        Intent i = new Intent(getApplicationContext(),AttendenceDriver.class);
        PendingIntent p = PendingIntent.getActivity(getApplicationContext(),0,i,0);
        builder.setContentIntent(p);

        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());

    }
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = "Personala Notification";
            String Description = "Include All The Personal Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name,importance);
            notificationChannel.setDescription(Description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        sensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        sensorManager.unregisterListener(lightEventListener);
//    }

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
    public void mappermitiond() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(getApplicationContext(), perms)) {

        } else {
            EasyPermissions.requestPermissions(this, "App need permissions to Access your location, Camera and Storage", 123, perms);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        if (requestCode == 1) {

            Intent t = new Intent(getApplicationContext(), AttendenceDriver.class);
            startActivity(t);
        }
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
