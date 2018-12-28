package site.shawnxxy.umby.weatherData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherDbHelper extends SQLiteOpenHelper {

    public static final String  DATABASE_NAME = "weather.db";

    /*
    - Changed the db ver to 2 because changed schema after table created (NOT NULL)
    - Changed the db ver to 3 because changed schema after ver 2 (UNIQUE on COLUMN_DATE)
     */
    private static final int DATABASE_VER = 3;

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE "
                + WeatherContract.WeatherEntry.TABLE_NAME
                + " ("
                + WeatherContract.WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WeatherContract.WeatherEntry.COLUMN_DATE + " INTEGER NOT NULL, "
                + WeatherContract.WeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL, "
                + WeatherContract.WeatherEntry.COLUMN_MIN_TEMPERATURE + " REAL NOT NULL, "
                + WeatherContract.WeatherEntry.COLUMN_MAX_TEMPERATURE + " REAL NOT NULL, "
                + WeatherContract.WeatherEntry.COLUMN_HUMIDITY + " REAL NOT NULL, "
                + WeatherContract.WeatherEntry.COLUMN_PRESSURE + " REAL NOT NULL, "
                + WeatherContract.WeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, "
                + WeatherContract.WeatherEntry.COLUMN_DEGREES + " REAL NOT NULL, "
                + " UNIQUE (" + WeatherContract.WeatherEntry.COLUMN_DATE
                +  ") ON CONFLICT REPLACE);";
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherContract.WeatherEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
