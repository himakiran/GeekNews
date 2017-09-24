package biz.chundi.geeknews.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;



/**
 * Created by himakirankumar on 18/09/17.
 */

public class NewsContract {

    /**
     * The Content Authority is a name for the entire content provider, similar to the relationship
     * between a domain name and its website. A convenient string to use for content authority is
     * the package name for the app, since it is guaranteed to be unique on the device.
     */
    public static final String CONTENT_AUTHORITY = "biz.chundi.geeknews";

    /**
     * The content authority is used to create the base of all URIs which apps will use to
     * contact this content provider.
     */
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * A list of possible paths that will be appended to the base URI for the news articles
     * table.
     */
    public static final String PATH_NEWS = "newsArticle";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    public static final class NewsArticleEntry implements BaseColumns {

        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;

        // Define the table schema
        public static final String TABLE_NAME ="articleTable";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_TITLE="title";
        public static final String COLUMN_DESC="description";
        public static final String COLUMN_URL="url";
        public static final String COLUMN_URLIMG="urlImage";
        public static final String COLUMN_PUBDATE="pubDate";
        public static final String COLUMN_SRC="newsSrc";

        // Define a function to build a URI to find a specific movie by it's identifier
        public static Uri buildNewsArticleUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }




    }



}

