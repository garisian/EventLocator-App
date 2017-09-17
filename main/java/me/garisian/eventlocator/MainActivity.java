package me.garisian.eventlocator;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import me.garisian.utilities.GPSTracker;


/**
 * MainActivity.java
 * Purpose: Initial activity for the app. Display a google map and textbox for user to type an
 *          address to base results off.
 *
 * @author Garisian Kana
 * @version 1.1
 *
 * Created on 2017-08-21
 */
public class MainActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    // Time interval for Splash Page
    private static int SPLASH_TIME_OUT = 4000;

    // Used for Debugging purposes
    static String TAG = "MainActivity";

    // Textbox which stores user's input address
    private EditText inputData;

    // Holder for GoogleMap object for future manipulation
    GoogleMap googleMap;

    // To get data for current Location
    private LocationManager locationManager;
    private LocationListener locationListener;
    Button btnGetLoc;

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
        setContentView(R.layout.activity_main);

        // Set the toolbar with options for settings etc at top of activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Map button for getting user's location
        btnGetLoc = (Button) findViewById(R.id.btnGetLoc);

        // Request permission from user
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION},123);

        // Extract user's current coordinates and display on googlemap and update Textbox with
        // respective address
        btnGetLoc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                // Create GPS object and extract the location
                GPSTracker g = new GPSTracker(getApplicationContext());
                Location l = g.getLocation();

                // Certain locations are prohibited by googlemaps ex: military bases etc
                if(l != null)
                {
                    // If location is populated, it will have all the information regarding the lcoation
                    double lat = l.getLatitude();
                    double lng = l.getLongitude();
                    Toast.makeText(getApplicationContext(), "LAT: "+lat+"\n LNG:" +lng,Toast.LENGTH_LONG).show();

                    // Display the location on googlemaps
                    putMarker(lat,lng,googleMap);

                    // Use longitude and latitude to get the closest address and display it
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try
                    {
                        addresses = geocoder.getFromLocation(lat, lng, 1);
                        String address = addresses.get(0).getAddressLine(0);
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        //String postalCode = addresses.get(0).getPostalCode();
                        //String knownName = addresses.get(0).getFeatureName();
                        String fullAddress = address+", "+city+", "+state+", "+country;
                        EditText myTextBox = (EditText) findViewById(R.id.DataInput);
                        myTextBox.setText(fullAddress);
                    }
                    catch(IOException e){}
                }
            }
        });

        // On buttonclick, extract the relevent address and pass the data to options activity
        Button btnDoSomething = (Button) (findViewById(R.id.btnDoSomething));
        btnDoSomething.setOnClickListener(new View.OnClickListener() {
            public void onClick(View irrelevant) {
                // Extract address displayed on textbox
                inputData = (EditText) findViewById(R.id.DataInput);
                String inputAddressString = inputData.getText().toString();

                // Create a bundle to pass in data to options activity
                Bundle infoBundle = new Bundle();
                Intent myIntent = new Intent(MainActivity.this, OptionsActivity.class);
                infoBundle.putString("inputAddress", inputAddressString);
                myIntent.putExtras(infoBundle);
                startActivity(myIntent);
            }
        });

        // Link fragment code in xml to googlemaps and display the google map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Description: Dynamically extract elements to put put in the extras menu top right of app
     *
     * @param: menu item which gets populated
     *
     * @return true;
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Description: Handles action bar item clicks here. Start a new activity intent whenever
     *              an option from the menu is clicked
     *
     * @param: Menu item which gets selected
     *
     * @return true;
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        // Based on the id of the clicked item, start the relevant intent
        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Description: Save GoogleMap instance only after map is loaded and ready to be modified.
     *
     * @param: GoogleMap object that represents the map displayed on Main Activity
     *
     * @return true;
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
    }

    /**
     * Description: Place a marker at given coordinates on the indicated map
     *
     * @param: "longitude" and "latitude" represent the coordinates of the location you want to mark
     *          "googleMap" is the GoogleMap instance
     *
     * @return none
     */
    public void putMarker(double longitude, double latitude, GoogleMap googleMap)
    {
        LatLng toronto = new LatLng(longitude, latitude);
        googleMap.addMarker(new MarkerOptions().position(toronto)
                .title("Marker in Toronto"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(toronto));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        googleMap.setOnMarkerClickListener(this);
    }

    /**
     * Description: On marker click display a message showing the marker's title
     *
     * @param: Marker instance that got selected by user
     *
     * @return none
     */
    @Override
    public boolean onMarkerClick(Marker marker)
    {
        // Retrieve the data from the marker and display popup message
        Toast.makeText(this, marker.getTitle() + " has been clicked.",
                Toast.LENGTH_SHORT).show();


        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
}