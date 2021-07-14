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
import com.squareup.picasso.Picasso;

import java.util.List;

import Main.GSONRequest;
import Main.VolleySingleton;
import Main.appSetting;
import Main.staffservice.reqSend;
import Main.staffservice.searchListusr;

public class SearchServiceMemberlistAdpterDriver extends RecyclerView.Adapter<SearchServiceMemberlistAdpterDriver.ServiceMemListViewHolderDriver> {

    Context context;
    List<searchListusr> usertableList;
//    searchListusr ulst;

    public SearchServiceMemberlistAdpterDriver(Context context, List<searchListusr> usertableList) {
        this.context = context;
        this.usertableList = usertableList;
    }

    @NonNull
    @Override
    public ServiceMemListViewHolderDriver onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listview_all_members_driver, null); //metna customize karala layout eka ganna oni

        return new ServiceMemListViewHolderDriver(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ServiceMemListViewHolderDriver serviceMemListViewHolderDriver, final int i) {

        final searchListusr ulst = usertableList.get(i);

        serviceMemListViewHolderDriver.name.setText(ulst.getFname());
        serviceMemListViewHolderDriver.crdvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileUserReq.class);
                intent.putExtra("Use_ID", ulst.getUserid());
                intent.putExtra("Use_stat", "2");
                context.startActivities(new Intent[]{intent});
//                Toast.makeText(context, "ID :" + ulst.getUserid(), Toast.LENGTH_SHORT).show();
            }
        });

//        ---------------------------image load----------------------

        String firstLetter = String.valueOf(ulst.getFname().charAt(0));
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(i);

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
                                Picasso.with(context).load(uri).placeholder(drawable).fit().centerCrop().into(serviceMemListViewHolderDriver.dp);

                            }
                        });
                    }
                });
                t.start();
            }else {
                String firstLetter1 = String.valueOf(ulst.getFname().charAt(0));
                ColorGenerator generator1 = ColorGenerator.MATERIAL;
                int color1 = generator1.getColor(i);

                TextDrawable drawable1 = TextDrawable.builder().buildRound(firstLetter1, color1);

                serviceMemListViewHolderDriver.dp.setImageDrawable(drawable1);
            }
        }

//        ---------------------------image load----------------------

        serviceMemListViewHolderDriver.cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = null;
                try {
                    Intent  i = new Intent(Intent.ACTION_DIAL);
                    i.setData(Uri.parse("tel:"+ulst.getCpany()));
                    context.startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        serviceMemListViewHolderDriver.msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent  i = new Intent(Intent.ACTION_SENDTO);
                    i.setType("text/plain");
                    i.setData(Uri.parse("smsto:"+ulst.getCpany()));
                    i.putExtra("sms_body","");
                    context.startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        serviceMemListViewHolderDriver.imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "ID :" + ulst.getUserid(), Toast.LENGTH_SHORT).show();

                final ProgressDialog progressDialog = new ProgressDialog(context);
                try {
                    progressDialog.setMessage("Please Wailt...");
                    progressDialog.setCancelable(false);
                    progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    reqSend reqS = new reqSend(appSetting.userid, ulst.getUserid(), 1, 1, 0, appSetting.usertypeid, 0);
                    final Gson gson = new Gson();
                    final String jsonString = gson.toJson(reqS);

                    String url = appSetting.volleyUrl + "removeMember_serv";

                    Response.Listener rl = new Response.Listener<String>() {
                        @SuppressLint("WrongViewCast")
                        @Override
                        public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
                            progressDialog.dismiss();
                            if (response.equals("1")) {
                                usertableList.remove(i);
                                notifyDataSetChanged();
//                        adapter.remove(adapter.getItem(position));
                                Toast.makeText(context, "Member Removed Successful", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("2")) {
                                Toast.makeText(context, "More than one result found", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("3")) {
                                Toast.makeText(context, "Request Accept", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("4")) {
                                Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (response.equals("5")) {
//                        serviceListViewHolderDriver.btnFindService.setText("Invite Service");
                                Toast.makeText(context, "Error User Count", Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public int getItemCount() {
        return usertableList.size();
    }

    class ServiceMemListViewHolderDriver extends RecyclerView.ViewHolder {

        TextView name;
        CardView crdvw;
        ImageView dp;
        Button imv,cl,msg;


        public ServiceMemListViewHolderDriver(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.namememlst);
            imv = itemView.findViewById(R.id.optionDrivememlstr);
            cl = itemView.findViewById(R.id.btncallstfServiceDriverMem);
            msg = itemView.findViewById(R.id.btnmsgstfServiceDriverMem);
            dp = itemView.findViewById(R.id.dpmemlst);
            crdvw = itemView.findViewById(R.id.crdvwmemlstDriver);
        }
    }

//    public void RemoveMember(){
//
//
//
//    }


}
