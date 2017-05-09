package com.apps.stenofh.geofencesexample;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    /***
     *
     * placeholders points
     * -3.095049, -59.995185
     * -3.094489, -59.995712
     * -3.096849, -59.992190
     * -3.092792, -59.997341
     * -3.093772, -59.993252
     *
     * */

    private GoogleApiClient mGoogleApiClient;
    private List<Geofence> mGeofenceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setting google api services
        setGoogleApiClient();

        // setting geofences of placeholder points
        setGeofences();

        // specifying geofences triggers
        GeofencingRequest geofencingRequest = getGeofencingRequest();

    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private void setGeofences() {
        double[] lat = new double[5];
        double[] lng = new double[5];
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

        for (int i = 0; i < 5; i++) {
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

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
