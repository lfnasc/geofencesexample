package com.apps.stenofh.geofencesexample;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
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

            // TODO: send a notification
            Log.i(TAG, geofenceTransitionDetails);

        } else {
            Log.e(TAG, "Invalid transition: " + geofenceTransition);
        }
    }

    private String getGeofenceTransitionDetails(int geofenceTransition, List triggeringGeofences) {
        return "Geofence Transition: " + geofenceTransition + TextUtils.join(", ", triggeringGeofences);
    }

}
