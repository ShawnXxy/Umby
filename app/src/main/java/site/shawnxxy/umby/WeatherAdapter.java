package site.shawnxxy.umby;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.constraint.solver.Cache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import site.shawnxxy.umby.utilities.DayUtils;
import site.shawnxxy.umby.utilities.WeatherUtils;
import site.shawnxxy.umby.weatherData.Location;

class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherAdapterViewHolder> {

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE = 1;

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

    private boolean useTodayLayout;

    private Cursor cursor;

//    public WeatherAdapter(WeatherAdapterOnCLickHandler handler) {
//        clickHandler = handler;
//    }
    public WeatherAdapter(@NonNull Context c, WeatherAdapterOnCLickHandler handler) {
        context = c;
        clickHandler = handler;
        useTodayLayout = context.getResources().getBoolean(R.bool.use_today_layout);
    }

    /**
     *     Enough ViewHolders will be created to fill the screen and allow for scrolling.
     */
    @Override
    public WeatherAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        int layoutId;

        switch (viewType) {
            // Use today layout for today
            case VIEW_TYPE_TODAY: {
                layoutId = R.layout.today_weather_list;
                break;
            }

            // Use list layout for future
            case VIEW_TYPE_FUTURE: {
                layoutId = R.layout.weather_data_list;
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

//        Context context = viewGroup.getContext();
//        int weatherDataList = R.layout.weather_data_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachToParent = false;

        View view = inflater.inflate(layoutId, viewGroup, attachToParent);
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

        /**
        *   Match weather icon
         */
        int weatherId = cursor.getInt(MainActivity.INDEX_WEATHER_CONDITION_ID);
        int weatherImageId;
//        weatherImageId = WeatherUtils.getSmallWeatherIcon(weatherId);

        int viewType = getItemViewType(position);

        switch (viewType) {
            // display large icon if layout is for today
            case VIEW_TYPE_TODAY: {
                weatherImageId = WeatherUtils.getLargeWeatherIcon(weatherId);
                break;
            }

            case VIEW_TYPE_FUTURE: {
                weatherImageId = WeatherUtils.getSmallWeatherIcon(weatherId);
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);

        }

        weatherAdapterViewHolder.iconImageView.setImageResource(weatherImageId);

        /**
         *          read data from the cursor based on date
          */
        long dateInMillis = cursor.getLong(MainActivity.INDEX_WEATHER_DATE);
        String dateStr = DayUtils.dateFormatted(context, dateInMillis, false);
        weatherAdapterViewHolder.dateTextView.setText(dateStr);

        /**
         *         Use weather id to get weather description
         */
        String descriptionStr = WeatherUtils.getWeatherCondition(context, weatherId);
        String descriptionAlly = context.getString(R.string.a11y_forecast, descriptionStr);
        weatherAdapterViewHolder.descriptionTextView.setText(descriptionStr);
        weatherAdapterViewHolder.descriptionTextView.setContentDescription(descriptionAlly);

        /**
         *         get high and low temperature
          */
        double highInCels = cursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMPERATURE);
        double lowInCels = cursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMPERATURE);
        String highStr = WeatherUtils.formatTemperature(context, highInCels);
        String lowStr = WeatherUtils.formatTemperature(context, lowInCels);
        String highAlly = context.getString(R.string.a11y_high_temp, highStr);
        String lowAlly = context.getString(R.string.a11y_low_temp, lowStr);
        weatherAdapterViewHolder.highTemperatureTextView.setText(highStr);
        weatherAdapterViewHolder.highTemperatureTextView.setContentDescription(highAlly);
        weatherAdapterViewHolder.lowTemperatureTextView.setText(lowStr);
        weatherAdapterViewHolder.lowTemperatureTextView.setContentDescription(lowAlly);

//        String highAndLow = WeatherUtils.formatHighLow(context, highInCels, lowInCels);
//        String weatherSummary = dateStr + " - " + description + " - " + highAndLow;
//        weatherAdapterViewHolder.weatherDataTextView.setText(weatherSummary);

    }

    /**
     *     Cache of forecast weather data list
     */
    class WeatherAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

//        final TextView weatherDataTextView;
        final TextView dateTextView;
        final TextView descriptionTextView;
        final TextView highTemperatureTextView;
        final TextView lowTemperatureTextView;
        final ImageView iconImageView;

        WeatherAdapterViewHolder(View view) {
            super(view);
//            weatherDataTextView = view.findViewById(R.id.weather_data_textview);
            dateTextView = view.findViewById(R.id.date);
            descriptionTextView = view.findViewById(R.id.weather_description);
            highTemperatureTextView = view.findViewById(R.id.high_temperature);
            lowTemperatureTextView = view.findViewById(R.id.low_temperature);
            iconImageView = view.findViewById(R.id.weather_icon);

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

    @Override
    public int getItemViewType(int position) {
        if (useTodayLayout && position == 0) {
            return VIEW_TYPE_TODAY;
        } else {
            return VIEW_TYPE_FUTURE;
        }
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
