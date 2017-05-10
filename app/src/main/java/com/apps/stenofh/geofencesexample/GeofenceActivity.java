package com.apps.stenofh.geofencesexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by leo on 10/05/17.
 */

public class GeofenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence);

        String geofencesDetails = getIntent().getStringExtra("geofenceTransitionDetails");

        TextView textViewGeofence = (TextView) findViewById(R.id.textViewGeofence);
        textViewGeofence.setText(geofencesDetails);
    }
}
