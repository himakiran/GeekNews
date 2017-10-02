package biz.chundi.geeknews.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by userhk on 02/10/17.
 */

public class ListViewWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListviewRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
