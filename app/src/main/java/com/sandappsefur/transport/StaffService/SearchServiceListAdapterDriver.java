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
import android.widget.Button;
import android.widget.ImageView;
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
import com.sandappsefur.transport.profile.profileDriverReq;
import com.squareup.picasso.Picasso;

import java.util.List;

import Main.GSONRequest;
import Main.VolleySingleton;
import Main.appSetting;
import Main.staffservice.reqSend;
import Main.staffservice.searchListusr;

public class SearchServiceListAdapterDriver extends RecyclerView.Adapter<SearchServiceListAdapterDriver.ServiceListViewHolderDriver> {

    Context context;
    List<searchListusr> usertableList;

    public SearchServiceListAdapterDriver(Context context, List<searchListusr> usertableList) {
        this.context = context;
        this.usertableList = usertableList;
    }

    @NonNull
    @Override
    public ServiceListViewHolderDriver onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.find_users_driver_listview,null); //metna customize karala layout eka ganna oni

        return new ServiceListViewHolderDriver(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ServiceListViewHolderDriver serviceListViewHolderDriver, int i) {


        final searchListusr ulst = usertableList.get(i);

        serviceListViewHolderDriver.name.setText(ulst.getFname());
        serviceListViewHolderDriver.startl.setText(ulst.getCpany());
        serviceListViewHolderDriver.endl.setText(ulst.getShtCount());
        if(ulst.getReqStat().equals("3")){
            serviceListViewHolderDriver.btnFindService.setText("Cancel Invite");
        }else  if(ulst.getReqStat().equals("1")){
            serviceListViewHolderDriver.btnFindService.setText("Accept");
        }else  if(ulst.getReqStat().equals("0")){
            serviceListViewHolderDriver.btnFindService.setText("Invite to Service");
        }

        //        ---------------------------image load----------------------

        String firstLetter = String.valueOf("A");
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(1);

        final TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color);

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://tranz-b8710.appspot.com/");
        StorageReference storageReference = storage.getReference("users");
        final StorageReference child = storageReference.child("profileimages/");

        if (ulst.getpPic() != null) {

            if (ulst.getpPic().equals("user" + ulst.getUserid())) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        child.child(ulst.getpPic()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                System.out.println(">>>");
                                // Picasso.with(context).load(uri).into(viewHolder.imageView);
                                Picasso.with(context).load(uri).placeholder(drawable).fit().centerCrop().into(serviceListViewHolderDriver.iv);

                            }
                        });
                    }
                });
                t.start();
            }
        }

//        ---------------------------image load----------------------


        serviceListViewHolderDriver.btnFindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"Request Service Button",Toast.LENGTH_SHORT).show();

                final ProgressDialog progressDialog = new ProgressDialog(context);
                try {
                    progressDialog.setMessage("Please Wailt...");
                    progressDialog.setCancelable(false);
                    progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    reqSend reqS = new reqSend(appSetting.userid, ulst.getUserid(), 1, 1, 0, appSetting.usertypeid,0);
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
                                serviceListViewHolderDriver.btnFindService.setText("Cancel Invite");
                                Toast.makeText(context, "Invite Send", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("2")) {
                                Toast.makeText(context, "More than one result found", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("3")) {
                                Toast.makeText(context, "Request Accept", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("4")) {
                                Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                            }else if (response.equals("5")) {
                                serviceListViewHolderDriver.btnFindService.setText("Invite Service");
                                Toast.makeText(context, "Invite Canceled", Toast.LENGTH_SHORT).show();
                            }else if (response.equals("6")) {
                                Toast.makeText(context, "You can't Send  Invite or Accept Request to Members.Because you don't have no space for new Members In Vehicle", Toast.LENGTH_SHORT).show();
                            }else if (response.equals("7")) {
                                Toast.makeText(context, "More Users", Toast.LENGTH_SHORT).show();
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
        serviceListViewHolderDriver.listitemcardserviceserch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileUserReq.class);
                intent.putExtra("Use_ID", ulst.getUserid());
                intent.putExtra("Use_stat", "1");
                context.startActivities(new Intent[]{intent});
            }
        });


    }

    @Override
    public int getItemCount() {
        return usertableList.size();
    }


    class ServiceListViewHolderDriver extends RecyclerView.ViewHolder{

        TextView name,startl,endl;
        ImageView iv;
        Button btnFindService;
        CardView listitemcardserviceserch;

        public ServiceListViewHolderDriver(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.fndUserdriverListvw);
            name = itemView.findViewById(R.id.usernmlv);
            startl = itemView.findViewById(R.id.startulv);
            endl = itemView.findViewById(R.id.endloculv);
            btnFindService = itemView.findViewById(R.id.inviteServiceid);
            listitemcardserviceserch = itemView.findViewById(R.id.listitemcardfindusers);
        }
    }

}
