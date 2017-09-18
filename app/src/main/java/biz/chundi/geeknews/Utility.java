package biz.chundi.geeknews;

/**
 * Created by userhk on 18/09/17.
 */

public class Utility {

    private  static String sortOrder = "top";
    private  static String newsSource = "wired-de";

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
}
