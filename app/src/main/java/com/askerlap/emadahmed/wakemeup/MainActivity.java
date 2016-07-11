package com.askerlap.emadahmed.wakemeup;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;

import android.app.PendingIntent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
 import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
 import android.widget.BaseAdapter;

import android.widget.LinearLayout;
import android.widget.ListView;
 import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
private DataBaseHelper DBHelper;
    private Setting_DataBaseHelper SHelper;
    ListView list;
    Context mycontext;
    private PendingIntent pendingIntent;
    /* Retrieve a PendingIntent that will perform a broadcast */


    ArrayList<String> allContacts,list_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        SHelper=new Setting_DataBaseHelper(this);
        SHelper.getReadableDatabase();
        if(SHelper.getSumOFAzkar()!=0)
         Toast.makeText(getApplicationContext(),SHelper.getAzkarperiod()+"",Toast.LENGTH_LONG).show();//toast to test the settings dataBase of the azkar
        pendingIntent=PendingIntent.getBroadcast(MainActivity.this,0,alarmIntent,0);

        list=(ListView)findViewById(R.id.db_contacts);
//set background from user
        ///////////////////////////////////////////////////////

        if(SHelper.getSumOFPic()==0){
            LinearLayout layout=(LinearLayout)findViewById(R.id.main_content_layout);
            layout.setBackground(getResources().getDrawable(R.drawable.bg));
        } else {
        LinearLayout layout=(LinearLayout)findViewById(R.id.main_content_layout);
        layout.setBackground(getResources().getDrawable(SHelper.getPicId()));}
        /////////////////////////////////////////////////////
        //Read from data base
        DBHelper=new DataBaseHelper(this);
        DBHelper.getReadableDatabase();
        //creat an arrylist for time and names
        list_time=new ArrayList<String>();
         list_time=DBHelper.getAllCategoryTime();
        allContacts=new ArrayList<String>();
        allContacts=DBHelper.getAllCategorys();
        mycontext=this;
//        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allContacts));
        list.setAdapter(new MyAdapter(this,allContacts,list_time));

        list.setItemsCanFocus(false);
        list.setTextFilterEnabled(true);
         list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Intent i = new Intent(MainActivity.this, Category_Contacts.class);
                 String categoryName = allContacts.get(position);
                 i.putExtra("fname", categoryName);
                 startActivity(i);
             }
         });
list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final AlertDialog.Builder comfirDialog = new AlertDialog.Builder(MainActivity.this);
        comfirDialog.setTitle(R.string.delete_category);
        comfirDialog.setMessage(R.string.delete_messg2);
        comfirDialog.setIcon(R.mipmap.ic_error);
        comfirDialog.setPositiveButton(R.string.btn_frag_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                DBHelper.getWritableDatabase();
                DBHelper.deleteCategory(allContacts.get(position));
                list_time=new ArrayList<String>();
                list_time=DBHelper.getAllCategoryTime();
                allContacts=new ArrayList<String>();
                allContacts=DBHelper.getAllCategorys();
                mycontext=getApplicationContext();
                list.setAdapter(new MyAdapter(mycontext, allContacts, list_time));
                list.setItemsCanFocus(false);
                list.setTextFilterEnabled(true);
                Toast.makeText(getApplicationContext(),R.string.category_Updatedelete,Toast.LENGTH_LONG).show();
            }
        });
        comfirDialog.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        comfirDialog.show();
        return true;
    }
});
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupNumber =allContacts.size()+"";
                Intent i=new Intent(MainActivity.this,Add_category.class);
                i.putExtra("key1", groupNumber);
//                Toast.makeText(getApplicationContext(),groupNumber+"",Toast.LENGTH_LONG).show();
                startActivity(i);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        showGroups();
        start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
start();
startAt10();

    }
    public void start() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1000;

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

    }
    public void startAt10() {
        int period;
        SHelper.getReadableDatabase();
        if(SHelper.getSumOFAzkar()==0)
            period=10;
        else
        period=SHelper.getAzkarperiod();
         PendingIntent pIntent;
        Intent alarmIntent = new Intent(MainActivity.this, azkarReciever.class);

        pIntent=PendingIntent.getBroadcast(MainActivity.this,0,alarmIntent,0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1000 * 60 * period;

        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 33);

        /* Repeating on every 20 minutes interval */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                interval, pIntent);
    }
    @Override
    protected void onResume() {
        super.onResume();
//        startService(new Intent(MainActivity.this, TestService.class));
        showGroups();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        
        return super.onCreateOptionsMenu(menu);

    }
public void showGroups(){
    if(SHelper.getSumOFPic()==0){
        LinearLayout layout=(LinearLayout)findViewById(R.id.main_content_layout);
        layout.setBackground(getResources().getDrawable(R.drawable.bg));
    } else {
        LinearLayout layout=(LinearLayout)findViewById(R.id.main_content_layout);
        layout.setBackground(getResources().getDrawable(SHelper.getPicId()));}
    list_time = new ArrayList<String>();
    list_time=DBHelper.getAllCategoryTime();
    allContacts=new ArrayList<String>();
    allContacts=DBHelper.getAllCategorys();
    mycontext=this;
    list.setAdapter(new MyAdapter(this, allContacts, list_time));
    list.setItemsCanFocus(false);
    list.setTextFilterEnabled(true);
}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.btn_refresh){
            Toast.makeText(getApplicationContext(),R.string.categories_update,Toast.LENGTH_LONG).show();
            showGroups();
//            list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allContacts));
            return true;
        }
        else if(id==R.id.action_about){
            startActivity(new Intent(MainActivity.this,About_Activity.class));
            return true;

        } else if(id==R.id.action_settings){
            startActivity(new Intent(MainActivity.this,SettingActivity.class));
            return true;
        }
        return true;
    }
    class MyAdapter extends BaseAdapter {

ArrayList <String> category_names,category_times;;
        LayoutInflater mInflater;
        Context context;
        MyAdapter(Context context,ArrayList <String> names,ArrayList <String> times){
            this.context=context;
            this.category_names=names;
            this.category_times=times;

            mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return category_names.size();
        }


        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
TextView category_name,category_time;
            View vi;

                vi=mInflater.inflate(R.layout.category_list,null);
            category_name=(TextView)vi.findViewById(R.id.category_name_list);
            category_time=(TextView)vi.findViewById(R.id.category_time_list);

            category_name.setText(category_names.get(position));
            category_time.setText(category_times.get(position));


            return vi;
        }

    }
}
