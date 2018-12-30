package site.shawnxxy.umby.weatherData;

import android.net.Uri;
import android.provider.BaseColumns;

import site.shawnxxy.umby.utilities.DayUtils;

public class WeatherContract {

    /**
     *  Implement content provider
     */
    public static final String CONTENT_AUTHORITY = "site.shawnxxy.umby";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_WEATHER = "weather";

    public static final class WeatherEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER).build();

        public static final String TABLE_NAME = "weather";
        // UTC time correlated to local date
        public static final String COLUMN_DATE = "date";
        // return weather icon from OpenWeatherMap API:https://openweathermap.org/weather-conditions
        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_MIN_TEMPERATURE = "min";
        public static final String COLUMN_MAX_TEMPERATURE = "max";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_WIND_SPEED= "wind_speed";
        // meteorological degrees (e.g, 0 is north, 180 is south).
        public static final String COLUMN_DEGREES = "degrees";

        public static Uri buildWeatherUriWithDate(long date) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(date)).build();
        }

        public static String getSqlSelectForToday() {
            long millisNowInUtc = DayUtils.millisToDate(System.currentTimeMillis());
            return WeatherEntry.COLUMN_DATE + ">=" + millisNowInUtc;
        }
    }
}
