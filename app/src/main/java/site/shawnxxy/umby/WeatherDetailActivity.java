package site.shawnxxy.umby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class WeatherDetailActivity extends AppCompatActivity {

    private static final String WEATHER_SHARE_TAG = "#Umby";

    private String forcast;
    private TextView weatherDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);

        weatherDetail = findViewById(R.id.weather_details_textview);

        Intent intentToStartActivity = getIntent();

        // Display weather details that was passed from MainActivity
        if (intentToStartActivity != null) {
            if (intentToStartActivity.hasExtra(Intent.EXTRA_TEXT)) {
                forcast = intentToStartActivity.getStringExtra(Intent.EXTRA_TEXT);
                weatherDetail.setText(forcast);
            }
        }
    }
}
