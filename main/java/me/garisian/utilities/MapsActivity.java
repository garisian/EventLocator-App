package me.garisian.utilities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import me.garisian.eventlocator.R;

/**
 * MapActivity.java
 * Purpose: Display googlemaps and tags whenever appropriate
 *
 * @author Garisian Kana
 * @version 1.0
 *
 * Created on 2017-09-10
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMarkerClickListener
{
    // GoogleMap variable stored for future manipulation in MainActivity
    private GoogleMap mMap;

    /**
     * Description: Overwritten method that gets run upon instantiation
     *
     * @param: Bundle which is used to set the foundation so we can link Fragments in xml file
     *         with or google maps.
     *
     * @return none
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Description: Overwritten method that gets called once onCreate is run and map is ready.
     *              By default display a marker in a fixed part of Toronto and zoom.
     *
     * @param: "GoogleMap" instance that is used to manipulate the map and add other features
     *
     * @return none
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    /**
     * Description: Place a marker at given coordinates on the indicated map
     *
     * @param: "longitude" and "latitude" represent the coordinates of the location you want to mark
     *          "googleMap" is the GoogleMap instance
     *
     * @return none
     */
    public void putMarker(double longitude, double latitude, GoogleMap googleMap) {
        LatLng toronto = new LatLng(longitude, latitude);
        googleMap.addMarker(new MarkerOptions().position(toronto)
                .title("Marker in Toronto"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(toronto));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        googleMap.setOnMarkerClickListener(this);
    }

    /**
     * Description: Methods that required implementation upon OnMarkerClickListener implements
     */
    @Override
    public boolean onMarkerClick(Marker marker)
    {
        return false;
    }
}