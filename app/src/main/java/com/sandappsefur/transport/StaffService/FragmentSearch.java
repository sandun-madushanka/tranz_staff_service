package com.sandappsefur.transport.StaffService;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.sandappsefur.transport.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Main.appSetting;
import Main.profile.usertable;
import Main.signup.signupdriver;
import Main.signup.signupnuser;
import Main.staffservice.searchListusr;
import Main.staffservice.searchListusrnew;

public class FragmentSearch extends Fragment {

    RecyclerView recyclerView;
    SearchServiceListAdapter adapter;
    List<searchListusrnew> ulist;
    View view;

    ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wailt...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();

        ulist = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleviewSearchService);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);


        ImageView imageView = view.findViewById(R.id.imageuserSeView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setAdapter(null);
                seachUserD();
            }
        });

//        Toast.makeText(getContext(), appSetting.usertypeid, Toast.LENGTH_SHORT).show();
        signupnuser snu = new signupnuser(appSetting.userid,null,null, appSetting.usertypeid);
        final Gson gson = new Gson();
        final String jsonString = gson.toJson(snu);
        String url = appSetting.volleyUrl + "UserSearchlistl_serv?jssonString="+jsonString;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url

                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

//                System.out.println(response);

                if (response.equals("1")) {
                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }else if (response.equals("2")) {
                    Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
                    recyclerView.setAdapter(null);
                } else {
                    recyclerView.setAdapter(null);
//                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    try {

                        JSONArray servicelist = new JSONArray(response);
                        for (int i = 0; i < servicelist.length(); i++) {
                            JSONObject Objectmarks = servicelist.getJSONObject(i);
                            String idu = Objectmarks.getString("Usid");
                            String unm = Objectmarks.getString("Fname");
                            String satrt = Objectmarks.getString("startL");
                            String end = Objectmarks.getString("endL");
                            String shtcnt = Objectmarks.getString("Shtcontt");
                            String avau = Objectmarks.getString("Avau");
                            String ppic = Objectmarks.getString("Ppic");
                            String reqStat = Objectmarks.getString("reqStat");

                            searchListusrnew userL = new searchListusrnew(idu,unm,null,satrt,end,shtcnt,avau,ppic,reqStat);
                            ulist.add(userL);

                        }
                        adapter = new SearchServiceListAdapter(getContext(), ulist);
                        recyclerView.setAdapter(adapter);

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                System.out.println(error);
            }
        });
        Volley.newRequestQueue(getContext()).add(stringRequest);

        return view;
    }

    public void seachUserD(){

        String srdl = ((EditText)view.findViewById(R.id.editTextdesdriv)).getText().toString();

//        Toast.makeText(getContext(), appSetting.usertypeid, Toast.LENGTH_SHORT).show();
        signupnuser snu = new signupnuser(appSetting.userid,srdl,null, appSetting.usertypeid);
        final Gson gson = new Gson();
        final String jsonString = gson.toJson(snu);
        String url = appSetting.volleyUrl + "UserSearchlistl_serv?jssonString="+jsonString;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url

                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

//                System.out.println(response);

                if (response.equals("1")) {
                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }else if (response.equals("2")) {
                    Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
                    recyclerView.setAdapter(null);
                } else {
                    recyclerView.setAdapter(null);
//                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    try {

                        JSONArray servicelist = new JSONArray(response);
                        for (int i = 0; i < servicelist.length(); i++) {
                            JSONObject Objectmarks = servicelist.getJSONObject(i);
                            String idu = Objectmarks.getString("Usid");
                            String unm = Objectmarks.getString("Fname");
                            String satrt = Objectmarks.getString("startL");
                            String end = Objectmarks.getString("endL");
                            String shtcnt = Objectmarks.getString("Shtcontt");
                            String avau = Objectmarks.getString("Avau");
                            String ppic = Objectmarks.getString("Ppic");
                            String reqStat = Objectmarks.getString("reqStat");

                            searchListusrnew userL = new searchListusrnew(idu,unm,null,satrt,end,shtcnt,avau,ppic,reqStat);
                            ulist.add(userL);

                        }
                        adapter = new SearchServiceListAdapter(getContext(), ulist);
//                        recyclerView.setAdapter(null);
                        recyclerView.setAdapter(adapter);

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                System.out.println(error);
            }
        });
        Volley.newRequestQueue(getContext()).add(stringRequest);


    }

}
