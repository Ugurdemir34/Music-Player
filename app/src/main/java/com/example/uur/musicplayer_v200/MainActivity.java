package com.example.uur.musicplayer_v200;

import android.Manifest;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView rec;
    Activity main;
    ArrayList<Sarki> sarkilar;
    ArrayList<Long> path;
    LinearLayoutManager mlinear;
    GridLayoutManager mgrid;
    Ozel adaptor;
    android.support.v7.app.ActionBar actbar;
    SearchView searchView;
    public static MediaPlayer media=new MediaPlayer();
    SeekBar skbar;
      View lyoynat;
    TextView txtsarki;
    private static  final int MY_PERMISSION_REQUEST=1;

    Intent favlar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main=this;
        path= new ArrayList<>();
        sarkilar=new ArrayList<>();
       favlar = new Intent(MainActivity.this,Favori.class);
        rec = (RecyclerView)findViewById(R.id.rcyview);
        actbar=getSupportActionBar();
        searchView = (SearchView)findViewById(R.id.ara);
        skbar =(SeekBar)findViewById(R.id.seekBar);
        lyoynat = (View)findViewById(R.id.oynatly);
      //lyoynat.setVisibility(View.INVISIBLE);
        txtsarki=lyoynat.findViewById(R.id.txtSarki);

        skbar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY));
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
            }
        }
        else
        {
            doStuff();
        }
       // txtsarki.startAnimation(AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left));
        adaptor = new Ozel(this,sarkilar,media,lyoynat);
        rec.setAdapter(adaptor);

        mlinear = new LinearLayoutManager(this);
        mlinear.setOrientation(LinearLayoutManager.VERTICAL);
        rec.setLayoutManager(mlinear);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s)
            {
                try
                {
                       // adaptor = new Ozel(main,sarkilar,media);
                       // ArrayList<Sarki> gecici2=adaptor.getFilterData(s.toLowerCase()+"");
                         ArrayList<Sarki> gecici2 = MyFilter.getFilterData(s.toLowerCase()+"",sarkilar);
                        // Ozel ozl = new Ozel(main,gecici2);
                         adaptor=new Ozel(main,gecici2,media,lyoynat);


                         rec.setAdapter(adaptor);
                }
                catch (Exception ex)
                {
                    Toast.makeText(main,ex.getMessage()+"",Toast.LENGTH_LONG).show();
                }
                return  false;
            }
        });
        //actbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#242d72")));
    }
    void  doStuff()
    {
        getMusic();
    }
    public  void  getMusic()
    {
        ContentResolver contentresolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentresolver.query(songUri,null,null,null,null);
        if(songCursor !=null &&  songCursor.moveToFirst()  )
        {
            int idColumn = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle= songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist= songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int artid = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            do{
                try
                {
                    long thisId = songCursor.getLong(idColumn);
                    String currentTitle=songCursor.getString(songTitle);
                    String currentArtist=songCursor.getString(songArtist);
                    long id = songCursor.getLong(artid);
                    Sarki gecici = new Sarki(thisId,currentTitle,currentArtist);
                    sarkilar.add(gecici);
                    path.add(thisId);
                }
                catch (Exception ex)
                {
                    Toast.makeText(main,ex.getMessage()+"",Toast.LENGTH_SHORT).show();
                }
            }while (songCursor.moveToNext());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.layout_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.vertical:
            mlinear.setOrientation(LinearLayoutManager.VERTICAL);

            rec.setLayoutManager(mlinear);
            break;
            case R.id.gridView:
                mgrid = new GridLayoutManager(this,2);

                rec.setLayoutManager(mgrid);
                break;
            case R.id.favoriler:
                this.finish();
                startActivity(favlar); break;

        }
        return super.onOptionsItemSelected(item);
    }


}
