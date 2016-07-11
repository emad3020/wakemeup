package com.askerlap.emadahmed.wakemeup;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

/**
 * Created by root on 19/04/16.
 */
public class azkarService extends Service {
    NotificationCompat.Builder nBuilder;
    int path[]={R.raw.morning_azkar_clip,R.raw.bzker_allah};
    int i=0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // MediaPlayer media=MediaPlayer.create(getApplicationContext(),R.raw.morning_azkar_clip);
       // media.start();
        String Title=getResources().getString(R.string.azkar_title_notification);
        String messag=getResources().getString(R.string.akzar_morning);
        nBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.mipmap.azkar_ic).setContentTitle(Title).setContentText(messag);



        // Allows notification to be cancelled when user clicks it
        nBuilder.setAutoCancel(true);

        // Issuing notification
        Notification note= nBuilder.build();
//        note.flags= Notification.FLAG_ONGOING_EVENT;
        int notificationId = 0;
        NotificationManager notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyMgr.notify(notificationId, note);
        MediaPlayer media=MediaPlayer.create(getApplicationContext(),path[i]);
        media.start();
        media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                if (i!=path.length)
                    i++;
                else
                    i=0;
            }
        });
        return START_STICKY;
    }


}
