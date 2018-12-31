package site.shawnxxy.umby.utilities;

import android.content.Context;
import android.net.Uri;
import android.print.PrinterId;
import android.util.Log;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import site.shawnxxy.umby.weatherData.Location;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String DYNAMIC_WEATHER_URL = "https://andfun-weather.udacity.com/weather";
    private static final String STATIC_WEATHER_URL = "https://andfun-weather.udacity.com/staticweather";
    private static final String FORECAST_BASE_URL = STATIC_WEATHER_URL;

    private static final String format = "json"; // data return in json
    private static final String units = "metric";
    private static final int numDays = 14; // return 14 days info

    final static String QUERY_PARAM = "q";
    final static String LAT_PARAM = "lat";
    final static String LON_PARAM = "lon";
    final static String FORMAT_PARAM = "mode";
    final static String UNITS_PARAM = "units";
    final static String DAYS_PARAM = "cnt";

    public static URL getUrl(Context context) {
        if (Location.isLocationLatLonAvailable(context)) {
            double[] prefCoord = Location.getLocationCoord(context);
            double lat = prefCoord[0];
            double lon = prefCoord[1];
            return buildUrlWithLatLon(lat, lon);
        } else {
            String locationQuery = Location.getPrefLocation(context);
            return buildUrl(locationQuery);
        }
    }

    // Query weather by location
    public static URL buildUrl(String  locationQuery) {
        Uri weatherQueryUri  = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, locationQuery)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri .toString());
            Log.v(TAG, "URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

//        URL url = null;
//        try {
//            url = new URL(buildUri.toString());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        Log.v(TAG, "Built URI " + url);
//
//        return url;
    }

    // Query weather by coordinates
    public static URL buildUrlWithLatLon(Double lat, Double lon) {
        Uri weatherQueryUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(LAT_PARAM, String.valueOf(lat))
                .appendQueryParameter(LON_PARAM, String.valueOf(lon))
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            Log.v(TAG, "URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}
