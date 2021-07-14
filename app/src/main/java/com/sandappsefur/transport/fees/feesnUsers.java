package com.sandappsefur.transport.fees;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Main.appSetting;
import Main.payment.PaymentS;
import Main.signup.signupnuser;

public class feesnUsers extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterPaymentsDUHistory adapter;
    List<PaymentS> ulist;
    ProgressDialog progressDialog;
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feesn_users);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date datec = new Date();
        formatter.format(datec);

        ((TextView) findViewById(R.id.histyyearu)).setText(String.valueOf(formatter.getCalendar().get(Calendar.YEAR)));

        pullToRefresh = findViewById(R.id.pullToRefreshPaymentUserHistory);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bodyuLoadpduhistoryUser(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wailt...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();

        bodyuLoadpduhistoryUser();

        updatePaymentNotifi();

    }

    private void updatePaymentNotifi() {

        try {

            signupnuser snu = new signupnuser(appSetting.userid, null, null, appSetting.usertypeid);
            final Gson gson = new Gson();
            final String jsonString = gson.toJson(snu);
            String url = appSetting.volleyUrl + "PaymentsDetailsnotifiUpdate_serv?jssonString=" + jsonString;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url

                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();

//                    System.out.println(response);

//                    if (response.equals("2")) {
////                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
//                    } else if (response.equals("1")) {
//                        Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
//                    } else if (response.equals("3")) {
//                        Toast.makeText(getApplicationContext()," Ok", Toast.LENGTH_SHORT).show();
//                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    System.out.println(error);
                }
            });
            Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void bodyuLoadpduhistoryUser() {

        ulist = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclevwpduhistoryUser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        signupnuser snu = new signupnuser(appSetting.userid, null, null, appSetting.usertypeid);
        final Gson gson = new Gson();
        final String jsonString = gson.toJson(snu);
        String url = appSetting.volleyUrl + "PaymentsDetailsViewHistory_serv?jssonString=" + jsonString;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url

                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                System.out.println(response);

                if (response.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                } else if (response.equals("2")) {
                    Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
                } else {

                    try {

                        JSONArray servicelist = new JSONArray(response);
                        for (int i = 0; i < servicelist.length(); i++) {
                            JSONObject Objectmarks = servicelist.getJSONObject(i);
                            String dat = Objectmarks.getString("datePayment");
                            String mntlyFee = Objectmarks.getString("mnthlyFee");
                            String pymntAmount = Objectmarks.getString("pymntAmount");
                            String pdue = Objectmarks.getString("uDue");
                            String mnth = Objectmarks.getString("mnthofPyid");

                            PaymentS userL = new PaymentS(dat, null, null, null, mntlyFee, mnth, pymntAmount, null, pdue, null, null, null, null);
                            ulist.add(userL);

                        }
                        adapter = new AdapterPaymentsDUHistory(getApplicationContext(), ulist);
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

    public void backpUser(View view) {
        onBackPressed();
    }

}
