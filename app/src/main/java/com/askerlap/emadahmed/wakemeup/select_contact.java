package com.askerlap.emadahmed.wakemeup;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class select_contact extends AppCompatActivity implements AdapterView.OnItemClickListener {
    List<String> name1 = new ArrayList<String>();
    List<String> phno1 = new ArrayList<String>();
    MyAdapter ma ;
    Button select;
    String tablename;
    private ListView contastsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getAllContacts(getContentResolver());
        tablename=getIntent().getStringExtra("category_name_key");
         contastsListView= (ListView) findViewById(R.id.activity_lv);
        ma=new MyAdapter();
         contastsListView.setAdapter(ma);
        contastsListView.setOnItemClickListener(this);
        contastsListView.setItemsCanFocus(false);
        contastsListView.setTextFilterEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
                helper.getWritableDatabase();

                for (int i = 0; i < name1.size(); i++) {
                    if (ma.mCheckStates.get(i)) {

                        helper.insertCategoryContent(tablename, name1.get(i), phno1.get(i));

                    }
                }
//                ((Category_Contacts) getActivity()).showContacts();

                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ma.toggle(position);


    }

    public void getAllContacts(ContentResolver cr){
        Cursor phones=cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while (phones.moveToNext()){
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            name1.add(name);
            phno1.add(phoneNumber);
        }
        phones.close();
    }



    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.select_contact_menu, menu);


         return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_search:
                // Associate searchable configuration with the SearchView
                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                SearchView searchView = (SearchView) item.getActionView();
//                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                // search action
                   onSearchRequested();

                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class MyAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener{
        int pp=0;
        private SparseBooleanArray mCheckStates;
        LayoutInflater mInflater;
        TextView tv1,tv;
        CheckBox cb;
        MyAdapter(){
            mCheckStates=new SparseBooleanArray(name1.size());
            mInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return name1.size();
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
            pp=position;
            View vi=convertView;
            if(convertView ==null)
                vi=mInflater.inflate(R.layout.contacts_layout,null);
            TextView tv=(TextView)vi.findViewById(R.id.name);
            tv1=(TextView)vi.findViewById(R.id.no);
            cb=(CheckBox)vi.findViewById(R.id.check);
            tv.setText(name1.get(position ));
            tv1.setText(phno1.get(position));
            cb.setTag(position);
            cb.setChecked(mCheckStates.get(position,false));
            cb.setOnCheckedChangeListener(this);

            return vi;
        }
        public boolean isChecked(int position){

            return mCheckStates.get(position,false);
        }
        public void setChecked(int position,boolean isChecked){

            mCheckStates.put(position,isChecked);
            notifyDataSetChanged();
        }
        public void toggle(int position){
            setChecked(position, !isChecked(position));
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mCheckStates.put((Integer) buttonView.getTag(), isChecked);
         }
    }
}
