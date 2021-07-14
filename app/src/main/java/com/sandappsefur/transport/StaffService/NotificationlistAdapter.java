package com.sandappsefur.transport.StaffService;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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

public class NotificationlistAdapter extends RecyclerView.Adapter<NotificationlistAdapter.NLAViewHolder> {

    Context context;
    List<searchListusr> usertableList;

    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference child;

    public NotificationlistAdapter(Context context, List<searchListusr> usertableList) {
        this.context = context;
        this.usertableList = usertableList;
    }

    @NonNull
    @Override
    public NLAViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listview_notification, null); //metna customize karala layout eka ganna oni


        return new NLAViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NLAViewHolder nlaViewHolder, final int i) {

        final searchListusr ulst = usertableList.get(i);

        nlaViewHolder.name.setText(ulst.getFname());
        if (ulst.getCpany().equals("0")) {
            nlaViewHolder.stat.setVisibility(View.GONE);
        } else if (ulst.getCpany().equals("2")) {
            nlaViewHolder.stat.setVisibility(View.GONE);
        } else if (ulst.getCpany().equals("4")) {
            nlaViewHolder.stat.setVisibility(View.GONE);
        }


        if (ulst.getReqStat().equals("2") || ulst.getReqStat().equals("4")) {
//            nlaViewHolder.btnFindService.setVisibility(View.GONE);
//            nlaViewHolder.dltreq.setVisibility(View.GONE);
        }

        //        ---------------------------image load----------------------

        String firstLetter = String.valueOf(ulst.getFname().charAt(0));
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(i);

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
                                Picasso.with(context).load(uri).placeholder(drawable).fit().centerCrop().into(nlaViewHolder.iv);
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

                nlaViewHolder.iv.setImageDrawable(drawable1);
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


        nlaViewHolder.btnFindService.setOnClickListener(new View.OnClickListener() {
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
//                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
                            progressDialog.dismiss();
                            if (appSetting.usertypeid.equals("2")) {
                                if (response.equals("1")) {
                                    nlaViewHolder.btnFindService.setText("Cancel Invite");
                                    Toast.makeText(context, "Invite Send", Toast.LENGTH_SHORT).show();
                                } else if (response.equals("2")) {
                                    Toast.makeText(context, "More than one result found", Toast.LENGTH_SHORT).show();
                                } else if (response.equals("3")) {
                                    nlaViewHolder.btnFindService.setVisibility(View.GONE);
                                    nlaViewHolder.dltreq.setVisibility(View.GONE);
                                    Toast.makeText(context, "Request Accept", Toast.LENGTH_SHORT).show();
                                } else if (response.equals("4")) {
                                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                                } else if (response.equals("5")) {
                                    nlaViewHolder.btnFindService.setText("Invite Service");
                                    Toast.makeText(context, "Invite Canceled", Toast.LENGTH_SHORT).show();
                                } else if (response.equals("6")) {
                                    Toast.makeText(context, "You can't Send  Invite or Accept Request to Members.Because you don't have no space for new Members In Vehicle", Toast.LENGTH_SHORT).show();
                                } else if (response.equals("7")) {
                                    Toast.makeText(context, "More Users", Toast.LENGTH_SHORT).show();
                                } else if (response.equals("10")) {
                                    Toast.makeText(context, "You can't Send  Request or Accept Invite. Because this user already in a Staff Service", Toast.LENGTH_SHORT).show();
                                }
                            } else if (appSetting.usertypeid.equals("3")) {
                                if (response.equals("1")) {
                                    nlaViewHolder.btnFindService.setText("Cancel Request");
                                    Toast.makeText(context, "Request Send", Toast.LENGTH_SHORT).show();
                                } else if (response.equals("2")) {
                                    Toast.makeText(context, "More than one result found", Toast.LENGTH_SHORT).show();
                                } else if (response.equals("3")) {
                                    nlaViewHolder.btnFindService.setVisibility(View.GONE);
                                    nlaViewHolder.dltreq.setVisibility(View.GONE);
                                    Toast.makeText(context, "Invite Accept", Toast.LENGTH_SHORT).show();
                                } else if (response.equals("4")) {
                                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                                } else if (response.equals("5")) {
                                    nlaViewHolder.btnFindService.setText("Request Service");
                                    Toast.makeText(context, "Request Canceled", Toast.LENGTH_SHORT).show();
                                } else if (response.equals("10")) {
                                    Toast.makeText(context, "You can't Send  Request or Accept Invite. Because you already in a Staff Service", Toast.LENGTH_SHORT).show();
                                }
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
        nlaViewHolder.dltreq.setOnClickListener(new View.OnClickListener() {
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

                    String url = appSetting.volleyUrl + "serviceReqdeletenot_serv";

                    Response.Listener rl = new Response.Listener<String>() {
                        @SuppressLint("WrongViewCast")
                        @Override
                        public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
//                    System.out.println(response);
                            progressDialog.dismiss();
                            if (appSetting.usertypeid.equals("2")) {
                                if (response.equals("1")) {
                                    nlaViewHolder.btnFindService.setText("Cancel Invite");
                                    Toast.makeText(context, "Invite Send", Toast.LENGTH_SHORT).show();
                                } else if (response.equals("2")) {
                                    Toast.makeText(context, "More than one result found", Toast.LENGTH_SHORT).show();
                                } else if (response.equals("3")) {
                                    usertableList.remove(i);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Delete Successful", Toast.LENGTH_SHORT).show();
                                }
                            } else if (appSetting.usertypeid.equals("3")) {

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

        nlaViewHolder.listitemcardnot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (appSetting.usertypeid.equals("2")) {
                    Intent intent = new Intent(context, ProfileUserReq.class);
                    intent.putExtra("Use_ID", ulst.getUserid());
                    intent.putExtra("Use_stat", "1");
                    context.startActivities(new Intent[]{intent});
                } else if (appSetting.usertypeid.equals("3")) {
                    Intent intent = new Intent(context, profileDriverReq.class);
                    intent.putExtra("Driv_ID", ulst.getUserid());
                    context.startActivities(new Intent[]{intent});
                }


            }
        });


        if (ulst.getCpany().equals("2")) {
            nlaViewHolder.con.setVisibility(View.GONE);
//            nlaViewHolder.dltreq.setVisibility(View.GONE);
        }else if (ulst.getCpany().equals("4")) {
            nlaViewHolder.con.setVisibility(View.GONE);
//            nlaViewHolder.dltreq.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return usertableList.size();
    }

    class NLAViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        Button btnFindService, dltreq;
        ImageView iv, stat;
        CardView listitemcardnot;
        ConstraintLayout con;

        public NLAViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.dpnofiimg);
            stat = itemView.findViewById(R.id.statnotifi);
            name = itemView.findViewById(R.id.namenoti);
            btnFindService = itemView.findViewById(R.id.reqServiceidnoti);
            dltreq = itemView.findViewById(R.id.deletenoti);
            listitemcardnot = itemView.findViewById(R.id.listitemcardnotification);
            con = itemView.findViewById(R.id.consbtnnoti);

        }
    }
}
