package site.shawnxxy.umby.utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import site.shawnxxy.umby.sync.SyncIntentService;
import site.shawnxxy.umby.weatherData.WeatherContract;

public class SyncUtils {

    private static boolean initialized;

    synchronized public static void initialize(@NonNull final Context context) {

        // perform the initialization once per app lifetime
        if (initialized) {
            return;
        }

        initialized = true;

        /**
         *  Check if the weather content provider is empty.
         *  Performing a query on the main thread may cause UI slow loading.
         *  Use a new thread to
         */
        new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground(Void... voids) {
                // uri for each row of weather data from the weather table
                Uri uri = WeatherContract.WeatherEntry.CONTENT_URI;

                String[] projectionColumns = {WeatherContract.WeatherEntry._ID};
                String selectionStmt = WeatherContract.WeatherEntry.getSqlSelectForToday();

                Cursor cursor = context.getContentResolver().query(uri,
                        projectionColumns,
                        selectionStmt,
                        null,
                        null);

                // Sync weather data if cursor is null
                if (null == cursor || cursor.getCount() == 0) {
                    startSyncImmediately(context);
                }

                // avoid memory leaks
                cursor.close();
                return null;
            }
        }.execute();
    }

    public static void startSyncImmediately (@NonNull final Context context) {
        Intent intentToSync = new Intent(context, SyncIntentService.class);
        context.startService(intentToSync);
    }
}
