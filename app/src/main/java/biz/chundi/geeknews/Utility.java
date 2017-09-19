package biz.chundi.geeknews;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biz.chundi.geeknews.data.model.ArticleResponse;
import biz.chundi.geeknews.data.model.remote.NewsService;
import biz.chundi.geeknews.data.model.remote.RetrofitClient;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by userhk on 18/09/17.
 */

public class Utility {

    private  static String sortOrder = "top";
    private  static String newsSource = "engadget";
    public static String LOG_TAG = Utility.class.getSimpleName();

    public Utility(){

    }

    public static void setSortOrder(String sort){
        sortOrder = sort;
    }

    public static void setNewsSource(String newsSrc){
        newsSource =newsSrc;
    }

    public static String getSortOrder(){
        return sortOrder;
    }

    public static String getNewsSource(){
        return newsSource;
    }

    public static final String BASE_URL = "https://newsapi.org/";

    public static NewsService getNewsService() {
        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);
    }
//    public static String getJsonFromNewsAPI(String src, String sort){
//
//
//        NewsService mService = getNewsService();
//
//
//        // Execute the call asynchronously. Get a positive or negative callback.
//        mService.getArticles(src,sort,BuildConfig.API_KEY).enqueue(new Callback<ArticleResponse>() {
//            @Override
//            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
//
//                if(response.isSuccessful()){
//                    Log.d(LOG_TAG," : " + response.body().getArticles().toString());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ArticleResponse> call, Throwable t) {
//                Log.d(LOG_TAG," ERROR : "+t.toString());
//
//            }
//        });
//
//        return null;
//    }

}
