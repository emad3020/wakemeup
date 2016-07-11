package com.askerlap.emadahmed.wakemeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by root on 19/04/16.
 */
public class azkarReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, azkarService.class);
        context.startService(startServiceIntent);
    }
}
