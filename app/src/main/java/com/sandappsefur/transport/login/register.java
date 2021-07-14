package com.sandappsefur.transport.login;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sandappsefur.transport.R;

public class register extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private  singupviewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        tabLayout = (TabLayout) findViewById(R.id.tabLayout_id1);
        viewPager = (ViewPager) findViewById(R.id.viewPager_id1);
        adapter = new singupviewPagerAdapter(getSupportFragmentManager());

        //framents here
        adapter.AddFragment(new Fragmenttransnuser(),"Normal users");
        adapter.AddFragment(new Fragmenttransdriv(),"Drivers");


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void btnSignin(View v){
        Intent i = new Intent(this, login.class);
        startActivity(i);
    }

}
