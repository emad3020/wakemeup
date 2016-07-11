package com.askerlap.emadahmed.wakemeup;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Category_Contacts extends AppCompatActivity {
    String Category_name;
    ArrayList<String>category_contactsName,category_contactsPhones;
    ListView mylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_contacts);
        mylist=(ListView)findViewById(R.id.db_category_contacts);
        assert getSupportActionBar() !=null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent extras=getIntent();
         Category_name =extras.getStringExtra("fname");
        DataBaseHelper helper=new DataBaseHelper(this);


        showContacts();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_contact_to_category, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.btn_menu_settings){

//            Toast.makeText(getApplicationContext(),Category_name,Toast.LENGTH_LONG).show();

//            Add_Contact_Fragment dig=new Add_Contact_Fragment(Category_name);
//            FragmentManager fg=getFragmentManager();
//            dig.show(fg,"Contacts Fragment");
            Intent i=new Intent(Category_Contacts.this,select_contact.class);
            i.putExtra("category_name_key",Category_name);
            startActivity(i);

            return true;
        } else if(item.getItemId()==R.id.btn_contacts_refresh){

            showContacts();
            Toast.makeText(getApplicationContext(),R.string.contacts_update,Toast.LENGTH_LONG).show();


            return  true;
        }
        else if(item.getItemId()==R.id.updtae_time_ic) {
            FragmentManager fg= getFragmentManager();
            update_fragment update=new update_fragment(Category_name);
            update.show(fg, "update my time");
            return true;
        }
        else if(item.getItemId()==android.R.id.home){
            //return to the parent Activity
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        else if(item.getItemId()==R.id.btn_info){
            DataBaseHelper helper=new DataBaseHelper(this);
            helper.getReadableDatabase();
            String time= helper.caregoryTime(Category_name);
            int contactsSum=helper.Category_numberOfROWS(Category_name);
            final AlertDialog.Builder comfirDialog = new AlertDialog.Builder(Category_Contacts.this);
            comfirDialog.setTitle(R.string.category_info_Title);
            comfirDialog.setMessage(getResources().getString(R.string.category_info_name) + " " + Category_name + "\n"
                    + getResources().getString(R.string.category_info_time) + time + "\n"
                    + getResources().getString(R.string.category_info_contacts )+ " " + contactsSum);
            comfirDialog.setIcon(R.mipmap.ic_error);
            comfirDialog.setPositiveButton(R.string.btn_frag_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            comfirDialog.show();

        }
        return true;
    }
    public void showContacts(){
        DataBaseHelper helper=new DataBaseHelper(this);
        Setting_DataBaseHelper SHelper=new Setting_DataBaseHelper(this);
        SHelper.getWritableDatabase();
        helper.getReadableDatabase();
        SHelper=new Setting_DataBaseHelper(this);
        if(SHelper.getSumOFPic()==0 ){
             mylist.setBackground(getResources().getDrawable(R.drawable.bg ));
        } else {
            LinearLayout layout=(LinearLayout)findViewById(R.id.main_content_layout);
            mylist.setBackground(getResources().getDrawable(SHelper.getPicId()));}
        category_contactsName=new ArrayList<String>();
        category_contactsPhones=new ArrayList<String>();
        category_contactsName=helper.getAllCategoryContent(Category_name);
        category_contactsPhones=helper.getPhones(Category_name);
        mylist.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, category_contactsName));
        mylist.setAdapter(new MyAdapter(this,category_contactsName,category_contactsPhones));
        mylist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder comfirDialog = new AlertDialog.Builder(Category_Contacts.this);
                comfirDialog.setTitle(R.string.delete_contact);
                comfirDialog.setMessage(R.string.delete_messg);
                comfirDialog.setIcon(R.mipmap.ic_error);
                comfirDialog.setPositiveButton(R.string.btn_frag_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DataBaseHelper helper1 = new DataBaseHelper(getApplicationContext());
                        helper1.deleteContact(category_contactsName.get(position));
                        showContacts();
                        Toast.makeText(getApplicationContext(), R.string.contact_UpdateDelete, Toast.LENGTH_LONG).show();
                    }
                });
                comfirDialog.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                comfirDialog.show();
                return false;
            }
        });
    }




    @Override
    protected void onResume() {
        super.onResume();
        showContacts();
    }

    @Override
    protected void onPause() {
        super.onPause();
        showContacts();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showContacts();
    }

    class MyAdapter extends BaseAdapter {

        ArrayList <String> contact_names,contact_nums;;
        LayoutInflater mInflater;
        Context context;
        MyAdapter(Context context,ArrayList <String> names,ArrayList <String> phones){
            this.context=context;
            this.contact_names=names;
            this.contact_nums=phones;

            mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return contact_names.size();
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
            TextView contact_name,contact_num;
             View vi;
             vi=mInflater.inflate(R.layout.contacts_list,null);
            contact_name=(TextView)vi.findViewById(R.id.contact_name_list);
            contact_num=(TextView)vi.findViewById(R.id.contact_phone_list);
            contact_name.setText(contact_names.get(position));
            contact_num.setText( contact_nums.get(position));
             return vi;
        }
     }
}
