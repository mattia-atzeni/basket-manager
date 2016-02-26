package com.basket.basketmanager;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String mAddress;
    public static final String EXTRA_ADDRESS = MapsActivity.class.getPackage().getName() + ".EXTRA_ADDRESS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mAddress = getIntent().getStringExtra(EXTRA_ADDRESS);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Geocoder gc = new Geocoder(this, Locale.getDefault());

        if (Geocoder.isPresent()) {

            try {

                List<Address> addresses = gc.getFromLocationName(mAddress, 1);

                Address mapAddress = addresses.get(0);

                double lat = mapAddress.getLatitude();
                double lng = mapAddress.getLongitude();

                LatLng location = new LatLng(lat, lng);

                mMap.addMarker(new MarkerOptions().position(location).title("Qui si trova il tuo campo"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));

            } catch (IOException e) {

                e.printStackTrace();
            }

        }
    }
}
