package site.shawnxxy.umby.weatherData;

import android.content.Context;

public class Location {

    public static final String CITY = "city";
    public static final String LAT = "lat";
    public static final String LON = "lon";

    // dummy location for test
    private static final String DEFAULT_LOCATION = "94043, USA";
    private static final double[] DEFAULT_COORD = {37.4284, 122.0724};
    private static final String DEFAULT_ADDR = "1600 Amphitheatre Parkway, Mountain View, CA 94043";

    static public void setLocation(Context c, String city, double lat, double lon) {

    }

    static public void setNewLocation(Context c, String newLocation, double lat, double lon) {

    }

    static public void resetLocationCoordinates(Context c) {

    }

    public static String getPrefLocation(Context c) {

        return getDefaultLocation();
    }

    //Returns true if the user has selected metric temperature display.
    public static boolean isMetric(Context c) {

        return true;
    }

    public static double[] getLocationCoordinates(Context c) {
        return getDefaultCoordinates();
    }

    //Returns true if the latitude and longitude values are available.
    public static boolean isLocationLatLonAvailable(Context c) {

        return false;
    }

    private static String getDefaultLocation() {

        return DEFAULT_LOCATION;
    }

    public static double[] getDefaultCoordinates() {

        return DEFAULT_COORD;
    }
}
