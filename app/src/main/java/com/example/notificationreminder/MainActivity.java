package com.example.notificationreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Register toolbar as top app bar */
        Toolbar topAppBar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);

        /** Create notification channel if Android version > Oreo */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Get misc information
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            // Create channel & set the description (for settings?)
            NotificationChannel channel = new NotificationChannel("defaultNotificationChannel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    // Set up the menu in the top bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }


    /** Called when the user taps the "Create Notification" button */
    public void triggerNotification(View view) {

        // Get user input from the text inputs, clear after user has input his text
        EditText editNotificationTitle = findViewById(R.id.inputNotificationTitle);
        EditText editNotificationText = findViewById(R.id.inputNotificationText);

        String notificationTitle = editNotificationTitle.getText().toString();
        String notificationText = editNotificationText.getText().toString();

        editNotificationTitle.getText().clear();
        editNotificationText.getText().clear();

        // This is where the notification get's created
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "defaultNotificationChannel")
                .setSmallIcon(R.drawable.ic_android_blue_24dp)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                //.addAction(R.drawable.ic_android_blue_24dp, getString(R.string.delete), deletePendingIntent); Unecessary for now

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
        managerCompat.notify(123, builder.build());


    }

}