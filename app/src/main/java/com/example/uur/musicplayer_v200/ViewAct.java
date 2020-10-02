package com.example.uur.musicplayer_v200;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by UĞUR on 27.04.2018.
 */

public class ViewAct extends Activity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        String word = getIntent().getStringExtra(MyWidget.EXTRA_WORD);
        if(word==null)
        {
            word="Servis hatası";
        }
        Toast.makeText(this,word,Toast.LENGTH_LONG).show();
        finish();
    }
}
