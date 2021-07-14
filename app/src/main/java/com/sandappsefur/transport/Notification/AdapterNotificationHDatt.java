package com.sandappsefur.transport.Notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sandappsefur.transport.Attendence.AdapterAttendanceDriverMemlst;
import com.sandappsefur.transport.R;

import java.util.List;

import Main.attendence.AttendenceU;
import Main.signup.signupnuser;

public class AdapterNotificationHDatt extends RecyclerView.Adapter<AdapterNotificationHDatt.NotiAttlstViewHolder> {

    Context context;
    List<signupnuser> usertableList;

    public AdapterNotificationHDatt(Context context, List<signupnuser> usertableList) {
        this.context = context;
        this.usertableList = usertableList;
    }

    @NonNull
    @Override
    public NotiAttlstViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listview_notification_dhome, null); //metna customize karala layout eka ganna oni

        return new NotiAttlstViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotiAttlstViewHolder notiAttlstViewHolder, int i) {

        final signupnuser ulst = usertableList.get(i);

        notiAttlstViewHolder.dt.setText(ulst.getName());
        notiAttlstViewHolder.morat.setText(ulst.getMobile());
        notiAttlstViewHolder.aftat.setText(ulst.getEmail());

    }

    @Override
    public int getItemCount() {
        return usertableList.size();
    }

    class NotiAttlstViewHolder extends RecyclerView.ViewHolder{

        TextView dt, morat, aftat;

        public NotiAttlstViewHolder(@NonNull View itemView) {
            super(itemView);

            dt = itemView.findViewById(R.id.datenothD);
            morat = itemView.findViewById(R.id.mngcntnothD);
            aftat = itemView.findViewById(R.id.aftcntnothD);

        }
    }

}
