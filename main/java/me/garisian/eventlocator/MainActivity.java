/*
* This file has the logic of the application.
* Everything from setting up the view and adding a basic menu, clicking a button,
* pulling things off the internet.
* Logic code that drives the application.
* */
package me.garisian.eventlocator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

// Used for popup to test user Input
import android.view.Gravity;
import android.widget.Toast;

// Used for google maps api
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import me.garisian.utilities.MapsActivity;

public class MainActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    static String TAG = "MainActivity";
    private EditText inputData;

    // To get data for current Location
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.i(TAG, "Application is running, yay!");
        Button btnDoSomething = (Button) (findViewById(R.id.btnDoSomething));
        btnDoSomething.setOnClickListener(new View.OnClickListener() {
            public void onClick(View irrelevant) {
                // Start NewActivity.class
                    /*
                    Intent myIntent = new Intent(MainActivity.this, ResultsActivity.class);
                  inputData = (EditText) findViewById(R.id.DataInput);
                  String inputAddressString = inputData.getText().toString();
                  */
                Intent myIntent = new Intent(MainActivity.this, OptionsActivity.class);
                inputData = (EditText) findViewById(R.id.DataInput);
                String inputAddressString = inputData.getText().toString();

                // Create a bundle to pass in data to results activity
                Bundle infoBundle = new Bundle();
                infoBundle.putString("inputAddress", inputAddressString);
                myIntent.putExtras(infoBundle);
                startActivity(myIntent);

                // Old code to show toast message of user input
                  /*
                      inputData = (EditText) findViewById(R.id.DataInput);
                      Toast toast = new Toast(getApplicationContext());
                      toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                      toast.makeText(MainActivity.this, inputData.getText(), toast.LENGTH_SHORT).show();
                  */
            }
        });
        //new MapsActivity();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.


       getCurrentLocation(googleMap);

    }

    public void getCurrentLocation(final GoogleMap googleMap) {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                putMarker(location.getLatitude(), location.getLongitude(), googleMap);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request for permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                }, 10);
            }
            return;
        } else {
            updateLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    updateLocation();
                return;
        }
    }

    public void putMarker(double longitude, double latitude, GoogleMap googleMap) {
        LatLng toronto = new LatLng(longitude, latitude);
        googleMap.addMarker(new MarkerOptions().position(toronto)
                .title("Marker in Toronto"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(toronto));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        googleMap.setOnMarkerClickListener(this);
    }

    public void updateLocation() {

        locationManager.requestLocationUpdates("gps", 10000, 10, locationListener);
    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        // Retrieve the data from the marker.
        Toast.makeText(this, marker.getTitle() + " has been clicked.",
                Toast.LENGTH_SHORT).show();


        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }


}