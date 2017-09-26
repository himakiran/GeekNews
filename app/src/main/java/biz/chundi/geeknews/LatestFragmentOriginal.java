package biz.chundi.geeknews;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import biz.chundi.geeknews.data.model.Article;
import biz.chundi.geeknews.data.model.ArticleResponse;
import biz.chundi.geeknews.data.model.remote.NewsService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link RecyclerViewAdapter.OnListArticleListener}
 * interface.
 */
public class LatestFragmentOriginal extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    public String LOG_TAG = LatestFragmentOriginal.class.getSimpleName();
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private RecyclerViewAdapter.OnListArticleListener mListener;
    private RecyclerViewAdapter mAdapter;
    private NewsService mService;
    private static final String SORTORDER = "latest";
    public String PREF  = "ArticlePref";
    SharedPreferences pref ;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LatestFragmentOriginal() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LatestFragmentOriginal newInstance(int columnCount) {
        LatestFragmentOriginal fragment = new LatestFragmentOriginal();
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
        pref = getActivity().getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mService = Utility.getNewsService();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mAdapter = new RecyclerViewAdapter(getActivity(), new ArrayList<Article>(), new RecyclerViewAdapter.OnListArticleListener() {

                @Override
                public void onArticleClick(long id){

                }
            },1);

            recyclerView.setAdapter(mAdapter);
            recyclerView.setHasFixedSize(true);
            ConnectivityManager cm =
                    (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if(isConnected) {
                // Below function launches the Retrofit to get JSON response
                loadArticles();
            }
            else
            {
                Toast.makeText(context, " Internet not available ", Toast.LENGTH_LONG).show();
            }
        }
        return view;
    }

    public void loadArticles() {
        mService.getArticles(pref.getString("NewsSrc","engadget"),SORTORDER,BuildConfig.API_KEY).enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {

                if(response.isSuccessful()) {
                    mAdapter.updateArticles(response.body().getArticles());
                   // Log.d(LOG_TAG, "Articles loaded from NEWS API : "+ response.body().getArticles().toString());
                }else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                   // Log.d(LOG_TAG, " Error "+statusCode);
                }
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {

               // Log.d(LOG_TAG, " Error  "+t.toString());

            }
        });
    }

//

}