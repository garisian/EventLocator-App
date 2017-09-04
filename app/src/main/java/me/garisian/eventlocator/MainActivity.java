/*
* This file has the logic of the application.
* Everything from setting up the view and adding a basic menu, clicking a button,
* pulling things off the internet.
* Logic code that drives the application.
* */
package me.garisian.eventlocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

// Used for popup to test user Input
import android.view.Gravity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static String TAG = "MainActivity";
    private EditText inputData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.i(TAG, "Application is running, yay!");

        Button btnDoSomething = (Button) (findViewById(R.id.btnDoSomething));
        btnDoSomething.setOnClickListener(new View.OnClickListener()
        {
              public void onClick(View irrelevant)
              {
                  // Start NewActivity.class
                  Intent myIntent = new Intent(MainActivity.this, ResultsActivity.class);
                  inputData = (EditText) findViewById(R.id.DataInput);
                  String inputAddressString = inputData.getText().toString();

                  // Create a bundle to pass in data to results activity
                  Bundle infoBundle = new Bundle();
                  infoBundle.putString("inputAddress", inputAddressString);
                  myIntent.putExtras(infoBundle);
                  startActivity(myIntent);

                  // Old code to show toast message of user input
                  /*
                      inputData = (EditText) findViewById(R.id.DataInput);
                      Toast toast = new Toast(getApplicationContext());
                      toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                      toast.makeText(MainActivity.this, inputData.getText(), toast.LENGTH_SHORT).show();
                  */
              }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.options_settings)
        {
            Intent intent = new Intent(this, OptionsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
