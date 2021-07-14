package com.sandappsefur.transport.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sandappsefur.transport.R;

import Main.GSONRequest;
import Main.VolleySingleton;
import Main.appSetting;
import Main.signup.signupdriver;
import Main.signup.signupnuser;

public class Fragmenttransdriv extends Fragment {

    View view;

    public Fragmenttransdriv() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_transdriv,container,false);

        Button btnsignupdriv = view.findViewById(R.id.btnsignupdrivb);

        btnsignupdriv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Please Wailt...");
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                progressDialog.show();

                String name = ((EditText) view.findViewById(R.id.nameep)).getText().toString();
                String nic = ((EditText) view.findViewById(R.id.nicdriv)).getText().toString();
                String vehicleno = ((EditText) view.findViewById(R.id.vehno)).getText().toString();
                String mobile = ((EditText) view.findViewById(R.id.mobep)).getText().toString();
                String email = ((EditText) view.findViewById(R.id.emailep)).getText().toString();
                String password = ((EditText) view.findViewById(R.id.passwordep)).getText().toString();

                signupdriver u2 = new signupdriver(name, nic, vehicleno, mobile, email, password);
                final Gson gson = new Gson();
                final String jsomString = gson.toJson(u2);

//                Toast.makeText(getContext(), name+nic+vehicleno, Toast.LENGTH_SHORT).show();

//                String url = "http://192.168.8.103:8080/tranz/signupmappdriver_serv?jssonString=" + jsomString;
                String url = appSetting.volleyUrl+"signupmappdriver_serv";

                Response.Listener rl = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                        System.out.println(response);
                        if (response.equals('1')) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "User Registered", Toast.LENGTH_SHORT).show();
                        } else if (response.equals('2')) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Already have mail", Toast.LENGTH_SHORT).show();
                        } else if (response.equals('3')) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Duplicate", Toast.LENGTH_SHORT).show();

                        }

                    }
                };

                Response.ErrorListener re = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Fail to Connect With Server", Toast.LENGTH_SHORT).show();
                        System.out.println(error.getMessage());
                    }
                };


                GSONRequest request = new GSONRequest(1, url, rl, re);// meka user krnwa uda widiyata anonimus widiyata hdanne nathuwa pahala clz eka hdla ekn code krnna
                request.json = jsomString;
                VolleySingleton.getVolleySingleton(getContext()).getRequestQueue().add(request);

            }
        });

        return view;

    }
}
