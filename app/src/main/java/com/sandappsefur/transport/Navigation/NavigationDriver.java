package com.sandappsefur.transport.Navigation;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.sandappsefur.transport.R;
import com.sandappsefur.transport.StaffService.SearchServiceMemberlistAdpterDriver;

import org.json.JSONArray;
import org.json.JSONObject;

import Main.GSONRequest;
import Main.VolleySingleton;
import Main.appSetting;
import Main.locationNav.locNav;
import Main.signup.signinadapter;
import Main.staffservice.reqSend;
import Main.staffservice.searchListusr;

public class NavigationDriver extends AppCompatActivity {

    GoogleMap map;
    Marker m;
    Location lastlocation;
    LocationRequest request;
    MarkerOptions markerOptions3;
    LatLng targetLatLng;
    double logtute;
    double latitute;

    double latdri;
    double longdri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_driver);

//        request = LocationRequest.create();
//        request.setInterval(200);
//        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        SupportMapFragment fragment = new SupportMapFragment();
        final FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.framelayoutmappDriver, fragment, "maps");
        transaction.commit();

        OnMapReadyCallback listner = new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    map.setMyLocationEnabled(true);
                } else {
                    Toast.makeText(getApplicationContext(), "No Permission", Toast.LENGTH_SHORT).show();
                    String permission[] = new String[1];
                    permission[0] = android.Manifest.permission.ACCESS_FINE_LOCATION;

                    requestPermissions(permission, 101);
                }

                LocationRequest request = LocationRequest.create();
                request.setInterval(2000);
                request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationCallback listner2 = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {

                        if (m != null) {
                            m.remove();
                        }
                        lastlocation = locationResult.getLastLocation();
                        MarkerOptions markerOptions1 = new MarkerOptions();
                        LatLng location2 = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());
                        CameraPosition.Builder camerabuilder = new CameraPosition.Builder();
                        camerabuilder.target(location2);
                        camerabuilder.zoom(10);
                        CameraPosition cameraPosition = camerabuilder.build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        map.animateCamera(cameraUpdate);
                        markerOptions1.position(location2);
                        markerOptions1.title("You");
                        markerOptions1.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
                        m = map.addMarker(markerOptions1);
                        m.showInfoWindow();
                        markerOptions1.flat(true); //map eka roate karaddi roate wenawa

                        logtute = lastlocation.getLongitude();
                        latitute = lastlocation.getLatitude();

                        sendMapLocationDriver();
                        ViewLocationDriver();

                    }
                };

                FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(getApplicationContext());

                if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//       map.setMyLocationEnabled(true);
                    client.requestLocationUpdates(request, listner2, Looper.myLooper());
                } else {

                    Toast.makeText(getApplicationContext(), "No Permission", Toast.LENGTH_SHORT).show();
                    String permission[] = new String[1];
                    permission[0] = android.Manifest.permission.ACCESS_FINE_LOCATION;

                    requestPermissions(permission, 101);
                }

                UiSettings settings = map.getUiSettings();
                settings.setZoomControlsEnabled(true);
                settings.setCompassEnabled(true);
                settings.setMapToolbarEnabled(true);


            }
        };
        fragment.getMapAsync(listner);



    }

    public void backNavDriver(View view) {
        onBackPressed();
    }

    public void sendMapLocationDriver(){
        try {

            locNav reqS = new locNav(appSetting.usertypeid,  appSetting.userid, logtute,latitute,1);
            final Gson gson = new Gson();
            final String jsonString = gson.toJson(reqS);

            String url = appSetting.volleyUrl + "updateLocationDriver_serv";

            Response.Listener rl = new Response.Listener<String>() {
                @SuppressLint("WrongViewCast")
                @Override
                public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
                    if (response.equals("1")) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("2")) {
                        Toast.makeText(getApplicationContext(), "Duplicate Error", Toast.LENGTH_SHORT).show();
                    }

                }
            };

            Response.ErrorListener re = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                }
            };


            GSONRequest request = new GSONRequest(1, url, rl, re);
            request.json = jsonString;
            VolleySingleton.getVolleySingleton(getApplicationContext()).getRequestQueue().add(request);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ViewLocationDriver(){
        try {

            locNav reqS = new locNav(appSetting.usertypeid,  appSetting.userid, 0.0,0.0,null);
            final Gson gson = new Gson();
            final String jsonString = gson.toJson(reqS);

            String url = appSetting.volleyUrl + "viewLocation_serv";

            Response.Listener rl = new Response.Listener<String>() {
                @SuppressLint("WrongViewCast")
                @Override
                public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
                    if (response.equals("1")) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("2")) {
                        Toast.makeText(getApplicationContext(), "Duplicate Error", Toast.LENGTH_SHORT).show();
                    }else if (response.equals("3")) {
                        Toast.makeText(getApplicationContext(), "Driver is Offline", Toast.LENGTH_SHORT).show();
                    }else{
                        if (response != null) {
                            try {

                                JSONArray servicelist = new JSONArray(response);
                                for (int i = 0; i < servicelist.length(); i++) {
                                    JSONObject Objectmarks = servicelist.getJSONObject(i);
                                    String usrnm = Objectmarks.getString("Fname");
                                    String latu = Objectmarks.getString("latu");
                                    String lngti = Objectmarks.getString("longu");
                                    String stu = Objectmarks.getString("usatt");

                                    latdri = Double.parseDouble(latu);
                                    longdri = Double.parseDouble(lngti);
//
//                                    System.out.println(latu+"@@@@@@@@@@@@@@@@@@@-------------------");
//                                    System.out.println(lngti+"@@@@@@@@@@@@@@@@@@@----------------------------------");
////
                                    Marker m2 = null;
                                    if(m2!= null){
                                        m2.remove();
                                    }

                                    targetLatLng = new LatLng(latdri, longdri);
                                    markerOptions3 = new MarkerOptions().position(targetLatLng);
                                    markerOptions3.title(usrnm);
                                    markerOptions3.icon(BitmapDescriptorFactory.fromResource(R.drawable.userpin));

                                    m2=map.addMarker(markerOptions3);

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }



                        }
                    }

                }
            };

            Response.ErrorListener re = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                }
            };


            GSONRequest request = new GSONRequest(1, url, rl, re);
            request.json = jsonString;
            VolleySingleton.getVolleySingleton(getApplicationContext()).getRequestQueue().add(request);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
