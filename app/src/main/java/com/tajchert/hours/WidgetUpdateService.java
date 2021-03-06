package com.tajchert.hours;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.tajchert.hours.widgets.Widget;
import com.tajchert.hours.widgets.WidgetInstance;
import com.tajchert.hours.widgets.WidgetListManager;

import java.util.ArrayList;

public class WidgetUpdateService extends Service {

	private void widgetStartUpdate(AppWidgetManager appWidgetManager, SharedPreferences prefs, WidgetInstance widget) {
		if (widget == null) {
			Log.e(Tools.AWESOME_TAG, "Widget null");
			stopSelf();
			return;
		}
		if (widget.resolution == 0) {
			widget.resolution = Widget.adaptResolution(this);
			WidgetListManager.updateWidget(widget.id, prefs, widget);
		}
		Log.d(Tools.AWESOME_TAG, "STYLE:" + widget.style);
		Widget.updateAppWidget(prefs, this, appWidgetManager, widget.id,widget);
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	Log.d(Tools.AWESOME_TAG, "onStart WidgetUpdateService");
        SharedPreferences prefs = this.getSharedPreferences("com.tajchert.hours", Context.MODE_PRIVATE);
        long timeNow = System.currentTimeMillis();
        if(timeNow-prefs.getLong(Tools.TIME_WIDGET_ALL_UPDATES, 0)>1200){
        	AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
			ArrayList<WidgetInstance> widgets = WidgetListManager.getWidgets(prefs);
			for(WidgetInstance widget: widgets){
				widgetStartUpdate(appWidgetManager, prefs, widget);
			}
			prefs.edit().putLong(Tools.TIME_WIDGET_ALL_UPDATES, timeNow).apply();
        }
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {        
        return null;
    }

}
