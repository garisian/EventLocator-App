package me.garisian.utilities;

/**
 * Created by garis on 2017-09-06.
 */
/*
    This class is a template for a location result from a POST query.
*/
public class GoogleLocation
{
    private String name;
    private String rating;
    private String currentlyOpen;

    public GoogleLocation()
    {
        this.name = "";
        this.rating = "";
        this.currentlyOpen = "";
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setRating(String rating)
    {
        this.rating = rating;
    }

    public String getRating()
    {
        return rating;
    }

    public void setOpenNow(String open)
    {
        this.currentlyOpen = open;
    }

    public String currentlyOpenNow()
    {
        return currentlyOpen;
    }
}
