package biz.chundi.geeknews.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import biz.chundi.geeknews.BuildConfig;
import biz.chundi.geeknews.Utility;
import biz.chundi.geeknews.data.model.ArticleResponse;
import biz.chundi.geeknews.data.model.remote.NewsService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static biz.chundi.geeknews.Utility.getNewsService;

/**
 * Created by userhk on 18/09/17.
 * http://blog.udinic.com/2013/07/24/write-your-own-android-sync-adapter
 * https://josiassena.com/building-a-sync-adapter-and-using-it-on-android/
 */

public class SyncNewsAdapterOriginal extends AbstractThreadedSyncAdapter {

    ContentResolver mContentResolver;
    public final String LOG_TAG = SyncNewsAdapterOriginal.class.getSimpleName();

    public SyncNewsAdapterOriginal(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncNewsAdapterOriginal(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();

    }

    @Override
    public void onPerformSync(Account account, Bundle bundle,
                              String s, ContentProviderClient contentProviderClient,
                              SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync Called.");

        String newsSource = Utility.getNewsSource();
        String sortOrder = Utility.getSortOrder();

        String forecastJsonStr = getJsonFromNewsAPI(newsSource,sortOrder);

        updateDBWithJsonData(forecastJsonStr);


    }
    public String getJsonFromNewsAPI(String src, String sort){

        NewsService mService = getNewsService();


        // Execute the call asynchronously. Get a positive or negative callback.
        mService.getArticles(src,sort,BuildConfig.API_KEY).enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {

                if(response.isSuccessful()){
                    Log.d(LOG_TAG," : " + response.toString());
                }

            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                Log.d(LOG_TAG," ERROR : "+t.toString());

            }
        });

        return null;
    }


    void updateDBWithJsonData(String jsonStr){

    }
}
