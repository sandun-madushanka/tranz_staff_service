package Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sandappsefur.transport.R;
import com.sandappsefur.transport.profile.profile;

import Main.attendence.AttendenceU;

public class Task1  extends AsyncTask {

    int i;
    Activity activity;

    int x = 0;
    int y = 1;

//    Dialog dialog;

    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    public Task1(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
//        TextView tv1 = activity.findViewById(R.id.textView);
//        tv1.setText("Start");

        alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialog = alertDialogBuilder.create();

        alertDialog.setTitle("Mark Attendance");
        alertDialog.setIcon(R.drawable.ic_directions_vehicle);
        alertDialog.setMessage("Are you sure to mark Attend?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(activity, "Cancel", Toast.LENGTH_SHORT).show();
//                        activity.finish();
                        cancel(true);
                    }
                }
        );
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(activity, "OK", Toast.LENGTH_SHORT).show();
//                        activity.finish();
                        cancel(true);
                        MarkArrendenceUserM();

                    }
                }
        );

        alertDialog.show();

//        dialog = ProgressDialog.show(activity, "Mark Attendance", "Please wait......");
//
    }

    @Override
    protected void onPostExecute(Object o) {
//        TextView tv2 = activity.findViewById(R.id.textView);
//        tv2.setText("End");
        Toast.makeText(activity, "Cancel", Toast.LENGTH_SHORT).show();
//                        activity.finish();
        MarkArrendenceUserM();
        alertDialog.dismiss();

//        dialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
//        TextView tv2 = activity.findViewById(R.id.textView2);
//        tv2.setText(String.valueOf(i));

        alertDialog.setTitle("Mark Attendance  ("+i+")");

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        for (i = 5; i >= 0; i--) {
            publishProgress();
//            try {
//                Thread.sleep(1000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt(); // Here!
                throw new RuntimeException(ex);
            }

        }
        return null;
    }

    public void MarkArrendenceUserM(){

        String dhft = ((Spinner) activity.findViewById(R.id.attendshif)).getSelectedItem().toString();
        String attstat = ((Spinner) activity.findViewById(R.id.attendstat)).getSelectedItem().toString();

        if(dhft.equals("Morning")){
            x= 0;
        }else if(dhft.equals("Afternoon")){
            x = 1;
        }

        if(attstat.equals("Not Attend")){
            y= 0;
        }else if(attstat.equals("Attend")){
            y = 1;
        }

        try {

            final ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Please Wailt...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.show();

            AttendenceU u2 = new AttendenceU(appSetting.userid,x,y,1,null,null,null,appSetting.usertypeid);
            final Gson gson = new Gson();
            final String jsonString = gson.toJson(u2);

            String url = appSetting.volleyUrl + "attendMarkU_serv";

            Response.Listener rl = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), "Responce eka awaaaa", Toast.LENGTH_SHORT).show();
                    System.out.println(response);
                    if (response.equals("1")) {
                        progressDialog.dismiss();
                        Toast.makeText(activity, "Attendance Mark Successful", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("2")) {
                        progressDialog.dismiss();
                        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("3")) {
                        progressDialog.dismiss();
                        Toast.makeText(activity, "Duplicate Error", Toast.LENGTH_SHORT).show();
                    }else if (response.equals("4")) {
                        progressDialog.dismiss();
                        Toast.makeText(activity, "Update Successful", Toast.LENGTH_SHORT).show();
                    }else if (response.equals("5")) {
                        progressDialog.dismiss();
                        Toast.makeText(activity, "You ae not in a StaffService. So you can't make attendance.", Toast.LENGTH_SHORT).show();
                    }


                }
            };

            Response.ErrorListener re = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(activity, "Fail to Connect With Server", Toast.LENGTH_SHORT).show();
                    System.out.println("---------------------------------------" + error);
                }
            };


            GSONRequest request = new GSONRequest(1, url, rl, re);
            request.json = jsonString;
            VolleySingleton.getVolleySingleton(activity).getRequestQueue().add(request);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
