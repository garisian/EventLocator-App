package me.garisian.eventlocator;

import android.location.Address;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.zip.Inflater;

/**
 * CenterActivity.java
 * Purpose: Initial activity that dynamically updates content without the need of refreshing
 *          NOT USED AT THE MOMENT
 * @author Garisian Kana
 * @version 1.1
 *
 * Created on 2017-09-19
 */

public class CenterActivity extends AppCompatActivity
{
    // List of buttons to navigate through activities
    Button getLocation, getOptions, getResults;

    /**
     * Description: Method gets called the moment activity initiates. Load activity with toolbar,
     *              and display every setting option with a button for user to select
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
        setContentView(R.layout.activity_center);

        // Set the toolbar with options for settings etc at top of activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activateButtons();
    }

    /**
     * Description: Map buttons with corresponding IDs for action retrieval and manipulation
     *
     * @param: none
     *
     * @return none
     */
    private void activateButtons()
    {
        // Map button for getting user's location
        getLocation = (Button) findViewById(R.id.addressButton);
        getOptions = (Button) findViewById(R.id.optionButtons);
        getResults = (Button) findViewById(R.id.resultsButton);

        // Add listeners
        getLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getApplicationContext());
                View variable = li.inflate(R.layout.activity_address,null,false);
                ViewGroup inclusionViewGroup = (ViewGroup)findViewById(R.id.updatableContent);
                inclusionViewGroup.addView(variable);
                //ViewGroup inclusionViewGroup = (ViewGroup)findViewById(R.id.updatableContent);
                //View child1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_addres);
                //inclusionViewGroup.addView(child1);
            }
        });
    }


}
