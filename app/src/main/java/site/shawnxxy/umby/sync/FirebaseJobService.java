package site.shawnxxy.umby.sync;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.AsyncTask;

public class FirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> fetchWeatherTask;

    @Override
    public boolean onStartJob(final JobParameters params) {

        fetchWeatherTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                SyncTask.syncWeather(context);
                jobFinished(params, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void avoid) {
                jobFinished(params, false);
            }
        };

        fetchWeatherTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        if (fetchWeatherTask != null) {
            fetchWeatherTask.cancel(true);
        }
        return true;
    }
}
