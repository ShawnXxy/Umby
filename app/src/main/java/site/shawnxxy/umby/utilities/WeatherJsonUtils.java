package site.shawnxxy.umby.utilities;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class WeatherJsonUtils {

    public static String[] parseWeatherJson(Context c,String weatherString) throws JSONException {
        final String WEATHER_LIST = "list";
        final String WEATHER_TEMPERATURE = "temperature";
        final String WEATHER_MAX = "max";
        final String WEATHER_MIN = "mim";
        final String WEATHER = "weather";
        final String WEATHER_DESCRIPTION = "description";
        final String WEATHER_MSG_CODE = "msg";
        String[] parsedWeatherData = null; // String array to hold each day's weather String

        JSONObject forecastJson = new JSONObject(weatherString);

        if (forecastJson.has(WEATHER_MSG_CODE)) {
            int errorCode = forecastJson.getInt(WEATHER_MSG_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND: // location invalid
                    return null;
                default: // cannot connect to server
                    return null;
            }
        }

        JSONArray weatherArray = forecastJson.getJSONArray(WEATHER_LIST);
        parsedWeatherData = new String[weatherArray.length()];

        long localDate = System.currentTimeMillis();
        long utcDate = DayUtils.localTimeToUTC(localDate);
        long startDay = DayUtils.normalizeDate(utcDate);

        for (int i = 0; i < weatherArray.length(); i++) {
            String date;
            String highLow;

            long dateTimeMillis;
            double high;
            double low;
            String description;

            // Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            dateTimeMillis = startDay + DayUtils.DAY_IN_MILLIS;
            date = DayUtils.dateFormatted(c, dateTimeMillis, false);

            JSONObject weatherObject = dayForecast.getJSONArray(WEATHER).getJSONObject(0);
            description = weatherObject.getString(WEATHER_DESCRIPTION);

            JSONObject temperatureObj = dayForecast.getJSONObject(WEATHER_TEMPERATURE);
            high = temperatureObj.getDouble(WEATHER_MAX);
            low = temperatureObj.getDouble(WEATHER_MIN);
            highLow= WeatherUtils.formatHighLows(context, high, low);

            parsedWeatherData[i] = date + " - " + description + " - " + highLow;
        }
        return parsedWeatherData;
    }

    // Parse JSON and convert it into ContentValues that can be inserted into database
    public static ContentValues[] getFullWeatherInfo(Context c, String forcastJson) {
        return null;
    }
}
