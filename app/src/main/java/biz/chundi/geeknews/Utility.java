package biz.chundi.geeknews;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import biz.chundi.geeknews.data.model.ArticleResponse;
import biz.chundi.geeknews.data.model.remote.NewsService;
import biz.chundi.geeknews.data.model.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by userhk on 18/09/17.
 */

public class Utility {

    private  static String sortOrder = "top";
    private  static String newsSource = "engadget";
    public static String LOG_TAG = Utility.class.getSimpleName();
    public static String result;
    public static ArrayList<String> videoIdsList;
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

    public static void setVideoIdsList(ArrayList<String> videosList){
        Log.d(LOG_TAG,"SET VIDEOS LIST : "+videoIdsList.toString());
        videoIdsList = videosList;
    }

    public static ArrayList<String> getVideoIdsList(){
        Log.d(LOG_TAG,"GET VIDEOS LIST : "+videoIdsList.toString());
        return videoIdsList;
    }

    public static final String BASE_URL = "https://newsapi.org/";

    public static final String ARTICLE_URL = "https://positionlogger.com/";

    public static final String YOUTUBE_URL = "https://www.googleapis.com/youtube/v3/search";


    public static NewsService getNewsService() {
        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);
    }




}
