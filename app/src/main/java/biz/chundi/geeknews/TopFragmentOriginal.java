package biz.chundi.geeknews;

import android.accounts.Account;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import biz.chundi.geeknews.data.model.Article;
import biz.chundi.geeknews.data.model.ArticleResponse;
import biz.chundi.geeknews.data.model.remote.NewsService;
import biz.chundi.geeknews.sync.NewsAccount;
import biz.chundi.geeknews.sync.SyncNewsAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link RecyclerViewAdapter.OnListArticleListener}
 * interface.
 */
public class TopFragmentOriginal extends Fragment {


    private static final String ARG_COLUMN_COUNT = "column-count";
    public String LOG_TAG = TopFragmentOriginal.class.getSimpleName();

    private int mColumnCount = 1;
    private RecyclerViewAdapter.OnListArticleListener mListener;
    private RecyclerViewAdapter mAdapter;
    private NewsService mService;
    private static final String SORTORDER = "top";

    SharedPreferences pref ;

    // Constants
    // The authority for the sync adapter's content provider
    public String AUTHORITY = "biz.chundi.geeknews.data.NewsContentProvider";
    // An account type, in the form of a domain name
    public String ACCOUNT_TYPE = "geeknews.chundi.biz";
    // The account name
    public String ACCOUNT = "dummynewsaccount";
    // Instance fields
    public Account mAccount;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TopFragmentOriginal() {
    }


    @SuppressWarnings("unused")
    public static TopFragmentOriginal newInstance(int columnCount) {
        TopFragmentOriginal fragment = new TopFragmentOriginal();
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
        View view = inflater.inflate(R.layout.fragment_top_list, container, false);

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
            },0);

            recyclerView.setAdapter(mAdapter);
            recyclerView.setHasFixedSize(true);
            // Below function launches the Retrofit to get JSON response
            loadArticles();
        }

        return view;
    }

    public void loadArticles() {
        setUpContentSync(pref.getString("NewsSrc","engadget"),SORTORDER);
        Log.d(LOG_TAG,"LoadArticles : "+pref.getString("NewsSrc","engadget"));
        mService.getArticles(pref.getString("NewsSrc","engadget"),SORTORDER,BuildConfig.API_KEY).enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {

                if(response.isSuccessful()) {
                    mAdapter.updateArticles(response.body().getArticles());
                    Log.d(LOG_TAG, "Articles loaded from NEWS API : "+ response.body().getArticles().toString());
                }else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                    Log.d(LOG_TAG, " Error "+statusCode);
                }
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {

                Log.d(LOG_TAG, " Error  "+t.toString());

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecyclerViewAdapter.OnListArticleListener) {
            mListener = (RecyclerViewAdapter.OnListArticleListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void setUpContentSync(String src, String sort){

        Log.d(LOG_TAG," setUpContentSync ");

        NewsAccount.createSyncAccount(getContext());
        SyncNewsAdapter.performSync(src,sort);




    }







}