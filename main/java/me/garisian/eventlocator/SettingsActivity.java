package me.garisian.eventlocator;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import me.garisian.utilities.Setting;


/**
 * SettingsActivity.java
 * Purpose: Display all settings
 *
 * @author Garisian Kana
 * @version 1.1
 *
 * Created on 2017-08-22
 */
public class SettingsActivity extends AppCompatActivity
{
    // Created to customize settings display
    MyCustomAdapter dataAdapter = null;

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
        setContentView(R.layout.activity_settings);

        // Set the toolbar with options for settings etc at top of activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Display the option items and wait for user action
        displayListView();
        checkButtonClick();
    }

    /**
     * Description: Load settings, display them, and wait for user to click on it
     *
     * @param: none
     *
     * @return none
     */
    private void displayListView()
    {
        //Array list of settings
        ArrayList<Setting> settingList = new ArrayList<Setting>();
        settingList.add(new Setting("Setting 1"));
        settingList.add(new Setting("Setting 2"));
        settingList.add(new Setting("Setting 3"));
        settingList.add(new Setting("Setting 4"));
        settingList.add(new Setting("Setting 5"));
        settingList.add(new Setting("Setting 6"));
        settingList.add(new Setting("Setting 7"));
        settingList.add(new Setting("Setting 8"));
        settingList.add(new Setting("Setting 9"));
        settingList.add(new Setting("Setting 10"));
        settingList.add(new Setting("Setting 11"));
        settingList.add(new Setting("Setting 12"));
        settingList.add(new Setting("Setting 13"));
        settingList.add(new Setting("Setting 14"));
        settingList.add(new Setting("Setting 15"));

        // Create an ArrayAdaptar from the String Array. Recycles old views instead of creating 1
        // for each view and saving
        dataAdapter = new MyCustomAdapter(this, R.layout.settings_info , settingList);
        ListView listView = (ListView) findViewById(R.id.listOfSettings);

        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        // Deal with Item Clicks
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                // When clicked, show a toast with the TextView text
                Setting country = (Setting) parent.getItemAtPosition(position);

                // Debugging Purposes. Please Work
                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + country.getName(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * MyCustomAdapter.java
     *
     * Purpose: Custom Adapter to display settings on activity
     *
     */
    private class MyCustomAdapter extends ArrayAdapter<Setting>
    {

        private ArrayList<Setting> settingList;

        public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Setting> settingList)
        {
            super(context, textViewResourceId, settingList);
            this.settingList = new ArrayList<Setting>();
            this.settingList.addAll(settingList);
        }

        // A View class that represents the data for a single setting
        private class ViewHolder
        {
            TextView code;
            CheckBox name;
        }

        /**
         * Description: Extract data and assign it to a view and wait for user action.
         *              Get a View that displays the data at the specified position in the data set
         *
         * @param: "position" is the position of the item within the adapter's data
         *         "convertView" is the old view to reuse, if possible.
         *         "parent" is the parent that this view will eventually be attached to
         *
         * @return "View" object corresponding to the data at the specified position
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            // Extract data and assign it to a view and wait for user action.
            if (convertView == null)
            {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.settings_info , null);

                // Create new vew and populate with data
                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                // Wait for user action and show popup message once clicked
                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Setting country = (Setting) cb.getTag();
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        country.setTrigger(cb.isChecked());
                    }
                });
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            // Set up what will be displayed for each setting
            Setting country = settingList.get(position);
            holder.name.setText(country.getName());
            holder.name.setChecked(country.isUsed());
            holder.name.setTag(country);

            return convertView;
        }
    }

    /**
     * Description: Show a Toast popup message the final button is clicked. Indicate which settings
     *              were selected
     *
     * @param: none
     *
     * @return none
     */
    private void checkButtonClick() {
        // Add a listener to the top button
        Button myButton = (Button) findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Go through every setting and collect the selected ones and toast the message
                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                ArrayList<Setting> countryList = dataAdapter.settingList;
                for(int i=0;i<countryList.size();i++){
                    Setting setting = countryList.get(i);
                    if(setting.isUsed()){
                        responseText.append("\n" + setting.getName());
                    }
                }
                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();
            }
        });
    }
}
