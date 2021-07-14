package Main;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.sandappsefur.transport.R;

public class NotificationHelper extends ContextWrapper {

    public static final String channel1ID = "channel1ID";
    public static final String channel1Name = "channel1Name";
    public static final String channel2ID = "channel2ID";
    public static final String channel2Name = "channel2Name";
    public static final String channel3ID = "channel3ID";
    public static final String channel3Name = "channel3Name";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CreateNotificationChanels();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void CreateNotificationChanels() {

        NotificationChannel channel1 = new NotificationChannel(
                channel1ID,
                channel1Name,
                NotificationManager.IMPORTANCE_HIGH
        );
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.colorPrimary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//        channel1.setDescription("This is Channel 1");


        getManager().createNotificationChannel(channel1);

        NotificationChannel channel2 = new NotificationChannel(
                channel2ID,
                channel2Name,
                NotificationManager.IMPORTANCE_LOW
        );

        channel2.enableLights(true);
        channel2.enableVibration(true);
        channel2.setLightColor(R.color.colorPrimary);
        channel2.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationChannel channel3 = new NotificationChannel(
                channel3ID,
                channel3Name,
                NotificationManager.IMPORTANCE_LOW
        );

        channel3.enableLights(true);
        channel3.enableVibration(true);
        channel3.setLightColor(R.color.colorPrimary);
        channel3.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        //        channel2.setDescription("This is Channel 2");


        getManager().createNotificationChannel(channel3);

//        manager = getSystemService(NotificationManager.class);
//        manager.createNotificationChannel(channel1);
//        manager.createNotificationChannel(channel2);


    }


    public NotificationManager getManager () {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return  mManager;
    }

    public NotificationCompat.Builder getChannel1Notification(String title, String message){
        return  new NotificationCompat.Builder(getApplicationContext(),channel1ID)
                .setContentTitle(title)
                .setContentInfo(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(message))
                .setSmallIcon(R.drawable.ic_directions_vehicle);
    }
    public NotificationCompat.Builder getChanne21Notification(String title, String message){
        return  new NotificationCompat.Builder(getApplicationContext(),channel2ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_directions_vehicle);
    }
    public NotificationCompat.Builder getChanne31Notification(String title, String message,PendingIntent intent){
        return  new NotificationCompat.Builder(getApplicationContext(),channel3ID)
                .setContentTitle(title)
                .setContentInfo(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentIntent(intent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.drawable.ic_directions_vehicle);
    }

}
