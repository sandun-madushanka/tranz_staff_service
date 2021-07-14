package Main;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ContentValues;
import android.util.Log;
import android.widget.Toast;

public class NotificationJobScheduler extends JobService {

    private static final String TAG = "Example JOB Service";
    private boolean jobCanceled = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");
        doBackgroundWork(params);

        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
//        final int[] count = {5};
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    Log.d(TAG, "run jb srv: " );
//                    Toast.makeText(getApplicationContext(), --count[0] + "",Toast.LENGTH_SHORT).show();
                    if (jobCanceled){
                        return;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


//                Log.d(TAG, "Finished ");
//                jobFinished(params,false);
            }
        }).start();


    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Canceled before completion");
        jobCanceled = true;
        return true;
    }
}
