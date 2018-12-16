package site.shawnxxy.umby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WeatherDetailActivity extends AppCompatActivity {

    private static final String WEATHER_SHARE_TAG = "#Umby";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
    }
}
