package com.example.notificationreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    // Where we store the input from the user to display in the second activity
    public static final String EXTRA_MESSAGE = "com.example.NotificationReminder.MESSAGE";

    Button notificationBtn;


    /** Called when the user taps the Send button */
    // Method needs to be void, only have View as a parameter and be public to work with onClick()
    public void sendMessage(View view) {

        // Display the second activity when someone clicks on the first button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }

    /** Called when the user taps the "Create Notification" button */
    public void triggerNotification(View view) {

        // This is where the notification get's created
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "defaultNotificationChannel")
                .setSmallIcon(R.drawable.ic_android_blue_24dp)
                .setContentTitle("Benachrichtigung 1")
                .setContentText("Dies ist der Inhalt ihrer Benachrichtigung")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
        managerCompat.notify(123, builder.build());


    }

}