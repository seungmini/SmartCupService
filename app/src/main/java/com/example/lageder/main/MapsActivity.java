package com.example.lageder.main;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import datas.SCSDBManager;
import datas.doublePair;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        SCSDBManager db_manager = new SCSDBManager(this, "abc12345.db", null, 1);
        ArrayList<doublePair> arr = db_manager.getLocation();
        Log.e("ABCDEFG","1");
        if(arr.size() != 0) {
            Log.e("ABCDEFG","2");
            int i = 1;
            for(doublePair temp : arr) {
                LatLng dummy = new LatLng(temp.getA(),temp.getB());
                mMap.addMarker(new MarkerOptions().position(dummy).title((i)+"번째 위치"));
                if(Double.compare(dummy.longitude,0.0) != 0 &&
                        Double.compare(dummy.latitude,0.0) != 0 &&
                        i == 1) mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dummy, 15));
                i++;
            }
        }
        else {
            Log.e("ABCDEFG","3");
            // Add a marker in Sydney and move the camera
            LatLng home = new LatLng(35.2130944, 129.0994075999995);
            mMap.addMarker(new MarkerOptions().position(home).title("Home"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, 16));
        }
    }
}
