package biz.chundi.geeknews.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class NewsContentProvider extends ContentProvider {

    public static final String LOG_TAG = NewsContentProvider.class.getSimpleName();
    private static final int NEWSARTICLE = 100;
    private static final int NEWSARTICLE_ID = 101;
    private static final int NEWSSRC = 102;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private NewsDBHelper mNewsDBHelper;

    public NewsContentProvider() {
    }

    /**
     * Builds a UriMatcher that is used to determine witch database request is being made.
     */
    public static UriMatcher buildUriMatcher() {
        String content = NewsContract.CONTENT_AUTHORITY;

        // All paths to the UriMatcher have a corresponding code to return
        // when a match is found (the ints above).
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, NewsContract.PATH_NEWS, NEWSARTICLE);
        matcher.addURI(content, NewsContract.PATH_NEWS + "/#", NEWSARTICLE_ID);

        return matcher;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mNewsDBHelper.getWritableDatabase();
        int rows; // Number of rows effected

        switch (sUriMatcher.match(uri)) {
            case NEWSARTICLE:
                rows = db.delete(NewsContract.NewsArticleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Because null could delete all rows:
        if (selection == null || rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case NEWSARTICLE:
                return NewsContract.NewsArticleEntry.CONTENT_TYPE;
            case NEWSARTICLE_ID:
                return NewsContract.NewsArticleEntry.CONTENT_ITEM_TYPE;
            case NEWSSRC:
                return NewsContract.NewsArticleEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mNewsDBHelper.getWritableDatabase();
        long _id;
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case NEWSARTICLE:
                _id = db.insert(NewsContract.NewsArticleEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = NewsContract.NewsArticleEntry.buildNewsArticleUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        // Use this on the URI passed into the function to notify any observers that the uri has
        // changed.
        getContext().getContentResolver().notifyChange(uri, null, true);
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        mNewsDBHelper = new NewsDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mNewsDBHelper.getWritableDatabase();
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case NEWSARTICLE:
                retCursor = db.query(
                        NewsContract.NewsArticleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                break;
            case NEWSARTICLE_ID:
                long _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        NewsContract.NewsArticleEntry.TABLE_NAME,
                        projection,
                        NewsContract.NewsArticleEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        final SQLiteDatabase db = mNewsDBHelper.getWritableDatabase();
        int rows;

        switch (sUriMatcher.match(uri)) {
            case NEWSARTICLE:
                rows = db.update(NewsContract.NewsArticleEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        getContext().getContentResolver().notifyChange(uri, null, true);
        return rows;

    }
}
