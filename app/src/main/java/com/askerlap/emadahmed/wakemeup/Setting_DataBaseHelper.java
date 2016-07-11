package com.askerlap.emadahmed.wakemeup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by root on 25/04/16.
 */
public class Setting_DataBaseHelper extends SQLiteOpenHelper  {
    private static final int SETTING_VERSIO= 1;
    private static final String AZKAR_TABLE="azkar_settings";
    private static final  int default_back= R.drawable.bg;
    private static final String default_name="BackGround 1";
    private static final String SETTING_TABLE="my_settings";
    private static final String CREATE_TABLE_SETTING=" create table "+SETTING_TABLE+"( _id INTEGER AUTO INCREMENT PRIMARY KEY ," +
            "background_name TEXT NOT NULL ,background_pic INTEGER );";
private static final String CRATE_TABLE_AZKAR="create table "+AZKAR_TABLE+" (azkar_id INTEGER AUTO INCREMENT PRIMARY KEY ,"+
        "azkar_start_time TEXT NOT NULL ,"+
        "azkar_stop_time TEXT NOT NULL ,"+
        "azkar_switch TEXT NOT NULL DEFAULT true ,"+
        "azkar_push_notify INTEGER NOT NULL DEFAULT 10 );";
    public Setting_DataBaseHelper(Context context ) {
        super(context, SETTING_TABLE, null, SETTING_VERSIO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SETTING);
        db.execSQL(CRATE_TABLE_AZKAR);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exist " + SETTING_TABLE);
        db.execSQL("drop table if exist "+AZKAR_TABLE);
        onCreate(db);

    }
    public void updateBackgroundPic(Integer newId,String name){
        ContentValues values=new ContentValues();
        SQLiteDatabase db=this.getWritableDatabase();
        values.put("background_pic", newId);
        values.put("background_name", name);
        db.insert(SETTING_TABLE, null, values);
    }
    public Integer getPicId(){
        ArrayList<String> allContacts=new ArrayList<String>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from "+SETTING_TABLE  ,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            allContacts.add(cursor.getString(cursor.getColumnIndex("background_pic")));
            cursor.moveToNext();
        }
        return Integer.parseInt(allContacts.get(allContacts.size()-1));
//
//        SQLiteDatabase db=this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select background_pic from " + SETTING_TABLE, null);
//        cursor.moveToFirst();
//
//
//        return Integer.parseInt(cursor.getString(0));
    }
    public Integer getSumOFPic(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + SETTING_TABLE + " where background_pic", null);
        cursor.moveToFirst();


        return cursor.getCount();
    }
public void insertAzkarSetting( int period ){
    SQLiteDatabase db=this.getWritableDatabase();
    ContentValues values=new ContentValues();

    values.put("azkar_push_notify",period);

     db.insert(AZKAR_TABLE, null, values);
}
    public Integer getSumOFAzkar(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + AZKAR_TABLE + " where azkar_push_notify", null);
        cursor.moveToFirst();


        return cursor.getCount();
    }
    public int getAzkarperiod(){
        ArrayList<Integer> allContacts=new ArrayList<Integer>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from "+AZKAR_TABLE  ,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            allContacts.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex("azkar_push_notify"))));
            cursor.moveToNext();
        }
        return allContacts.get(allContacts.size());
    }
}
