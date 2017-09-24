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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
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
    CustomGridAdapter dataAdapter = null;
    String optionList = "";

    // Mapped display strings to google query strings
    Map<String,String> optionsMapping =  new HashMap<String,String>();

    // Keep Track of What User Selected
    final Map<String,String> selectedList = new HashMap<String, String>();

    /**
     * Description: Method to remove animations from clicking back inbetween activities
     *
     * @param: Bundle
     *
     * @return none
     */
    @Override
    public void onPause()
    {
        super.onPause();
        overridePendingTransition(0, 0);
    }

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

        // Hide the default title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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

        // Getting a reference to gridview of MainActivity
        GridView gridView = (GridView) findViewById(R.id.myGridView);
        CustomGridAdapter gridAdapter = new CustomGridAdapter(OptionsActivity.this, optionList);

        // Setting an adapter containing images to the gridview
        gridView.setAdapter(gridAdapter);
    }


    public class CustomGridAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<Option> items;
        LayoutInflater inflater;
        int position;
        Button button;

        public CustomGridAdapter(Context context, ArrayList<Option> items) {
            this.context = context;
            this.items = items;
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {

            if (convertView == null) {
                inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.options_gridview, null);
            }
            this.position = position;
            Button button = (Button) convertView.findViewById(R.id.grid_item);
            button.setText(items.get(position).getName());


            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Button cb = (Button) v ;
                    Option country = (Option) cb.getTag();
/*
                    Toast.makeText(getApplicationContext(),
                            "Clicked on mooooooooo: " + cb.getText() ,
                            Toast.LENGTH_LONG).show();
*/
                    // When clicked, show a toast with the TextView text
                    Option singleOption = (Option) items.get(position);
                    // Debugging Purposes. Please Work
/*                    Toast.makeText(getApplicationContext(),
                            "Clicked on Row: " + singleOption.getName(),
                            Toast.LENGTH_LONG).show();*/


                    items.get(position).setTrigger(items.get(position).isUsed()? false:true);
                    // Update selected list to show user
                    if(items.get(position).isUsed())
                    {
                        selectedList.put(items.get(position).getName(),"");
                        v.setBackgroundResource(R.drawable.option_chosen_button);
                    }
                    else
                    {
                        selectedList.remove(items.get(position).getName());
                        v.setBackgroundResource(R.drawable.option_not_chosen_button);
                    }

                    //updateSelected();
                }
            });
            Log.i("LALALALA -- INSIDE","----------------------------");
            Log.i("LALALALA -- INSIDE",items.get(position).getName());
            Log.i("LALALALA -- INSIDE",Boolean.toString(items.get(position).isUsed()));
            Log.i("LALALALA -- INSIDE","----------------------------");
            if(items.get(position).isUsed())
            {
                selectedList.put(items.get(position).getName(),"");
                button.setBackgroundResource(R.drawable.option_chosen_button);
            }
            else
            {
                selectedList.remove(items.get(position).getName());
                button.setBackgroundResource(R.drawable.option_not_chosen_button);
            }

            return convertView;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
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
                for ( String key : selectedList.keySet() )
                {
                    if(optionList.equals(""))
                    {
                        optionList+=optionsMapping.get(key).trim();
                        Log.v("OPTIONSACTIVITY", key);
                        Log.v("OPTIONSACTIVITY", key.trim());
                    }
                    else
                    {
                        optionList+="|"+optionsMapping.get(key).trim();
                        Log.v("OPTIONSACTIVITY", optionList);
                        Log.v("OPTIONSACTIVITY", key);
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
                    overridePendingTransition(0, 0);
                }
            }
        });

    }
}
