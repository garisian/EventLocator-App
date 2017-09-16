package me.garisian.utilities;

/**
 * GoogleLocation.java
 * Purpose: Template used to structure locations from JSON result
 *
 * @author Garisian Kana
 * @version 1.1
 *
 * Created on 2017-09-06
 */
public class GoogleLocation
{
    // Variable elemnts used to store info regarding a result location
    private String name;
    private String rating = "";
    private boolean currentlyOpen;
    private double distanceFromUser;
    private int maxHeight;
    private int maxWidth;
    private String photoReference;
    private String address;
    private String longitude;
    private String latitude;
    private String distance;

    /**
     * Description: Constructor that requires the rating and name of restaurant. Constructor
     *              will not be invoked unless restaurant has both a valid name and rating.
     *              Certain resturants do not specify Opening/Closing hours so default to close;
     *
     * @param: "name" for restaurant name
     *         "rating" for the restauran'ts rating
     *
     * @return CONSTRUCTOR method
     */
    public GoogleLocation(String name, String rating)
    {
        this.name = name;
        this.rating = rating;
        this.currentlyOpen = false;
    }

    /**
     * Description: Compute straight line distance from user specified location and results'
     *              location
     *
     * @param: "lat1" and "lng1" represent the latitude and longitude of the current location
     *         "lat2" and "lng2" represent the latitude and longitude of the result location
     *
     * @return VOID method
     */
    public void setDistanceFromCoordinates(double lat1, double lon1, double lat2, double lon2)
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

    /**
     * Description: Compute straight line distance from user specified location and results'
     *              location
     *
     * @param: "distance" is a string representation of a distance with commas and units
     *
     * @return VOID method
     */
    public void setDistance(String distance)
    {
        this.distance = distance;
    }

    /**
     * Description: A series of getters and setters for the class variables
     */
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

    public String getDistance()
    {
        return distance;
    }

    public void setPhotoReference(String photoReference)
    {
        this.photoReference = photoReference;
    }

    public String getPhotoReference()
    {
        return photoReference;
    }

    public double[] getPhotoHeightWidth()
    {
        return new double[]{maxHeight, maxWidth};
    }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getLongitude() { return longitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }

    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    public void setPhotoHeightWidth(int maxHeight, int maxWidth)
    {
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
    }
}
