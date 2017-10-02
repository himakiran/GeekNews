package biz.chundi.geeknews.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import biz.chundi.geeknews.R;
import biz.chundi.geeknews.Utility;
import biz.chundi.geeknews.data.NewsDBHelper;


/**
 * Created by userhk on 02/10/17.
 */

public class ListviewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    SharedPreferences pref;
    private Context mContext;
    private ArrayList<String> records;
    private NewsDBHelper newsDBHelper;


    public ListviewRemoteViewsFactory(Context context, Intent intent) {

        mContext = context;

    }

    @Override
    public void onCreate() {


        updateWidgetListView();

    }

    @Override
    public void onDataSetChanged() {

        updateWidgetListView();
    }

    @Override
    public void onDestroy() {

        records.clear();
        newsDBHelper.close();
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        Log.d("WidgetCreatingView", "WidgetCreatingView");
        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(),
                R.layout.news_app_widget_item);

        Log.d("Loading", records.get(i));
        remoteView.setTextViewText(R.id.item_widget, records.get(i));

        Bundle extras = new Bundle();

        extras.putInt(NewsAppWidget.EXTRA_ITEM, i);

        Intent fillInIntent = new Intent();

        fillInIntent.putExtra("news_article_title", records.get(i));

        fillInIntent.putExtras(extras);

        // Make it possible to distinguish the individual on-click

        // action of a given item

        remoteView.setOnClickFillInIntent(R.id.item_widget, fillInIntent);

        // Return the RemoteViews object.

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private void updateWidgetListView() {
        records = new ArrayList<String>();

        String newsSrc = Utility.getNewsSource();

        newsDBHelper = new NewsDBHelper(mContext);
        for (String each : newsDBHelper.getTitles(newsDBHelper.getReadableDatabase(), newsSrc)
                ) {
            records.add(each);
        }

    }
}
