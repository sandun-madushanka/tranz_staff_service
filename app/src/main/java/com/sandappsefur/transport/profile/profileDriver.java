package com.sandappsefur.transport.profile;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sandappsefur.transport.R;
import com.sandappsefur.transport.home.HomeD;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import Main.GSONRequest;
import Main.VolleySingleton;
import Main.appSetting;
import Main.profile.usertable;
import Main.profile.utableDriver;
import Main.signup.signupdriver;
import Main.staffservice.reqSend;

public class profileDriver extends AppCompatActivity implements SensorListener {

    TextView iduser, userLogin, fname, mobile, details, vno, shtcount, available, users, bookstatus, vehicleno, nic, status, fee, imagedefault, imageurl, occupation, company, uaddress, pickuploc, droploc, sheetcount, availableu, bookedstatus;
    Dialog myDialog;

    ImageView iv;
    ProgressDialog progressDialog;

    //    Calendar myCalendar;
    TextView edittext;
    //    DatePickerDialog.OnDateSetListener date;
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
        setContentView(R.layout.activity_profile_driver);

//        -------------sensor------------------
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorMgr.registerListener(this,SensorManager.SENSOR_ACCELEROMETER,SensorManager.SENSOR_DELAY_GAME);

//        -------------sensor------------------


        myDialog = new Dialog(this);

        iv = findViewById(R.id.imageViewDrivedp);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wailt...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();

//        myCalendar = Calendar.getInstance();

        edittext = (TextView) findViewById(R.id.datedeadDriver);

//        date = new DatePickerDialog.OnDateSetListener(){
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                String dateshw = year+"-"+month+"-"+dayOfMonth;
//                edittext.setText(dateshw);
//            }
//        };


        viewProfile();

    }

    public void viewProfile() {

        String firstLetter = String.valueOf("A");
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(1);

        final TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color);

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://tranz-b8710.appspot.com/");
        StorageReference storageReference = storage.getReference("users");
        final StorageReference child = storageReference.child("profileimages/");

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
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Invalid User", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("2")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                    } else {

                        if (response != null) {
                            final utableDriver utbl = gson.fromJson(response, utableDriver.class);

                            if (utbl.getImageurl() != null) {
                                if (utbl.getImageurl().equals("user" + appSetting.userid)) {
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


                            fname = findViewById(R.id.nameDriver);
                            fname.setText(utbl.getFname());
                            occupation = findViewById(R.id.occupationDriver);
                            occupation.setText(utbl.getOccupation());
                            ((TextView) findViewById(R.id.vnoDriver)).setText(utbl.getVehicleno());
                            ((TextView) findViewById(R.id.shtcountDriver)).setText(utbl.getSheetcount());
                            int shtcount = Integer.parseInt(utbl.getSheetcount());
                            int avaimembercount = Integer.parseInt(utbl.getAvailableu());
                            int availableshtcnt = shtcount - avaimembercount;
                            String avashtot = String.valueOf(availableshtcnt);
                            ((TextView) findViewById(R.id.availableshtcountDriver)).setText(avashtot);
                            int vstut = Integer.parseInt(utbl.getBookedstatus());
                            String vstatus = String.valueOf(vstut);
                            ((TextView) findViewById(R.id.svtatusDriver)).setText(vstatus);
                            pickuploc = findViewById(R.id.picupDriver);
                            pickuploc.setText(utbl.getPickuploc());
                            droploc = findViewById(R.id.endplaceDriver);
                            droploc.setText(utbl.getDroploc());
                            company = findViewById(R.id.companyDriver);
                            company.setText(utbl.getCompany());
                            uaddress = findViewById(R.id.addressDriver);
                            uaddress.setText(utbl.getUaddress());
                            nic = findViewById(R.id.nicDriver);
                            nic.setText(utbl.getNic());
                            mobile = findViewById(R.id.mobileDriver);
                            mobile.setText(utbl.getMobile());
                            details = findViewById(R.id.detailsDriver);
                            details.setText(utbl.getDetails());
                            ((TextView) findViewById(R.id.datedeadDriver)).setText(utbl.getDeadDate());
                            if (utbl.getVstat().equals("1")) {
                                ((TextView) findViewById(R.id.svtatusDriver)).setText("Perfect");
                            } else if (utbl.getVstat().equals("0")) {
                                ((TextView) findViewById(R.id.svtatusDriver)).setText("Under Maintenance");
                            }

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

    public void btnHomeDriver(View view) {

        Intent i = new Intent(getApplicationContext(), HomeD.class);
        startActivity(i);
    }

    public void btnChengeVehicleStatus(View view) {


        myDialog.setContentView(R.layout.popup_updatestatus);
        TextView closebtn = myDialog.findViewById(R.id.closeVehicleStatus);
        Button btnupdate = myDialog.findViewById(R.id.btnstatusupdate);
        Button btnclose = myDialog.findViewById(R.id.btnstatusclose1);

        ArrayList<String> bl = new ArrayList<>();
        bl.add("Breakdown");
        bl.add("Perfect");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, bl);

        Spinner sp = myDialog.findViewById(R.id.vehiclestatusDriver);
        sp.setAdapter(adapter);

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                viewProfile();
            }
        });
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                viewProfile();
            }
        });


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stat = ((Spinner) myDialog.findViewById(R.id.vehiclestatusDriver)).getSelectedItem().toString();
                String st = "";
                if (stat.equals("Breakdown")) {
                    st = "0";
                } else if (stat.equals("Perfect")) {
                    st = "1";
                }

                try {
                    progressDialog.setMessage("Please Wailt...");
                    progressDialog.setCancelable(false);
                    progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    signupdriver reqS = new signupdriver(appSetting.userid, st, null, null, null, appSetting.usertypeid);
                    final Gson gson = new Gson();
                    final String jsonString = gson.toJson(reqS);

                    String url = appSetting.volleyUrl + "updateDriverVehicleStatus_serv";

                    Response.Listener rl = new Response.Listener<String>() {
                        @SuppressLint("WrongViewCast")
                        @Override
                        public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
                            progressDialog.dismiss();
                            if (response.equals("1")) {
//                                ((EditText)myDialog.findViewById(R.id.svtatusDriver)).setText(((Spinner) myDialog.findViewById(R.id.vehiclestatusDriver)).getSelectedItem().toString());
                                Toast.makeText(getApplicationContext(), "Vehicle Status Update Successful", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("2")) {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
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

                viewProfile();

            }
        });

    }

    public void btnChengeDateDeadLine(View view) {


        myDialog.setContentView(R.layout.popup_updatedeadline_date);
        TextView closebtn = myDialog.findViewById(R.id.closeDateDeadline);
        Button btnupdate = myDialog.findViewById(R.id.btndatedeadupdate);
        Button btnclose = myDialog.findViewById(R.id.btndatedeadclose1);

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
//                Toast.makeText(getApplicationContext(), "User Error", Toast.LENGTH_SHORT).show();
                String et = ((EditText) myDialog.findViewById(R.id.editdate)).getText().toString();
                if (!et.matches("[0-9]+")) {
                    Toast.makeText(getApplicationContext(), "Invalid Number", Toast.LENGTH_SHORT).show();
                } else {
                    if (Integer.parseInt(et) > 1 && Integer.parseInt(et) < 28) {

                        try {
                            progressDialog.setMessage("Please Wailt...");
                            progressDialog.setCancelable(false);
                            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                            progressDialog.show();

                            signupdriver reqS = new signupdriver(appSetting.userid, null, null, null, null, et);
                            final Gson gson = new Gson();
                            final String jsonString = gson.toJson(reqS);

                            String url = appSetting.volleyUrl + "updateDriverDatedead_serv";

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
                                        Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();
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

                    } else {
                        Toast.makeText(getApplicationContext(), "The entered number shuld be grater than 1 and less than 28", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

//        new DatePickerDialog(this, date, myCalendar
//                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    public void btnEditProfileDriver(View view) {


        ((LinearLayout) findViewById(R.id.linearlayoutdriverprofile)).removeAllViews();

        FragmentDriverDetails fragment2 = new FragmentDriverDetails();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.linearlayoutdriverprofile, fragment2, "f2");
        transaction.commit();

    }

    public void btnviewAllMembers(View view) {
        Intent i = new Intent(this, viewAllMembersDriver.class);
        startActivity(i);
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
//                    onBackPressed();
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

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//
//
//
//        super.onSaveInstanceState(outState);
//    }


//    @Override
//    protected void onPause() {
//        System.out.println("pauseeeeeeeeeeeeeeeeeeeeeeeeeeeee");
////        ((LinearLayout) findViewById(R.id.linearlayoutdriverprofile)).removeAllViews();
//        LinearLayout ll = (LinearLayout) findViewById(R.id.linearlayoutdriverprofile);
//        ll.removeAllViews();
//
//        FragmentDriverDetails fragment2 = new FragmentDriverDetails();
//
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.linearlayoutdriverprofile, fragment2, "f2");
//        transaction.commit();
//
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        System.out.println("distroyeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
//        ((LinearLayout) findViewById(R.id.linearlayoutdriverprofile)).removeAllViews();
//        super.onDestroy();
//    }
}

