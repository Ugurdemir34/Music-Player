package com.example.uur.musicplayer_v200;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;

public class MyWidget extends AppWidgetProvider
{
    public static String BUTTON_REWIND = "com.example.uur.musiplayer_v200.MyWidget.btnrewind";
    public static String BUTTON_FORWARD = "com.example.uur.musiplayer_v200.MyWidget.btnforward";
    public static String BUTTON_PLAY = "com.example.uur.musiplayer_v200.MyWidget.btnplay";
    public static String EXTRA_WORD= "com.example.uur.musicplayer_v200.MyWidget.WORD";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
    {


    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {

        for(int i = 0; i < appWidgetIds.length;i++)
        {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
            Intent play = new Intent(BUTTON_PLAY);
            PendingIntent playpend = PendingIntent.getBroadcast(context,0,play,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widgetplay,playpend);
            imageEdit(mediaControl(),views);
            views.setTextViewText(R.id.widgetad,Ozel.currentSong.getAd());
            views.setTextViewText(R.id.widgetsanatci,Ozel.currentSong.getSanatci());

            Intent rewind = new Intent(BUTTON_REWIND);
            PendingIntent rewindpend = PendingIntent.getBroadcast(context,0,rewind,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widgetrewind,rewindpend);

            Intent forward = new Intent(BUTTON_FORWARD);
            PendingIntent forwardpend = PendingIntent.getBroadcast(context,0,forward,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widgetforward,forwardpend);

            Intent listclick = new Intent(EXTRA_WORD);
            PendingIntent pnd = PendingIntent.getBroadcast(context,0,listclick,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widgetlinear,pnd);

            Intent list_intent = new Intent(context,Service.class);
            list_intent .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetIds[i]);
            list_intent.setData(Uri.parse(list_intent.toUri(Intent.URI_INTENT_SCHEME)));
           // RemoteViews lst = new RemoteViews(context.getPackageName(),R.layout.my_widget);
            views.setRemoteAdapter(appWidgetIds[i],R.id.widgetlst,list_intent);

            Intent clickIntent = new Intent(context,ViewAct.class);
            PendingIntent clickPi = PendingIntent.getBroadcast(context,0,clickIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widgetlst,clickPi);



            appWidgetManager.updateAppWidget(appWidgetIds[i], views);


        }


    }
    @Override
    public void onEnabled(Context context)
    {
    }
    @Override
    public void onDisabled(Context context)
    {
    }

    public boolean mediaControl()
    {
        boolean deger=false;
        if(Ozel.med.isPlaying())
        {
           // v.setImageViewResource(R.id.widgetplay,R.drawable.widgetpause);
            deger = true;
        }
        if(!Ozel.med.isPlaying())
        {
          //  v.setImageViewResource(R.id.widgetplay,R.drawable.widgetplay);
            deger=false;
        }
        return  deger;
    }
    public void imageEdit(boolean deger,RemoteViews v)
    {
        if(deger)
        {
            v.setImageViewResource(R.id.widgetplay,R.drawable.widgetpause);
        }
        else if(deger==false)
        {
            v.setImageViewResource(R.id.widgetplay,R.drawable.widgetplay);

        }
    }
    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
        if(BUTTON_PLAY.equals(intent.getAction()))
        {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName playcomp = new ComponentName(context, MyWidget.class);
           // imageEdit(mediaControl(),views);
            if(mediaControl()==false)
            {
                Ozel.baslat();
                Ozel.stop.setImageResource(R.drawable.pause);
                Ozel.isSongPlaying=true;
                views.setImageViewResource(R.id.widgetplay,R.drawable.widgetpause);
            }
            else if(mediaControl())
            {
                Ozel.med.pause();
                Ozel.isSongPlaying=false;
                Ozel.stop.setImageResource(R.drawable.play);
                views.setImageViewResource(R.id.widgetplay,R.drawable.widgetplay);

            }


            appWidgetManager.updateAppWidget(playcomp,views);
        }
       else if(BUTTON_FORWARD.equals(intent.getAction()))
        {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName forwardcomp = new ComponentName(context, MyWidget.class);
            Ozel.med.seekTo((int)Ozel.seekbar.getProgress()+10000);


            appWidgetManager.updateAppWidget(forwardcomp,views);
        }
      else if(BUTTON_REWIND.equals(intent.getAction()))
        {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            Ozel.med.seekTo((int)Ozel.seekbar.getProgress()-10000);

            ComponentName rewindcomp = new ComponentName(context, MyWidget.class);
            Toast.makeText(context,"rewind",Toast.LENGTH_SHORT).show();
            appWidgetManager.updateAppWidget(rewindcomp,views);
        }



    }
}

