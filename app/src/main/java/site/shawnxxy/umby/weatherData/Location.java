package site.shawnxxy.umby.weatherData;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import site.shawnxxy.umby.R;

public class Location {

    public static final String CITY = "city";
    public static final String LAT = "lat";
    public static final String LON = "lon";

    // dummy location for test
//    private static final String DEFAULT_LOCATION = "94043, USA";
//    private static final double[] DEFAULT_COORD = {37.4284, 122.0724};
//    private static final String DEFAULT_ADDR = "1600 Amphitheatre Parkway, Mountain View, CA 94043";

    static public void setLocationCoord(Context c, double lat, double lon) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong(LAT, Double.doubleToRawLongBits(lat));
        editor.putLong(LON, Double.doubleToRawLongBits(lon));
        editor.apply();
    }

//    static public void setNewLocation(Context c, String newLocation, double lat, double lon) {
//    }

    static public void resetLocationCoord(Context c) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(LAT);
        editor.remove(LON);
        editor.apply();

    }

    /**
     *  return user's preferred location
     */
    public static String getPrefLocation(Context c) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        String keyForLocation = c.getString(R.string.pref_location_key);
        String defaultLocation = c.getString(R.string.pref_location_default);
//        return getDefaultLocation();
        return preferences.getString(keyForLocation, defaultLocation);
    }

    //Returns true if the user has selected metric temperature display.
    public static boolean isMetric(Context c) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        String keyForUnits = c.getString(R.string.pref_units_key);
        String defaultUnits = c.getString(R.string.pref_units_metric);
        String prefUnits = preferences.getString(keyForUnits, defaultUnits);
        String metric = c.getString(R.string.pref_units_metric);
        boolean userPrefMetrics;
        if (metric.equals(prefUnits)) {
            userPrefMetrics = true;
        } else {
            userPrefMetrics =false;
        }
        return userPrefMetrics;
    }

    public static double[] getLocationCoord(Context c) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);

        double[] prefCoord = new double[2];

        prefCoord[0] = Double.longBitsToDouble(sharedPreferences.getLong(LAT, Double.doubleToRawLongBits(0.0)));
        prefCoord[1] = Double.longBitsToDouble(sharedPreferences.getLong(LON, Double.doubleToRawLongBits(0.0)));

//        return getDefaultCoordinates();
        return prefCoord;
    }

    //Returns true if the latitude and longitude values are available.
    public static boolean isLocationLatLonAvailable(Context c) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);

        boolean containLat = sharedPreferences.contains(LAT);
        boolean containLon = sharedPreferences.contains(LON);
        boolean isLatLonValid = false;
        if (containLat && containLon) {
            isLatLonValid = true;
        }

        return isLatLonValid;
    }

//    private static String getDefaultLocation() {
//
//        return DEFAULT_LOCATION;
//    }
//
//    public static double[] getDefaultCoordinates() {
//
//        return DEFAULT_COORD;
//    }

    public static long getLastNotificationTimeInMillis(Context context) {

        String lastNotificationKey = context.getString(R.string.pref_last_notification);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        long lastNotificationTime = sharedPreferences.getLong(lastNotificationKey, 0);
        return lastNotificationTime;
    }

    public static long getEllapsedTimeSinceLastNotification(Context context) {
        long lastNotificationInTimeMillis = Location.getLastNotificationTimeInMillis(context);
        long timeSinceLastNotification = System.currentTimeMillis() - lastNotificationInTimeMillis;
        return timeSinceLastNotification;
    }

    public static void saveLastNotificationTime(Context context, long notificationTime) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String lastNotificationKey = context.getString(R.string.pref_last_notification);
        editor.putLong(lastNotificationKey, notificationTime);
        editor.apply();
    }
}
