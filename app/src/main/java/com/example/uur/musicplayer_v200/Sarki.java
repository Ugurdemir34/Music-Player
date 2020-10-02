package com.example.uur.musicplayer_v200;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by UĞUR on 24.03.2018.
 */

public class Sarki
{
    private long id;
    private String ad;
    private String sanatci;

    public Sarki (long sid,String sad,String ssanatci)
    {
        id = sid;
        ad=sad;
        sanatci=ssanatci;


    }
    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id=id;
    }
    public String getAd()
    {
        return this.ad;
    }
    public void setAd(String isim)
    {
        this.ad=isim;
    }
    public String getSanatci()
    {
        return this.sanatci;
    }
    public void setSanatci(String isim) {
        this.sanatci = isim;
    }
    public static Sarki findSarkiById(ArrayList<Sarki> srk,long id)
    {
        Sarki s=null;
        for(int i =0; i < srk.size();i++)
        {
            if(srk.get(i).getId() == id)
            {
                s = srk.get(i); break;
            }
        }
        return  s;
    }

}
