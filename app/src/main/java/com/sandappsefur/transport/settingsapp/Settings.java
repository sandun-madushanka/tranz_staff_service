package com.sandappsefur.transport.settingsapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.clans.fab.FloatingActionButton;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;
import com.sandappsefur.transport.R;
import com.sandappsefur.transport.login.login;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Main.GSONRequest;
import Main.VolleySingleton;
import Main.appSetting;
import Main.signup.signupdriver;

public class Settings extends AppCompatActivity {

    Dialog myDialog;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        myDialog = new Dialog(this);

        FloatingActionButton fab = findViewById(R.id.mailnuserset);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Faq.class);
                startActivity(i);
            }
        });

    }

    public void btnLogoutU(View view) {

        SharedPreferences sp = getSharedPreferences("settingslogin", MODE_PRIVATE);

        SharedPreferences.Editor e = sp.edit();

//        e.remove("u1"); //meken puluwn apita ekn eka ain krnna

        e.clear();//meken puluwn apita serama eka para ain krla danna shared reference
        e.apply();

        appSetting.userid = "null";
        appSetting.userloginid = "null";
        appSetting.usertypeid = "null";
        appSetting.loginStatus = "0";


        Intent i = new Intent(getApplicationContext(), login.class);

        startActivity(i);

    }

    public void settingback(View view) {
        onBackPressed();
    }

    public void brnpieChartAttemdance(View view) {

        myDialog.setContentView(R.layout.popup_member_attendance_chart);
        TextView closebtn = myDialog.findViewById(R.id.closepupieatdMem);

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        try {
//            progressDialog.setMessage("Please Wailt...");
//            progressDialog.setCancelable(false);
//            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
//            progressDialog.show();

            signupdriver reqS = new signupdriver(appSetting.userid, null, null, null, null, appSetting.usertypeid);
            final Gson gson = new Gson();
            final String jsonString = gson.toJson(reqS);

//            String url = appSetting.volleyUrl + "piechartPaymentDriver_serv";
            String url = appSetting.volleyUrl + "piechartAttendanceUser_serv";

            Response.Listener rl = new Response.Listener<String>() {
                @SuppressLint("WrongViewCast")
                @Override
                public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
//                    progressDialog.dismiss();
                    if (response.equals("1")) {
//                                ((EditText)myDialog.findViewById(R.id.svtatusDriver)).setText(((Spinner) myDialog.findViewById(R.id.vehiclestatusDriver)).getSelectedItem().toString());
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("2")) {
                        Toast.makeText(getApplicationContext(), "There is no marked attendance yet", Toast.LENGTH_SHORT).show();
                    } else {
                        if (response != null) {

                            int morac = 0;
                            int moracn = 0;
                            int aftac = 0;
                            int aftacn = 0;

                            try {
                                JSONObject atd = new JSONObject(response);

                                morac = Integer.parseInt(atd.getString("morC"));
                                moracn = Integer.parseInt(atd.getString("morCN"));
                                aftac = Integer.parseInt(atd.getString("aftC"));
                                aftacn = Integer.parseInt(atd.getString("aftCN"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            PieChart pieChart = myDialog.findViewById(R.id.pieChartUatd);

                            ArrayList<PieEntry> allEntries = new ArrayList<>();

                            PieEntry entry1 = new PieEntry(morac, "Morning attend marked Count");
                            allEntries.add(entry1);

                            PieEntry entry2 = new PieEntry(moracn, "Morning not attend marked Count");
                            allEntries.add(entry2);

                            PieEntry entry3 = new PieEntry(aftac, "Afternoon attend marked Count");
                            allEntries.add(entry3);

                            PieEntry entry4 = new PieEntry(aftacn, "Afternoon not attend marked Count");
                            allEntries.add(entry4);

                            PieDataSet pieDataSet = new PieDataSet(allEntries, "Marked Attendance of current Month");
                            PieData pieData = new PieData(pieDataSet);
//                            pieData.setValueTextColor(getColor(R.color.deepblue));
                            pieChart.setData(pieData);

                            ArrayList<Integer> color = new ArrayList<>();
                            color.add(getColor(R.color.red));
                            color.add(getColor(R.color.yellow));
                            color.add(getColor(R.color.green));
                            color.add(getColor(R.color.deepblue));

                            pieDataSet.setColors(color);

                            pieChart.setDescription(null);

                            pieChart.animateY(4000, Easing.EasingOption.EaseInCubic);
                            pieChart.animateX(2000, Easing.EasingOption.EaseInExpo);

                            ArrayList<LegendEntry> legendEntries = new ArrayList<>();

                            LegendEntry legend1 = new LegendEntry("Morning not attend marked Count", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getColor(R.color.red));
                            legendEntries.add(legend1);

                            LegendEntry legend2 = new LegendEntry("Morning not attend marked Count", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getColor(R.color.yellow));
                            legendEntries.add(legend2);

                            LegendEntry legend3 = new LegendEntry("Afternoon attend marked Count", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getColor(R.color.green));
                            legendEntries.add(legend3);

                            LegendEntry legend4 = new LegendEntry("Afternoon not attend marked Count", Legend.LegendForm.CIRCLE, Float.NaN, Float.NaN, null, getColor(R.color.deepblue));
                            legendEntries.add(legend4);

                            pieChart.getLegend().setCustom(legendEntries);
                            pieChart.getLegend().setWordWrapEnabled(true);
                            pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);


                            pieChart.getLegend().setEnabled(false);
                            pieChart.setCenterText("Marked Attendance of current Month");
                            pieChart.setCenterTextSize(20);
                            pieDataSet.setValueTextSize(10);

                            //Customize The Chart


                            pieChart.invalidate();

                        }
                    }

                }
            };

            Response.ErrorListener re = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    progressDialog.dismiss();
                    System.out.println(error.getMessage());
                }
            };


            GSONRequest request = new GSONRequest(1, url, rl, re);
            request.json = jsonString;
            VolleySingleton.getVolleySingleton(getApplicationContext()).getRequestQueue().add(request);


        } catch (Exception e) {
//            progressDialog.dismiss();
            e.printStackTrace();
        }


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();


    }

    public void btnChangePwd(View view) {

        myDialog.setContentView(R.layout.popup_member_update_password);
        TextView closetxt = myDialog.findViewById(R.id.closeusruppwd);
        Button closebtn = myDialog.findViewById(R.id.btncloseusruppwd);
        Button btnupdate = myDialog.findViewById(R.id.btnupdatepudrv);

        closetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpwd = ((EditText) myDialog.findViewById(R.id.oldpwdup)).getText().toString();
                String newpwd = ((EditText) myDialog.findViewById(R.id.newpwdup)).getText().toString();
                String renewpwd = ((EditText) myDialog.findViewById(R.id.repwdup)).getText().toString();
//                Toast.makeText(getApplicationContext(), mnth, Toast.LENGTH_SHORT).show();
                if (newpwd.equals(renewpwd)) {
                    try {
//                        progressDialog.setMessage("Please Wailt...");
//                        progressDialog.setCancelable(false);
//                        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
//                        progressDialog.show();

                        signupdriver reqS = new signupdriver(appSetting.userid, oldpwd, newpwd, renewpwd, null, appSetting.usertypeid);
                        final Gson gson = new Gson();
                        final String jsonString = gson.toJson(reqS);

                        String url = appSetting.volleyUrl + "updateUserPassword_serv";

                        Response.Listener rl = new Response.Listener<String>() {
                            @SuppressLint("WrongViewCast")
                            @Override
                            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
//                                progressDialog.dismiss();
                                if (response.equals("1")) {
                                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                                } else if (response.equals("2")) {
                                    Toast.makeText(getApplicationContext(), "Password Not Match", Toast.LENGTH_SHORT).show();
                                } else if (response.equals("3")) {
                                    Toast.makeText(getApplicationContext(), "Update Success", Toast.LENGTH_SHORT).show();
                                }

                            }
                        };

                        Response.ErrorListener re = new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
//                                progressDialog.dismiss();
                                System.out.println(error.getMessage());
                            }
                        };


                        GSONRequest request = new GSONRequest(1, url, rl, re);
                        request.json = jsonString;
                        VolleySingleton.getVolleySingleton(getApplicationContext()).getRequestQueue().add(request);


                    } catch (Exception e) {
//                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Password doesn't match",Toast.LENGTH_LONG).show();
                }


            }
        });

//                Toast.makeText(getApplicationContext(), mnth, Toast.LENGTH_SHORT).show();


        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();


    }

}
