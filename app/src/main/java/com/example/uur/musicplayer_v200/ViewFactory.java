package com.example.uur.musicplayer_v200;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by UĞUR on 27.04.2018.
 */

public class ViewFactory implements RemoteViewsService.RemoteViewsFactory
{
    DatabaseHelper dbhelper;
    SQLiteDatabase db;
    private  ArrayList<Sarki> items=null;
    private Context ctxt=null;
    private int appWidgetId;
    public ViewFactory(Context ctxt, Intent intent) {
        this.ctxt = ctxt;
        dbhelper = new DatabaseHelper(ctxt);
        db = dbhelper.getReadableDatabase();
        items = dbhelper.favorikelimelistesi(db);
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }
    @Override
    public void onCreate() {

    }
    @Override
    public void onDataSetChanged() {

    }
    @Override
    public void onDestroy() {

    }
    @Override
    public int getCount() {
        return(items.size());
    }
    @Override
    public RemoteViews getViewAt(int position)
    {
        RemoteViews row=new RemoteViews(ctxt.getPackageName(),
                R.layout.widget_row);
        row.setTextViewText(android.R.id.text1, items.get(position).getAd());
        row.setTextViewText(android.R.id.text2, items.get(position).getSanatci());

        Intent i=new Intent();
        Bundle extras=new Bundle();
        extras.putString(MyWidget.EXTRA_WORD, items.get(position).getAd());
        PendingIntent clickPi = PendingIntent.getBroadcast(ctxt,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        row.setOnClickPendingIntent(R.id.widgetlinear,clickPi);

        i.putExtras(extras);
        row.setOnClickFillInIntent(android.R.id.text1, i);

        return(row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return(1);
    }

    @Override
    public long getItemId(int i) {
        return(i);
    }

    @Override
    public boolean hasStableIds() {
        return (true);
    }
}
