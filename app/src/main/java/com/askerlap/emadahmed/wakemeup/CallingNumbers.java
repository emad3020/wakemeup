package com.askerlap.emadahmed.wakemeup;


import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
 import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;

import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
 import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class CallingNumbers extends AppCompatActivity  {
    ArrayList<String> phones=new ArrayList<String>();
    public static int not_answer_count = 0;
    public static final int MAX_RINGING_NUMBER = 2;
public void endCall(){
    try {
        //String serviceManagerName = "android.os.IServiceManager";
        String serviceManagerName = "android.os.ServiceManager";
        String serviceManagerNativeName = "android.os.ServiceManagerNative";
        String telephonyName = "com.android.internal.telephony.ITelephony";

        Class telephonyClass;
        Class telephonyStubClass;
        Class serviceManagerClass;
        Class serviceManagerStubClass;
        Class serviceManagerNativeClass;
        Class serviceManagerNativeStubClass;

        Method telephonyCall;
        Method telephonyEndCall;
        Method telephonyAnswerCall;
        Method getDefault;

        Method[] temps;
        Constructor[] serviceManagerConstructor;

        // Method getService;
        Object telephonyObject;
        Object serviceManagerObject;

        telephonyClass = Class.forName(telephonyName);
        telephonyStubClass = telephonyClass.getClasses()[0];
        serviceManagerClass = Class.forName(serviceManagerName);
        serviceManagerNativeClass = Class.forName(serviceManagerNativeName);

        Method getService = // getDefaults[29];
                serviceManagerClass.getMethod("getService", String.class);

        Method tempInterfaceMethod = serviceManagerNativeClass.getMethod(
                "asInterface", IBinder.class);

        Binder tmpBinder = new Binder();
        tmpBinder.attachInterface(null, "fake");

        serviceManagerObject = tempInterfaceMethod.invoke(null, tmpBinder);
        IBinder retbinder = (IBinder) getService.invoke(serviceManagerObject, "phone");
        Method serviceMethod = telephonyStubClass.getMethod("asInterface", IBinder.class);

        telephonyObject = serviceMethod.invoke(null, retbinder);
        //telephonyCall = telephonyClass.getMethod("call", String.class);
        telephonyEndCall = telephonyClass.getMethod("endCall");
        //telephonyAnswerCall = telephonyClass.getMethod("answerRingingCall");

        telephonyEndCall.invoke(telephonyObject);

    } catch (Exception e) {
        e.printStackTrace();
        Log.e("TAG ERROR",
                "FATAL ERROR: could not connect to telephony subsystem");
        Log.e("TAG ERROR", "Exception object: " + e);
    }
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String name_category=getIntent().getStringExtra("key_category");
        DataBaseHelper helper=new DataBaseHelper(this);
        helper.getReadableDatabase();

        phones=helper.getPhones(name_category);
        try {
            contact(phones);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


//        TelephonyManager telephonyManager=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        telephonyManager.listen(new TeleListener(), PhoneStateListener.LISTEN_CALL_STATE);

    }
/*********************************
*              i do this
**********************************/

public void monitorCallState()
{


    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up



        String srvcName = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(srvcName);

        PhoneStateListener callStateListener = new PhoneStateListener() {

            int prev_state ;
            int count = 0;


            public void onCallStateChanged(int state, String incomingNumber) {

               switch (state)
               {
                   case TelephonyManager.CALL_STATE_IDLE:


                       if (prev_state == TelephonyManager.CALL_STATE_OFFHOOK )
                       {

                           if (count >= 43 && count < 50) {
                               count = 0;
                               if (not_answer_count < MAX_RINGING_NUMBER)
                               not_answer_count++;

                               else
                                   not_answer_count = 0;
//                               Toast.makeText(getApplicationContext(), "not answer not_answer_count =:"+not_answer_count, Toast.LENGTH_LONG).show();

                           }

                           else if(count<43){
//                               Toast.makeText(getApplicationContext(), "cancle", Toast.LENGTH_LONG).show();
                               not_answer_count = 0;
                               count = 0;

                           }

                       }

//                       Toast.makeText(getApplicationContext(), "idle count = :"+count, Toast.LENGTH_LONG).show();

                       break;
                   case TelephonyManager.CALL_STATE_OFFHOOK:
//                       Toast.makeText(getApplicationContext(), "offhook", Toast.LENGTH_LONG).show();
                       Timer timer=new Timer();
                       timer.scheduleAtFixedRate(new TimerTask() {
                           @Override
                           public void run() {
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {

                                       count++;
                                       if (count>50)
                                       {
                                           endCall();
                                           count = 0;
                                       }
                                   }
                               });
                           }
                       }, 1000, 1000);

                       prev_state = state;
                       break;
                   default:
                      // Toast.makeText(getApplicationContext(), "from default case ", Toast.LENGTH_LONG).show();


               }


            }
        };

    telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
}

    /*************************************
     *
     End of Attia's Method
     ****************************/
    public void contact (ArrayList<String> phones) throws InterruptedException {

        if (phones.size()>0 )

        {
            for (int i= 0 ;i<phones.size();i++) {

                Intent phoneCall = new Intent(Intent.ACTION_CALL);
                String dial = phones.get(i);
                phoneCall.setData(Uri.parse("tel:" + dial));
                try {
                    startActivity(phoneCall);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();

                }

                monitorCallState();
                if (not_answer_count <MAX_RINGING_NUMBER) {
                    i--;
                }
                Thread thr =new Thread(){
                    public void run() {
                        try {
                            sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                };
                thr.start();
            }

        }


    }



}
