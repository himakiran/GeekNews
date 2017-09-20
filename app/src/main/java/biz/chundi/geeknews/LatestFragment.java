package biz.chundi.geeknews;

import android.content.Context;
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
import biz.chundi.geeknews.dummy.DummyContent;
import biz.chundi.geeknews.dummy.DummyContent.DummyItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static biz.chundi.geeknews.Utility.LOG_TAG;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link RecyclerViewAdapter.OnListArticleListener}
 * interface.
 */
public class LatestFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    public String LOG_TAG = LatestFragment.class.getSimpleName();
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private RecyclerViewAdapter.OnListArticleListener mListener;
    private RecyclerViewAdapter mAdapter;
    private NewsService mService;
    private static final String SORTORDER = "latest";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LatestFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LatestFragment newInstance(int columnCount) {
        LatestFragment fragment = new LatestFragment();
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
            });

            recyclerView.setAdapter(mAdapter);
            recyclerView.setHasFixedSize(true);
            // Below function launches the Retrofit to get JSON response
            loadArticles();
        }
        return view;
    }

    public void loadArticles() {
        mService.getArticles(Utility.getNewsSource(),SORTORDER,BuildConfig.API_KEY).enqueue(new Callback<ArticleResponse>() {
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


}
