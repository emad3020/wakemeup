package com.askerlap.emadahmed.wakemeup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class About_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_layout);
        assert getSupportActionBar()!=null;
        getSupportActionBar().hide();
    }
}
