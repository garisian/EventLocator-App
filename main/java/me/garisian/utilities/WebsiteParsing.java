package me.garisian.utilities;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * WebsiteParsing.java
 * Purpose: Given a URL get the response html/JSON and store it in a buffer
 *
 * @author Garisian Kana
 * @version 1.1
 *
 * Created on 2017-09-04
 */
public class WebsiteParsing
{
    private URL url;
    private BufferedReader reader;

    public WebsiteParsing(String url)
    {
        try
        {
            this.url = new URL(url);
        }
        catch(MalformedURLException error){ System.err.println(error); }
        catch(Exception error) { System.err.println(error); }
    }


    /**
     * Description: Send the url and save the returned information in a bufferedreader
     *
     * @param: noner
     *
     * @return none
     */
    public void loadWebsiteContents()
    {
        reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        }
        catch(IOException error) { Log.i("WebsiteParsing", error.toString()); }
        catch(Exception error) { Log.i("WebsiteParsing", error.toString()); }
    }

    /**
     * Description: A series of getters and setters for the class variables
     */
    public BufferedReader getReader() {
        return reader;
    }
}
