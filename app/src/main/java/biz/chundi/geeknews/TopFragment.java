package biz.chundi.geeknews;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import biz.chundi.geeknews.data.NewsContract;
import biz.chundi.geeknews.sync.NewsAccount;
import biz.chundi.geeknews.sync.SyncNewsAdapter;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link RecyclerViewAdapter.OnListArticleListener}
 * interface.
 */
public class TopFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    static final int COL_TABLE_NAME = 0;
    static final int COL_AUTHOR = 1;
    static final int COL_TITLE = 2;
    static final int COL_DESC = 3;
    static final int COL_URL = 4;
    static final int COL_URLIMG = 5;
    static final int COL_PUBDATE = 6;
    static final int COL_SRC = 7;
    static final int COL_SORTORDER = 8;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String SORTORDER = "top";
    private static final int NEWS_LOADER = 101;
    private static final String[] NEWS_ARTICLE_COLUMNS = {


            NewsContract.NewsArticleEntry.TABLE_NAME + "." + NewsContract.NewsArticleEntry._ID,
            NewsContract.NewsArticleEntry.COLUMN_AUTHOR,
            NewsContract.NewsArticleEntry.COLUMN_TITLE,
            NewsContract.NewsArticleEntry.COLUMN_DESC,
            NewsContract.NewsArticleEntry.COLUMN_URL,
            NewsContract.NewsArticleEntry.COLUMN_URLIMG,
            NewsContract.NewsArticleEntry.COLUMN_PUBDATE,
            NewsContract.NewsArticleEntry.COLUMN_SRC,
            NewsContract.NewsArticleEntry.COLUMN_SORTORDER
    };
    public String LOG_TAG = TopFragment.class.getSimpleName();

    SharedPreferences pref;
    private int mColumnCount = 1;
    private int mpos = ListView.INVALID_POSITION;
    private NewsCursorAdapter mNewsCursorAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TopFragment() {
    }


    @SuppressWarnings("unused")
    public static TopFragment newInstance(int columnCount) {
        TopFragment fragment = new TopFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        String[] from = {NewsContract.NewsArticleEntry.COLUMN_TITLE, NewsContract.NewsArticleEntry.COLUMN_DESC,
                NewsContract.NewsArticleEntry.COLUMN_URLIMG, NewsContract.NewsArticleEntry.COLUMN_PUBDATE,};
        int[] to = {R.id.title, R.id.description, R.id.article_image, R.id.pubDate};
        mNewsCursorAdapter = new NewsCursorAdapter(getContext(), 0, null, from, to, 0);
        pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        getLoaderManager().initLoader(NEWS_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_list, container, false);


        // Set the adapter
        if (view instanceof ListView) {

            ListView lView = (ListView) view;
            lView.setAdapter(mNewsCursorAdapter);
/*
        To code later
 */
//            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                    // CursorAdapter returns a cursor at the correct position for getItem(), or null
//                    // if it cannot seek to that position.
//                    Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
//                    Log.v("CHK-FORECASTFRAGMENT", cursor.getString(1));
//
//
//                    if (cursor != null) {
//                        String locationSetting = Utility.getPreferredLocation(getActivity());
//                        ((Callback) getActivity()).onItemSelected(WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
//                                locationSetting, cursor.getLong(COL_WEATHER_DATE)
//                        ));
//
//
//                    }
//                    // save the selected position.
//                    mpos = position;
//
//                }
//            });
            if (savedInstanceState != null && savedInstanceState.containsKey("select-pos")) {
                // The listview probably hasn't even been populated yet.  Actually perform the
                // swapout in onLoadFinished.
                mpos = savedInstanceState.getInt("select-pos");
            }


            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                // Below function launches the Retrofit to get JSON response
                loadArticles();
            } else {
                Toast.makeText(getContext(), " Internet not available ", Toast.LENGTH_LONG).show();
            }
        }

        return view;
    }

    public void loadArticles() {
        setUpContentSync(pref.getString("NewsSrc", "engadget"), SORTORDER);
        Log.d(LOG_TAG, "LoadArticles : " + pref.getString("NewsSrc", "engadget"));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mpos != ListView.INVALID_POSITION) {
            outState.putInt("select-pos", mpos);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(NEWS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        SyncNewsAdapter.performSync(pref.getString("NewsSrc", "engadget"), SORTORDER);

    }



    public void setUpContentSync(String src, String sort) {

        Log.d(LOG_TAG, " setUpContentSync ");

        NewsAccount.createSyncAccount(getContext());
        SyncNewsAdapter.performSync(src, sort);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri NewsArticlesUri = NewsContract.NewsArticleEntry.buildNewsArticleSUri();
        // This is the select criteria ie get all articles from the selected src and whose sortorder is top
        final String SELECTION = "((" +
                NewsContract.NewsArticleEntry.COLUMN_SRC + " == '" + pref.getString("NewsSrc", "engadget") + "') AND (" +
                NewsContract.NewsArticleEntry.COLUMN_SORTORDER + " == '" + SORTORDER + "' ))";


        return new CursorLoader(this.getContext(), NewsArticlesUri, NEWS_ARTICLE_COLUMNS, SELECTION, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        mNewsCursorAdapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mNewsCursorAdapter.swapCursor(null);

    }


}
