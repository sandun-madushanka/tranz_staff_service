package com.sandappsefur.transport.StaffService;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import Main.GSONRequest;
import Main.VolleySingleton;
import Main.appSetting;
import Main.signup.signupnuser;
import Main.staffservice.profileloadMember;
import Main.staffservice.profilereqLoad;
import Main.staffservice.reqSend;
import Main.staffservice.searchListusr;

public class FragmentNewsfeed extends Fragment {

    ProgressDialog progressDialog;
    View view;

    String dusid;
    TextView iduser, userLogin, fname, mobile, details, vno, shtcount, available, users, bookstatus, vehicleno, nic, status, fee, imagedefault, imageurl, occupation, company, uaddress, pickuploc, droploc, sheetcount, availableu, bookedstatus;

    ImageView iv;
    String uid;
    TextView vsttat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        iv = view.findViewById(R.id.profileDrivernfu);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wailt...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();

        viewstfServiceuH();

        Button requsetServ = view.findViewById(R.id.getServicereqnfu);
        requsetServ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeServ();
            }
        });
        Button calld = view.findViewById(R.id.btncallstfServiceUserMem);
        calld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calldriver();
            }
        });
        Button smsdri = view.findViewById(R.id.btnmsgstfServiceUserMem);
        smsdri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsdriver();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fname = view.findViewById(R.id.namenfu);
        vsttat = view.findViewById(R.id.vstatnot);
        pickuploc = view.findViewById(R.id.picupnfu);
        droploc = view.findViewById(R.id.endplacenfu);
        company = view.findViewById(R.id.companynfu);
        uaddress = view.findViewById(R.id.addressnfu);
        nic = view.findViewById(R.id.nicnfu);
        mobile = view.findViewById(R.id.mobilenfu);
        details = view.findViewById(R.id.detailsnfu);


    }

    public void calldriver() {
        String nm = ((TextView) view.findViewById(R.id.mobilenfu)).getText().toString();
//        Toast.makeText(getContext(), nm, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:" + nm));
        startActivity(i);
    }

    public void smsdriver() {
        String nm = ((TextView) view.findViewById(R.id.mobilenfu)).getText().toString();
//        Toast.makeText(getContext(), nm, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setType("text/plain");
        i.setData(Uri.parse("smsto:" + nm));
        i.putExtra("sms_body", "");
        startActivity(i);
    }

    public void viewstfServiceuH() {

        try {

            String firstLetter = String.valueOf("A");
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(1);

            final TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color);

            FirebaseStorage storage = FirebaseStorage.getInstance("gs://tranz-b8710.appspot.com/");
            StorageReference storageReference = storage.getReference("users");
            final StorageReference child = storageReference.child("profileimages/");

            signupnuser snu = new signupnuser(appSetting.userid, null, null, appSetting.usertypeid);
            final Gson gson = new Gson();
            final String jsonString = gson.toJson(snu);
            String url = appSetting.volleyUrl + "stfServenws_serv?jssonString=" + jsonString;

            Response.Listener rl = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
                    if (response.equals("1")) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Error User", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("2")) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("3")) {
                        progressDialog.dismiss();
                        view.findViewById(R.id.linerloutlyr).setVisibility(View.GONE);
//                         LinearLayout linearLayout = (LinearLayout) getActivity().getWindow().findViewById(R.id.linerloutlyr);
//                         linearLayout.
                        Toast.makeText(getContext(), "You are not in a Staff Service, Request Staff Services", Toast.LENGTH_LONG).show();
                    } else {

                        if (response != null) {
                            final profileloadMember utbl = gson.fromJson(response, profileloadMember.class);

                            if (utbl.getImageurl() != null) {
                                if (utbl.getImageurl().equals("user" + utbl.getIduser())) {
                                    Thread t = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            child.child(utbl.getImageurl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    System.out.println(">>>");
                                                    // Picasso.with(context).load(uri).into(viewHolder.imageView);
                                                    Picasso.with(getContext()).load(uri).placeholder(drawable).fit().centerCrop().into(iv);

                                                }
                                            });
                                        }
                                    });
                                    t.start();
                                }
                            }

                            fname.setText(utbl.getFname());
                            int vstat = Integer.parseInt(utbl.getSrUMFee());
////                            Toast.makeText(getContext(), utbl.getSrUMFee(), Toast.LENGTH_SHORT).show();
//                            TextView vsttat = view.findViewById(R.id.vstatnot);
                            if (vstat == 1) {
                                vsttat.setText("Perfect");
                                vsttat.setTextColor(view.getResources().getColor(R.color.green));
                            } else if (vstat == 0) {
                                vsttat.setText("Breakdown");
                                vsttat.setTextColor(view.getResources().getColor(R.color.red));

                            }
                            ((TextView) view.findViewById(R.id.vnonfu)).setText(utbl.getVehicleno());
                            ((TextView) view.findViewById(R.id.shtcountnfu)).setText(utbl.getSheetcount());
                            int shtcount = Integer.parseInt(utbl.getSheetcount());
                            int avaimembercount = Integer.parseInt(utbl.getAvailableu());
                            int availableshtcnt = shtcount - avaimembercount;
                            String avashtot = String.valueOf(availableshtcnt);
                            ((TextView) view.findViewById(R.id.availableshtcountnfu)).setText(avashtot);
                            int vstut = Integer.parseInt(utbl.getBookedstatus());
                            String vstatus = String.valueOf(vstut);
                            pickuploc.setText(utbl.getPickuploc());
                            droploc.setText(utbl.getDroploc());
                            company.setText(utbl.getCompany());
                            uaddress.setText(utbl.getUaddress());
                            nic.setText(utbl.getNic());
                            mobile.setText(utbl.getMobile());
                            details.setText(utbl.getDetails());

                            uid = utbl.getIduser();

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
            VolleySingleton.getVolleySingleton(getContext()).getRequestQueue().add(request);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void removeServ() {

        try {
            progressDialog.setMessage("Please Wailt...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.show();

            reqSend reqS = new reqSend(appSetting.userid, uid, 1, 1, 0, appSetting.usertypeid, 0);
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
                        ((Button) view.findViewById(R.id.getServicereqnfu)).setText("Cancel Request");
                        Toast.makeText(getContext(), "Request Send", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("2")) {
                        ((Button) view.findViewById(R.id.getServicereqnfu)).setText("Request Service");
                        Toast.makeText(getContext(), "Removed Successful", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("3")) {
                        Toast.makeText(getContext(), "Invite Accept", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("4")) {
                        ((Button) view.findViewById(R.id.getServicereqnfu)).setText("Request Service");
                        Toast.makeText(getContext(), "Removed Successful", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("5")) {
                        ((Button) view.findViewById(R.id.getServicereqnfu)).setText("Request Service");
                        Toast.makeText(getContext(), "Request Canceled", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("10")) {
                        Toast.makeText(getContext(), "You can't Send  Request or Accept Invite. Because you already in a Staff Service", Toast.LENGTH_SHORT).show();
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
            VolleySingleton.getVolleySingleton(getContext()).getRequestQueue().add(request);


        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }


    }

}
