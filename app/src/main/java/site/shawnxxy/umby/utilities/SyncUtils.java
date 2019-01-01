package site.shawnxxy.umby.utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import site.shawnxxy.umby.sync.SyncIntentService;

public class SyncUtils {

    public static void startSyncImmediately (@NonNull final Context context) {
        Intent intentToSync = new Intent(context, SyncIntentService.class);
        context.startService(intentToSync);
    }
}
