package me.garisian.eventlocator;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

import me.garisian.utilities.Option;

public class OptionsActivity extends AppCompatActivity {

    MyCustomOptionsAdapter dataAdapter = null;
    String optionList = "";

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
        optionList.add(new Option("airport"));
        optionList.add(new Option("aquarium"));
        optionList.add(new Option("bank"));
        optionList.add(new Option("bus_station"));
        optionList.add(new Option("clothing_store"));
        optionList.add(new Option("convenience_store"));
        optionList.add(new Option("electronics_store"));
        optionList.add(new Option("fire_station"));
        optionList.add(new Option("gas_station"));
        optionList.add(new Option("gym"));
        optionList.add(new Option("hair_care"));
        optionList.add(new Option("hardware_store"));
        optionList.add(new Option("hindu_temple"));
        optionList.add(new Option("hospital"));
        optionList.add(new Option("jewelry_store"));
        optionList.add(new Option("lawyer"));
        optionList.add(new Option("library"));
        optionList.add(new Option("liquor_store"));
        optionList.add(new Option("movie_rental"));
        optionList.add(new Option("movie_theater"));
        optionList.add(new Option("museum"));
        optionList.add(new Option("night_club"));
        optionList.add(new Option("parking"));
        optionList.add(new Option("police"));
        optionList.add(new Option("post_office"));
        optionList.add(new Option("restaurant"));
        optionList.add(new Option("school"));
        optionList.add(new Option("subway_station"));
        optionList.add(new Option("university"));
        optionList.add(new Option("university"));
        optionList.add(new Option("zoo"));

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

    // Show a Toast popup message whenever a button is clicked
    private void checkButtonClick() {
        Button myButton = (Button) findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                optionList = "";
                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");
                ArrayList<Option> countryList = dataAdapter.settingList;
                for(int i=0;i<countryList.size();i++){
                    Option setting = countryList.get(i);
                    if(setting.isUsed()){
                        responseText.append("\n" + setting.getName());
                        if(optionList.equals(""))
                        {
                            optionList+=setting.getName();
                        }
                        else
                        {
                            optionList+="|"+setting.getName();
                        }
                    }
                }
                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();


                // Send userAddress from previous activity and selected options for result extraction
                Bundle bundle = getIntent().getExtras();
                String inputAddressString= bundle.getString("inputAddress");
                Bundle infoBundle = new Bundle();
                infoBundle.putString("inputAddress", inputAddressString);
                infoBundle.putString("options", optionList);

                Intent myIntent = new Intent(OptionsActivity.this, ResultsActivity.class);
                myIntent.putExtras(infoBundle);
                startActivity(myIntent);
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
