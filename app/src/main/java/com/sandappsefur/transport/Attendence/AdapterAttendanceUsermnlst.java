package com.sandappsefur.transport.Attendence;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sandappsefur.transport.R;

import java.util.List;

import Main.signup.signupnuser;

public class AdapterAttendanceUsermnlst extends RecyclerView.Adapter<AdapterAttendanceUsermnlst.AtndUMlstViewHolder> {

    Context context;
    List<signupnuser> usertableList;

    public AdapterAttendanceUsermnlst(Context context, List<signupnuser> usertableList) {
        this.context = context;
        this.usertableList = usertableList;
    }

    @NonNull
    @Override
    public AtndUMlstViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listview_attendence_userview,null);

        return new AtndUMlstViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AtndUMlstViewHolder atndUMlstViewHolder, int i) {

        signupnuser ulst = usertableList.get(i);

        atndUMlstViewHolder.datev.setText(ulst.getName());
        if(ulst.getMobile().equals("0")){
            atndUMlstViewHolder.mngtxt.setText("Morning");
        }else if(ulst.getMobile().equals("1")){
            atndUMlstViewHolder.mngtxt.setText("Afternoon");
        }

        if(ulst.getEmail().equals("0")){
            atndUMlstViewHolder.afttxt.setText("Not Attend");
            atndUMlstViewHolder.afttxt.setTextColor(context.getResources().getColor(R.color.red));
        }else if(ulst.getEmail().equals("1")){
            atndUMlstViewHolder.afttxt.setText("Attend");
            atndUMlstViewHolder.afttxt.setTextColor(context.getResources().getColor(R.color.green));
        }


    }

    @Override
    public int getItemCount() {
        return usertableList.size();
    }


    class AtndUMlstViewHolder extends RecyclerView.ViewHolder{

        TextView datev,mngtxt,afttxt;

        public AtndUMlstViewHolder(@NonNull View itemView) {
            super(itemView);
            datev = itemView.findViewById(R.id.attmdate);
            mngtxt = itemView.findViewById(R.id.atttimetyp);
            afttxt = itemView.findViewById(R.id.attstat);

        }
    }

}
