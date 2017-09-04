package me.garisian.eventlocator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class ResultsActivity extends AppCompatActivity {

    static String TAG = "ResultsActivity";
    private String userAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getUserAddress();
    }

    public void getUserAddress()
    {
        Bundle bundle = getIntent().getExtras();
        String text= bundle.getString("inputAddress");
        Log.i(TAG, text);
        userAddress = text;
    }

}
