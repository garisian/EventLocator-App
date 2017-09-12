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
    private boolean currentlyOpen;
    private double distanceFromUser;

    public GoogleLocation()
    {
        this.name = "";
        this.rating = "";
        this.currentlyOpen = false;
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

    public void setOpenNow(boolean open)
    {
        this.currentlyOpen = open;
    }

    public boolean currentlyOpenNow()
    {
        return currentlyOpen;
    }

    public void setDistanceFromCoordinates(float lat1, float lon1, float lat2, float lon2)
    {
        double R = 6378.137; // Radius of earth in KM
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        distanceFromUser = R * c;
    }

    public double getDistance()
    {
        return distanceFromUser;
    }
}
