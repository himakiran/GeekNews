package biz.chundi.geeknews.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biz.chundi.geeknews.BuildConfig;
import biz.chundi.geeknews.NewsApiInterface;
import biz.chundi.geeknews.NewsApiResponse;
import biz.chundi.geeknews.Utility;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by userhk on 18/09/17.
 */

public class SyncNewsAdapter extends AbstractThreadedSyncAdapter {

    ContentResolver mContentResolver;
    public final String LOG_TAG = SyncNewsAdapter.class.getSimpleName();

    public SyncNewsAdapter(Context context, boolean autoInitialize) {
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

        String forecastJsonStr = getJsonFromNewsAPI(newsSource,sortOrder);

        updateDBWithJsonData(forecastJsonStr);


    }
    public String getJsonFromNewsAPI(String src, String sort){

        Map<String, String> data = new HashMap<>();
        data.put("source",src);
        data.put("sortBy",sort);
        data.put("apiKey", BuildConfig.API_KEY);

        String API_BASE_URL = "https://newsapi.org/";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit = builder.client( httpClient.build()).build();


        // Create a very simple REST adapter which points the NewsApi  endpoint.
        NewsApiInterface client =  retrofit.create(NewsApiInterface.class);

        // Fetch the JSON response
        Call<List<NewsApiResponse>> call = client.getJsonStr(data);

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<List<NewsApiResponse>> () {
            @Override
            public void onResponse(Call<List<NewsApiResponse>> call, Response<List<NewsApiResponse>> response) {
                // The network call was a success and we got a response
                // TODO: use the repository list and display it
                Log.v(LOG_TAG,response.toString());
            }

            @Override
            public void onFailure(Call<List<NewsApiResponse>> call, Throwable t) {
                // the network call was a failure
                // TODO: handle error
                Log.e(LOG_TAG,t.toString());

            }
        });

        return null;
    }

    void updateDBWithJsonData(String jsonStr){

    }
}
