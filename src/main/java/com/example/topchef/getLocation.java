package com.example.topchef;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;


import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class getLocation extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final int REQUEST_INTERNET = 200;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;

    public getLocation() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        orderLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void sendBroadCast(Double lat,Double lng,String Address)
    {
    Intent broadcastIntent = new Intent();
    broadcastIntent.setAction("com.example.unknown.topchef.UserPanel");
    broadcastIntent.putExtra("lat", lat);
    broadcastIntent.putExtra("lng", lng);
    broadcastIntent.putExtra("Address", Address);
    sendBroadcast(broadcastIntent);
}
    private void orderLocation() {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
                createLocationRequest();
            mGoogleApiClient.connect();
    }
    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else
        {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            String Address=getAddress(currentLatitude,currentLongitude);
            sendBroadCast(currentLatitude,currentLongitude,Address);
        }

    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        String Address=getAddress(currentLatitude,currentLongitude);
        sendBroadCast(currentLatitude,currentLongitude,Address);


    }

    private String getAddress(double currentLatitude, double currentLongitude) {

        String addres_Str = "unavailable";
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            addres_Str = country.concat("/").concat(state).concat("/").concat(city);
            if (knownName != null) {
                addres_Str.concat("(").concat(knownName).concat(")");
            }
            return addres_Str;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addres_Str;
    }
}
