package site.shawnxxy.umby;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import site.shawnxxy.umby.utilities.DayUtils;
import site.shawnxxy.umby.utilities.WeatherUtils;
import site.shawnxxy.umby.weatherData.WeatherContract;
import site.shawnxxy.umby.databinding.ActivityWeatherDetailBinding;

public class WeatherDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String WEATHER_SHARE_TAG = "#Umby";

    public static final String[] WEATHER_DETAIL_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMPERATURE,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMPERATURE,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };

    /**
     *  Index used to quickly query in SQL db
     */
    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMPERATURE = 1;
    public static final int INDEX_WEATHER_MIN_TEMPERATURE = 2;
    public static final int INDEX_WEATHER_HUMIDITY = 3;
    public static final int INDEX_WEATHER_PRESSURE = 4;
    public static final int INDEX_WEATHER_WIND_SPEED = 5;
    public static final int INDEX_WEATHER_DEGREES = 6;
    public static final int INDEX_WEATHER_CONDITION_ID = 7;

    private static final int DETAIL_LOADER_ID = 596; // number is dummy

    private Uri uri;

    private String forecast;

    /**
     *  Remove textview and replace with data binding
     */
//    private TextView weatherDetail;
//    private TextView dateTextView;
//    private TextView descriptionTextView;
//    private TextView highTemperatureTextView;
//    private TextView lowTemperatureTextView;
//    private TextView humidityTextView;
//    private TextView windTextView;
//    private TextView pressureTextView;

    private ActivityWeatherDetailBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);

//        weatherDetail = findViewById(R.id.weather_details_textview);
//        dateTextView = findViewById(R.id.date);
//        descriptionTextView = findViewById(R.id.weather_description);
//        highTemperatureTextView = findViewById(R.id.high_temperature);
//        lowTemperatureTextView = findViewById(R.id.low_temperature);
//        humidityTextView = findViewById(R.id.humidity);
//        windTextView = findViewById(R.id.wind);
//        pressureTextView = findViewById(R.id.pressure);

        /**
         *  Remove textview and replace with data binding
         */
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_weather_detail);

//        Intent intentToStartActivity = getIntent();
//
//        // Display weather details that was passed from MainActivity
//        if (intentToStartActivity != null) {
//            if (intentToStartActivity.hasExtra(Intent.EXTRA_TEXT)) {
//                forecast = intentToStartActivity.getStringExtra(Intent.EXTRA_TEXT);
//                weatherDetail.setText(forecast);
//            }
//        }

        uri = getIntent().getData();
        if (uri == null) {
            throw new NullPointerException("URI for weahter details cannot be null!");
        }

        getSupportLoaderManager().initLoader(DETAIL_LOADER_ID, null, this);
    }

    /**
     *  Display menu and implement sharing
     */
//    private Intent intentToShareForecast() {
//        Intent share = ShareCompat.IntentBuilder.from(this)
//                .setType("text/plain")
//                .setText(forecast + WEATHER_SHARE_TAG)
//                .getIntent();
//        return share;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
//        MenuItem menuItem = menu.findItem(R.id.action_share);
//        menuItem.setIntent(intentToShareForecast());
        return true;
    }

    /**
     *
     *  Launch Settings menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        if (id == R.id.action_share) {
            Intent startShareActivity = createShareIntent();
            startActivity(startShareActivity);
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    private Intent createShareIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(forecast + WEATHER_SHARE_TAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        switch (loaderId) {
            case DETAIL_LOADER_ID:
                return new CursorLoader(this,
                        uri,
                        WEATHER_DETAIL_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        boolean cursorWithValidData = false;
        if (cursor != null && cursor.moveToFirst()) {
            cursorWithValidData = true;
        }

        if (!cursorWithValidData) {
            return;
        }

        int weatherId = cursor.getInt(INDEX_WEATHER_CONDITION_ID);
        int weatherImageId = WeatherUtils.getLargeWeatherIcon(weatherId);
        dataBinding.primaryInfo.weatherIcon.setImageResource(weatherImageId);

        long localDate = cursor.getLong(INDEX_WEATHER_DATE);
        String dateStr = DayUtils.dateFormatted(this, localDate, true);
//        dateTextView.setText(dateStr);
        dataBinding.primaryInfo.date.setText(dateStr);

        String descriptionStr = WeatherUtils.getWeatherCondition(this, weatherId);
        String descriptionAlly = getString(R.string.a11y_forecast, descriptionStr);
//        descriptionTextView.setText(descriptionStr);
        dataBinding.primaryInfo.weatherDescription.setText(descriptionStr);
        dataBinding.primaryInfo.weatherDescription.setContentDescription(descriptionAlly);
        dataBinding.primaryInfo.weatherIcon.setContentDescription(descriptionAlly);

        double highInCels = cursor.getDouble(INDEX_WEATHER_MAX_TEMPERATURE);
        String highStr = WeatherUtils.formatTemperature(this, highInCels);
        String highAlly = getString(R.string.a11y_high_temp, highStr);
//        highTemperatureTextView.setText(highStr);
        dataBinding.primaryInfo.highTemperature.setText(highStr);
        dataBinding.primaryInfo.highTemperature.setContentDescription(highAlly);

        double lowInCels = cursor.getDouble(INDEX_WEATHER_MIN_TEMPERATURE);
        String lowStr = WeatherUtils.formatTemperature(this, lowInCels);
        String lowAlly = getString(R.string.a11y_low_temp, lowStr);
//        lowTemperatureTextView.setText(lowStr);
        dataBinding.primaryInfo.highTemperature.setText(highStr);
        dataBinding.primaryInfo.highTemperature.setContentDescription(lowAlly);

        float humidity = cursor.getFloat(INDEX_WEATHER_HUMIDITY);
        String humidityStr = getString(R.string.format_humidity, humidity);
        String humidityAlly = getString(R.string.a11y_humidity, humidityStr);
//        humidityTextView.setText(humidityStr);
        dataBinding.details.humidity.setText(humidityStr);
        dataBinding.details.humidity.setContentDescription(humidityAlly);
        dataBinding.details.humidityLabel.setContentDescription(humidityAlly);

        float windSpeed = cursor.getFloat(INDEX_WEATHER_WIND_SPEED);
        float windDirection = cursor.getFloat(INDEX_WEATHER_DEGREES);
        String windStr = WeatherUtils.getFormattedWind(this, windSpeed, windDirection);
        String windA11y = getString(R.string.a11y_wind, windStr);
//        windTextView.setText(windStr);
        dataBinding.details.windMeasurement.setText(windStr);
        dataBinding.details.windMeasurement.setContentDescription(windA11y);
        dataBinding.details.windLabel.setContentDescription(windA11y);

        float pressure = cursor.getFloat(INDEX_WEATHER_PRESSURE);
        String pressureStr = getString(R.string.format_pressure, pressure);
        String pressureA11y = getString(R.string.a11y_pressure, pressureStr);
//        pressureTextView.setText(pressureStr);
        dataBinding.details.pressure.setText(pressureStr);
        dataBinding.details.pressure.setContentDescription(pressureA11y);
        dataBinding.details.pressureLabel.setContentDescription(pressureA11y);

        forecast = String.format("%s - %s -%s/%s", dateStr, descriptionStr, highStr, lowStr);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
