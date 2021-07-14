package com.sandappsefur.transport.Attendence;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.sandappsefur.transport.R;
import com.sandappsefur.transport.StaffService.ProfileUserReq;
import com.sandappsefur.transport.fees.AdapterPaymentsDUHistory;
import com.sandappsefur.transport.home.Home;
import com.sandappsefur.transport.home.HomeD;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Main.GSONRequest;
import Main.Task1;
import Main.VolleySingleton;
import Main.appSetting;
import Main.attendence.AttendenceU;
import Main.payment.PaymentS;
import Main.profile.usertable;
import Main.signup.signinadapter;
import Main.signup.signupnuser;
import Main.staffservice.profilereqLoad;

public class AttendenceUser extends AppCompatActivity {

    int x = 0;
    int y = 1;

    ProgressDialog progressDialog;
    ImageView iv;
    RecyclerView recyclerView;
    AdapterAttendanceUsermnlst adapter;
    List<signupnuser> ulist;
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_user);

        iv = findViewById(R.id.profiledpAtt);

        pullToRefresh = findViewById(R.id.pullToRefreshAttendaceUser);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadIntaAtt();
                listAttendance();; // your code
                pullToRefresh.setRefreshing(false);
            }
        });

        ArrayList<String> bl = new ArrayList<>();
        bl.add("Morning");
        bl.add("Afternoon");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, bl);

        Spinner sp = findViewById(R.id.attendshif);
        sp.setAdapter(adapter);

        ArrayList<String> al2 = new ArrayList<>();
        al2.add("Attend");
        al2.add("Not Attend");
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, al2);

        Spinner sp2 = findViewById(R.id.attendstat);
        sp2.setAdapter(adapter2);

        loadIntaAtt();
        listAttendance();

    }

    public void bkHomeAttU(View view) {
        onBackPressed();
    }

    public void MarkArrendenceUser(View view) {

        Task1 t1 = new Task1(this);
        t1.execute();

    }

    public void loadIntaAtt() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wailt...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();

        String firstLetter = String.valueOf("A");
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(1);

        final TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color);

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://tranz-b8710.appspot.com/");
        StorageReference storageReference = storage.getReference("users");
        final StorageReference child = storageReference.child("profileimages/");

        try {

            signupnuser u2 = new signupnuser(appSetting.userid, null, null, appSetting.usertypeid);
            final Gson gson = new Gson();
            final String jsonString = gson.toJson(u2);

            String url = appSetting.volleyUrl + "loadAttendancePrf_serv";

            Response.Listener rl = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
                    if (response.equals("1")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    } else {

                        if (response != null) {
                            final signupnuser utbl = gson.fromJson(response, signupnuser.class);

                            if (utbl.getMobile() != null) {
                                if (utbl.getMobile().equals("user" + appSetting.userid)) {
                                    Thread t = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            child.child(utbl.getMobile()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

                            ((TextView) findViewById(R.id.UserNamatt)).setText(utbl.getName());
                            if (utbl.getEmail().equals("0")) {
                                ((TextView) findViewById(R.id.mngatt)).setText("Not attend");
                            } else if (utbl.getEmail().equals("1")) {
                                ((TextView) findViewById(R.id.mngatt)).setText("Attend");
                            }

                            if (utbl.getPassword().equals("0")) {
                                ((TextView) findViewById(R.id.evngatt)).setText("Not attend");
                            } else if (utbl.getPassword().equals("1")) {
                                ((TextView) findViewById(R.id.evngatt)).setText("Attend");
                            }
//                            Toast.makeText(getApplicationContext(), utbl.getEmail(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getApplicationContext(), utbl.getPassword(), Toast.LENGTH_SHORT).show();

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

    public  void listAttendance(){

        ulist = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclevwattendencehistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        signupnuser snu = new signupnuser(appSetting.userid, null, null, appSetting.usertypeid);
        final Gson gson = new Gson();
        final String jsonString = gson.toJson(snu);
        String url = appSetting.volleyUrl + "UserAttLstvwhis_serv?jssonString=" + jsonString;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url

                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressDialog.dismiss();

//                System.out.println(response);

                if (response.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                } else if (response.equals("2")) {
                    Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
                } else {
                    TextView tvnd = findViewById(R.id.nodataattend);
                    tvnd.setVisibility(View.GONE);

                    try {

                        JSONArray servicelist = new JSONArray(response);
                        for (int i = 0; i < servicelist.length(); i++) {
                            JSONObject Objectmarks = servicelist.getJSONObject(i);
                            String dat = Objectmarks.getString("attDate");
                            String mngat = Objectmarks.getString("atTim");
                            String afteat = Objectmarks.getString("atStat");

                            signupnuser userL = new signupnuser(dat, mngat, afteat, null);
                            ulist.add(userL);

                        }
                        adapter = new AdapterAttendanceUsermnlst(getApplicationContext(), ulist);
                        recyclerView.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                System.out.println(error);
            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }

}
