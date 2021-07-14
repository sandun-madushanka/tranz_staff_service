package com.sandappsefur.transport.profile;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import Main.payment.PaymentS;
import Main.signup.signupnuser;

public class viewAllMembersDriver extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterMemberAlllst adapter;
    List<PaymentS> ulist;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_members_driver);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wailt...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();

        mainBodayMEmAllDriverProf();

    }

    public void backviewallMem(View view) {
        onBackPressed();
    }

    public void mainBodayMEmAllDriverProf(){
        ulist = new ArrayList<>();
        recyclerView = findViewById(R.id.recycleviewallmemList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        signupnuser snu = new signupnuser(appSetting.userid, null, null, appSetting.usertypeid);
        final Gson gson = new Gson();
        final String jsonString = gson.toJson(snu);
        String url = appSetting.volleyUrl + "PaymentsDetailsView_serv?jssonString=" + jsonString;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url

                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                System.out.println(response);

                if (response.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                } else if (response.equals("2")) {
                    Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
                } else if (response.equals("3")) {
                    Toast.makeText(getApplicationContext(), "Duplicate Entry", Toast.LENGTH_SHORT).show();
                }  else {

                    try {

                        JSONArray servicelist = new JSONArray(response);
                        for (int i = 0; i < servicelist.length(); i++) {
                            JSONObject Objectmarks = servicelist.getJSONObject(i);
                            String idu = Objectmarks.getString("Usid");
                            String unm = Objectmarks.getString("Fname");
                            String Ocup = Objectmarks.getString("Ocup");
                            String ppic = Objectmarks.getString("Ppic");
                            String dFee = Objectmarks.getString("Dfee");
                            String statPayment = Objectmarks.getString("StusPymnt");
                            String cMonth = Objectmarks.getString("cMonth");

                            PaymentS userL = new PaymentS(idu,Ocup,null, unm, dFee, cMonth, null, null, null,statPayment,null,null,ppic);
                            ulist.add(userL);

                        }
                        adapter = new AdapterMemberAlllst(getApplicationContext(), ulist);
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
