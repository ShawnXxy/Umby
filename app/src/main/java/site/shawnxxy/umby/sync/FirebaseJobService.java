package site.shawnxxy.umby.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class FirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> fetchWeatherTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        fetchWeatherTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                SyncTask.syncWeather(context);
                jobFinished(job, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void avoid) {
                jobFinished(job, false);
            }
        };

        fetchWeatherTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (fetchWeatherTask != null) {
            fetchWeatherTask.cancel(true);
        }
        return true;
    }
}
