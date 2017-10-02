package me.garisian.eventlocator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import me.garisian.utilities.GoogleLocation;
import me.garisian.utilities.WebsiteParsing;

/**
 * ResultsActivity.java
 * Purpose: Display all options and transfer address and options to results activity
 *
 * @author Garisian Kana
 * @version 1.1
 *
 * Created on 2017-09-01
 */
public class ResultsActivity extends AppCompatActivity {

    // Used for Debugging purposes
    private String TAG = "ResultsActivity";

    // Variables used for the POST call wia google locations api
    private String userAddress;
    private String userOptions;
    private String userLatitude;
    private String userLongtitude;
    private int radius = 500;

    // List of locations retrieved from Google
    ArrayList<GoogleLocation> googlePlaceList = new ArrayList<GoogleLocation>();

    // Created to customize settings display
    MyCustomResultsAdapter dataAdapter = null;
    private String dataString = "";

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
        setContentView(R.layout.activity_results);

        // Set the toolbar with options for settings etc at top of activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hide the default title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Print User input from main activity
        getUserInput();

        // start the AsyncTask Call since post call needs to be in unique thread
        new GetPlaces().execute();
    }

    /**
     * GetPlaces.java
     * Purpose: Create a google map object and get location results from google
     */
    private class GetPlaces extends AsyncTask
    {
        // Dialog to show loading bar while retrieving results
        private ProgressDialog dialog = new ProgressDialog(ResultsActivity.this);

        /**
         * Description: Show a loading bar until app parses and displays the results
         *
         * @param: none
         *
         * @return none
         */
        @Override
        protected void onPreExecute()
        {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        /**
         * Description: Get coordinates of specified address and request data before showing
         *              anything to user
         *
         * @param: "params" required as method is overwritten
         *
         * @return none
         */
        @Override
        protected Object doInBackground(Object[] params)
        {
            getCoordinates();
            getRequestData();
            return null;
        }

        /**
         * Description: Remove loading screen and display results once everything is ready
         *
         * @param: "result" required as method is overwritten
         *
         * @return none
         */
        @Override
        protected void onPostExecute(Object result)
        {
            // Data should be extracted at this point. Display the data on the activity panel
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }

            // Display the results in activity
            displayResults();
            return;
        }


        /**
         * Description: Get user's coordinates for the input address
         *
         * @param: none
         *
         * @return none
         */
        public void getCoordinates()
        {
            // replace spaces with + for google POST request
            String scriptAddress = userAddress.replaceAll(" ", "+").toLowerCase();

            String requestURL = "https://maps.googleapis.com/maps/api/geocode/json?"+
                    "address="+scriptAddress+
                    "&key=AIzaSyBfxw5cINgN7q89t-HGIsnsb6lRUDU8rjQ";

            // Load the URL using websiteparsing and extract the bufferedreader containing the
            // results
            WebsiteParsing s = new WebsiteParsing(requestURL);
            s.loadWebsiteContents();

            // Change results into JSON object and extract coordinates
            try
            {
                // Read everyine in bufferedreader and combine results to convert into JSON object
                BufferedReader dataPipe = s.getReader();
                String line;
                String combined = "";
                while((line=dataPipe.readLine())!= null)
                {
                    combined += line;
                }
                dataPipe.close();

                // Convert string to a JSON Object for easy Object Extraction
                JSONObject jsonObj = new JSONObject(combined);
                JSONObject json = jsonObj.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                userLatitude = json.get("lat").toString();
                userLongtitude = json.get("lng").toString();
            }
            catch(IOException e)
            {
                Log.v(TAG, "IOEXCEPTION ERROR");
                Log.v(TAG, e.toString());
            }
            catch(Exception e)
            {
                Log.v(TAG, "EXCEPTION ERROR");
                Log.v(TAG, e.toString());
            }

        }

        /**
         * Description: Extract data and turn them into GooglePlace Objects
         *
         * @param: none
         *
         * @return none
         */
        public void getRequestData()
        {
            // Create the request string based on the said variables
            String requestURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"+
                    "location="+ userLatitude +","+ userLongtitude +
                    "&type="+userOptions+
                    "&radius="+radius+
                    "&key=AIzaSyBfxw5cINgN7q89t-HGIsnsb6lRUDU8rjQ";
            //String requestURL2 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+userLatitude+","+userLongtitude+"&rankby=distance&types=food&key=AIzaSyBfxw5cINgN7q89t-HGIsnsb6lRUDU8rjQ";

            // Send the request and store the response in a bufferedreader
            WebsiteParsing s = new WebsiteParsing(requestURL);
            s.loadWebsiteContents();

            // Read the content and populate each location as a GoogleLocation object
            try
            {
                // Get the BufferedReader and convert it into JSON Object
                BufferedReader dataPipe = s.getReader();
                dataString = convertToString(dataPipe);
                JSONObject jsonObj = new JSONObject(dataString);


                // Iterate through each result in JSON and create a GoogleLocation with the
                // respective data
                JSONArray jsonArray = jsonObj.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    if(jsonArray.getJSONObject(i).has("name"))
                    {
                        GoogleLocation place = new GoogleLocation(jsonArray.getJSONObject(i).optString("name"),jsonArray.getJSONObject(i).optString("rating"));
                        if (jsonArray.getJSONObject(i).has("opening_hours"))
                        {
                            if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now"))
                            {
                                if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true"))
                                {
                                    place.setOpenNow(true);
                                }
                            }
                        }
                        if(jsonArray.getJSONObject(i).has("vicinity"))
                        {
                            place.setAddress(jsonArray.getJSONObject(i).optString("vicinity"));
                        }

                        if(jsonArray.getJSONObject(i).has("photos"))
                        {
                            place.setPhotoHeightWidth(Integer.parseInt(jsonArray.getJSONObject(i).getJSONArray("photos").getJSONObject(0).getString("height")),
                                    Integer.parseInt(jsonArray.getJSONObject(i).getJSONArray("photos").getJSONObject(0).getString("width")));
                            place.setPhotoReference(jsonArray.getJSONObject(i).getJSONArray("photos").getJSONObject(0).getString("photo_reference"));
                        }
                        //https://maps.googleapis.com/maps/api/place/photo?
                        // maxheight=___________
                        // &key=AIzaSyBfxw5cINgN7q89t-HGIsnsb6lRUDU8rjQ
                        // &photoreference=____________

                        // Based on the current coordinates and location's coordinates, extract the
                        // straight line distance
                        JSONObject json = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                        place.setLatitude(json.get("lat").toString());
                        place.setLongitude(json.get("lng").toString());
                        //float currentLatitude = Float.parseFloat(json.get("lat").toString());
                        //float currentLongitude = Float.parseFloat(json.get("lng").toString());
                        //place.setDistanceFromCoordinates(Float.parseFloat(userLatitude),Float.parseFloat(userLongtitude),currentLatitude,currentLongitude);
                        googlePlaceList.add(place);
                    }
                }

                // Send another post request with every location and find the distance
                updateDistances();
            }
            catch(IOException e)
            {
                Log.v(TAG, "Something terribly went wrong. Results Activity: IOException caught");
                Log.v(TAG, e.toString());
            }
            catch(JSONException e)
            {
                Log.v(TAG, "Something terribly went wrong. Results Activity: JSONException caught");
                Log.v(TAG, e.toString());
            }
            catch(Exception e)
            {
                Log.v(TAG, "Something terribly went wrong. Results Activity: Exception caught");
                Log.v(TAG, e.toString());
            }
        }

        /**
         * Description: Find distances for every single locations in a single request
         *
         * @param: none
         *
         * @return none
         */
        public void updateDistances()
        {
            // Keep a spare arraylist to keep order in the rare case the device code changed and
            // reordered locations
            List<GoogleLocation> myList = new ArrayList<GoogleLocation>();
            String destinations = "";
            // Structure for distance matrix googleapi post request

            for(GoogleLocation location: googlePlaceList)
            {
                myList.add(location);
                if(destinations == "")
                {
                    destinations+=location.getLatitude()+","+location.getLongitude();
                }
                else
                {
                    destinations+="|"+location.getLatitude()+","+location.getLongitude();
                }

            }
            //Log.i(TAG,destinations);
            //Log.i(TAG,userLatitude);
            //Log.i(TAG,userLongtitude);
            String distanceRequtest = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric"+
                    "&origins="+ userLatitude +","+ userLongtitude +
                    "&destinations="+destinations+
                    "&key=AIzaSyBfxw5cINgN7q89t-HGIsnsb6lRUDU8rjQ";

            // Send the request and store the response in a bufferedreader
            WebsiteParsing s = new WebsiteParsing(distanceRequtest);
            s.loadWebsiteContents();

            // Read the content and populate each location as a GoogleLocation object
            try
            {
                // Get the BufferedReader and convert it into JSON Object
                BufferedReader dataPipe = s.getReader();
                dataString = convertToString(dataPipe);
                JSONObject jsonObj = new JSONObject(dataString);

                // Iterate through each result in JSON and set the distance from user's address
                JSONArray jsonArray = jsonObj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    String distance = jsonArray.getJSONObject(i).getJSONObject("distance").get("text").toString();
                    myList.get(i).setDistance(distance);
                }

            }
            catch(IOException e)
            {
                Log.v(TAG, "Something terribly went wrong. Results Activity: IOException caught");
                Log.v(TAG, e.toString());
            }
            catch(JSONException e)
            {
                Log.v(TAG, "Something terribly went wrong. Results Activity: JSONException caught");
                Log.v(TAG, e.toString());
            }
            catch(Exception e)
            {
                Log.v(TAG, "Something terribly went wrong. Results Activity: Exception caught");
                Log.v(TAG, e.toString());
            }

        }


        /**
         * Description: Convert a bufferedreader to a String
         *
         * @param: "datapipe" is a bufferedreader containing the data that needs to be converted
         *
         * @return String containing data from the bufferedreader
         */
        public String convertToString(BufferedReader dataPipe) throws IOException
        {
            String line;
            String combined = "";
            while((line=dataPipe.readLine())!= null)
            {
                combined += line;
                //Log.i("GET THIS",line);
            }
            dataPipe.close();
            return combined;
        }

        /**
         * Description: Debug method to print contents in a bufferedReader
         *
         * @param: "datapipe" is a bufferedreader containing the data that needs to be printed
         *
         * @return none
         */
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

    /**
     * MyCustomResultsAdapter.java
     *
     * Purpose: Custom Adapter to display resutls on activity
     *
     */
    private class MyCustomResultsAdapter extends ArrayAdapter<GoogleLocation>
    {

        private ArrayList<GoogleLocation> resultsList;

        public MyCustomResultsAdapter(Context context, int textViewResourceId, ArrayList<GoogleLocation> settingList)
        {
            super(context, textViewResourceId, settingList);
            this.resultsList = new ArrayList<GoogleLocation>();
            this.resultsList.addAll(settingList);
        }

        // A View class that represents the data for a single setting
        private class ViewHolder
        {
            TextView locationNameField;
            TextView locationOpenOrNot;
            TextView placeholderAddress;
            TextView distancePlaceHolder;
            RatingBar locationRating;
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
                convertView = vi.inflate(R.layout.result_info , null);

                // Assign xml element to a holder element
                holder = new ViewHolder();
                //holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.locationNameField = (TextView) convertView.findViewById(R.id.locationNameField);
                holder.locationOpenOrNot = (TextView) convertView.findViewById(R.id.locationOpenOrNot);
                holder.placeholderAddress = (TextView) convertView.findViewById(R.id.placeholderAddress);
                holder.locationRating = (RatingBar) convertView.findViewById(R.id.locationRating);
                holder.distancePlaceHolder = (TextView) convertView.findViewById(R.id.distancePlaceHolder);
                convertView.setTag(holder);

                // Wait for user action and show popup message once clicked
                /*holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        GoogleLocation country = (GoogleLocation) cb.getTag();
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                    }
                });*/
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            // Set up what will be displayed for each result
            GoogleLocation location = resultsList.get(position);
            String isOpen = location.currentlyOpenNow()? "OPEN": "CLOSED";
            //holder.name.setText(location.getName()+" -Currently "+isOpen + " -Distance "+location.getDistance());
            //holder.name.setText(location.getName()+" -Currently "+isOpen);
            holder.locationNameField.setText(location.getName());
            holder.locationOpenOrNot.setText(isOpen);
            holder.placeholderAddress.setText(location.getAddress());

            // Certain locations do not have a rating available. In that case show nothing
            if(location.getRating().equals(""))
            {
                holder.locationRating.setRating(0);
                holder.locationRating.setVisibility(View.INVISIBLE);
            }
            else
            {
                holder.locationRating.setVisibility(View.VISIBLE);
                holder.locationRating.setRating(Float.parseFloat(location.getRating()));
            }
            holder.distancePlaceHolder.setText(location.getDistance());
            return convertView;
        }
    }

    /**
     * Description: Set Up which xml style the results will be shown in and get the correct element
     *              ID
     *
     * @param: none
     *
     * @return none
     */
    public void displayResults()
    {
        dataAdapter = new MyCustomResultsAdapter(this, R.layout.result_info, googlePlaceList);
        ListView listView = (ListView) findViewById(R.id.listOfResults);

        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        // Whenever user clicks on any point in the row
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // When clicked, show a toast with the TextView text
                GoogleLocation location = (GoogleLocation) parent.getItemAtPosition(position);
                // Debugging Purposes. Please Work
/*                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + location.getName(),
                        Toast.LENGTH_LONG).show();*/

                // Start a popup session which displays extra details regarding the lcoation
                Bundle infoBundle = new Bundle();
                Intent myIntent = new Intent(ResultsActivity.this, LocationDetailsActivity.class);
                infoBundle.putString("userLongtitude", userLongtitude);
                infoBundle.putString("userLatitude", userLatitude);
                infoBundle.putString("locationLongitude",location.getLatitude());
                infoBundle.putString("locationLatitude",location.getLongitude());
                myIntent.putExtras(infoBundle);
                startActivity(myIntent);
                overridePendingTransition(0, 0);
            }
        });
    }


    /**
     * Description: Get user's input data from extra bundle set up in previous activities
     *
     * @param: none
     *
     * @return none
     */
    public void getUserInput()
    {
        Bundle bundle = getIntent().getExtras();
        String text= bundle.getString("inputAddress");
        String options= bundle.getString("options");
        userAddress = text;
        userOptions = options;
    }

}


