package com.askerlap.emadahmed.wakemeup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Date;


public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_TABLE_1 = "category";
    private static final String DATABASE_TABLE_2 = "category_content";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE_1= "create table " + DATABASE_TABLE_1 + "("+"  category_id INTEGER PRIMARY KEY AUTOINCREMENT," +

            "category_name text NOT NULL ," +
            "category_time text NOT NULL "+

            ");";
    private static final String CREATE_TABLE_2 = "create table "+DATABASE_TABLE_2 +"(_id INTEGER PRIMARY KEY AUTOINCREMENT , category_name text NOT NULL ,"+
            "name text   ,"+
            "phone_num TEXT   );";

    public DataBaseHelper(Context context) {
        super(context, "contacts", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_1);
        db.execSQL(CREATE_TABLE_2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_1);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_2);
        onCreate(db);

    }
    public boolean insertCategory(String category_name , String time  ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("category_name", category_name);
        values.put("category_time ", time);

        db.insert(DATABASE_TABLE_1, null, values);


    return true;
}
    public boolean insertCategoryContent(String NAME,String name , String phone  ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("category_name",NAME);
        values.put("name",name);
        values.put("phone_num", phone);
        db.insert(DATABASE_TABLE_2, null, values);


        return true;
    }

    public Cursor getcategory(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("Select * from "+DATABASE_TABLE_1+" where _id="+id+"",null);
        return cursor;
    }
    
    public int Category_numberOfROWS(String tableName)
    {
        int numRow=0;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select name from "+DATABASE_TABLE_2+" where category_name='"+tableName+"'"    ,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            numRow++;
            cursor.moveToNext();
        }
        return numRow;
    }
    public Integer deleteCategory(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(DATABASE_TABLE_1,"category_name = ?",new String []{name});
        db.delete(DATABASE_TABLE_2, "category_name = ?", new String[]{name});
        return 0;

    }
    public Integer deleteContact(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(DATABASE_TABLE_2,"name = ?",new String []{name});
    }


    public ArrayList<String>getAllCategoryContent(String tableName){
        ArrayList<String>allContacts=new ArrayList<String>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select name from "+DATABASE_TABLE_2+" where category_name='"+tableName+"'"    ,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            allContacts.add(cursor.getString(cursor.getColumnIndex("name")));
            cursor.moveToNext();
        }
        return allContacts;
    }
    public ArrayList<String>getAllCategoryTime(){
        ArrayList<String>allContacts=new ArrayList<String>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from "+DATABASE_TABLE_1  ,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            allContacts.add(cursor.getString(cursor.getColumnIndex("category_time")));
            cursor.moveToNext();
        }
        return allContacts;
    }
    public ArrayList<String>getAllCategorys(){
        ArrayList<String>allContacts=new ArrayList<String>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from "+DATABASE_TABLE_1,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            allContacts.add(cursor.getString(cursor.getColumnIndex("category_name")));
            cursor.moveToNext();
        }
        return allContacts;
    }
    public ArrayList<String> getPhones(String category_name)
    {
        ArrayList<String> phones = new ArrayList<String>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select phone_num from "+DATABASE_TABLE_2+" where category_name='"+category_name+"'",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            phones.add(cursor.getString(cursor.getColumnIndex("phone_num")));
            cursor.moveToNext();
        }

        return phones;
    }
    public String caregoryName(String time){
        String category_name ;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select category_name from " + DATABASE_TABLE_1 + " where category_time='"+time+"'", null);
        cursor.moveToFirst();


        return cursor.getString(0);
    }
    public void updateCategory( String newTime ,String name){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put("category_time ",newTime);
       db.update(DATABASE_TABLE_1, values, "category_name = '"+name+"'", null);
    }
    public String caregoryTime(String name){
        String category_name ;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select category_time from " + DATABASE_TABLE_1 + " where category_name='"+name+"'", null);
        cursor.moveToFirst();


        return cursor.getString(0);
    }
    public ArrayList<String>SearchFor(String Name){
        ArrayList<String>allContacts=new ArrayList<String>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select category_name from "+DATABASE_TABLE_2+" where name ='"+Name+"'",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            allContacts.add(cursor.getString(cursor.getColumnIndex("category_name")));
            cursor.moveToNext();
        }
        return allContacts;
    }
}
