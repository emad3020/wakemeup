package com.askerlap.emadahmed.wakemeup;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by root on 02/05/16.
 */

public class searchResults extends AppCompatActivity {
    private ListView Results;
    List<String> Contacts_names = new ArrayList<String>();
    List<String> Contacts_phnos = new ArrayList<String>();
    ArrayList<String> array_results=new ArrayList<String>();
    DataBaseHelper Helper;
    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Results=(ListView)findViewById(R.id.txtQuery);

        Helper=new DataBaseHelper(this);
        String query = getIntent().getStringExtra(SearchManager.QUERY);
        Toast.makeText(getApplicationContext(),query,Toast.LENGTH_LONG).show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         /**
         * Use this query to display search results like
         * 1. Getting the data from SQLite and showing in listview
         * 2. Making webrequest and displaying the data
         * For now we just display the query only
         */
//        array_results=Helper.SearchFor(query);
        findContact(query);
        Results.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array_results));

    }
    public void getAllContacts(ContentResolver cr){
        Cursor phones=cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while (phones.moveToNext()){
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Contacts_names.add(name);
            Contacts_phnos.add(phoneNumber);
        }
        phones.close();
    }
    public void findContact(String query){
getAllContacts(getContentResolver());
        for(int i=0;i<Contacts_phnos.size();i++){

            if(Contacts_names.get(i).toLowerCase().contains(query))
                array_results.add(Contacts_names.get(i));

        }
       if(array_results.size()==0)
           array_results.add("Not Found");

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_result_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_search_ok){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    /**
     * Handling intent data
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
             Toast.makeText(getApplicationContext(),query,Toast.LENGTH_LONG).show();
            /**
             * Use this query to display search results like
             * 1. Getting the data from SQLite and showing in listview
             * 2. Making webrequest and displaying the data
             * For now we just display the query only
             */
        array_results=Helper.SearchFor(query);
            Results.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array_results));
//            txtQuery.setText("Search Query: " + query);


        }

    }
}
