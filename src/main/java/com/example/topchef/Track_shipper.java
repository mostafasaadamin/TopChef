package com.example.topchef;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.valdesekamdem.library.mdtoast.MDToast;

public class Track_shipper extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private IntentFilter mIntentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_shipper);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.example.unknown.topchef");
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }
    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Double lat=intent.getDoubleExtra("lat",-1);
            Double lng=intent.getDoubleExtra("lng",-1);
            String shipper=intent.getStringExtra("shipper");
            if(lat==-1||lng==-1)
            {
            MDToast.makeText(context,"shipper does not open map",2000,MDToast.TYPE_WARNING).show();
            return;
            }
            String order_id=intent.getStringExtra("order_id");
            LatLng sydney = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(sydney).title(""+shipper));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 5.0f));
            mMap.setBuildingsEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

        }
    };
}
