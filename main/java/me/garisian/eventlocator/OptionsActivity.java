package me.garisian.eventlocator;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import me.garisian.utilities.Option;

/**
 * OptionsActivity.java
 * Purpose: Display all options and transfer address and options to results activity
 *
 * @author Garisian Kana
 * @version 1.1
 *
 * Created on 2017-09-01
 */
public class OptionsActivity extends AppCompatActivity
{
    // Used for Debugging purposes
    private String TAG = "OptionsActivity";

    // Created to customize settings display
    MyCustomOptionsAdapter dataAdapter = null;
    String optionList = "";

    // Mapped display strings to google query strings
    Map<String,String> optionsMapping =  new HashMap<String,String>();

    /**
     * Description: Method gets called the moment activity initiates. Load activity with toolbar,
     *              and display every option option with a button for user to select
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
        setContentView(R.layout.activity_options);

        // Set the toolbar with options for settings etc at top of activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Display the option items and wait for user action
        displayListView();
        checkButtonClick();
    }

    /**
     * Description: Load settings, display them, and wait for user to click on it. Options are
     *              extracted from assets/OptionList.txt
     *
     * @param: none
     *
     * @return none
     */
    private void displayListView()
    {
        //Array list of Option
        ArrayList<Option> optionList = new ArrayList<Option>();
        populateOptions(optionList);

        // create an ArrayAdaptar from the String Array. Recycles old views instead of creating 1
        // for each view and saving
        dataAdapter = new MyCustomOptionsAdapter(this, R.layout.settings_info , optionList);
        ListView listView = (ListView) findViewById(R.id.listOfOptions);

        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        // Deal with Item Clicks
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // When clicked, show a toast with the TextView text
                Option country = (Option) parent.getItemAtPosition(position);
                // Debugging Purposes. Please Work
                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + country.getName(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Description: Go through every option in the external file and create an Option object and
     *              save it in an arraylist
     *
     * @param: "optionList" is the list the options are stored
     *
     * @return none
     */
    private void populateOptions(ArrayList<Option> optionList)
    {
        try
        {
            // Open up file from assets and store data inside bufferedreader
            AssetManager am = getAssets();
            InputStream is = am.open("OptionsList.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // Iterate through each line and create a new Option and store the data
            String line;
            while ((line = br.readLine()) != null) {
                String[] elements = line.split("=");
                optionList.add(new Option(elements[0]));
                optionsMapping.put(elements[0], elements[1]);
            }
        }
        catch(FileNotFoundException e)
        {
            Log.v(TAG, "FILENOTFOUND ERROR");
            Log.v(TAG, System.getProperty("user.dir"));
        }
        catch(IOException e)
        {
            Log.v(TAG, "IOEXCEPTION ERROR");
            Log.v(TAG, e.toString());
        }
        catch(Exception e)
        {
            Log.v(TAG, "Everything just failed");
            Log.v(TAG, e.toString());
        }
    }

    /**
     * Description: Show a Toast popup message the final button is clicked. Indicate which options
     *              were selected and pass that data + user's address onto results page for POST
     *              request
     *
     * @param: none
     *
     * @return none
     */
    private void checkButtonClick()
    {
        Button myButton = (Button) findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Reset optionList everytime in case user jumps back and forth between activities
                optionList = "";

                // Go through the list of options and gather selected options to pass onto results
                // activity
                ArrayList<Option> countryList = dataAdapter.settingList;
                for(int i=0;i<countryList.size();i++){
                    Option setting = countryList.get(i);
                    if(setting.isUsed())
                    {
                        if(optionList.equals(""))
                        {
                            optionList+=optionsMapping.get(setting.getName()).trim();
                            Log.v("OPTIONSACTIVITY", optionList);
                        }
                        else
                        {
                            optionList+="|"+optionsMapping.get(setting.getName()).trim();
                            Log.v("OPTIONSACTIVITY", optionList);
                        }
                    }
                }

                // Send userAddress from previous activity and selected options for result extraction
                Bundle bundle = getIntent().getExtras();
                String inputAddressString= bundle.getString("inputAddress");
                Bundle infoBundle = new Bundle();
                infoBundle.putString("inputAddress", inputAddressString);
                infoBundle.putString("options", optionList);

                // Make sure at least one option is selected or google POST request returns fail
                if(optionList == "")
                {
                    Toast.makeText(getApplicationContext(), "Select at least one option.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent myIntent = new Intent(OptionsActivity.this, ResultsActivity.class);
                    myIntent.putExtras(infoBundle);
                    startActivity(myIntent);
                }
            }
        });
    }

    /**
     * MyCustomOptionsAdapter.java
     *
     * Purpose: Custom Adapter to display settings on activity
     *
     */
    private class MyCustomOptionsAdapter extends ArrayAdapter<Option>
    {
        private ArrayList<Option> settingList;

        public MyCustomOptionsAdapter(Context context, int textViewResourceId, ArrayList<Option> settingList)
        {
            super(context, textViewResourceId, settingList);
            this.settingList = new ArrayList<Option>();
            this.settingList.addAll(settingList);
        }

        // A View class that represents the data for a single setting
        private class ViewHolder {
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
            if (convertView == null) {
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
                        Option country = (Option) cb.getTag();
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        country.setTrigger(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            // Set up what will be displayed for each option
            Option country = settingList.get(position);
            holder.name.setText(country.getName());
            holder.name.setChecked(country.isUsed());
            holder.name.setTag(country);

            return convertView;
        }
    }

}
