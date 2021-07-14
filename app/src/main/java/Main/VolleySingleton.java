package Main;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton{

    private static VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private Context context;

    private VolleySingleton(Context context){
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getVolleySingleton(Context context){

        if(volleySingleton==null){
            volleySingleton = new VolleySingleton(context);
        }
        return volleySingleton;
    }

    public RequestQueue getRequestQueue(){

        if(requestQueue== null){
            requestQueue = Volley.newRequestQueue(context);
        }
        return  requestQueue;

    }

}


///*
//this how to apply in code
//Volley.newRequestQueue(getApplicationContext()).add(request);
//
////VolleySingleton.getVolleySingleton(getApplicationContext()).getRequestQueue().add(request);//meka thamai uda hadapu singleton code eka apply krna widiya