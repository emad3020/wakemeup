package com.askerlap.emadahmed.wakemeup;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
public class Add_category extends AppCompatActivity {
    private   EditText category_name;
    private TextView time;
    ArrayList<String> categoriesNames;
    ArrayList<String> categoriesTimes;
  public static String NewCategoryName="";
    private Button addCategory,cancelAdding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        category_name=(EditText)findViewById(R.id.et_categoryName);
        Intent intent=getIntent();
        String numberG=intent.getStringExtra("key1");
        category_name.setText("Group" + numberG);
        time=(TextView)findViewById(R.id.et_categoryTime);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });
        addCategory=(Button)findViewById(R.id.btn_done);
        cancelAdding=(Button)findViewById(R.id.btn_cancel);
        cancelAdding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DataBaseHelper helper=new DataBaseHelper(this);
        categoriesNames= new ArrayList<String>();categoriesNames=helper.getAllCategorys();
        categoriesTimes= new ArrayList<String>();categoriesTimes=helper.getAllCategoryTime();
         addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewCategoryName=category_name.getText().toString() ;
                if (!category_name.getText().toString().isEmpty() && !time.getText().toString().isEmpty()
                        ) {
                    for (int i= 0; i<categoriesNames.size();i++)
                    {
                        if(NewCategoryName.equals( categoriesNames.get(i))||time.getText().equals(categoriesTimes.get(i))){
                            Toast.makeText(getApplicationContext(),R.string.existing_category,Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    DataBaseHelper db = new DataBaseHelper(getApplicationContext());
                    db.getWritableDatabase();
                    db.insertCategory(NewCategoryName, time.getText().toString());
                    finish();

                } else {
                    final AlertDialog.Builder comfirDialog = new AlertDialog.Builder(Add_category.this);
                    comfirDialog.setTitle(R.string.error);
                    comfirDialog.setMessage(R.string.error_messg);
                    comfirDialog.setIcon(R.mipmap.ic_error);
                    comfirDialog.setPositiveButton(R.string.btn_frag_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (category_name.getText().length() == 0)
                                category_name.setTextColor(Color.RED);
                            else if (time.getText().toString().isEmpty())
                                time.setHintTextColor(Color.RED);
                            else {
                                category_name.setTextColor(Color.RED);
                                time.setHintTextColor(Color.RED);
                            }
                            dialog.dismiss();
                        }
                    });
                    comfirDialog.show();
                }
            }
        });
    }
    public void timePicker(){
        CustomTimePickerDialog customTimePickerDialog=new CustomTimePickerDialog(Add_category.this,timeSetListener,
                Calendar.getInstance().get(Calendar.HOUR),
                CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE)+CustomTimePickerDialog.TIME_PICKER_INTERVAL),false);
        customTimePickerDialog.setTitle(R.string.category_Time);
        customTimePickerDialog.show();
    }
    public static class CustomTimePickerDialog extends TimePickerDialog{
        public static final int TIME_PICKER_INTERVAL=1;
        private boolean mIgnoreEvent=false;
        public CustomTimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
            super(context, callBack, hourOfDay, minute, is24HourView);
        }
        @Override
        public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
            super.onTimeChanged(timePicker, hourOfDay, minute);
            if (!mIgnoreEvent){
                minute = getRoundedMinute(minute);
                mIgnoreEvent=true;
                timePicker.setCurrentMinute(minute);
                mIgnoreEvent=false;
            }
        }

        public static  int getRoundedMinute(int minute){
            if(minute % TIME_PICKER_INTERVAL != 0){
                int minuteFloor = minute - (minute % TIME_PICKER_INTERVAL);
                minute = minuteFloor + (minute == minuteFloor + 1 ? TIME_PICKER_INTERVAL : 0);
                if (minute == 60)  minute=0;
            }
            return minute;
        }
    }
    private CustomTimePickerDialog.OnTimeSetListener timeSetListener = new CustomTimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            time.setText(String.format("%02d", hourOfDay) + ":" +String.format("%02d", minute));
        }
    };
}

