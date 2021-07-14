package com.sandappsefur.transport.profile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;

import Main.GSONRequest;
import Main.VolleySingleton;
import Main.appSetting;
import Main.profile.usertable;
import Main.staffservice.profilereqLoad;
import Main.staffservice.reqSend;

public class profileDriverReq extends AppCompatActivity {

    String dusid;
    TextView iduser, userLogin, fname, mobile, details, vno, shtcount, available, users, bookstatus, vehicleno, nic, status, fee, imagedefault, imageurl, occupation, company, uaddress, pickuploc, droploc, sheetcount, availableu, bookedstatus;
    Button requsetServ;

    ImageView iv;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_driver_req);

        iv = findViewById(R.id.profileDriverReqdp);

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Please Wailt...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();

        if (getIntent().hasExtra("Driv_ID")) {
            dusid = getIntent().getStringExtra("Driv_ID");


            viewProfileDriverReq();

//            Toast.makeText(this, "ID :" + dusid, Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Data Passing Error", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

    }

    public void viewProfileDriverReq() {

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

                            fname = findViewById(R.id.nameDriverr);
                            fname.setText(utbl.getFname());
                            ((TextView) findViewById(R.id.vnoDriverr)).setText(utbl.getVehicleno());
                            ((TextView) findViewById(R.id.shtcountDriverr)).setText(utbl.getSheetcount());
                            int shtcount = Integer.parseInt(utbl.getSheetcount());
                            int avaimembercount = Integer.parseInt(utbl.getAvailableu());
                            int availableshtcnt = shtcount - avaimembercount;
                            String avashtot = String.valueOf(availableshtcnt);
                            ((TextView) findViewById(R.id.availableshtcountDriverr)).setText(avashtot);
                            int vstut = Integer.parseInt(utbl.getBookedstatus());
                            String vstatus = String.valueOf(vstut);
                            pickuploc = findViewById(R.id.picupDriverr);
                            pickuploc.setText(utbl.getPickuploc());
                            droploc = findViewById(R.id.endplaceDriverr);
                            droploc.setText(utbl.getDroploc());
                            company = findViewById(R.id.companyDriverr);
                            company.setText(utbl.getCompany());
                            uaddress = findViewById(R.id.addressDriverr);
                            uaddress.setText(utbl.getUaddress());
                            nic = findViewById(R.id.nicDriverr);
                            nic.setText(utbl.getNic());
                            mobile = findViewById(R.id.mobileDriverr);
                            mobile.setText(utbl.getMobile());
                            details = findViewById(R.id.detailsDriverr);
                            details.setText(utbl.getDetails());
                            requsetServ = findViewById(R.id.getServicereq);
                            if (utbl.getReqStatus().equals("0")) {
                                requsetServ.setText("Request Service");
                            } else if (utbl.getReqStatus().equals("1")) {
                                requsetServ.setText("Cancel Request");
                            } else if (utbl.getReqStatus().equals("3")) {
                                requsetServ.setText("Accept Invite");
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

    public void btnBackDriver(View view) {
        onBackPressed();

    }

    public void reqService(View view) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        try {
            progressDialog.setMessage("Please Wailt...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.show();

            reqSend reqS = new reqSend(appSetting.userid, dusid, 1, 1, 0, appSetting.usertypeid, 0);
            final Gson gson = new Gson();
            final String jsonString = gson.toJson(reqS);

            String url = appSetting.volleyUrl + "serviceReq_serv";

            Response.Listener rl = new Response.Listener<String>() {
                @SuppressLint("WrongViewCast")
                @Override
                public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
                    progressDialog.dismiss();
                    if (response.equals("1")) {
                        ((Button) findViewById(R.id.getServicereq)).setText("Cancel Request");
                        Toast.makeText(getApplicationContext(), "Request Send", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("2")) {
                        Toast.makeText(getApplicationContext(), "More than one result found", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("3")) {
                        Toast.makeText(getApplicationContext(), "Invite Accept", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("4")) {
                        Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("5")) {
                        ((Button) findViewById(R.id.getServicereq)).setText("Request Service");
                        Toast.makeText(getApplicationContext(), "Request Canceled", Toast.LENGTH_SHORT).show();
                    }else if (response.equals("10")) {
                        Toast.makeText(getApplicationContext(), "You can't Send  Request or Accept Invite. Because you already in a Staff Service", Toast.LENGTH_SHORT).show();
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

    public void btnpdrCall(View view){
        try {
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:"+((TextView) findViewById(R.id.mobileReqU)).getText()));
            startActivity(i);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Connection Error",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public void btnpdrmsg(View view){
        try {
            Intent  i = new Intent(Intent.ACTION_SENDTO);
            i.setType("text/plain");
            i.setData(Uri.parse("smsto:"+((TextView) findViewById(R.id.mobileReqU)).getText()));
            i.putExtra("sms_body","");
            startActivity(i);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Connection Error",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


}
