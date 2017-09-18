package biz.chundi.geeknews;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import static android.R.attr.src;

/**
 * Created by userhk on 18/09/17.
 */

public interface NewsApiInterface {


    @GET("/v1/articles")
    Call<List<NewsApiResponse>> getJsonStr(
            @QueryMap Map<String, String> options

    );
}
