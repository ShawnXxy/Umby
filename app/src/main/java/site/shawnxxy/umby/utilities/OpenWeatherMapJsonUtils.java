package site.shawnxxy.umby.utilities;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import site.shawnxxy.umby.weatherData.Location;
import site.shawnxxy.umby.weatherData.WeatherContract;

import static site.shawnxxy.umby.utilities.WeatherUtils.getWeatherCondition;

/**
 *  Utils for open weather map information
 */
public final class OpenWeatherMapJsonUtils {

    private static final String OWM_CITY = "city";
    private static final String OWM_COOR = "coord";
    private static final String OWM_LAT = "lat";
    private static final String OWM_LON = "lon";
    private static final String OWM_LIST = "list";
    private static final String OWM_PRESSURE = "pressure";
    private static final String OWM_HUMIDITY = "humidity";
    private static final String OWM_WIND_SPEED = "wind speed";
    private static final String OWM_WIND_DIRECTION = "direction";
    private static final String OWM_TEMPERATURE = "temperature";
    private static final String OWM_TEMPERATURE_MAX = "max temperature";
    private static final String OWM_TEMPERATURE_MIN = "min temperature";
    private static final String OWM_WEATHER = "weather";
    private static final String OWM_WEATHER_ID = "weather id";
    private static final String OWM_MSG_CODE = "message code";


    public static String[] parseWeatherJson(Context c,String weatherStr) throws JSONException {
//        final String WEATHER_LIST = "list";
//        final String WEATHER_TEMPERATURE = "temperature";
//        final String WEATHER_MAX = "max";
//        final String WEATHER_MIN = "mim";
//        final String WEATHER = "weather";
//        final String WEATHER_DESCRIPTION = "description";
//        final String WEATHER_MSG_CODE = "msg";

        String[] parsedWeatherData = null; // String array to hold each day's weather String

        JSONObject forecastJson = new JSONObject(weatherStr);

        if (forecastJson.has(OWM_MSG_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MSG_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND: // location invalid
                    return null;
                default: // cannot connect to server
                    return null;
            }
        }

        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);
        parsedWeatherData = new String[weatherArray.length()];

//        long localDate = System.currentTimeMillis();
//        long utcDate = DayUtils.localTimeToUTC(localDate);
//        long startDay = DayUtils.normalizeDate(utcDate);


        for (int i = 0; i < weatherArray.length(); i++) {
            String date;
            String highLow;

            long dateTimeMillis;
            double high;
            double low;
            int weatherId;
            String description;

            // Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            dateTimeMillis = startDay + DayUtils.DAY_IN_MILLIS * i;
            date = DayUtils.dateFormatted(c, dateTimeMillis, false);

            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            weatherId = weatherObject.getInt(OWM_WEATHER_ID);
//            description = weatherObject.getString(WEATHER_DESCRIPTION);
            description = getWeatherCondition(c, weatherId);

            JSONObject temperatureObj = dayForecast.getJSONObject(OWM_TEMPERATURE);
            high = temperatureObj.getDouble(OWM_TEMPERATURE_MAX);
            low = temperatureObj.getDouble(OWM_TEMPERATURE_MIN);
            highLow= WeatherUtils.formatHighLow(c, high, low);

            parsedWeatherData[i] = date + " - " + description + " - " + highLow;
        }
        return parsedWeatherData;
    }

    // Parse JSON and convert it into ContentValues that can be inserted into database
    public static ContentValues[] getFullWeatherInfo(Context c, String forcastStr) throws JSONException{

        JSONObject forecastJson = new JSONObject(forcastStr);

        if (forecastJson.has(OWM_MSG_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MSG_CODE);
            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND: // location invalid
                    return null;
                default: // cannot connect to server
                    return null;
            }
        }

        JSONArray weatherJsonArray = forecastJson.getJSONArray(OWM_LIST);
        JSONObject cityObj = forecastJson.getJSONObject(OWM_CITY);
        JSONObject coord = cityObj.getJSONObject(OWM_COOR);
        double lat = coord.getDouble(OWM_LAT);
        double lon = coord.getDouble(OWM_LON);
        Location.setLocationCoord(c, lat, lon);

        ContentValues[] weatherContentValues = new ContentValues[weatherJsonArray.length()];

//        long localDate = System.currentTimeMillis();
//        long utcDate = DayUtils.localTimeToUTC(localDate);
        long startDay = DayUtils.millisToUtcToday();

        for (int i = 0; i < weatherJsonArray.length(); i++) {

            long dateTimeInMillis;
            double pressure;
            int humidity;
            double windSpeed;
            double windDirection;
            double high;
            double low;
            int weatherId;

            JSONObject dayForecast = weatherJsonArray.getJSONObject(i);

            dateTimeInMillis = startDay + DayUtils.DAY_IN_MILLIS * i;
            pressure = dayForecast.getDouble(OWM_PRESSURE);
            humidity = dayForecast.getInt(OWM_HUMIDITY);
            windSpeed = dayForecast.getDouble(OWM_WIND_SPEED);
            windDirection = dayForecast.getDouble(OWM_WIND_DIRECTION);

            JSONObject weatherObj = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            weatherId = weatherObj.getInt(OWM_WEATHER_ID);

            JSONObject temperatureObj = dayForecast.getJSONObject(OWM_TEMPERATURE);
            high = temperatureObj.getDouble(OWM_TEMPERATURE_MAX);
            low = temperatureObj.getDouble(OWM_TEMPERATURE_MIN);

            ContentValues weatherValues = new ContentValues();
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, dateTimeInMillis);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, humidity);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, pressure);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, windSpeed);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, windDirection);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMPERATURE, high);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMPERATURE, low);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, weatherId);

            weatherContentValues[i] = weatherValues;
        }

        return weatherContentValues;
    }
}
