package com.sandappsefur.transport.profile;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.sandappsefur.transport.R;
import com.sandappsefur.transport.profile.FragmentUserDetails;
import com.sandappsefur.transport.profile.profile;
import com.squareup.picasso.Picasso;

import java.util.List;

import Main.GSONRequest;
import Main.VolleySingleton;
import Main.appSetting;
import Main.profile.usertable;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserViewDetailFragment extends Fragment {
    private TextView iduser, userLogin, fname, mobile, details, vehicleno, nic, status, fee, imagedefault, imageurl, occupation, company, uaddress, pickuploc, droploc, sheetcount, availableu, bookedstatus;

    ImageView iv;
    ProgressDialog progressDialog;

    public UserViewDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println("fragmenttttttttttttttttttt loaded");


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_view_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fname = getView().findViewById(R.id.usernameuf);
        occupation = getView().findViewById(R.id.occupationu);
        pickuploc = getView().findViewById(R.id.pickPlaceUid);
        droploc = getView().findViewById(R.id.endplaceuid);
        company = getView().findViewById(R.id.companyu);
        uaddress = getView().findViewById(R.id.addressu);
        nic = getView().findViewById(R.id.nicu);
        mobile = getView().findViewById(R.id.mobu);
        details = getView().findViewById(R.id.dtialsu);
        iv = getView().findViewById(R.id.imageViewUserdp);


        Button editbtn = getView().findViewById(R.id.btnedituserprofile);
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnEditProfile(v);
            }
        });

        viewProfileUser();

    }

    public void btnEditProfile(View view) {

//        ((LinearLayout) getActivity().findViewById(R.id.linearlayoutuserprofile)).removeAllViews();

//        FragmentUserDetails fragment1 = new FragmentUserDetails();

//        FragmentManager manager = getFragmentManager();
//        List<Fragment> fragments = manager.getFragments();
//        for (Fragment f : fragments) {
//            manager.beginTransaction().remove(f);
//        }
//        manager.beginTransaction().remove(this);
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.add(R.id.linearlayoutuserprofile, fragment1, "feditprofile");
////        transaction.replace(R.id.linearlayoutuserprofile, fragment1, "feditprofile");
//        transaction.commit();
        profile activity = (profile) getActivity();
        activity.btnEditProfile();

    }

    public void viewProfileUser() {

        progressDialog = new ProgressDialog(getContext());
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
                        Toast.makeText(getContext(), "Invalid User", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("2")) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                    } else {

                        if (response != null) {
                            final usertable utbl = gson.fromJson(response, usertable.class);

                            if (utbl.getImageurl() != null) {
                                if (utbl.getImageurl().equals("user" + appSetting.userid)) {
                                    Thread t = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            child.child(utbl.getImageurl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    System.out.println(">>>-----------------------------------");
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
                            occupation.setText(utbl.getOccupation());
                            pickuploc.setText(utbl.getPickuploc());
                            droploc.setText(utbl.getDroploc());
                            company.setText(utbl.getCompany());
                            uaddress.setText(utbl.getUaddress());
                            nic.setText(utbl.getNic());
                            mobile.setText(utbl.getMobile());
                            details.setText(utbl.getDetails());

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
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

}
