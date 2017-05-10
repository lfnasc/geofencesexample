package com.apps.stenofh.geofencesexample;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

/**
 * Created by leo on 09/05/17.
 */

public class GeofenceTransitionsIntentService extends IntentService {

    public static String TAG = "LEO";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GeofenceTransitionsIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {

            Log.e(TAG, String.valueOf(geofencingEvent.getErrorCode()));
            return;
        }

        // getting transition type
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // testing if was of interest
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // getting geofence trigged
            List triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // getting details
            String geofenceTransitionDetails = getGeofenceTransitionDetails(geofenceTransition, triggeringGeofences);

            // sengding notification
            sendNotification();

            Log.i(TAG, geofenceTransitionDetails);

        } else {
            Log.e(TAG, "Invalid transition: " + geofenceTransition);
        }
    }

    private void sendNotification() {
        // building a notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Geofence close!")
                .setContentText("You are near of a geofence of interest!");

        // creating a explicit intent
        Intent resultIntent = new Intent(this, GeofenceActivity.class);

        // ensures that navigation backward from the Activity leads out of application to the HomeScreen
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // adding the back stack for the intent
        stackBuilder.addParentStack(GeofenceActivity.class);

        // adding the intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mID allows to update the notification later on
        int mId = 0;
        mNotificationManager.notify(mId, mBuilder.build());

    }

    private String getGeofenceTransitionDetails(int geofenceTransition, List triggeringGeofences) {
        return "Geofence Transition: " + geofenceTransition + TextUtils.join(", ", triggeringGeofences);
    }

}
