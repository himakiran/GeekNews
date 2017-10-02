package biz.chundi.geeknews.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import static biz.chundi.geeknews.Utility.LOG_TAG;

/**
 * Created by userhk on 21/09/17.
 */

public class SyncNewsAdapterService extends Service {

    private static SyncNewsAdapter syncNewsAdapter = null;
    private static final Object syncAdapterLock = new Object();
    public String LOG_TAG = SyncNewsAdapterService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

       // Log.d(LOG_TAG, "SyncNewsAdapterService called");

        /*
         * Create the sync adapter as a singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
        synchronized (syncAdapterLock) {
            if (syncNewsAdapter == null) {
                syncNewsAdapter = new SyncNewsAdapter(getApplicationContext(), true);
            }
        }
    }

    /**
     * Return an object that allows the system to invoke
     * the sync adapter.
     *
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        /*
         * Get the object that allows external processes
         * to call onPerformSync(). The object is created
         * in the base class code when the SyncAdapter
         * constructors call super()
         */
        return syncNewsAdapter.getSyncAdapterBinder();

    }
}
