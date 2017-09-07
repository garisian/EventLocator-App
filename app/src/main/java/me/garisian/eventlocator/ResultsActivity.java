package me.garisian.eventlocator;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import me.garisian.utilities.WebsiteParsing;

public class ResultsActivity extends AppCompatActivity {

    static String TAG = "ResultsActivity";
    private String userAddress;
    static String locationLatitude = "51.503186";
    static String locationLongtitude = "-0.126446";
    static String type = "restaurant";
    static int radius = 500;


    public void getUserAddress()
    {
        Bundle bundle = getIntent().getExtras();
        String text= bundle.getString("inputAddress");
        Log.i(TAG, text);
        userAddress = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Extract data from a fixed address
        getRequestData();

        // Print User input from main activity
        // ------ getUserAddress();
    }

    public void getRequestData()
    {
        String requestURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"+
                                    "location="+locationLatitude+","+locationLongtitude+
                                    "&type="+type+
                                    "&radius="+radius+
                                    "&key=AIzaSyBfxw5cINgN7q89t-HGIsnsb6lRUDU8rjQ";

        WebsiteParsing s = new WebsiteParsing(requestURL);
        s.loadWebsiteContents();

        // Read content
        try
        {
            BufferedReader test = s.getReader();

            String line;
            while((line=test.readLine())!= null)
            {
                System.out.println(line);
            }
            test.close();
        }
        catch(Exception e)
        {
            System.out.println("YOU hsouldn't have come here");
        }

    }

}


