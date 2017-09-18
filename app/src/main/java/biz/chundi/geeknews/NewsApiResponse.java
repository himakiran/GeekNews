package biz.chundi.geeknews;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

/**
 * Created by userhk on 18/09/17.
 */

public class NewsApiResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("source")
    private String source;
    @SerializedName("sortBy")
    private String sortBy;
    @SerializedName("articles")
    private JSONArray articles;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public void setArticles(JSONArray articles) {
        this.articles = articles;
    }


    public NewsApiResponse(){

    }

    public String getStatus() {
        return status;
    }

    public String getSource() {
        return source;
    }

    public String getSortBy() {
        return sortBy;
    }

    public JSONArray getArticles() {
        return articles;
    }


}
