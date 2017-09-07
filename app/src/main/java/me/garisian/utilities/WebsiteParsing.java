package me.garisian.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.MalformedURLException;
import java.nio.Buffer;

/**
 * Created by garis on 2017-09-04.
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

    public void loadWebsiteContents()
    {
        reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        }
        catch(IOException error) { System.err.println(error); }
        catch(Exception error) { System.err.println(error); }

    }

    public BufferedReader getReader() {
        return reader;
    }
}
