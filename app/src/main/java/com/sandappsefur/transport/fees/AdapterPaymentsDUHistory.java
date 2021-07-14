package com.sandappsefur.transport.fees;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sandappsefur.transport.R;

import java.util.List;

import Main.payment.PaymentS;

public class AdapterPaymentsDUHistory extends RecyclerView.Adapter<AdapterPaymentsDUHistory.PDUHistoryViewHolder>{

    Context context;
    List<PaymentS> usertableList;

    public AdapterPaymentsDUHistory(Context context, List<PaymentS> usertableList) {
        this.context = context;
        this.usertableList = usertableList;
    }

    @NonNull
    @Override
    public PDUHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listview_payments_duser_history, null); //metna customize karala layout eka ganna oni

        return new PDUHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PDUHistoryViewHolder pduHistoryViewHolder, int i) {

        final PaymentS ulst = usertableList.get(i);

        pduHistoryViewHolder.mnt.setText(ulst.getMonth());
        pduHistoryViewHolder.datev.setText(ulst.getUserid());
        pduHistoryViewHolder.mntlyfee.setText(ulst.getDefaultFee());
        pduHistoryViewHolder.pymnam.setText(ulst.getPayment());
        pduHistoryViewHolder.du.setText(ulst.getDue());

    }

    @Override
    public int getItemCount() {
        return usertableList.size();
    }

    class PDUHistoryViewHolder extends RecyclerView.ViewHolder{

        TextView datev,mntlyfee,pymnam,du,mnt;

        public PDUHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            datev = itemView.findViewById(R.id.datep);
            mnt = itemView.findViewById(R.id.mnth);
            mntlyfee = itemView.findViewById(R.id.mntlyfee);
            pymnam = itemView.findViewById(R.id.pymnam);
            du = itemView.findViewById(R.id.du);
        }
    }

}
