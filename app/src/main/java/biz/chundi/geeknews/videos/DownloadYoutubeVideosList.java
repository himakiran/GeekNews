package biz.chundi.geeknews.videos;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import biz.chundi.geeknews.BuildConfig;
import biz.chundi.geeknews.Utility;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by himakirankumar on 29/09/17.
 */

public class DownloadYoutubeVideosList extends AsyncTask<String, Void, ArrayList<String>> {

    public DownloadYoutubeVideosList(){

    }
    public String LOG_TAG = DownloadYoutubeVideosList.class.getSimpleName();
    ArrayList<String> videoIdsList;
    @Override
    protected ArrayList<String> doInBackground(String... params) {
        // https://www.googleapis.com/youtube/v3
//            /search?part=snippet
//                    &q=YouTube+Data+API
//                    &type=video
//                    &videoCaption=closedCaption
//                    &key={YOUR_API_KEY}
        Log.d(LOG_TAG," Search Parameter given is "+params[0]);
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Utility.YOUTUBE_URL).newBuilder();
        urlBuilder.addQueryParameter("part", "snippet");
        urlBuilder.addQueryParameter("q", params[0]);
        urlBuilder.addQueryParameter("key", BuildConfig.API_KEY_YOUTUBE);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Log.e(LOG_TAG , " ENTERED HERE");
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                JSONObject json = new JSONObject(response.body().string());
                JSONArray items =  json.getJSONArray("items");
                videoIdsList = getVideoIdsList(items);
                RecyclerAdapter.VideoID = videoIdsList;
                //Log.d(LOG_TAG,videoIdsList.toString());
                //Log.d(LOG_TAG," items length : "+items.length()+" "+items.toString());
                //Log.d(LOG_TAG,response.body().string());
            }
        } catch (Exception e){
            Log.e(LOG_TAG , " REQUEST FAIL");

        }
        return videoIdsList;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        super.onPostExecute(result);

        Log.d(LOG_TAG," RESULT "+result);
    }

    public ArrayList<String> getVideoIdsList(JSONArray jsonArray){

        ArrayList<String> result  = new ArrayList<>();
        JSONObject j , r;
        for(int i = 0 ; i < jsonArray.length() ; i++) {
            try {
                j = (JSONObject) jsonArray.get(i);

                r = (JSONObject) j.getJSONObject("id");
                Log.d(LOG_TAG," I = "+i+" "+r.toString());
                if(r.getString("kind").equals("youtube#video"))
                    result.add(i,r.getString("videoId"));
                else
                    result.add(null);
            } catch (Exception e) {
                Log.e(LOG_TAG,e.toString());
            }
        }
        return  result;
    }
}

