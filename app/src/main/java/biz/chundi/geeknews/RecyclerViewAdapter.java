package biz.chundi.geeknews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import biz.chundi.geeknews.data.model.Article;


import java.util.List;



/**
 * {@link RecyclerView.Adapter} that can display a {@link Article} and makes a call to the
 * specified {@link OnListArticleListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private List<Article> mArticle;
    private Context context;
    private final OnListArticleListener mListener;

    public RecyclerViewAdapter(Context c, List<Article> items, OnListArticleListener listener) {
        mArticle = items;
        mListener = listener;
        context = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_top, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mArticle.get(position);
        holder.mTitleView.setText(mArticle.get(position).getTitle());
        holder.mDescriptionView.setText(mArticle.get(position).getDescription());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onArticleClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArticle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mDescriptionView;
        public Article mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mDescriptionView = (TextView) view.findViewById(R.id.description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDescriptionView.getText() + "'";
        }
    }



    public void updateArticles(List<Article> items) {
        mArticle = items;
        notifyDataSetChanged();
    }

    private  Article getItem(int adapterPosition) {
        return mArticle.get(adapterPosition);
    }

    public interface OnListArticleListener {
        void onArticleClick(long id);
    }
}
