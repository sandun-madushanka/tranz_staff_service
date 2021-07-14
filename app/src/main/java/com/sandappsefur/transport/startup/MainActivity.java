package com.sandappsefur.transport.startup;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.sandappsefur.transport.R;
import com.sandappsefur.transport.home.Home;
import com.sandappsefur.transport.home.HomeD;
import com.sandappsefur.transport.login.login;

import Main.appSetting;

public class MainActivity extends AppCompatActivity {


    private TextView tv;
    private ImageView iv;

    SwipeRefreshLayout pullToRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pullToRefresh = findViewById(R.id.swipetoRefreshMact);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                iv = findViewById(R.id.iv);
                Animation anm = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.mytransition);
                iv.startAnimation(anm);

                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = cm.getActiveNetworkInfo();

                if(ni !=null && ni.isConnected()){
                    Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Please Connect to the Internet for Continue App",Toast.LENGTH_SHORT).show();
                }

                pullToRefresh.setRefreshing(false);
            }
        });

        iv = findViewById(R.id.iv);
        Animation anm = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        iv.startAnimation(anm);

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if(ni !=null && ni.isConnected()){

            final Intent i = new Intent(this, login.class);
            final Intent i2 = new Intent(this, HomeD.class);
            final Intent i3 = new Intent(this, Home.class);

            SharedPreferences sp = getSharedPreferences("settingslogin", MODE_PRIVATE);

            String getShuserid = sp.getString("shuserid", "null");
            String getShuserloginid = sp.getString("shuserloginid", "null");
            String getShusertypeid = sp.getString("shusertypeid", "null");
            String getShloginStatus = sp.getString("shlogintype", "0");

            appSetting.userid = getShuserid;
            appSetting.userloginid = getShuserloginid;
            appSetting.usertypeid = getShusertypeid;
            appSetting.loginStatus = getShloginStatus;


//        System.out.println(appSetting.userid + "," + appSetting.userloginid + "," + appSetting.usertypeid + "," + appSetting.loginStatus);

            Thread t = new Thread() {
                public void run() {
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {

                        if (appSetting.loginStatus.equals("0")) {
                            startActivity(i);
                        } else if (appSetting.loginStatus.equals("1")) {
                            if (appSetting.usertypeid.equals("2")) {
                                startActivity(i2);
                            } else if (appSetting.usertypeid.equals("3")) {
                                startActivity(i3);
                            }else{
//                            startActivity(i);
                            }
                        }


                        finish();
                    }
                }
            };
            t.start();

        }else{
            Toast.makeText(this,"Please Connect to the Internet",Toast.LENGTH_SHORT).show();
        }




    }
}
