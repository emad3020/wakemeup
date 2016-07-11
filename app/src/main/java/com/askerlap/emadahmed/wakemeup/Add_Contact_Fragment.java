package com.askerlap.emadahmed.wakemeup;

import android.app.DialogFragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

public class Add_Contact_Fragment extends DialogFragment implements AdapterView.OnItemClickListener{
    List<String> name1 = new ArrayList<String>();
    List<String> phno1 = new ArrayList<String>();
    MyAdapter ma ;
    Button select;
    String tablename;
 public Add_Contact_Fragment(String name){
    this.tablename=name;
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_all_contatcts, null, false);
        getDialog().setTitle(R.string.frag_db);
        getAllContacts(getActivity().getContentResolver());
        ListView lv= (ListView) view.findViewById(R.id.lv);
        ma = new MyAdapter();
        lv.setAdapter(ma);
        lv.setOnItemClickListener(this);
        lv.setItemsCanFocus(false);
        lv.setTextFilterEnabled(true);
        // adding
        select = (Button) view.findViewById(R.id.button1);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataBaseHelper helper=new DataBaseHelper(getActivity().getApplication());
                helper.getWritableDatabase();

                for (int i=0;i<name1.size();i++) {
                    if (ma.mCheckStates.get(i)){

                        helper.insertCategoryContent(tablename,phno1.get(i),name1.get(i));

                }
                }
                ((Category_Contacts)getActivity()).showContacts();

                dismiss();

            }
        });
        return view;
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
            name1.add(phoneNumber);
            phno1.add(name);
        }
phones.close();
    }
    class MyAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener{
        int pp=0;
        private SparseBooleanArray mCheckStates;
        LayoutInflater mInflater;
        TextView tv1,tv;
        CheckBox cb;
        MyAdapter(){
            mCheckStates=new SparseBooleanArray(name1.size());
            mInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
