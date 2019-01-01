package site.shawnxxy.umby;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import site.shawnxxy.umby.utilities.FakeDataUtils;
import site.shawnxxy.umby.utilities.SyncUtils;
import site.shawnxxy.umby.weatherData.Location;
import site.shawnxxy.umby.weatherData.WeatherContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, WeatherAdapter.WeatherAdapterOnCLickHandler{

    private final String TAG = MainActivity.class.getSimpleName();

    public static final String[] MAIN_FORECAST_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMPERATURE,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMPERATURE,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };

    /**
     *  Index used to quickly query in SQL db
     */
    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMPERATURE = 1;
    public static final int INDEX_WEATHER_MIN_TEMPERATURE = 2;
    public static final int INDEX_WEATHER_CONDITION_ID = 3;

    private static final int FORECAST_LOADER_ID = 596; // number is dummy

    /**
     *      Field to display the weather
      */
//    private TextView weatherDataTextView;
    private RecyclerView weatherDataRecyclerView;
    private WeatherAdapter weatherAdapter;
    private int position = RecyclerView.NO_POSITION;

    /**
     *      Field to display error message if any
     */
//    private TextView errorMsg;

    /**
     *      Field to display progressbar if available
     */
    private ProgressBar loadingProgressBar;

//    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0f);

        /**
         *  For testing ONLY.
         */
//        FakeDataUtils.insertFakeData(this);

        //get reference for weather data
        weatherDataRecyclerView = findViewById(R.id.weather_data_recyclerview);

        //dummy data for test
//        String[] dummyWeatherPool = {
//                "Today, May 17 - Clear - 17°C / 15°C",
//                "Tomorrow - Cloudy - 19°C / 15°C",
//                "Thursday - Rainy- 30°C / 11°C",
//                "Friday - Thunderstorms - 21°C / 9°C",
//                "Saturday - Thunderstorms - 16°C / 7°C",
//                "Sunday - Rainy - 16°C / 8°C",
//                "Monday - Partly Cloudy - 15°C / 10°C",
//                "Tue, May 24 - Meatballs - 16°C / 18°C",
//                "Wed, May 25 - Cloudy - 19°C / 15°C",
//                "Thu, May 26 - Stormy - 30°C / 11°C",
//                "Fri, May 27 - Hurricane - 21°C / 9°C",
//                "Sat, May 28 - Meteors - 16°C / 7°C",
//                "Sun, May 29 - Apocalypse - 16°C / 8°C",
//                "Mon, May 30 - Post Apocalypse - 15°C / 10°C",
//        };
//        // iterate the dummy to textview
//        for (String dummyWeatherData : dummyWeatherPool) {
//            weatherDataTextView.append(dummyWeatherData + "\n\n\n");
//        }

        // get reference for error message
//        errorMsg = findViewById(R.id.error_msg);

        // ger reference for loading progress bar
        loadingProgressBar = findViewById(R.id.loading_progressbar);

        // Support vertical or horizontal orientations
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        weatherDataRecyclerView.setLayoutManager(linearLayoutManager);

        //
        weatherDataRecyclerView.setHasFixedSize(true);

        // Linking weather data with the views in the end of recyclerview list
        weatherAdapter = new WeatherAdapter(this, this);
        weatherDataRecyclerView.setAdapter(weatherAdapter);

        showLoading();

        /**
         *          Implement AsyncTaskLoader
          */
//        int loaderId = FORECAST_LOADER_ID;
//        LoaderManager.LoaderCallbacks<String[]> callbacks = MainActivity.this;
//        Bundle bundleforLoader = null;
//        getSupportLoaderManager().initLoader(loaderId, bundleforLoader, callbacks);


        getSupportLoaderManager().initLoader(FORECAST_LOADER_ID, null, this);

        SyncUtils.StartSyncImmediately(this);

//        Log.d(TAG, "onCreate: registering preference changed listener");
//
//        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

//        loadWeatherData();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        switch (id) {
            case FORECAST_LOADER_ID:
                Uri forecastQueryUri = WeatherContract.WeatherEntry.CONTENT_URI;
                String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
                String selection = WeatherContract.WeatherEntry.getSqlSelectForToday();
                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_FORECAST_PROJECTION,
                        selection,
                        null,
                        sortOrder);
            default:
                throw new RuntimeException("Loader Not Impletementd: " + id);
        }

        /**
         *  REMOVE BELOW FOR REFRACTOR
         */
//        return new AsyncTaskLoader<String[]>(this) {
//
//            String[] weatherData = null;

//            @Override
//            protected void onStartLoading() {
//                if (weatherData != null) {
//                    deliverResult(weatherData);
//                } else {
//                    loadingProgressBar.setVisibility(View.VISIBLE);
//                    forceLoad();
//                }
//            }
//
//            @Override
//            public String[] loadInBackground() {
//                String locationQuery = Location.getPrefLocation(MainActivity.this);
//
//                URL weatherRequestUrl = NetworkUtils.buildUrl(locationQuery);
//
//                try {
//                    String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
//
//                    String[] jsonWeatherData = OpenWeatherMapJsonUtils.parseWeatherJson(MainActivity.this, jsonWeatherResponse);
//
//                    return jsonWeatherData;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//
//            public void deliverResult(String[] data) {
//                weatherData = data;
//                super.deliverResult(data);
//            }
//        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        weatherAdapter.swapCursor(data);
        if (position == RecyclerView.NO_POSITION) {
            position = 0;
        }
        weatherDataRecyclerView.smoothScrollToPosition(position);

        if (data.getCount() != 0) {
            showWeatherData();
        }

        /**
         *  REMOVE BELOW FOR REFRACTOR
         */
//        loadingProgressBar.setVisibility(View.INVISIBLE);
//        weatherAdapter.setNewWeatherData(data);
//        if (null == data) {
//            weatherDataRecyclerView.setVisibility(View.INVISIBLE);
//            errorMsg.setVisibility(View.VISIBLE);
//        } else {
//            showWeatherData();
//        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        weatherAdapter.swapCursor(null);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if (PREFERENCES_HAVE_BEEN_UPDATED) {
//            Log.d(TAG, "onStart: preferences were updated");
//            getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this);
//            PREFERENCES_HAVE_BEEN_UPDATED = false;
//        }
//    }

    /**
     *  REMOVE BELOW FOR REFRACTOR
     */
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
//    }

    /**
     *  Helper function used to hide weather data when refreshing
     *  REMOVE BELOW FOR REFRACTOR
     */
//    private void hideWeatherData() {
//        weatherAdapter.setNewWeatherData(null);
//    }

    /**
     *     helper function to display weather data
     */
    private void showWeatherData() {
        // if there is error msg
//        errorMsg.setVisibility(View.INVISIBLE);

        loadingProgressBar.setVisibility(View.INVISIBLE);
        weatherDataRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        weatherDataRecyclerView.setVisibility(View.INVISIBLE);
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     *     get location to load weather data
     *     REMOVE BELOW FOR REFRACTOR
     */
//    private void loadWeatherData() {
//        showWeatherData();
//
//        String location = Location.getPrefLocation(this);
//        new FetchWeatherTask().execute(location);
//    }

    /**
     *    Show message when is clicked
     */
    @Override
    public void onClick(long date) {
//        Context context = this;
//        Toast.makeText(context, weatherForDay, Toast.LENGTH_SHORT).show();

        // A new activity will start to load weather details if click on it
//        Class destination = WeatherDetailActivity.class;
//        Intent intentToStartDetailActivity = new Intent(context, destination);
        // Pass weather data to detail activity
//        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, weatherForDay);
//        startActivity(intentToStartDetailActivity);

        Intent weatherDetailIntent =  new Intent(MainActivity.this, WeatherDetailActivity.class);
        Uri uriForDateClicked = WeatherContract.WeatherEntry.buildWeatherUriWithDate(date);
        weatherDetailIntent.setData(uriForDateClicked);
        startActivity(weatherDetailIntent);
    }

    /**
     *     network request
     *     REMOVE BELOW FOR REFRACTOR
     */
//    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
//
//        // show loading progressbar
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            loadingProgressBar.setVisibility(View.VISIBLE);
//        }
//
//        // request in background
//        @Override
//        protected String[] doInBackground(String... params) {
//            if (params.length == 0) {
//                return null;
//            }
//
//            String location = params[0];
//            URL weatherRequestUrl = NetworkUtils.buildUrl(location);
//
//            try {
//                String weatherJson = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
//
//                String[] weatherData = OpenWeatherMapJsonUtils.parseWeatherJson(MainActivity.this, weatherJson);
//
//                return weatherData;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        /**
//         *         Display result
//         */
//        @Override
//        protected void onPostExecute(String[] weatherData) {
//            // hide progressbar
//            loadingProgressBar.setVisibility(View.INVISIBLE);
//
//            if (weatherData != null) {
//                showWeatherData();
////                for (String weather : weatherData) {
////                    weatherDataTextView.append(weather + "\n\n\n");
////                }
//                weatherAdapter.setNewWeatherData(weatherData);
//            } else {
//                // hide weather data
//                weatherDataRecyclerView.setVisibility(View.INVISIBLE);
//                // display error message
//                errorMsg.setVisibility(View.VISIBLE);
//            }
//        }
//    }

    /**
     *  Help function to load map
     */
    private void openLocationMap() {
//        String address = Location.getPrefLocation(this);

        double[] coords = Location.getLocationCoord(this);
        String lat = Double.toString(coords[0]);
        String lon = Double.toString(coords[1]);
//        Uri geoLocation = Uri.parse("geo:0,0?q=" + address);
        Uri geoLocation = Uri.parse("geo: " + lat + ", " + lon);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(TAG, "Couldn't get " + geoLocation.toString() + ", no receiving apps installed!");
        }
    }

    /**
     *   inflate the menu: return true to display the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forecast, menu);
        return true;
    }

    /**
     *     Click refresh button
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.action_refresh) {
////            weatherDataTextView.setText("");
////            weatherAdapter.setNewWeatherData(null);
//            hideWeatherData();
////            loadWeatherData();
//            getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this);
//            return true;
//        }

        // open map when clicked
        if (id == R.id.action_map) {
            openLocationMap();
            return true;
        }

        // open settings menu when clicked
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *  REMOVE BELOW FOR REFRACTOR
     */
//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
//        PREFERENCES_HAVE_BEEN_UPDATED = true;
//    }
}
