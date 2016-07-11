package com.askerlap.emadahmed.wakemeup;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;

import android.content.Intent;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;

public class myService extends Service {
    private long hour,minut;
    String system_time;
    boolean time_to_call = true ;
    NotificationCompat.Builder nBuilder;

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

    @Override
    public void onCreate() {
        super.onCreate();
        CreateNotification();

    }
    public void call()
    {
        Calendar c=Calendar.getInstance();
        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
        ArrayList<String> category_times=new ArrayList<String>();
        helper.getReadableDatabase();

        category_times= helper.getAllCategoryTime();
        hour=c.getTime().getHours();
        minut=c.getTime().getMinutes();

        if (minut<10)
            system_time=hour+":0"+minut;
        else if(hour<10)
            system_time="0"+hour+":"+minut;
        else
            system_time=hour+":"+minut;


        for (int i= 0 ;i<category_times.size();i++)
        {
            if(system_time.equals(category_times.get(i))) {


                Intent ii = new Intent(this, CallingNumbers.class);
                ii.addFlags(Intent.FLAG_FROM_BACKGROUND);
                ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ii.putExtra("key_category",helper.caregoryName(system_time));
                if (time_to_call == true) {
                    startActivity(ii);

                    Thread thr = new Thread() {
                        public void run() {
                            try {
                                sleep(60000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    };

                    thr.start();
                    time_to_call = false;
                }

              }


        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //makes the app run in the background
          call();
        return START_STICKY;
    }

    private void CreateNotification() {
        // Create Intent for notification
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Defining notification
        String Title=getResources().getString(R.string.service_notify);
        nBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.app_icon).setContentTitle(Title).setContentIntent(pi);

        Notification note= nBuilder.build();
        note.flags= Notification.FLAG_ONGOING_EVENT;
        int notificationId = 1;
        NotificationManager notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyMgr.notify(notificationId, note);
    }

    private void azkarNotification() {
        String Title = getResources().getString(R.string.service_notify);

        nBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.azkar_ic).setContentTitle(Title);


        // Allows notification to be cancelled when user clicks it
        nBuilder.setAutoCancel(true);

        // Issuing notification
        Notification note = nBuilder.build();
//        note.flags= Notification.FLAG_ONGOING_EVENT;
        int notificationId = 0;
        NotificationManager notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyMgr.notify(notificationId, note);
    }

}
