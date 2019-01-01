package site.shawnxxy.umby.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import java.net.URL;

import site.shawnxxy.umby.utilities.NetworkUtils;
import site.shawnxxy.umby.utilities.OpenWeatherMapJsonUtils;
import site.shawnxxy.umby.weatherData.WeatherContract;

public class SyncTask {

    synchronized public static void syncWeather(Context context) {
        try {
            URL weatherRequestUrl = NetworkUtils.getUrl(context);
            // get weather info in json format
            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
            // parse json into list
            ContentValues[] weatherValues = OpenWeatherMapJsonUtils.getFullWeatherInfo(context, jsonWeatherResponse);

            if (weatherRequestUrl != null && weatherValues.length != 0) {
                // delete old weather data in prevous day before inserting new data for today or future
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(WeatherContract.WeatherEntry.CONTENT_URI, null, null);
                contentResolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, weatherValues);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
