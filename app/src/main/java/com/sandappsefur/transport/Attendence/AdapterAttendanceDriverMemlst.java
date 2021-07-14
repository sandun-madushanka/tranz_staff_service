package com.sandappsefur.transport.Attendence;

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

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sandappsefur.transport.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Main.attendence.AttendenceU;

public class AdapterAttendanceDriverMemlst extends RecyclerView.Adapter<AdapterAttendanceDriverMemlst.AttMemListViewHolder> {
    Context context;
    List<AttendenceU> usertableList;

    public AdapterAttendanceDriverMemlst(Context context, List<AttendenceU> usertableList) {
        this.context = context;
        this.usertableList = usertableList;
    }

    @NonNull
    @Override
    public AttMemListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listview_attendence_drivervw, null); //metna customize karala layout eka ganna oni

        return new AttMemListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AttMemListViewHolder attMemListViewHolder, int i) {
        final AttendenceU ulst = usertableList.get(i);

        attMemListViewHolder.name.setText(ulst.getDateatt());

        if(ulst.getMsgnotifi().equals("0")){
            attMemListViewHolder.morat.setText("Not Attend");
            attMemListViewHolder.morat.setTextColor(context.getResources().getColor(R.color.red));
        }else if(ulst.getMsgnotifi().equals("1")){
            attMemListViewHolder.morat.setText("Attend");
            attMemListViewHolder.morat.setTextColor(context.getResources().getColor(R.color.green));
        }else if(ulst.getMsgnotifi().equals("2")){
            attMemListViewHolder.morat.setText("Not Marked Yet");
            attMemListViewHolder.morat.setTextColor(context.getResources().getColor(R.color.red));
        }

        if(ulst.getExtrS().equals("0")){
            attMemListViewHolder.aftat.setText("Not Attend");
            attMemListViewHolder.aftat.setTextColor(context.getResources().getColor(R.color.red));
        }else if(ulst.getExtrS().equals("1")){
            attMemListViewHolder.aftat.setText("Attend");
            attMemListViewHolder.aftat.setTextColor(context.getResources().getColor(R.color.green));
        }else if(ulst.getExtrS().equals("2")){
            attMemListViewHolder.aftat.setText("Not Marked Yet");
            attMemListViewHolder.aftat.setTextColor(context.getResources().getColor(R.color.red));
        }
    final String mobn= String.valueOf(ulst.getTimetype());
        attMemListViewHolder.clat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = null;
                try {
                    Intent i = new Intent(Intent.ACTION_DIAL);
                    i.setData(Uri.parse("tel:"+mobn));
                    context.startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        attMemListViewHolder.msgat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent  i = new Intent(Intent.ACTION_SENDTO);
                    i.setType("text/plain");
                    i.setData(Uri.parse("smsto:"+mobn));
                    i.putExtra("sms_body","");
                    context.startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


//        ---------------------------image load----------------------

        String firstLetter = String.valueOf("A");
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(1);

        final TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color);

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://tranz-b8710.appspot.com/");
        StorageReference storageReference = storage.getReference("users");
        final StorageReference child = storageReference.child("profileimages/");

        if (ulst.getMonthatt() != null) {

            if (ulst.getMonthatt().equals("user" + ulst.getUserid())) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        child.child(ulst.getMonthatt()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
//                                System.out.println(">>>");
                                // Picasso.with(context).load(uri).into(viewHolder.imageView);
                                Picasso.with(context).load(uri).placeholder(drawable).fit().centerCrop().into(attMemListViewHolder.dp);

                            }
                        });
                    }
                });
                t.start();
            }
        }

//        ---------------------------image load----------------------


    }

    @Override
    public int getItemCount() {
        return usertableList.size();
    }

    class AttMemListViewHolder extends RecyclerView.ViewHolder {

        TextView name, morat, aftat;
        ImageView dp;
        Button clat, msgat;

        public AttMemListViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameattlstdriver);
            morat = itemView.findViewById(R.id.mngattdrv);
            aftat = itemView.findViewById(R.id.aftattdrv);
            dp = itemView.findViewById(R.id.dpmemlstattdrv);
            clat = itemView.findViewById(R.id.btncallattDriverMem);
            msgat = itemView.findViewById(R.id.btnmsgattDriverMem);

        }
    }

}
