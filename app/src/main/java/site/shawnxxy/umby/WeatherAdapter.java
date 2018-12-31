package site.shawnxxy.umby;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.constraint.solver.Cache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import site.shawnxxy.umby.utilities.DayUtils;
import site.shawnxxy.umby.utilities.WeatherUtils;
import site.shawnxxy.umby.weatherData.Location;

class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherAdapterViewHolder> {

    private final Context context;

//    private String[] weatherData;

    /**
     *  Interface for click event
     */
    final WeatherAdapterOnCLickHandler clickHandler;
    public interface WeatherAdapterOnCLickHandler {
//        void onClick(String weatherForDay);
        void onClick(long date);
    }

    private Cursor cursor;

//    public WeatherAdapter(WeatherAdapterOnCLickHandler handler) {
//        clickHandler = handler;
//    }
    public WeatherAdapter(@NonNull Context c, WeatherAdapterOnCLickHandler handler) {
        context = c;
        clickHandler = handler;
    }

    /**
     *     Enough ViewHolders will be created to fill the screen and allow for scrolling.
     */
    @Override
    public WeatherAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        Context context = viewGroup.getContext();
        int weatherDataList = R.layout.weather_data_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachToParent = false;

        View view = inflater.inflate(weatherDataList, viewGroup, attachToParent);
        view.setFocusable(true);
        return new WeatherAdapterViewHolder(view);
    }

    /**
     *     Update weather data (today) on fixed position
     */
    @Override
    public void onBindViewHolder(WeatherAdapterViewHolder weatherAdapterViewHolder, int position) {
//        String weatherToday = weatherData[position];
//        weatherAdapterViewHolder.weatherDataTextView.setText(weatherToday);

        cursor.moveToPosition(position);

        // read data from the cursor
        long dateInMillis = cursor.getLong(MainActivity.INDEX_WEATHER_DATE);
        String dateStr = DayUtils.dateFormatted(context, dateInMillis, false);

        // Use weather id to get weather description
        int weatherId = cursor.getInt(MainActivity.INDEX_WEATHER_CONDITION_ID);
        String description = WeatherUtils.getWeatherCondition(context, weatherId);

        // get high and low temperature
        double highInCels = cursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMPERATURE);
        double lowInCels = cursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMPERATURE);
        String highAndLow = WeatherUtils.formatHighLow(context, highInCels, lowInCels);

        String weatherSummary = dateStr + " - " + description + " - " + highAndLow;

        weatherAdapterViewHolder.weatherDataTextView.setText(weatherSummary);

    }

    /**
     *     Cache of forecast weather data list
     */
    class WeatherAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView weatherDataTextView;

        WeatherAdapterViewHolder(View view) {
            super(view);
            weatherDataTextView = view.findViewById(R.id.weather_data_textview);
            // Implement click listener
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
//            String weatherForDay = weatherData[adapterPosition];
            cursor.moveToPosition(adapterPosition);
//            String weatherForDay = weatherDataTextView.getText().toString();
            long dateInMillis = cursor.getLong(MainActivity.INDEX_WEATHER_DATE);
            clickHandler.onClick(dateInMillis);
        }
    }

    /**
     *     Control number of weather data list
     */
    @Override
    public int getItemCount() {
        if (null == cursor) {
            return 0;
        }
//            return weatherData.length;
        return cursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    /**
     *     set weather data with no need to create new adapter
     *     REMOVE BELOW FOR REFRACTOR
     */
//    public void setNewWeatherData(String[] currentWeatherData) {
//        weatherData = currentWeatherData;
//        notifyDataSetChanged();
//    }
}
