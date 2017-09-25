package biz.chundi.geeknews.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by userhk on 18/09/17.
 */

public class NewsDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "newsArticles.db";

    public NewsDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Called when the database is first created.
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
     * @param db The SQLiteDatabase the table is being inserted into.
     */
    private void addNewsArticleTable(SQLiteDatabase db){
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
    }


}
