package com.example.android.lab_6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity { //FragmentActivity   AppCompatActivity


    private final LatLng mDestinationLatLng = new LatLng(43.075464, -89.404336);
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient; // save the instance
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            // code to display marker
            googleMap.addMarker(new MarkerOptions().position(mDestinationLatLng).title("Destination"));
            displayMyLocation();
        });
        // obtain a fusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void displayMyLocation(){
        //check if permission granted
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        //if not, ask for it
        if(permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        //if permission granted, display marker at current location
        else {
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, task -> {
                Location mLastKnownLocation = task.getResult();
                LatLng lastKnown = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                if(task.isSuccessful() && mLastKnownLocation != null){
                    mMap.addPolyline(new PolylineOptions().add(
                        new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()),
                                mDestinationLatLng));

                    mMap.addMarker(new MarkerOptions().position(lastKnown).title("Current Location"));
                }
            });
        }
    }
}
