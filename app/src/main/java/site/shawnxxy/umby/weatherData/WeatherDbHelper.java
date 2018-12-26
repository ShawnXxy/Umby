package site.shawnxxy.umby.weatherData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherDbHelper extends SQLiteOpenHelper {

    public static final String  DATABASE_NAME = "weather.db";

    private static final int DATABASE_VER = 1;

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE "
                + WeatherContract.WeatherEntry.TABLE_NAME
                + " ("
                + WeatherContract.WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WeatherContract.WeatherEntry.COLUMN_DATE + " INTEGER, "
                + WeatherContract.WeatherEntry.COLUMN_WEATHER_ID + " INTEGER, "
                + WeatherContract.WeatherEntry.COLUMN_MIN_TEMPERATURE + " REAL, "
                + WeatherContract.WeatherEntry.COLUMN_MAX_TEMPERATURE + " REAL, "
                + WeatherContract.WeatherEntry.COLUMN_HUMIDITY + " REAL, "
                + WeatherContract.WeatherEntry.COLUMN_PRESSURE + " REAL, "
                + WeatherContract.WeatherEntry.COLUMN_WIND_SPEED + " REAL, "
                + WeatherContract.WeatherEntry.COLUMN_DEGREES + " REAL"
                + ");";
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {

    }

}
