package com.example.uur.musicplayer_v200;

import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

/**
 * Created by UĞUR on 24.03.2018.
 */
public class Ozel extends RecyclerView.Adapter<Ozel.MyViewHolder>
{
    ArrayList<Sarki> mVeri;
    LayoutInflater inflater;
    LinearLayout ln;
   static Context main;
    public static MediaPlayer med;
    public static  View oynat;
    public static ImageButton stop;
    public static int oneTimeOnly = 0;
    private  static double startTime = 0;
    private static double finalTime = 0;
    static  TextView startTimeField;
    static TextView endTimeField;
    static Handler myHandler = new Handler();
    static int forwardTime = 5000;
    static int backwardTime= 5000;
    static   SeekBar seekbar;
    public static boolean isSongPlaying=false;
    DatabaseHelper dbHelper;
    public  static  ImageButton rewindbtn;
    public static  ImageButton forwardbtn;
    public static Sarki currentSong;
    public Ozel(Context context, ArrayList<Sarki> Sarki_Liste,MediaPlayer m, View oynat)
    {
        //inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater=LayoutInflater.from(context);// KISA YOL
        this.mVeri = Sarki_Liste;
        med=m;
        main=context;
        this.oynat=oynat;
        dbHelper = new DatabaseHelper(main);
        dbHelper.CreateDataBase();
        dbHelper.openDataBase();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
      View v=inflater.inflate(R.layout.list_item,parent,false);

      MyViewHolder holder = new MyViewHolder(v);
        return  holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
       Collections.sort(mVeri, new Comparator<Sarki>() {
          @Override
          public int compare(Sarki sarki, Sarki t1)
          {
              return  sarki.getAd().compareTo(t1.getAd());
          }
       });
       Collections.reverse(mVeri);
       Sarki dinlenen=mVeri.get(position);
       holder.setData(dinlenen,position);
    }
    @Override
    public int getItemCount()
    {
        return mVeri.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtad;
        TextView txtsanatci;
        TextView txtoynatsarki;
        ImageView sil;
        ImageView favori;
        ImageView rsm;
        int clickPos=0;
        long id=0;
        String imgpath;
        public MyViewHolder(final View itemView)
        {
            super(itemView);
            txtad=(TextView) itemView.findViewById(R.id.txtad);
            txtsanatci=(TextView)itemView.findViewById(R.id.txtsanatci);
            sil = (ImageView)itemView.findViewById(R.id.sil);
            favori = (ImageView)itemView.findViewById(R.id.fav);
            rsm=(ImageView) itemView.findViewById(R.id.img);
            stop = (ImageButton)oynat.findViewById(R.id.imgplay);
            rewindbtn = (ImageButton)oynat.findViewById(R.id.rewind);
            forwardbtn = (ImageButton)oynat.findViewById(R.id.forward);
            seekbar = (SeekBar)oynat.findViewById(R.id.seekBar);
            seekbar.setClickable(true);
            if(main.getClass()==Favori.class)
            {
                favori.setVisibility(View.INVISIBLE);
            }
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b)
                {
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar)
                {
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar)
                {
                    med.seekTo(seekBar.getProgress());
                }
            });
            endTimeField = (TextView)oynat.findViewById(R.id.txtend);
          endTimeField.setVisibility(View.INVISIBLE);
            startTimeField = (TextView)oynat.findViewById(R.id.txtstart);
          startTimeField.setVisibility(View.INVISIBLE);

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(isSongPlaying==true)
                    {
                        med.pause();
                        stop.setImageResource(R.drawable.play);
                        isSongPlaying=false;
                    }
                    else
                    {
                        baslat();
                        isSongPlaying=true;
                        stop.setImageResource(R.drawable.pause);
                    }
                    updateWidget();
                }
            });

            rewindbtn.setOnTouchListener(new View.OnTouchListener()
             {
                 @Override
                 public boolean onTouch(View view, MotionEvent motionEvent)
                 {
                     switch (motionEvent.getAction())
                     {
                         case MotionEvent.ACTION_DOWN:
                             med.seekTo((int)seekbar.getProgress()-10000);
                             return  true;


                     }
                     return false;
                 }
             });
            forwardbtn.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent)
                {
                    switch (motionEvent.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            med.seekTo((int)seekbar.getProgress()+10000);
                            return  true;


                    }
                    return false;
                }
            });


            sil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                  deleteItem(clickPos, id);
                }
            });
            ln = (LinearLayout)itemView.findViewById(R.id.ln);
            txtoynatsarki=(TextView)oynat.findViewById(R.id.txtSarki);
            //txtoynatsarki.setVisibility(View.INVISIBLE);
            ln.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                        playSong(id);
                        txtoynatsarki.setVisibility(View.VISIBLE);
                        isSongPlaying=true;
                        stop.setImageResource(R.drawable.pause);
                        currentSong =Sarki.findSarkiById(mVeri,id);

                        txtoynatsarki.setText(txtad.getText());
                        updateWidget();
                }
            });
            favori.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    String adi = txtad.getText().toString();
                    String sanatci = txtsanatci.getText().toString();
                    dbHelper.ekle(id,adi,sanatci);
                    Toast.makeText(main,"Favori Listesine Eklendi !",Toast.LENGTH_SHORT).show();
                }
            });
        }
        public void setData(Sarki dinlenen, int position)
        {
           txtad.setText(dinlenen.getAd());
           txtsanatci.setText(dinlenen.getSanatci());
           currentSong = dinlenen;
           id =  dinlenen.getId();


           try{
               if(imgpath!=null)
               {Bitmap bmImg = BitmapFactory.decodeFile(imgpath);
                   rsm.setImageBitmap(Bitmap.createScaledBitmap(bmImg, 60, 60, false));
                   rsm.setImageBitmap(bmImg);
               }
           }
           catch (Exception ex)
           {
               Toast.makeText(main,ex.getMessage()+"",Toast.LENGTH_SHORT).show();
           }
          this.clickPos=position;
        }
    }
    public static void playSong(long id)
    {
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,id);
        startTimeField.setVisibility(View.VISIBLE);
        endTimeField.setVisibility(View.VISIBLE);
        med.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try
        {
            med.reset();
            med.stop();

            med.setDataSource(main,contentUri);
            med.prepare();
            oneTimeOnly=0;

            baslat();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public  static void baslat()
    {
        seekbar.setProgress(0);
        med.start();
        finalTime = med.getDuration();
        startTime = med.getCurrentPosition();
        if(oneTimeOnly == 0)
        {
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }
        if(oynat.getVisibility() == View.INVISIBLE)
        {
            oynat.setVisibility(View.VISIBLE);
        }
        //Muziğin toplamda ne kadar süre oldugunu  endTimeField controller ına yazdırıyoruz...
        endTimeField.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes((long) finalTime)))
        );
        //Muziğin başladıgı andan itibaren gecen süreyi ,startTimeField controller ına yazdırıyoruz...
        startTimeField.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes((long) startTime)))
        );
        //Muziğin hangi sürede oldugunu gostermek icin, seekbar kullarak gosteriyoruz...
        seekbar.setProgress((int)startTime);

         myHandler.postDelayed(UpdateSongTime,100);
        //Muzik suan calan durumnda oldugu icin , pause true yapıyoruz(durdurulabilir olması icin...)
    }
    public void rewind(View view) {
        int temp = (int)startTime;
        if((temp-backwardTime)>0){
            startTime = startTime - backwardTime;
            med.seekTo((int) startTime);
        }
        else{
            //Muzigin çalıs suresi ilk 5 saniye gelmeden ,geri tusuna basarsanız kosulu icin uyarı yazdıyoruz
            Toast.makeText(main,"İlk 5 saniyedeyken muziği geri alamazsınız",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void forward(View view){
        int temp = (int)startTime;
        if((temp+forwardTime)<=finalTime){
            startTime = startTime + forwardTime;
            med.seekTo((int) startTime);
        }
        else{
            //Muzigin çalıs suresi son 5 saniye geldiginde ,ileri tusuna basarsanız kosulu icin uyarı yazdıyoruz
            Toast.makeText(main, "Son 5 saniyedeyken muziği ilerletemezsiz",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private static Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = med.getCurrentPosition();
            startTimeField.setText(String.format("%02d:%02d",TimeUnit.MILLISECONDS.toMinutes((long)startTime),TimeUnit.MILLISECONDS.toSeconds((long)startTime)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));
            //Muziğin hangi sürede oldugunu gostermek icin, seekbar kullarak gosteriyoruz...
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };
    public  void  deleteItem(final int position, final long id)
    {
        if(main.getClass()==Favori.class)
        {
            AlertDialog.Builder build = new AlertDialog.Builder(main, android.R.style.Animation);
            build.setTitle("Uyarı !");
            build.setMessage("Silmek İstediğinize Emin Misiniz ?");
            build.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    dbHelper.sil(id);
                    removeOnList(position);
                }
            });
            build.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            build.show();
        }
        else {
               removeOnList(position );
        }

        //notifyDataSetChanged(); ANİMASYONSUZ VE BİR USTTEKİ KODLARA GEREKYOK
    }
    public void removeOnList(int position)
    {
        mVeri.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,mVeri.size());
    }
    public ArrayList<Sarki> getFilterData(String ch)
    {
        ArrayList<Sarki>gecici = new ArrayList<>();
        if(ch.isEmpty())
        {
              gecici = mVeri;
        }
        else
        {
            for(Sarki s:mVeri)
            {
                if(s.getAd().toLowerCase().contains(ch))
                {
                    gecici.add(s);
                }
                else
                {
                }
            }
        }
        return gecici;
    }
    public void updateWidget()
    {
        Intent intent = new Intent(main, MyWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager manager = AppWidgetManager.getInstance(main);
        ComponentName component = new ComponentName(main, MyWidget.class);
        int[] ids = AppWidgetManager.getInstance(main).getAppWidgetIds(component);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        MyWidget my = new MyWidget();
        my.onUpdate(main,manager,ids);
    }


}
