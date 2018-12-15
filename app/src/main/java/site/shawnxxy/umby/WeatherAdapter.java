package site.shawnxxy.umby;

import android.content.Context;
import android.support.constraint.solver.Cache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherAdapterViewHolder> {

    private String[] weatherData;

    public WeatherAdapter() {

    }

//    Cache of forecast weather data list
    public class WeatherAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView weatherDataTextView;

        public WeatherAdapterViewHolder(View view) {
            super(view);
            weatherDataTextView = view.findViewById(R.id.weather_data_textview);
        }
    }

    // Enough ViewHolders will be created to fill the screen and allow for scrolling.
    @Override
    public WeatherAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int weatherDataList = R.layout.weather_data_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachToParent = false;

        View view = inflater.inflate(weatherDataList, viewGroup, attachToParent);
        return new WeatherAdapterViewHolder(view);
    }

    // Update weather data (today) on fixed position
    @Override
    public void onBindViewHolder(WeatherAdapterViewHolder weatherAdapterViewHolder, int position) {
        String weatherToday = weatherData[position];
        weatherAdapterViewHolder.weatherDataTextView.setText(weatherToday);
    }

    // Control number of weather data list
    @Override
    public int getItemCount() {
        if (null == weatherData) {
            return 0;
        } else {
            return weatherData.length;
        }
    }

    // set weather data with no need to create new adapter
    public void setNewWeatherData(String[] currentWeatherData) {
        weatherData = currentWeatherData;
        notifyDataSetChanged();
    }
}
