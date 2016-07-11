package com.askerlap.emadahmed.wakemeup;

import android.content.Context;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
Setting_DataBaseHelper SHelper;
    ArrayList<Integer> time_delay=new ArrayList<Integer>();
    private CheckBox azkar_switch;
    Integer [] background_ids={R.drawable.bg,R.drawable.bg_2,R.drawable.bg_3,R.drawable.bg_4,R.drawable.bg_5};
    String [] names={"BackGround 1","BackGround 2","BackGround 3","BackGround 4","Background 5"};
    Spinner sp,period;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        for(int i=10;i<30;i+=5){
            time_delay.add(i);

        }
        azkar_switch=(CheckBox)findViewById(R.id.enable_disable);
        SHelper=new Setting_DataBaseHelper(this);
        sp=(Spinner)findViewById(R.id.sp_backgrounds);
        period=(Spinner)findViewById(R.id.sp_period);
        period.setAdapter(new ArrayAdapter<Integer>(this,android.R.layout.simple_list_item_1,time_delay));
        sp.setAdapter(new spinnerAdapter(this,names,background_ids));
        assert getSupportActionBar() !=null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_save){
            SHelper.updateBackgroundPic(background_ids[sp.getSelectedItemPosition()], names[sp.getSelectedItemPosition()]);
            SHelper.insertAzkarSetting(time_delay.get(period.getSelectedItemPosition()));
             Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),time_delay.get(period.getSelectedItemPosition())+"",Toast.LENGTH_LONG).show();
            finish();
            return true;
        } else if(id==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return true;
    }

    class spinnerAdapter extends BaseAdapter{
        String [] names;
        Integer[] images;
        Context context;
        LayoutInflater mInflater;
        public  spinnerAdapter(Context context,String[] names,Integer[] images){
            this.images=images;
            this.names=names;
            this.context=context;
            mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return names.length;
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
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {

            View myview;
            TextView namesOfBackground ;
            ImageView IconOfBackground;
            myview=mInflater.inflate(R.layout.spinner_list,null);
            namesOfBackground=(TextView)myview.findViewById(R.id.backName);
            IconOfBackground=(ImageView)myview.findViewById(R.id.backIcon);
            namesOfBackground.setText(names[position]);
            IconOfBackground.setImageResource(images[position]);
//            if (position==0)
//                IconOfBackground.setVisibility(View.GONE);
            return myview;
        }
    }
}
