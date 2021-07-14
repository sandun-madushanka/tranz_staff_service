package com.sandappsefur.transport.Attendence;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.sandappsefur.transport.R;
import com.sandappsefur.transport.fees.AdapterPaymentDriverMemberList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Main.appSetting;
import Main.attendence.AttendenceU;
import Main.payment.PaymentS;
import Main.signup.signupnuser;

public class AttendenceDriver extends AppCompatActivity {

    SwipeRefreshLayout pullToRefresh;

    RecyclerView recyclerView;
    AdapterAttendanceDriverMemlst adapter;
    List<AttendenceU> ulist;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_driver);

        pullToRefresh = findViewById(R.id.pullToRefreshAttendaceDriver);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainBodayAttendanceDriver(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wailt...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();

        mainBodayAttendanceDriver();

    }
    public void bkHomeAttD(View view){
        onBackPressed();
    }

    public void mainBodayAttendanceDriver(){

        ulist = new ArrayList<>();
        recyclerView = findViewById(R.id.recycleviewAttendenceDriver);
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
                progressDialog.dismiss();

//                System.out.println(response);

                if (response.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                } else if (response.equals("2")) {
                    TextView tv = findViewById(R.id.nodataAttDriver);
                    tv.setText("No Members");
//                    Toast.makeText(getApplicationContext(), "No Members", Toast.LENGTH_SHORT).show();
                }else if (response.equals("3")) {
                    TextView tv = findViewById(R.id.nodataAttDriver);
                    tv.setText("No Data");
//                    Toast.makeText(getApplicationContext(), "No Members", Toast.LENGTH_SHORT).show();
                } else if (response.equals("4")) {
                    Toast.makeText(getApplicationContext(), "Duplicate Entry", Toast.LENGTH_SHORT).show();
                }  else {
                    TextView tv = findViewById(R.id.nodataAttDriver);
                    tv.setVisibility(View.GONE);
                    try {

                        JSONArray servicelist = new JSONArray(response);
                        for (int i = 0; i < servicelist.length(); i++) {
                            JSONObject Objectmarks = servicelist.getJSONObject(i);
                            String idu = Objectmarks.getString("Usid");
                            String unm = Objectmarks.getString("Fname");
                            String ppic = Objectmarks.getString("Ppic");
                            String mobU = Objectmarks.getString("mobU");
                            String morAtt = Objectmarks.getString("morAtt");
                            String aftAtt = Objectmarks.getString("aftAtt");

                            AttendenceU userL = new AttendenceU(idu,Integer.parseInt(mobU),null, null,unm,ppic,morAtt,aftAtt);
                            ulist.add(userL);

                        }
                        adapter = new AdapterAttendanceDriverMemlst(getApplicationContext(), ulist);
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
