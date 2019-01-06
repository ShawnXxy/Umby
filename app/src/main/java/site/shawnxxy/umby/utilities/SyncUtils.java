package site.shawnxxy.umby.utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import site.shawnxxy.umby.sync.FirebaseJobService;
import site.shawnxxy.umby.sync.SyncIntentService;
import site.shawnxxy.umby.weatherData.WeatherContract;

public class SyncUtils {

    /**
     *  Sync data every 3 hours
     */
    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SEC = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SEC = SYNC_INTERVAL_SEC / 3;

    private static boolean initialized;

    private static final String SYNC_TAG = "sync";

    /**
     *  Schedule periodic weather sync
     */
    static void scheduleWeatherSync(@NonNull final Context context) {
        com.firebase.jobdispatcher.Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job syncJob = dispatcher.newJobBuilder()
                .setService(FirebaseJobService.class)
                .setTag(SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER) // set how long this sync job persists
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SEC,
                        SYNC_INTERVAL_HOURS + SYNC_FLEXTIME_SEC
                ))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(syncJob);

    }

    synchronized public static void initialize(@NonNull final Context context) {

        // perform the initialization once per app lifetime
        if (initialized) {
            return;
        }

        initialized = true;

        scheduleWeatherSync(context);

        /**
         *  Check if the weather content provider is empty.
         *  Performing a query on the main thread may cause UI slow loading.
         *  Use a new thread to
         */
        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
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
            }
        });
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            public Void doInBackground(Void... voids) {
//                // uri for each row of weather data from the weather table
//                Uri uri = WeatherContract.WeatherEntry.CONTENT_URI;
//
//                String[] projectionColumns = {WeatherContract.WeatherEntry._ID};
//                String selectionStmt = WeatherContract.WeatherEntry.getSqlSelectForToday();
//
//                Cursor cursor = context.getContentResolver().query(uri,
//                        projectionColumns,
//                        selectionStmt,
//                        null,
//                        null);
//
//                // Sync weather data if cursor is null
//                if (null == cursor || cursor.getCount() == 0) {
//                    startSyncImmediately(context);
//                }
//
//                // avoid memory leaks
//                cursor.close();
//                return null;
//            }
//        }.execute();

    }

    public static void startSyncImmediately (@NonNull final Context context) {
        Intent intentToSync = new Intent(context, SyncIntentService.class);
        context.startService(intentToSync);
    }
}
