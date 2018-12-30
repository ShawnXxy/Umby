package site.shawnxxy.umby.weatherData;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import site.shawnxxy.umby.utilities.DayUtils;

public class FakeDataUtils {

    private static int[] weatherIds = {200,300,500,711,900,962};

    /**
     *  Create dummy data with random for test
     */
    private static ContentValues createTestWeatherContentValues(long date) {
        ContentValues testWeatherValues = new ContentValues();
        testWeatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, date);
        testWeatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, date);
        testWeatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, Math.random()*2);
        testWeatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, Math.random()*100);
        testWeatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, 870 + Math.random()*100);
        int maxTemp = (int)(Math.random()*100);
        testWeatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMPERATURE, maxTemp);
        testWeatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMPERATURE, maxTemp - (int) (Math.random()*10));
        testWeatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, Math.random()*10);
        testWeatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, weatherIds[(int)(Math.random()*10)%5]);
        return testWeatherValues;
    }

    public static void insertFakeData(Context context) {
        long today = DayUtils.millisToDate(System.currentTimeMillis());
        List<ContentValues> fakeValues = new ArrayList<ContentValues>();
        for (int i = 0; i < 7; i++) {
            fakeValues.add(createTestWeatherContentValues(today + TimeUnit.DAYS.toMillis(i)));
        }

        context.getContentResolver().bulkInsert(
                WeatherContract.WeatherEntry.CONTENT_URI,
                fakeValues.toArray(new ContentValues[7])
        );
    }
}
