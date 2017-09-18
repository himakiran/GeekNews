package biz.chundi.geeknews.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by userhk on 18/09/17.
 */

public class SyncNewsAuthService extends Service {
    // Instance field that stores the authenticator object
    private SyncNewsAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new SyncNewsAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
