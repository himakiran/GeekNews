package biz.chundi.geeknews.data;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import biz.chundi.geeknews.widget.NewsAppWidget;

/**
 * Created by userhk on 18/09/17.
 */

public class NewsDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "newsArticles.db";
    public Context mContext;

    public NewsDBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }


    /**
     * Called when the database is first created.
     *
     * @param db The database being created, which all SQL statements will be executed on.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        addNewsArticleTable(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * Inserts the movie table into the database.
     *
     * @param db The SQLiteDatabase the table is being inserted into.
     */
    private void addNewsArticleTable(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + NewsContract.NewsArticleEntry.TABLE_NAME + " (" +
                        NewsContract.NewsArticleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        NewsContract.NewsArticleEntry.COLUMN_AUTHOR + " TEXT , " +
                        NewsContract.NewsArticleEntry.COLUMN_TITLE + " TEXT , " +
                        NewsContract.NewsArticleEntry.COLUMN_DESC + " TEXT , " +
                        NewsContract.NewsArticleEntry.COLUMN_URL + " TEXT , " +
                        NewsContract.NewsArticleEntry.COLUMN_URLIMG + " TEXT , " +
                        NewsContract.NewsArticleEntry.COLUMN_PUBDATE + " INTEGER,  " +
                        NewsContract.NewsArticleEntry.COLUMN_SRC + " TEXT,  " +
                        NewsContract.NewsArticleEntry.COLUMN_SORTORDER + " TEXT  " + ");"
        );

        Intent intent_article_update = new Intent(mContext, NewsAppWidget.class);
        intent_article_update.setAction(NewsAppWidget.UPDATE_NEWS_ARTICLES);

        mContext.sendBroadcast(intent_article_update);
    }

    // Helper function to retrieve titles of latest articles of selected news source for use in widget

    public String[] getTitles(SQLiteDatabase db, String newsSrc) {

        // return db.execSQL()

        String[] stringList;
        int i = 0;
        String retQuery = "SELECT " + NewsContract.NewsArticleEntry.COLUMN_TITLE + " FROM " +
                NewsContract.NewsArticleEntry.TABLE_NAME + " WHERE " + " (" + NewsContract.NewsArticleEntry.COLUMN_SRC +
                " = " + '"' + newsSrc + '"' + " )" + " ORDER BY " + NewsContract.NewsArticleEntry.COLUMN_PUBDATE
                + " DESC " + ";";

        Cursor c = db.rawQuery(retQuery, null);

        stringList = new String[c.getCount()];

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            String Title = c.getString(c.getColumnIndex("title"));
            stringList[i++] = Title; //This I use to create listlayout dynamically and show all the Titles in it
            c.moveToNext();
        }

        Log.d("DBHELPER", stringList.toString());
        db.close();
        return stringList;
    }

}
