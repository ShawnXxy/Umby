package site.shawnxxy.umby;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;

import site.shawnxxy.umby.utilities.NetworkUtils;
import site.shawnxxy.umby.utilities.WeatherJsonUtils;
import site.shawnxxy.umby.weatherData.Location;

public class MainActivity extends AppCompatActivity {

    // Field to display the weather
//    private TextView weatherDataTextView;
    private RecyclerView weatherDataRecyclerView;
    private WeatherAdapter weatherAdapter;
    // Field to display error message if any
    private TextView errorMsg;
    // Field to display progressbar if available
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        errorMsg = findViewById(R.id.error_msg);

        // Support vertical or horizontal orientations
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        weatherDataRecyclerView.setLayoutManager(linearLayoutManager);

        //
        weatherDataRecyclerView.setHasFixedSize(true);

        // Linking weather data with the views in the end of recyclerview list
        weatherAdapter = new WeatherAdapter();
        weatherDataRecyclerView.setAdapter(weatherAdapter);

        // ger reference for loading progress bar
        loadingProgressBar = findViewById(R.id.loading_progressbar);

        loadWeatherData();
    }

    // helper function to display weather data
    private void showWeatherData() {
        // if there is error msg
        errorMsg.setVisibility(View.INVISIBLE);
        weatherDataRecyclerView.setVisibility(View.VISIBLE);
    }

    // get location to load weather data
    private void loadWeatherData() {
        showWeatherData();

        String location = Location.getPrefLocation(this);
        new FetchWeatherTask().execute(location);
    }

    // network request
    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        // show loading progressbar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressBar.setVisibility(View.VISIBLE);
        }

        // request in background
        @Override
        protected String[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String location = params[0];
            URL weatherRequestUrl = NetworkUtils.buildUrl(location);

            try {
                String weatherJson = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);

                String[] weatherData = WeatherJsonUtils.parseWeatherJson(MainActivity.this, weatherJson);

                return weatherData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        // Display result
        @Override
        protected void onPostExecute(String[] weatherData) {
            // hide progressbar
            loadingProgressBar.setVisibility(View.INVISIBLE);

            if (weatherData != null) {
                showWeatherData();
//                for (String weather : weatherData) {
//                    weatherDataTextView.append(weather + "\n\n\n");
//                }
                weatherAdapter.setNewWeatherData(weatherData);
            } else {
                // hide weather data
                weatherDataRecyclerView.setVisibility(View.INVISIBLE);
                // display error message
                errorMsg.setVisibility(View.VISIBLE);
            }
        }
    }

    // inflate the menu: return true to display the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forecast, menu);
        return true;
    }

    // Click refresh button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
//            weatherDataTextView.setText("");
            weatherAdapter.setNewWeatherData(null);
            loadWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
