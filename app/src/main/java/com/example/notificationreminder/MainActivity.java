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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;


public class MainActivity extends AppCompatActivity {

    // variable used to generate notificationIds for variables
    public int notificationId;
    // Creates a tooltip manager
    ToolTipsManager mToolTipsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the tooltipManager object
        mToolTipsManager = new ToolTipsManager();

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
     *
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
     * Called when the user taps the "Create Notification" button, handles the entire process
     * of getting the user input, handeling intents and adding a snackbar to give the user feedback.
     *
     * @param view
     */
    public void triggerNotification(View view) {

        // Create a notificationId for everything that's coming
        generateNotificationId();


        // Creates the checkbox and sets a boolean to make a notification permanent
        CheckBox checkBoxSetPermanent = findViewById(R.id.checkboxSetPermanent);
        boolean booleanSetPermanent = false;
        if (checkBoxSetPermanent.isChecked()){
            booleanSetPermanent = true;
        } else {
            booleanSetPermanent = false;
        }


        // Get user input from the text inputs, clear after user has input his text
        EditText editNotificationTitle = findViewById(R.id.inputNotificationTitle);
        EditText editNotificationText = findViewById(R.id.inputNotificationText);

        String notificationTitle = editNotificationTitle.getText().toString();
        String notificationText = editNotificationText.getText().toString();

        editNotificationTitle.getText().clear();
        editNotificationText.getText().clear();


        // Set notificationText to be NotificationTitle if the text is empty
        if (notificationText.equals("")){
            notificationText = notificationTitle;
        }


        // Creates an intent to communicate with the BroadcastReceiver (NotificationReceiver) and
        // adds the notificationId to it
        Intent deleteIntent = new Intent(this, NotificationReceiver.class);
        deleteIntent.setAction("ACTION_DELETE_NOTIFICATION");
        deleteIntent.putExtra("ID", notificationId);
        PendingIntent deletePendingIntent = PendingIntent.getBroadcast(this, notificationId, deleteIntent, PendingIntent.FLAG_IMMUTABLE);


        // This is where the notification gets created
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "defaultNotificationChannel")
                .setSmallIcon(R.mipmap.ic_launcher_notify_round)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationText))
                // Sets whether the notification is permanent
                .setOngoing(booleanSetPermanent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_info_24, getString(R.string.delete), deletePendingIntent);


        // Sends the notification
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
        managerCompat.notify(notificationId, builder.build());


        // Add snackbar to give the user feedback that the notification was created
        Snackbar.make(view, R.string.snackbar_notificationCreated, Snackbar.LENGTH_LONG)
                .show();

    }


    /**
     * Generates an int number that is the current time, then cleans it and returns it
     *
     * @return
     */
    public int generateNotificationId() {

        String timestamp = java.time.LocalTime.now().toString();
        String timestampCleaned = timestamp.replace(":", "")
                .replace(".", "");
        notificationId = Integer.parseInt(timestampCleaned);

        return notificationId;

    }

    // Called when the user taps the "help icon" right next to the "Set permanent" checkbox
    public void seeHelp(View view) {

        RelativeLayout layout = findViewById(R.id.wrapperLayoutCardDesign);
        LinearLayout layoutView = findViewById(R.id.linearLayoutPermanentCheckbox);

        // Create tooltip
        String message = getString(R.string.tooltip_checkboxSetPermanent);
        ToolTip.Builder builder = new ToolTip
                .Builder(this, layoutView, layout, message, ToolTip.POSITION_ABOVE);

        // Show tooltip
        mToolTipsManager.show(builder.build());


    }
}