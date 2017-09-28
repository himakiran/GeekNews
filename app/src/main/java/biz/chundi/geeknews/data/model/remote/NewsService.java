package biz.chundi.geeknews.data.model.remote;

import biz.chundi.geeknews.data.model.ArticleResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by userhk on 19/09/17.
 * https://www.androidhive.info/2016/05/android-working-with-retrofit-http-library/
 * https://code.tutsplus.com/tutorials/getting-started-with-retrofit-2--cms-27792
 */

public interface NewsService {



        //https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=xxxxxxxxxxxxxx


        @GET("/v1/articles")
        Call<ArticleResponse> getArticles(@Query("source") String src,@Query("sortBy") String sortOrder,
                                          @Query("apiKey") String apiKey);

    //http://positionlogger.com/clean.php?url=https://blog.mozilla.org/blog/2017/09/26/firefox-quantum-beta-developer-edition/

        @GET("/clean.php")
        Call<ArticleResponse> getArticleText (@Query("url") String url);
}
