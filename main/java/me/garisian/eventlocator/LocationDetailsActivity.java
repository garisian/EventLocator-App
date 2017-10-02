package me.garisian.eventlocator;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * LocationDetailsActivity.java
 * Purpose: Display a map showing user's location, business' location, and the shortest path between
 *          the two
 *
 * @author Garisian Kana
 * @version 1.1
 *
 * Created on  2017-09-28
 */


public class LocationDetailsActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback
{
    // Debugging Purposes
    private String TAG = "LocationDetailsActivity";

    // Builder that contains the map constraints for the xzoom
    LatLngBounds.Builder builder = new LatLngBounds.Builder();

    // List containing used markers
    ArrayList<MarkerOptions> listOfMarkers = new ArrayList<MarkerOptions>();

    // The ratios for the popup screen relative to the phone dimenstion
    private double widthRatio = 0.8;
    private double heightRatio = 0.8;

    GoogleMap gm;

    /**
     * Description: Method gets called the moment app initiates. Load activity with toolbar,
     *              googlemaps, and all relevant buttons
     *
     * @param: Bundle
     *
     * @return none
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Link activity layout to a layout in xml file
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationdetail);

        // Get screen size in pizels so we can make a smaller popup screen
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width * widthRatio), (int)(height * heightRatio));

        // Link fragment code in xml to googlemaps and display the google map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.gm = googleMap;
        Bundle bundle = getIntent().getExtras();
        String userLongtitude= bundle.getString("userLongtitude");
        String userLatitude= bundle.getString("userLatitude");
        String locationLongtitude= bundle.getString("locationLongitude");
        String locationLatitude= bundle.getString("locationLatitude");

        putMarker(Double.parseDouble(userLatitude),Double.parseDouble(userLongtitude), googleMap, "Your Location");
        putMarker(Double.parseDouble(locationLongtitude),Double.parseDouble(locationLatitude), googleMap, "Business' Location");

        zoomIn(Double.parseDouble(userLatitude),Double.parseDouble(userLongtitude), googleMap);
    }

    /**
     * Description: Place a marker at given coordinates on the indicated map
     *
     * @param: "longitude" and "latitude" represent the coordinates of the location you want to mark
     *          "googleMap" is the GoogleMap instance
     *
     * @return none
     */
    public void putMarker(double longitude, double latitude, GoogleMap googleMap, String title)
    {
        LatLng toronto = new LatLng(longitude, latitude);
        MarkerOptions temp = new MarkerOptions().position(toronto)
                .title(title);
        listOfMarkers.add(temp);
        googleMap.addMarker(temp);
        googleMap.setOnMarkerClickListener(this);
    }

    /**
     * Description: Zoom into an area so both markers are visible
     *
     * @param: "longitude" and "latitude" represent the coordinates of the location you want to mark
     *          "googleMap" is the GoogleMap instance
     *
     * @return none
     */
    public void zoomIn(double longitde, double latitude, GoogleMap googleMap)
    {
/*        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(longitde, latitude)));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));*/
/*
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(longitde, latitude))      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
*/

        for (MarkerOptions marker : listOfMarkers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();


        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                // Move camera.
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(),150);
                gm.animateCamera(cu);
                // Remove listener to prevent position reset on camera move.
            }
        });


    }
}

