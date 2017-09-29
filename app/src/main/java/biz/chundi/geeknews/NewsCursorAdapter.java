package biz.chundi.geeknews;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;


/**
 * Created by userhk on 25/09/17.
 */

public class NewsCursorAdapter extends SimpleCursorAdapter {
    private static final int VIEW_TYPE_TOP = 0;
    private static final int VIEW_TYPE_LATEST = 1;
    private static final int VIEW_TYPE_POPULAR = 2;
    private static final int VIEW_TYPE_COUNT = 3;

    private int layout_type;

    public NewsCursorAdapter(Context context, int layout_id, Cursor cursor, String[] from, int[] to, int flags) {
        super(context, layout_id, cursor, from, to, flags);
        layout_type = layout_id;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(layout_type);
        int layoutID = -1;
        switch (viewType) {
            case VIEW_TYPE_TOP:
                layoutID = R.layout.fragment_top;
                break;
            case VIEW_TYPE_LATEST:
                layoutID = R.layout.fragment_latest;
                break;
            case VIEW_TYPE_POPULAR:
                layoutID = R.layout.fragment_popular;
                break;
            default:
                layoutID = R.layout.fragment_top;
        }

        View view = LayoutInflater.from(context).inflate(layoutID, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setTag(vh);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder vh = (ViewHolder) view.getTag();

        int viewType = getItemViewType(layout_type);

        Resources res  = context.getResources();

        if (viewType == VIEW_TYPE_TOP) {
            if (cursor.getString(TopFragment.COL_URLIMG)==null) {
                vh.imgView.setImageDrawable(res.getDrawable(R.drawable.ic_launcher));
            } else{
                Picasso.with(context).load(cursor.getString(TopFragment.COL_URLIMG)).into(vh.imgView);
            }
            vh.title.setText(cursor.getString(TopFragment.COL_TITLE));
            vh.description.setText(cursor.getString(TopFragment.COL_DESC));
        }
        else if(viewType == VIEW_TYPE_LATEST) {
            if (cursor.getString(TopFragment.COL_URLIMG)==null) {
                vh.imgView.setImageDrawable(res.getDrawable(R.drawable.ic_launcher));
            } else{
                Picasso.with(context).load(cursor.getString(LatestFragment.COL_URLIMG)).into(vh.imgView);
            }
            vh.title.setText(cursor.getString(LatestFragment.COL_TITLE));
            vh.pubDate.setText(cursor.getString(LatestFragment.COL_PUBDATE));
        }

        else if(viewType == VIEW_TYPE_POPULAR) {
            if (cursor.getString(TopFragment.COL_URLIMG)==null) {
                vh.imgView.setImageDrawable(res.getDrawable(R.drawable.ic_launcher));
            } else{
                Picasso.with(context).load(cursor.getString(PopularFragment.COL_URLIMG)).into(vh.imgView);
            }
            vh.title.setText(cursor.getString(PopularFragment.COL_TITLE));
            vh.description.setText(cursor.getString(PopularFragment.COL_DESC));
        }

    }

        @Override
        public int getItemViewType ( int position){
            switch (position) {
                case 0:
                    return VIEW_TYPE_TOP;
                case 1:
                    return VIEW_TYPE_LATEST;
                case 2:
                    return VIEW_TYPE_POPULAR;
            }
            return 0;

        }

        @Override
        public int getViewTypeCount () {
            return VIEW_TYPE_COUNT;
        }

    public static class ViewHolder {
        public final ImageView imgView;
        public final TextView title;
        public final TextView description;
        public final TextView pubDate;


        public ViewHolder(View view) {
            imgView = (ImageView) view.findViewById(R.id.article_image);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            pubDate = (TextView) view.findViewById(R.id.pubDate);

        }
    }



    }



