package com.example.notificationreminder;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.notificationreminder.BuildConfig;

public class InfoActivity extends AppCompatActivity {

    // Gets the version code & version name from the gradle build file
    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Set the navigationIcon to redirect a page up
        View topAppBarSub = findViewById(R.id.topAppBarSub);
        topAppBarSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set the version name text view
        TextView versionNameTextView = findViewById(R.id.textView_versionName);
        versionNameTextView.setText("Version: " + versionName);

    }



}