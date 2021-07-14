package com.sandappsefur.transport.StaffService;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.sandappsefur.transport.R;
import com.sandappsefur.transport.profile.profileDriver;
import com.sandappsefur.transport.profile.profileDriverReq;
import com.squareup.picasso.Picasso;

import java.util.List;

import Main.GSONRequest;
import Main.VolleySingleton;
import Main.appSetting;
import Main.profile.usertable;
import Main.staffservice.profilereqLoad;
import Main.staffservice.reqSend;
import Main.staffservice.searchListusr;
import Main.staffservice.searchListusrnew;

public class SearchServiceListAdapter extends RecyclerView.Adapter<SearchServiceListAdapter.ServiceListViewHolder> {
    Context context;
    List<searchListusrnew> usertableList;

    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference child;

    public SearchServiceListAdapter(Context context, List<searchListusrnew> usertableList) {
        this.context = context;
        this.usertableList = usertableList;
    }

    @NonNull
    @Override
    public ServiceListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_service_listview, null); //metna customize karala layout eka ganna oni

        return new ServiceListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ServiceListViewHolder serviceListViewHolder, int i) {
        final searchListusrnew ulst = usertableList.get(i);

        int tot = (Integer.parseInt(ulst.getShtCount()) - Integer.parseInt(ulst.getAvaU()));

        serviceListViewHolder.start.setText(ulst.getStartl());
        serviceListViewHolder.end.setText(ulst.getEndl());
        serviceListViewHolder.name.setText(ulst.getFname());
        serviceListViewHolder.avaSeat.setText(String.valueOf(tot));
        if (ulst.getReqStat().equals("1")) {
            serviceListViewHolder.btnFindService.setText("Cancel Request");
        } else if (ulst.getReqStat().equals("0")) {
            serviceListViewHolder.btnFindService.setText("Request Service");
        } else if (ulst.getReqStat().equals("3")) {
            serviceListViewHolder.btnFindService.setText("Accept Invite");
        }

        //        ---------------------------image load----------------------

        String firstLetter = String.valueOf("A");
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(1);

        final TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color);

        storage = FirebaseStorage.getInstance("gs://tranz-b8710.appspot.com/");
        storageReference = storage.getReference("users/");
        child = storageReference.child("profileimages/");

//        System.out.println(child.child(ulst.getpPic()).getDownloadUrl()+"----------------------------------");

        if (ulst.getpPic() != null) {

            if (ulst.getpPic().equals("user" + ulst.getUserid())) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        child.child(ulst.getpPic()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
//                                System.out.println(">>>>>>>>>>>>>>>>>>>>-------------------");
//                                 Picasso.with(context).load(uri).into(serviceListViewHolder.iv);
                                Picasso.with(context).load(uri).placeholder(drawable).fit().centerCrop().into(serviceListViewHolder.iv);
//                                Glide.with(context).load(uri).apply(RequestOptions.circleCropTransform()).into(serviceListViewHolder.iv);

                            }
                        });
                    }
                });
                t.start();
            } else {
                String firstLetter1 = String.valueOf(ulst.getFname().charAt(0));
                ColorGenerator generator1 = ColorGenerator.MATERIAL;
                int color1 = generator1.getColor(i);

                TextDrawable drawable1 = TextDrawable.builder().buildRound(firstLetter1, color1);

                serviceListViewHolder.iv.setImageDrawable(drawable1);
            }
        }
//        -----------------------------

//        String firstLetter = String.valueOf(ulst.getFname().charAt(0));
//        ColorGenerator generator = ColorGenerator.MATERIAL;
//        int color = generator.getColor(i);
//
//        TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color);
//
//        serviceListViewHolder.iv.setImageDrawable(drawable);

//        ---------------------------image load----------------------


        serviceListViewHolder.btnFindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"Request Service Button",Toast.LENGTH_SHORT).show();

                final ProgressDialog progressDialog = new ProgressDialog(context);
                try {
                    progressDialog.setMessage("Please Wailt...");
                    progressDialog.setCancelable(false);
                    progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    reqSend reqS = new reqSend(appSetting.userid, ulst.getUserid(), 1, 1, 0, appSetting.usertypeid, 0);
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
                                serviceListViewHolder.btnFindService.setText("Cancel Request");
                                Toast.makeText(context, "Request Send", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("2")) {
                                Toast.makeText(context, "More than one result found", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("3")) {
                                Toast.makeText(context, "Invite Accept", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("4")) {
                                Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("5")) {
                                serviceListViewHolder.btnFindService.setText("Request Service");
                                Toast.makeText(context, "Request Canceled", Toast.LENGTH_SHORT).show();
                            }else if (response.equals("10")) {
                                Toast.makeText(context, "You can't Send  Request or Accept Invite. Because you already in a Staff Service", Toast.LENGTH_SHORT).show();
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
                    VolleySingleton.getVolleySingleton(context).getRequestQueue().add(request);


                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }


            }
        });
        serviceListViewHolder.listitemcardserviceserch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, profileDriverReq.class);
                intent.putExtra("Driv_ID", ulst.getUserid());
                context.startActivities(new Intent[]{intent});
            }
        });

    }

    @Override
    public int getItemCount() {
        return usertableList.size();
    }


    class ServiceListViewHolder extends RecyclerView.ViewHolder {

        TextView usid, start,end, name, avaSeat;
        Button btnFindService;
        ImageView iv;
        CardView listitemcardserviceserch;

        public ServiceListViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.SearchServicelstDp);
            name = itemView.findViewById(R.id.unmssl);
            start = itemView.findViewById(R.id.startdlv);
            end = itemView.findViewById(R.id.endlocdlv);
            avaSeat = itemView.findViewById(R.id.seatcnt);
            btnFindService = itemView.findViewById(R.id.reqServiceid);
            listitemcardserviceserch = itemView.findViewById(R.id.listitemcardserviceserch);


        }
    }


}
