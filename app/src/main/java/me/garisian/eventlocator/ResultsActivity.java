package me.garisian.eventlocator;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONObject;


import me.garisian.utilities.GoogleLocation;
import me.garisian.utilities.WebsiteParsing;

public class ResultsActivity extends AppCompatActivity {

    private String TAG = "ResultsActivity";

    // Variables used for the POST call wia google locations api
    private String userAddress;
    private String userOptions;
    private String locationLatitude;
    private String locationLongtitude;
    private String type = "restaurant";
    private int radius = 500;

    ArrayList<GoogleLocation> googlePlaceList = new ArrayList<GoogleLocation>();
    MyCustomResultsAdapter dataAdapter = null;

    private String dataString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Print User input from main activity
        getUserInput();
        // start the AsyncTask Call since post call needs to be in unique thread
        new GetPlaces().execute();

    }

    private class GetPlaces extends AsyncTask
    {
        private ProgressDialog dialog = new ProgressDialog(ResultsActivity.this);

        @Override
        protected void onPreExecute()
        {
            // nothing for now
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params)
        {
            getCoordinates();
            getRequestData();
            return null;
        }

        @Override
        protected void onPostExecute(Object result)
        {
            // Data should be extracted at this point. Display the data on the activity panel
            /*
            for(GoogleLocation item: googlePlaceList)
            {
                System.out.println(item.currentlyOpenNow());
                System.out.println(item.getName());
                System.out.println(item.getRating());
                System.out.println("------------------------------");
            }
            */

            // create an ArrayAdaptar from the String Array. Recycles old views instead of creating 1
            // for each view and saving
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
            displayResults();
            return;
        }

        public void getCoordinates()
        {
            String scriptAddress = userAddress.replaceAll(" ", "+").toLowerCase();

            String requestURL = "https://maps.googleapis.com/maps/api/geocode/json?"+
                    "address="+scriptAddress+
                    "&key=AIzaSyBfxw5cINgN7q89t-HGIsnsb6lRUDU8rjQ";
            WebsiteParsing s = new WebsiteParsing(requestURL);
            s.loadWebsiteContents();

            try {
                BufferedReader dataPipe = s.getReader();
                //printData(dataPipe);

                // Convert bufferedreader to a JSON Object for easy Object Extraction
                String line;
                String combined = "";
                while((line=dataPipe.readLine())!= null)
                {
                    combined += line;
                }
                dataPipe.close();

                JSONObject jsonObj = new JSONObject(combined);
                JSONObject json = jsonObj.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                locationLatitude = json.get("lat").toString();
                locationLongtitude = json.get("lng").toString();

                //Log.i("HELLOOOOOOOOOOOOOOOOOOO",json.get("lat").toString());
                //Log.i("HELLOOOOOOOOOOOOOOOOOOO",json.get("lng").toString());
            }
            catch(IOException e)
            {}
            catch(Exception e)
            {}

        }

        public void getRequestData()
        {
            String requestURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"+
                    "location="+locationLatitude+","+locationLongtitude+
                    "&type="+userOptions+
                    "&radius="+radius+
                    "&key=AIzaSyBfxw5cINgN7q89t-HGIsnsb6lRUDU8rjQ";

            WebsiteParsing s = new WebsiteParsing(requestURL);
            s.loadWebsiteContents();

            // Read content
            try
            {
                BufferedReader dataPipe = s.getReader();
                //printData(dataPipe);

                // Convert bufferedreader to a JSON Object for easy Object Extraction
                dataString = convertToString(dataPipe);
                JSONObject jsonObj = new JSONObject(dataString);
                //Log.i("THIS BETTER WORK", jsonObj.get("results").toString());
                JSONArray jsonArray = jsonObj.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    GoogleLocation place = new GoogleLocation();
                    if(jsonArray.getJSONObject(i).has("name"))
                    {
                        place.setName(jsonArray.getJSONObject(i).optString("name"));
                        place.setRating(jsonArray.getJSONObject(i).optString("rating", " "));

                        if (jsonArray.getJSONObject(i).has("opening_hours"))
                        {
                            if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now"))
                            {
                                if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true"))
                                {
                                    place.setOpenNow("YES");
                                }
                                else {
                                    place.setOpenNow("NO");
                                }
                            }
                        }
                        else {
                            place.setOpenNow("Not Known");
                        }
                    }
                    googlePlaceList.add(place);
                }
            }
            catch(IOException e)
            {
                System.out.println("Something terribly went wrong. Results Activity: IOException caught");
                System.out.println(e);
            }
            catch(JSONException e)
            {
                System.out.println("Something terribly went wrong. Results Activity: JSONException caught");
            }
            catch(Exception e)
            {
                System.out.println("Something terribly went wrong. Results Activity: Exception caught");
                System.out.println(e);
            }
        }

        public String convertToString(BufferedReader dataPipe) throws IOException
        {
            String line;
            String combined = "";
            while((line=dataPipe.readLine())!= null)
            {
                combined += line;
            }
            dataPipe.close();
            return combined;
        }

        public void printData(BufferedReader dataPipe) throws IOException
        {
            String line;
            while((line=dataPipe.readLine())!= null)
            {
                Log.i(TAG, line);
            }
            dataPipe.close();
        }
    }

    private class MyCustomResultsAdapter extends ArrayAdapter<GoogleLocation>
    {

        private ArrayList<GoogleLocation> resultsList;

        public MyCustomResultsAdapter(Context context, int textViewResourceId, ArrayList<GoogleLocation> settingList)
        {
            super(context, textViewResourceId, settingList);
            this.resultsList = new ArrayList<GoogleLocation>();
            this.resultsList.addAll(settingList);
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
                        GoogleLocation country = (GoogleLocation) cb.getTag();
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        // country.setTrigger(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            GoogleLocation location = resultsList.get(position);
            holder.name.setText(location.getName());
            //holder.name.setChecked(country.isUsed());
            holder.name.setTag(location);

            return convertView;
        }
    }

    public void displayResults()
    {
        dataAdapter = new MyCustomResultsAdapter(this, R.layout.settings_info, googlePlaceList);
        ListView listView = (ListView) findViewById(R.id.listOfResults);

        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
    }

    public void getUserInput()
    {
        Bundle bundle = getIntent().getExtras();
        String text= bundle.getString("inputAddress");
        String options= bundle.getString("options");
        userAddress = text;
        userOptions = options;
    }
}


