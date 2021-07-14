package com.sandappsefur.transport.StaffService;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.sandappsefur.transport.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Main.GSONRequest;
import Main.VolleySingleton;
import Main.appSetting;
import Main.profile.usertable;
import Main.signup.signinadapter;
import Main.signup.signupdriver;
import Main.staffservice.profileloadMember;
import Main.staffservice.profilereqLoad;
import Main.staffservice.reqSend;

public class ProfileUserReq extends AppCompatActivity {

    String dusid, statUreq;
    TextView iduser, userLogin, fname, mobile, details, vno, shtcount, available, users, bookstatus, vehicleno, nic, status, fee, imagedefault, imageurl, occupation, company, uaddress, pickuploc, droploc, sheetcount, availableu, bookedstatus;
    Button requsetServ;
    Dialog myDialogstatusChange;

    ImageView iv;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user_req);

        myDialogstatusChange = new Dialog(this);

        iv = findViewById(R.id.profilrUserReqDP);

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Please Wailt...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();


//        findViewById(R.id.userfeelo).setVisibility(View.GONE);
//        findViewById(R.id.vwaftrbtnUser).setVisibility(View.GONE);

        if (getIntent().hasExtra("Use_ID") && getIntent().hasExtra("Use_stat")) {

            if (getIntent().getStringExtra("Use_stat").equals("1")) {

                findViewById(R.id.userfeelo).setVisibility(View.GONE);
                findViewById(R.id.vwaftrbtnUser).setVisibility(View.GONE);

                dusid = getIntent().getStringExtra("Use_ID");

                LoaduserProf();

            } else if (getIntent().getStringExtra("Use_stat").equals("2")) {

                dusid = getIntent().getStringExtra("Use_ID");

                LoaduserProf();

            }


//            Toast.makeText(this, "ID :" + dusid, Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Data Passing Error", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

    }

    public void btnBackDriver(View view) {
        onBackPressed();

    }

    public void LoaduserProf() {

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
                @SuppressLint("WrongViewCast")
                @Override
                public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
                    progressDialog.dismiss();
                    if (response.equals("1")) {
                        Toast.makeText(getApplicationContext(), "Invalid User", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("2")) {
                        Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("3")) {
                        Toast.makeText(getApplicationContext(), "More than one result found", Toast.LENGTH_SHORT).show();
                    } else {

                        if (response != null) {
                            final profileloadMember utbl = gson.fromJson(response, profileloadMember.class);

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


                            fname = findViewById(R.id.UserNameReqU);
                            fname.setText(utbl.getFname());
                            ((TextView) findViewById(R.id.cpnyReqU)).setText(utbl.getCompany());
                            ((TextView) findViewById(R.id.picupReqU)).setText(utbl.getPickuploc());
                            ((TextView) findViewById(R.id.endplaceReqU)).setText(utbl.getDroploc());
                            ((TextView) findViewById(R.id.OcupaReqU)).setText(utbl.getOccupation());
                            ((TextView) findViewById(R.id.addressReqU)).setText(utbl.getUaddress());
                            ((TextView) findViewById(R.id.nicReqU)).setText(utbl.getNic());
                            mobile = findViewById(R.id.mobileReqU);
                            mobile.setText(utbl.getMobile());
//                            ((TextView) findViewById(R.id.mobileReqU)).setText(utbl.getMobile());
                            ((TextView) findViewById(R.id.detailsReqU)).setText(utbl.getDetails());
                            ((TextView) findViewById(R.id.feeUserset)).setText(utbl.getFee());

                            requsetServ = findViewById(R.id.getServiceReqU);
                            if (utbl.getReqStatus().equals("0")) {
                                requsetServ.setText("Invite Service");
                            } else if (utbl.getReqStatus().equals("1")) {
                                requsetServ.setText("Accept Request");
                            } else if (utbl.getReqStatus().equals("3")) {
                                requsetServ.setText("Cancel Invite");
                            } else if (utbl.getReqStatus().equals("2")) {
                                requsetServ.setText("Remove Member");
                            } else if (utbl.getReqStatus().equals("4")) {
                                requsetServ.setText("Remove Member");
                            }

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

    public void invitetoService(View view) {
        progressDialog = new ProgressDialog(this);
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
                        ((Button) findViewById(R.id.getServiceReqU)).setText("Cancel Request");
                        Toast.makeText(getApplicationContext(), "Invite Send", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("2")) {
                        ((Button) findViewById(R.id.getServiceReqU)).setText("Invite Service");
                        findViewById(R.id.userfeelo).setVisibility(View.GONE);
                        findViewById(R.id.vwaftrbtnUser).setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Member Removed", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("3")) {
                        Toast.makeText(getApplicationContext(), "Request Accept", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("4")) {
                        ((Button) findViewById(R.id.getServiceReqU)).setText("Invite Service");
                        findViewById(R.id.userfeelo).setVisibility(View.GONE);
                        findViewById(R.id.vwaftrbtnUser).setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Member Removed", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("5")) {
                        ((Button) findViewById(R.id.getServiceReqU)).setText("Invite Service");
                        Toast.makeText(getApplicationContext(), "Invite Canceled", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("6")) {
                        Toast.makeText(getApplicationContext(), "You can't Send Invite or Accept Request to Members.Because you don't have space for new Members In Vehicle", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("7")) {
                        Toast.makeText(getApplicationContext(), "More Users", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("8")) {
                        Toast.makeText(getApplicationContext(), "More than one result found", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("9")) {
                        Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                    }else if (response.equals("10")) {
                        Toast.makeText(getApplicationContext(), "You can't Send  Request or Accept Invite. Because this user already in a Staff Service", Toast.LENGTH_SHORT).show();
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

    public void btnChengeUserFeeAmount(View view) {

        myDialogstatusChange.setContentView(R.layout.popup_userfees_driver);
        TextView closebtn = myDialogstatusChange.findViewById(R.id.closeUserfee);
        Button btnupdate = myDialogstatusChange.findViewById(R.id.btnupdateUserfee);
        Button btnclose = myDialogstatusChange.findViewById(R.id.btncloseuserfee);
//        ((EditText)myDialogstatusChange.findViewById(R.id.userfreeamount)).setText("0");

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialogstatusChange.dismiss();
            }
        });
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialogstatusChange.dismiss();
            }
        });


        myDialogstatusChange.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogstatusChange.show();

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"btn click", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(),((EditText)myDialogstatusChange.findViewById(R.id.userfreeamount)).getText().toString(), Toast.LENGTH_SHORT).show();
                try {

//                    reqSend reqS = new reqSend(appSetting.userid, dusid, 1, 1, 0, appSetting.usertypeid, 0);
                    signupdriver reqS = new signupdriver(appSetting.userid,dusid,appSetting.usertypeid,((EditText)myDialogstatusChange.findViewById(R.id.userfreeamount)).getText().toString(),null,null);
                    final Gson gson = new Gson();
                    final String jsonString = gson.toJson(reqS);

                    String url = appSetting.volleyUrl + "updateUserFee_serv";

                    Response.Listener rl = new Response.Listener<String>() {
                        @SuppressLint("WrongViewCast")
                        @Override
                        public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
                            progressDialog.dismiss();
                            if (response.equals("1")) {
                                Toast.makeText(getApplicationContext(), "More Users", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("2")) {
                                Toast.makeText(getApplicationContext(), "More than one result", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("3")) {
                                ((TextView)findViewById(R.id.feeUserset)).setText(((EditText)myDialogstatusChange.findViewById(R.id.userfreeamount)).getText().toString());
                                Toast.makeText(getApplicationContext(), "Fee Updated", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("4")) {
                                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
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

    public void btnpurCall(View view){
        try {
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:"+((TextView) findViewById(R.id.mobileReqU)).getText()));
            startActivity(i);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Connection Error",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public void btnpurmsg(View view){
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