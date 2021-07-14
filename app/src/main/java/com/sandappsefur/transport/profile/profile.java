package com.sandappsefur.transport.profile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sandappsefur.transport.R;
import com.sandappsefur.transport.home.Home;

import java.util.ArrayList;
import java.util.List;

public class profile extends AppCompatActivity {

    TextView iduser, userLogin, fname, mobile, details, vehicleno, nic, status, fee, imagedefault, imageurl, occupation, company, uaddress, pickuploc, droploc, sheetcount, availableu, bookedstatus;

    Dialog myDialogstatusChange;
    FragmentUserDetails fragment1;

    ImageView iv;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        myDialogstatusChange = new Dialog(this);

//        iv = findViewById(R.id.imageViewUserdp);

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Please Wailt...");
//        progressDialog.setCancelable(false);
//        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
//        progressDialog.show();

//        viewProfileUser();


//        FragmentManager supportFragmentManager = getSupportFragmentManager();
//        List<Fragment> fragments = supportFragmentManager.getFragments();
//
//        for(Fragment f : fragments){
////            supportFragmentManager.beginTransaction().remove(f);// mekn serama fragment ain wenwa
//        }

//        supportFragmentManager.beginTransaction().add(new UserViewDetailFragment(),"userview").commit();


//        fragment1 = new FragmentUserDetails();

        loadNormalUserProfile();
        System.out.println("outtttttttttttttttttt---------------------------------------------");


    }

    public void loadNormalUserProfile() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        List<Fragment> fragments = manager.getFragments();
        for (Fragment f : fragments) {
          //  transaction.remove(f);
        }


        transaction.replace(R.id.linearlayoutuserprofile, new UserViewDetailFragment(), "f1");
//    transaction.replace(R.id.linearlayoutuserprofile, new FragmentUserDetails(), "f1");
        transaction.commit();
    }

    public void btnHomeUser(View view) {
        Intent i = new Intent(getApplicationContext(), Home.class);
        startActivity(i);
    }

    public void btnEditProfile() {

        ((LinearLayout) findViewById(R.id.linearlayoutuserprofile)).removeAllViews();

        fragment1 = new FragmentUserDetails();


        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        List<Fragment> fragments = manager.getFragments();
        for (Fragment f : fragments) {
          //  transaction.remove(f);
        }


        transaction.replace(R.id.linearlayoutuserprofile, fragment1, "f1");
        transaction.commit();

    }

    private void loadViewProfile() {

        UserViewDetailFragment frag = new UserViewDetailFragment();
    }

    public void btnChengeUserStatus(View view) {

        myDialogstatusChange.setContentView(R.layout.popup_updatestatususers);
        TextView closebtn = myDialogstatusChange.findViewById(R.id.closeUserStatus);
        Button btnupdate = myDialogstatusChange.findViewById(R.id.btnstatusupdateUser);
        Button btnclose = myDialogstatusChange.findViewById(R.id.btnstatusclose1user);

        ArrayList<String> bl = new ArrayList<>();
        bl.add("User");
        bl.add("Doctor");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, bl);

        Spinner sp = myDialogstatusChange.findViewById(R.id.userstatusattend);
        sp.setAdapter(adapter);

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


            }
        });

    }

//    public void btnEditProfile(View view){}

}
