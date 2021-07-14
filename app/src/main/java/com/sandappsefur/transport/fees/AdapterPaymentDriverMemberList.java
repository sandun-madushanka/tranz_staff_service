package com.sandappsefur.transport.fees;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sandappsefur.transport.R;
import com.sandappsefur.transport.StaffService.SearchServiceMemberlistAdpterDriver;
import com.squareup.picasso.Picasso;

import java.util.List;

import Main.payment.PaymentS;
import Main.staffservice.searchListusr;

public class AdapterPaymentDriverMemberList extends RecyclerView.Adapter<AdapterPaymentDriverMemberList.PDMListViewHolder> {

    Context context;
    List<PaymentS> usertableList;

    public AdapterPaymentDriverMemberList(Context context, List<PaymentS> usertableList) {
        this.context = context;
        this.usertableList = usertableList;
    }

    @NonNull
    @Override
    public PDMListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listview_payments_driver, null); //metna customize karala layout eka ganna oni

        return new PDMListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PDMListViewHolder pdmListViewHolder, int i) {

        final PaymentS ulst = usertableList.get(i);

        pdmListViewHolder.name.setText(ulst.getName());
        pdmListViewHolder.month.setText(ulst.getMonth());
        if(ulst.getStatusPayment().equals("0")){
            pdmListViewHolder.status.setText("Not Paid");
        }else if(ulst.getStatusPayment().equals("1")){
            pdmListViewHolder.status.setText("Paid");
        }
        pdmListViewHolder.fee.setText(ulst.getDefaultFee());
        pdmListViewHolder.crdvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PaymentDriverUserDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Use_ID", ulst.getUserid());
                intent.putExtra("Use_stat", "2");
                context.startActivities(new Intent[]{intent});
//                Toast.makeText(context, "ID :" + ulst.getFname(), Toast.LENGTH_SHORT).show();
            }
        });
        pdmListViewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PaymentDUserHistory.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Use_ID", ulst.getUserid());
                intent.putExtra("Use_stat", "2");
                context.startActivities(new Intent[]{intent});
//                Toast.makeText(context, "ID :" + ulst.getUserid(), Toast.LENGTH_SHORT).show();
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

        if (ulst.getImgUrl() != null) {

            if (ulst.getImgUrl().equals("user" + ulst.getUserid())) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        child.child(ulst.getImgUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
//                                System.out.println(">>>");
                                // Picasso.with(context).load(uri).into(viewHolder.imageView);
                                Picasso.with(context).load(uri).placeholder(drawable).fit().centerCrop().into(pdmListViewHolder.dp);

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

    class PDMListViewHolder extends RecyclerView.ViewHolder{

        TextView name,status,month,fee;
        CardView crdvw;
        ImageView dp;
        Button btn;

        public PDMListViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.namepymtlstdriver);
            status = itemView.findViewById(R.id.statuspymntdMem);
            month = itemView.findViewById(R.id.Monthpymtlstdrv);
            fee = itemView.findViewById(R.id.mnthdfltFee);
            btn = itemView.findViewById(R.id.vwhstrymembtn);
            dp = itemView.findViewById(R.id.dpmemlstpymntdrv);
            crdvw = itemView.findViewById(R.id.crdvwpymntmemlst);
        }
    }
}
