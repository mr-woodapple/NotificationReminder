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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar, menu);
        return true;
    }

    // Handles clicks on the menu bar icons
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_info:
                Intent startInfoIntent = new Intent(this, InfoActivity.class);
                startActivity(startInfoIntent);
                return true;

            // TODO: Add History here

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    // ID for the notifications (each notification has a unique id)
    public int notificationId = 0;

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
        managerCompat.notify(notificationId, builder.build());

        // Add snackbar to give the user feedback that something happened
        Snackbar.make(view, R.string.snackbar_notificationCreated, Snackbar.LENGTH_LONG)
                .show();

        // Increase notificationID +1
        notificationId += 1;
    }

}