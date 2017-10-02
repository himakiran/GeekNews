package biz.chundi.geeknews.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import biz.chundi.geeknews.MainActivity;
import biz.chundi.geeknews.R;

/**
 * Implementation of App Widget functionality.
 */
public class NewsAppWidget extends AppWidgetProvider {

    public static final String UPDATE_NEWS_ARTICLES = "android.appwidget.action.APPWIDGET_UPDATE";
    public static final String EXTRA_ITEM = "biz.chundi.geeknews.EXTRA_ITEM";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

//        CharSequence widgetText = context.getString(R.string.app_name);
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.news_app_widget);
//        views.setTextViewText(R.id.appwidget_text, widgetText);
//
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);

        Intent intent = new Intent(context, ListViewWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.news_app_widget);
        rv.setRemoteAdapter(appWidgetId, R.id.list_view_widget, intent);

        Intent startActivityIntent = new Intent(context,MainActivity.class);

        PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        rv.setPendingIntentTemplate(R.id.list_view_widget, startActivityPendingIntent);

        rv.setEmptyView(R.id.list_view_widget, R.id.empty_view);

        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public void onReceive(Context context, Intent intent){

        AppWidgetManager mgr = AppWidgetManager.getInstance(context);

        if (intent.getAction().equals(UPDATE_NEWS_ARTICLES)) {

            int appWidgetIds[] = mgr.getAppWidgetIds(new ComponentName(context,NewsAppWidget.class));

            Log.e("received", intent.getAction());

            mgr.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view_widget);



        }

        super.onReceive(context, intent);

    }
}

