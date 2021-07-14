package com.sandappsefur.transport.fees;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.sandappsefur.transport.Notification.PaymentNotificationAppDri;
import com.sandappsefur.transport.R;
import com.sandappsefur.transport.StaffService.ProfileUserReq;
import com.sandappsefur.transport.profile.profile;
import com.sandappsefur.transport.profile.profileDriver;
import com.sandappsefur.transport.startup.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import Main.AlertReciver;
import Main.GSONRequest;
import Main.NotificationHelper;
import Main.NotificationJobScheduler;
import Main.VolleySingleton;
import Main.appSetting;
import Main.profile.usertable;
import Main.signup.signupdriver;
import Main.staffservice.profilereqLoad;

import static com.sandappsefur.transport.Notification.PaymentNotificationAppDri.CHANEL_1_ID;

public class PaymentDriverUserDetails extends AppCompatActivity {

    SwipeRefreshLayout pullToRefresh;
    ProgressDialog progressDialog;

    String dusid;
    ImageView iv;
    Dialog myDialog;

    private NotificationManagerCompat notificationManager;
    private NotificationHelper mnotificationHelper;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_driver_user_details);

        mnotificationHelper = new NotificationHelper(getApplicationContext());
        notificationManager = NotificationManagerCompat.from(this);

        myDialog = new Dialog(this);

        iv = findViewById(R.id.profilrpdudDP);

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Please Wailt...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();

        if (getIntent().hasExtra("Use_ID")) {
            dusid = getIntent().getStringExtra("Use_ID");
            pullToRefresh = findViewById(R.id.pullToRefreshPaymentDriverUsrDetails);
            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    bodauLoadpduDetails(); // your code
                    pullToRefresh.setRefreshing(false);
                }
            });

            bodauLoadpduDetails();

//            Toast.makeText(this, "ID :" + dusid, Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Data Passing Error", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

    }

    public void backBtnDriverPayment(View view) {
        onBackPressed();
    }

    public void bodauLoadpduDetails() {
        String firstLetter = String.valueOf("A");
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(1);

        final TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color);

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://tranz-b8710.appspot.com/");
        StorageReference storageReference = storage.getReference("users");
        final StorageReference child = storageReference.child("profileimages/");

        try {

            usertable u2 = new usertable(dusid, appSetting.userid, appSetting.usertypeid, "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null");
            final Gson gson = new Gson();
            final String jsonString = gson.toJson(u2);

            String url = appSetting.volleyUrl + "paymentDriverupdetailsload_serv";

            Response.Listener rl = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
                    if (response.equals("1")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Invalid User", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("2")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                    } else {

                        if (response != null) {
                            final profilereqLoad utbl = gson.fromJson(response, profilereqLoad.class);

                            if (utbl.getImageurl() != null) {
                                if (utbl.getImageurl().equals("user" + dusid)) {
                                    Thread t = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            child.child(utbl.getImageurl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    System.out.println(">>>");
                                                    // Picasso.with(context).load(uri).into(viewHolder.imageView);
                                                    Picasso.with(getApplicationContext()).load(uri).placeholder(drawable).fit().centerCrop().into(iv);

                                                }
                                            });
                                        }
                                    });
                                    t.start();
                                }
                            }

                            ((TextView) findViewById(R.id.UserNamepdud)).setText(utbl.getFname());
                            ((TextView) findViewById(R.id.cpnypdud)).setText(utbl.getCompany());
                            ((TextView) findViewById(R.id.mnthpdud)).setText(utbl.getVehicleno());
                            ((TextView) findViewById(R.id.datepdud)).setText(utbl.getDetails());
                            if (utbl.getStatus().equals("0")) {
                                ((TextView) findViewById(R.id.statuspdud)).setText("Not Paid");
                            } else if (utbl.getStatus().equals("1")) {
                                ((TextView) findViewById(R.id.statuspdud)).setText("Paid");

                            }
                            if(utbl.getFee().equals("null")){
                                Toast.makeText(getApplicationContext(), "Set Monthly Charge First", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ProfileUserReq.class);
                                intent.putExtra("Use_ID", dusid);
                                intent.putExtra("Use_stat", "2");
                                startActivity(intent);
                            }
                            ((TextView) findViewById(R.id.dfeepdud)).setText(utbl.getFee());
                            ((TextView) findViewById(R.id.paidamoutpdud)).setText(utbl.getAvailableu());
                            ((TextView) findViewById(R.id.duepdud)).setText(utbl.getBookedstatus());
                            ((TextView) findViewById(R.id.ddatepdud)).setText(utbl.getMobile());

                            progressDialog.dismiss();

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

    public void ViewHistorypdud(View view) {
        Intent intent = new Intent(this, PaymentDUserHistory.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("Use_ID", dusid);
        intent.putExtra("Use_stat", "2");
        this.startActivities(new Intent[]{intent});
    }

    public void Updatepaymentpdud(View view) {
        myDialog.setContentView(R.layout.popup_paymentupdate_driver);
        TextView closebtn = myDialog.findViewById(R.id.closepudrv);
        Button btnupdate = myDialog.findViewById(R.id.btnupdatepudrv);
        Button btnclose = myDialog.findViewById(R.id.btnclosepudrv);

        ArrayList<String> bl = new ArrayList<>();
        bl.add("January");
        bl.add("February");
        bl.add("March");
        bl.add("April");
        bl.add("May");
        bl.add("June");
        bl.add("July");
        bl.add("August");
        bl.add("September");
        bl.add("October");
        bl.add("November");
        bl.add("December");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, bl);

        Spinner sp = myDialog.findViewById(R.id.mnthupdatepymnt);
        sp.setAdapter(adapter);

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mnth = ((Spinner) myDialog.findViewById(R.id.mnthupdatepymnt)).getSelectedItem().toString();
                String pymtamnt = ((EditText) myDialog.findViewById(R.id.pppymtamnt)).getText().toString();
//                Toast.makeText(getApplicationContext(), mnth, Toast.LENGTH_SHORT).show();
                try {
                    progressDialog.setMessage("Please Wailt...");
                    progressDialog.setCancelable(false);
                    progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    signupdriver reqS = new signupdriver(dusid, appSetting.userid, mnth, pymtamnt, null, appSetting.usertypeid);
                    final Gson gson = new Gson();
                    final String jsonString = gson.toJson(reqS);

                    String url = appSetting.volleyUrl + "updateUserPaidAmount_serv";

                    Response.Listener rl = new Response.Listener<String>() {
                        @SuppressLint("WrongViewCast")
                        @Override
                        public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
                            progressDialog.dismiss();
                            if (response.equals("1")) {
                                Toast.makeText(getApplicationContext(), "User Error", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("2")) {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("3")) {
                                Toast.makeText(getApplicationContext(), "Payment Already Made", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("4")) {
                                Toast.makeText(getApplicationContext(), "Payment Successful", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("5")) {
                                Toast.makeText(getApplicationContext(), "Pay only Due Amount", Toast.LENGTH_SHORT).show();
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
        });

    }

    public void testNoti(View view) {

//        try {
//            Intent intent = new Intent(this, PaymentsDriver.class);
//            PendingIntent contentIntend = PendingIntent.getActivity(this, 0, intent, 0);
//
//            Notification notification = new NotificationCompat.Builder(this, PaymentNotificationAppDri.CHANEL_1_ID)
//                    .setSmallIcon(R.drawable.ic_directions_vehicle)
//                    .setContentTitle("Test Title")
//                    .setContentText("Sime Example Test")
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                    .setColor(getColor(R.color.colorPrimary))
//                    .setAutoCancel(true)
//                    .setContentIntent(contentIntend)
//                    .build();
//            notificationManager.notify(1, notification);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        final Calendar c = Calendar.getInstance();
////        c.set(Calendar.HOUR_OF_DAY,0);
////        c.set(Calendar.MINUTE,0);
//        c.set(Calendar.SECOND, 1);

//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(getApplicationContext(), AlertReciver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
//
////        if(c.before(Calendar.getInstance())){
////            c.add(Calendar.DATE,1);
////        }
//
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

        final int[] count = {5}; //Declare as inatance variable

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        final Toast toast = Toast.makeText(
                                getApplicationContext(), --count[0] + "",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, 0);
                        c.set(Calendar.MINUTE, 0);
                        c.set(Calendar.SECOND, 1);

//                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                        Intent intent = new Intent(getApplicationContext(), AlertReciver.class);
//                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
//
//                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

//                        NotificationCompat.Builder nb = mnotificationHelper.getChannel1Notification("Title", "Message",);
//                        mnotificationHelper.getManager().notify(1,nb.build());

                    }
                });
            }
        }, 0, 1000);


//        ComponentName componentName = new ComponentName(this,NotificationJobScheduler.class);
//        JobInfo info = new JobInfo.Builder(123,componentName)
//                .setRequiresCharging(true)
//                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
//                .setPersisted(true)
//                .setPeriodic(15*60*1000)
//                .build();
//
//        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//        int resultCode = scheduler.schedule(info);
//        if(resultCode == JobScheduler.RESULT_SUCCESS){
//            Log.d(TAG,"Job Scheduled");
//        }else{
//            Log.d(TAG,"Job Scheduling Fail");
//
//        }

    }

    public void testNotiCancel(View view) {
        Calendar c = Calendar.getInstance();
//        c.set(Calendar.HOUR_OF_DAY,0);
//        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND, 5);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);

//        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//        scheduler.cancel(123);
//        Log.d(TAG,"Job Canceled");

    }
}
