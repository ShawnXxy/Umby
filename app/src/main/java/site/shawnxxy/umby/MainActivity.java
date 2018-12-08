package site.shawnxxy.umby;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.net.URL;

import site.shawnxxy.umby.utilities.NetworkUtils;
import site.shawnxxy.umby.utilities.WeatherJsonUtils;
import site.shawnxxy.umby.weatherData.Location;

public class MainActivity extends AppCompatActivity {

    // Field to store the weather display
    private TextView weatherDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get reference
        weatherDataTextView = findViewById(R.id.weather_data_textview);
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

        loadWeatherData();
    }

    // get location to load weather data
    private void loadWeatherData() {
        String location = Location.getPrefLocation(this);
        new FetchWeatherTask().execute(location);
    }

    // network request
    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

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
            if (weatherData != null) {
                for (String weather : weatherData) {
                    weatherDataTextView.append(weather + "\n\n\n");
                }
            }
        }
    }
}
