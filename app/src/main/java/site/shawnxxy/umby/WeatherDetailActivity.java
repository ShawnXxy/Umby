package site.shawnxxy.umby;

import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class WeatherDetailActivity extends AppCompatActivity {

    private static final String WEATHER_SHARE_TAG = "#Umby";

    private String forecast;
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
                forecast = intentToStartActivity.getStringExtra(Intent.EXTRA_TEXT);
                weatherDetail.setText(forecast);
            }
        }
    }

    /**
     *  Display menu and implement sharing
     */
    private Intent intentToShareForecast() {
        Intent share = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(forecast + WEATHER_SHARE_TAG)
                .getIntent();
        return share;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(intentToShareForecast());
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

        return super.onOptionsItemSelected(menuItem);
    }
}
