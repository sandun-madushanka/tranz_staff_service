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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.sandappsefur.transport.R;

import java.util.HashMap;
import java.util.Map;

import Main.GSONRequest;
import Main.VolleySingleton;
import Main.appSetting;
import Main.signup.signupnuser;

public class Fragmenttransnuser extends Fragment {

    View view;

    public Fragmenttransnuser() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_transnu, container, false);

        Button btnsignupnu = view.findViewById(R.id.btnsignupnuc);

        btnsignupnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Please Wailt...");
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                progressDialog.show();

                String name = ((EditText) view.findViewById(R.id.namenu)).getText().toString();
                String mobile = ((EditText) view.findViewById(R.id.mobilenu)).getText().toString();
                String email = ((EditText) view.findViewById(R.id.emailnu)).getText().toString();
                String password = ((EditText) view.findViewById(R.id.passwordnu)).getText().toString();

                signupnuser u = new signupnuser(name, mobile, email, password);
                final Gson gson = new Gson();
                final String jsomString = gson.toJson(u);

                String url = appSetting.volleyUrl + "signupmappnusers_serv";

                Response.Listener rl = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                        if (response.equals("1")) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "User Registered", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("2")) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Already have mail", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("3")) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Duplicate", Toast.LENGTH_SHORT).show();

                        }

                    }
                };

                Response.ErrorListener re = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                };

//                StringRequest request = new StringRequest(StringRequest.Method.POST, url, rl, re) {
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        HashMap<String, String> data = new HashMap<>();
//                        data.put("id", jsomString);
//
//                        return data;
//                    }
//                };
                GSONRequest request = new GSONRequest(1, url, rl, re);// meka user krnwa uda widiyata anonimus widiyata hdanne nathuwa pahala clz eka hdla ekn code krnna
                request.json = jsomString;
                VolleySingleton.getVolleySingleton(getContext()).getRequestQueue().add(request);

            }
        });

        return view;
    }
}
