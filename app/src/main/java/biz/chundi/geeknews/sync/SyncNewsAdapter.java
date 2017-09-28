package biz.chundi.geeknews.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import biz.chundi.geeknews.BuildConfig;
import biz.chundi.geeknews.data.NewsContract;
import biz.chundi.geeknews.data.NewsDBHelper;
import biz.chundi.geeknews.data.model.Article;
import biz.chundi.geeknews.data.model.ArticleResponse;
import biz.chundi.geeknews.data.model.remote.NewsService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.src;
import static biz.chundi.geeknews.Utility.getArticleTextService;
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

        String newsSource = bundle.getString("newsSrc","engadget");
        String sortOrder = bundle.getString("sortOrder","top");

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
                    updateDBWithArticlesData(response.body().getSource(),response.body().getSortBy(),response.body().getArticles());

                }

            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                Log.d(LOG_TAG," ERROR : "+t.toString());

            }
        });

        return null;
    }


    void updateDBWithArticlesData(String newsSrc,String sortOrder, List<Article> articleList){

        Log.d(LOG_TAG,"No of Articles : "+articleList.size());

        Vector<ContentValues> cVVector = new Vector<ContentValues>(articleList.size());
        ContentValues ArticleValues;

        for(int index=0;index < articleList.size();index+=1){

            ArticleValues = new ContentValues();

            ArticleValues.put((NewsContract.NewsArticleEntry.COLUMN_AUTHOR),articleList.get(index).getAuthor());
            ArticleValues.put((NewsContract.NewsArticleEntry.COLUMN_TITLE),articleList.get(index).getTitle());
            ArticleValues.put((NewsContract.NewsArticleEntry.COLUMN_DESC),articleList.get(index).getDescription());
            ArticleValues.put((NewsContract.NewsArticleEntry.COLUMN_URL),articleList.get(index).getUrl());
            ArticleValues.put((NewsContract.NewsArticleEntry.COLUMN_URLIMG),articleList.get(index).getUrlToImage());
            ArticleValues.put((NewsContract.NewsArticleEntry.COLUMN_PUBDATE),articleList.get(index).getPublishedAt());
            ArticleValues.put((NewsContract.NewsArticleEntry.COLUMN_SRC),newsSrc);
            ArticleValues.put((NewsContract.NewsArticleEntry.COLUMN_SORTORDER),sortOrder);

            cVVector.add(index,ArticleValues);
        }
        Log.d(LOG_TAG," cVVector is : "+cVVector.toString());
        int inserted = 0;



            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = getContext().getContentResolver().bulkInsert(NewsContract.NewsArticleEntry.CONTENT_URI, cvArray);


            }

        Log.d(LOG_TAG," Records Inserted are : "+inserted);

        // To delete records older than five days

        /*
            https://stackoverflow.com/questions/11771580/deleting-android-sqllite-rows-older-than-x-days?rq=1
         */
        //String WHERE = "NewsContract.NewsArticleEntry.COLUMN_PUBDATE <= date('now','-5 day')";

        String sql = "DELETE FROM articleTable WHERE pubDate <= date('now','-5 day')";
        NewsDBHelper dbHelper = new NewsDBHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql);
        db.close();



    }

    /**
     * Manual force Android to perform a sync with our SyncAdapter.
     */
    public static void performSync(String src, String sortOrder) {
        Log.d(LOG_TAG, " perform Sync ");
        Bundle b = new Bundle();
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        b.putString("newsSrc",src);
        b.putString("sortOrder",sortOrder);
        ContentResolver.requestSync(NewsAccount.getAccount(),
                NewsContract.CONTENT_AUTHORITY, b);

    }

    // Using  https://positionlogger.com  to get article text
    public static void getArticleText(String url) {

        Log.d(LOG_TAG," : " +"Utility function getArticleText called");
        NewsService mService = getArticleTextService();
        final ArticleResponse articleResponse = new ArticleResponse();


        try {
            Response<ArticleResponse> response = mService.getArticleText(url).execute();
            articleResponse.setArticleText(response.body().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}
