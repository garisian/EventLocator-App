package me.garisian.utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * GPSTracker.java
 * Purpose: Extract the user's current location using device's GPS and request permission where
 *          needed.
 *
 * @author Garisian Kana
 * @version 1.1
 *
 * Created on 2017-09-13
 */
public class GPSTracker implements LocationListener
{

    private Context context;

    public GPSTracker(Context context)
    {
        this.context = context;
    }

    /**
     * Description: Check permissions with user and if permitted enable device GPS and extract
     *              current user's location stored in a "Location" variable
     *
     * @param: None
     *
     *
     * @return Location instance containing all relevant location information
     */
    public Location getLocation()
    {
        // Check if permissions are granted
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(context, "Permission not granted.", Toast.LENGTH_SHORT).show();
            return null;
        }

        // System Service for GPS. Check if GPS is enabled
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Request location updates for this class every 6 secs or within 10m movement
        if(isGPSEnabled)
        {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10, this);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;
        }

        // This point is reached only if GPS is not enabled. Request user to do so
        Toast.makeText(context, "Please enable GPS.", Toast.LENGTH_LONG).show();
        return null;
    }

    /**
     * Description: Methods that required implementation upon LocationListener implements
     */
    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}
