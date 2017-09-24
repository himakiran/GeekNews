package biz.chundi.geeknews.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import biz.chundi.geeknews.BuildConfig;
import biz.chundi.geeknews.Utility;
import biz.chundi.geeknews.data.NewsContract;
import biz.chundi.geeknews.data.model.Article;
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

public class SyncNewsAdapter extends AbstractThreadedSyncAdapter {

    ContentResolver mContentResolver;
    public final static String LOG_TAG = SyncNewsAdapter.class.getSimpleName();

    public SyncNewsAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
        Log.d(LOG_TAG, "SyncNewsAdapterCalled");
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncNewsAdapter(
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

        getArticlesFromNewsAPI(newsSource,sortOrder);




    }
    public Void getArticlesFromNewsAPI(String src, String sort){

        Log.d(LOG_TAG, "getArticlesFromeNewsApi");

        NewsService mService = getNewsService();


        // Execute the call asynchronously. Get a positive or negative callback.
        mService.getArticles(src,sort,BuildConfig.API_KEY).enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {

                if(response.isSuccessful()){
                    Log.d(LOG_TAG," : " + response.toString());
                    updateDBWithArticlesData(response.body().getArticles());

                }

            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                Log.d(LOG_TAG," ERROR : "+t.toString());

            }
        });

        return null;
    }


    void updateDBWithArticlesData(List<Article> articleList){

        Log.d(LOG_TAG,"No of Articles : "+articleList.size());

    }

    /**
     * Manual force Android to perform a sync with our SyncAdapter.
     */
    public static void performSync() {
        Log.d(LOG_TAG, " perform Sync ");
        Bundle b = new Bundle();
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(NewsAccount.getAccount(),
                NewsContract.CONTENT_AUTHORITY, b);
    }
}
