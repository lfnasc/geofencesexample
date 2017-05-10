package com.apps.stenofh.geofencesexample;

import android.*;
import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> , ActivityCompat.OnRequestPermissionsResultCallback{

    /***
     *
     * placeholders points
     * -3.095049, -59.995185
     * -3.094489, -59.995712
     * -3.096849, -59.992190
     * -3.092792, -59.997341
     * -3.093772, -59.993252
     * -3.015206, -59.958409
     *
     * */

    public static String TAG = "LEO";

    private GoogleApiClient mGoogleApiClient;
    private List<Geofence> mGeofenceList;
    private PendingIntent mGeofencePendingIntent;

    private GeofencingRequest geofencingRequest;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setting google api services
        setGoogleApiClient();

    }

    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        mGeofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private void setGeofences() {
        double[] lat = new double[6];
        double[] lng = new double[6];
        mGeofenceList = new ArrayList<>();

        lat[0] = -3.095049;
        lng[0] = -59.995185;
        lat[1] = -3.094489;
        lng[1] = -59.995712;
        lat[2] = -3.096849;
        lng[2] = -59.992190;
        lat[3] = -3.092792;
        lng[3] = -59.997341;
        lat[4] = -3.093772;
        lng[4] = -59.993252;
        lat[5] = -3.015206;
        lng[5] = -59.958409;

        for (int i = 0; i < 6; i++) {
            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId("Teste [" + i + "]")
                    .setCircularRegion(
                            lat[i],
                            lng[i],
                            10
                    )
                    .setExpirationDuration(30000)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build()
            );
        }
    }

    private void setGoogleApiClient() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Google Api Client connected!");

        // setting geofences of placeholder points
        setGeofences();

        // specifying geofences triggers
        geofencingRequest = getGeofencingRequest();

        // defining an intent for geofence transitions
        pendingIntent = getGeofencePendingIntent();

        // adding geofences
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            Log.i(TAG, "Acces fine location permitted!");
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    geofencingRequest,
                    pendingIntent
            ).setResultCallback(this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Google Api Client suspended!");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Google Api Client connect failed!");
    }

    @Override
    public void onResult(@NonNull Status status) {
        // result of adding geofences
    }
}
