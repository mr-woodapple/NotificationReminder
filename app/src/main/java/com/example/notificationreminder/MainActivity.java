package com.example.notificationreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {


    public int notificationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register toolbar as top app bar
        Toolbar topAppBar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);

        // Create notification channel if Android version > Oreo
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


    /**
     *  Set up the menu in the top bar
     * @param menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar, menu);
        return true;
    }

    /**
     * Handles clicks on the menu bar icons
     *
     * @param item
     * @return
     */
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


    /**
     * Called when the user taps the "Create Notification" button
     * @param view
     */
    public void triggerNotification(View view) {

        // Create a notificationId for everything that's coming
        generateNotificationID();


        // Creates an intent to communicate with the BroadcastReceiver
        Intent deleteIntent = new Intent(this, MyBroadcastReceiver.class);
        deleteIntent.setAction(Intent.ACTION_DELETE);
        deleteIntent.putExtra("notificationId", notificationId);
        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(this, 0, deleteIntent, 0);


        // Get user input from the text inputs, clear after user has input his text
        EditText editNotificationTitle = findViewById(R.id.inputNotificationTitle);
        EditText editNotificationText = findViewById(R.id.inputNotificationText);

        String notificationTitle = editNotificationTitle.getText().toString();
        String notificationText = editNotificationText.getText().toString();

        editNotificationTitle.getText().clear();
        editNotificationText.getText().clear();


        // This is where the notification get's created
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "defaultNotificationChannel")
                .setSmallIcon(R.mipmap.ic_launcher_notify_round)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                // Allows to have an expandable notification (& much more content)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_android_blue_24dp, getString(R.string.delete), deletePendingIntent);


        // Creates a unique ID based on the current timestamp (only time, no date included)
        // and convert it to an int
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
        String timestamp = java.time.LocalTime.now().toString();
        String timestampCleaned = timestamp.replace(":", "")
                                            .replace(".", "");
        notificationId = Integer.parseInt(timestampCleaned);
        managerCompat.notify(notificationId, builder.build());


        // Add snackbar to give the user feedback that the notification was created
        Snackbar.make(view, R.string.snackbar_notificationCreated, Snackbar.LENGTH_LONG)
                .show();

    }

    public int generateNotificationID() {
        String timestamp = java.time.LocalTime.now().toString();
        String timestampCleaned = timestamp.replace(":", "")
                .replace(".", "");
        notificationId = Integer.parseInt(timestampCleaned);

        return notificationId;
    }

}