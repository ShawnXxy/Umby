package site.shawnxxy.umby.utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import site.shawnxxy.umby.R;
import site.shawnxxy.umby.WeatherDetailActivity;
import site.shawnxxy.umby.weatherData.Location;
import site.shawnxxy.umby.weatherData.WeatherContract;

public class NotificationUtils {

    public static final String[] WEATHER_NOTIFICATION= {
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMPERATURE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMPERATURE
    };

    public static final int INDEX_WEATHER_ID = 0;
    public static final int INDEX_MAX_TEMPERATURE = 1;
    public static final int INDEX_MIN_TEPERATURE = 2;

    private static final int WEATHER_NOTIFICATION_ID = 596;

    public static void weatherNotification(Context context) {
        Uri weatherTodayUri = WeatherContract.WeatherEntry.buildWeatherUriWithDate(DayUtils.millisToDate(System.currentTimeMillis()));

        Cursor cursor = context.getContentResolver().query(
                weatherTodayUri,
                WEATHER_NOTIFICATION,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            int weatherId = cursor.getInt(INDEX_WEATHER_ID);
            double high = cursor.getDouble(INDEX_MAX_TEMPERATURE);
            double low = cursor.getDouble(INDEX_MIN_TEPERATURE);

            Resources resources = context.getResources();
            int smallResourceId = WeatherUtils.getSmallWeatherIcon(weatherId);
            int largeResourceId = WeatherUtils.getLargeWeatherIcon(weatherId);
            Bitmap icon = BitmapFactory.decodeResource(resources, smallResourceId);

            String notificationTitle = context.getString(R.string.app_name);
            String notificationText = getNofiicationText(context, weatherId, high, low);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setSmallIcon(smallResourceId)
                    .setLargeIcon(icon)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setAutoCancel(true);

            Intent intentToStartDetailActivity = new Intent(context, WeatherDetailActivity.class);
            intentToStartDetailActivity.setData(weatherTodayUri);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addNextIntentWithParentStack(intentToStartDetailActivity);
            PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(WEATHER_NOTIFICATION_ID, notificationBuilder.build());

            Location.saveLastNotificationTime(context, System.currentTimeMillis());
        }

        cursor.close();
    }

    private static String getNofiicationText(Context context, int weatherId, double high, double low) {
        String description = WeatherUtils.getWeatherCondition(context, weatherId);
        String notification = context.getString(R.string.format_notification);

        String notificationText = String.format(
                notification,
                description,
                WeatherUtils.formatTemperature(context, high),
                WeatherUtils.formatTemperature(context, low)
        );

        return notificationText;
    }
}
