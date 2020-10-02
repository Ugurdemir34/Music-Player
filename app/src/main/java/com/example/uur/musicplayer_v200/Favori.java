package com.example.uur.musicplayer_v200;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class Favori extends AppCompatActivity
{

    ArrayList<Sarki> myfavlist = new ArrayList<>();
    RecyclerView rec;
    LinearLayoutManager mlinear;
    SearchView src;
    Ozel ozel;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favori);
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        setTitle(R.string.fav);
        src = (SearchView)findViewById(R.id.ara);
        rec= (RecyclerView)findViewById(R.id.rcyview);
        final View lyoynat = (View)findViewById(R.id.oynatly);
        lyoynat.setVisibility(View.INVISIBLE);
        final ArrayList<Sarki> gecici = dbhelper.favorikelimelistesi(db);
        ozel = new Ozel(this,gecici,MainActivity.media,lyoynat);
        rec.setAdapter(ozel);
        mlinear = new LinearLayoutManager(this);
        mlinear.setOrientation(LinearLayoutManager.VERTICAL);
        rec.setLayoutManager(mlinear);
        src.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s)
            {
                try
                {
                    ArrayList<Sarki> gecici2 = MyFilter.getFilterData(s.toLowerCase()+"",gecici);
                    ozel=new Ozel(Favori.this,gecici2,MainActivity.media,lyoynat);
                    rec.setAdapter(ozel);
                }
                catch (Exception ex)
                {
                    Toast.makeText(Favori.this,ex.getMessage()+"",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });


    }

    @Override
    public void onBackPressed()
    {
        Intent main = new Intent(Favori.this,MainActivity.class);
        startActivity(main);
    }
}




















