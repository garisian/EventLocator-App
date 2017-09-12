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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.garisian.utilities.Option;

public class OptionsActivity extends AppCompatActivity {

    MyCustomOptionsAdapter dataAdapter = null;
    String optionList = "";
    Map<String,String> optionsMapping =  new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Display the option items and wait for user action
        displayListView();
        checkButtonClick();
    }

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

    private void populateOptions(ArrayList<Option> optionList)
    {
        try
        {
            AssetManager am = getAssets();
            InputStream is = am.open("OptionsList.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                String[] elements = line.split("=");
                optionList.add(new Option(elements[0]));
                optionsMapping.put(elements[0], elements[1]);
            }
        }
        catch(FileNotFoundException e)
        {
            Log.v("OPTIONSACTIVITY ", "FILENOTFOUND ERROR");
            Log.v("OPTIONSACTIVITY", System.getProperty("user.dir"));
        }
        catch(IOException e)
        {
            Log.v("OPTIONSACTIVITY", "IOEXCEPTION ERROR");
            Log.v("OPTIONSACTIVITY", e.toString());
        }
        catch(Exception e)
        {}
    }

    // Show a Toast popup message whenever a button is clicked
    private void checkButtonClick() {
        Button myButton = (Button) findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                optionList = "";
                //StringBuffer responseText = new StringBuffer();
                //responseText.append("The following were selected...\n");
                ArrayList<Option> countryList = dataAdapter.settingList;
                for(int i=0;i<countryList.size();i++){
                    Option setting = countryList.get(i);
                    if(setting.isUsed()){
                        //responseText.append("\n" + setting.getName());
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
               // Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();


                // Send userAddress from previous activity and selected options for result extraction
                Bundle bundle = getIntent().getExtras();
                String inputAddressString= bundle.getString("inputAddress");
                Bundle infoBundle = new Bundle();
                infoBundle.putString("inputAddress", inputAddressString);
                infoBundle.putString("options", optionList);

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

    private class MyCustomOptionsAdapter extends ArrayAdapter<Option>
    {

        private ArrayList<Option> settingList;

        public MyCustomOptionsAdapter(Context context, int textViewResourceId, ArrayList<Option> settingList)
        {
            super(context, textViewResourceId, settingList);
            this.settingList = new ArrayList<Option>();
            this.settingList.addAll(settingList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.settings_info , null);
                //R.layout.country_info
                //R.xml.settings_info
                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

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

            Option country = settingList.get(position);
            holder.name.setText(country.getName());
            holder.name.setChecked(country.isUsed());
            holder.name.setTag(country);

            return convertView;
        }
    }

}
