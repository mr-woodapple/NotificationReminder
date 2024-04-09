package de.holzapfelgrafik.notificationreminder;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class NotificationReceiver extends BroadcastReceiver {

    /**
     * Allows the user to delete the notification from an addAction in the notification.
     *
     * Grabs the generated notificationId, creates a NotificationManager and then cancels
     * the notification with the given id.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra("ID", 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        notificationManager.cancel(id);

    }


}
