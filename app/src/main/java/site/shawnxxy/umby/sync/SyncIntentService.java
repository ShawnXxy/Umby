package site.shawnxxy.umby.sync;

import android.app.IntentService;
import android.content.Intent;

public class SyncIntentService extends IntentService {

    public SyncIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SyncTask.syncWeather(this);
    }
}
