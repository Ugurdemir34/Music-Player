package com.example.uur.musicplayer_v200;


import java.util.ArrayList;

/**
 * Created by UĞUR on 28.03.2018.
 */

public class MyFilter
{
    public static ArrayList<Sarki> getFilterData(String ch, ArrayList<Sarki> newSarki)
    {
        ArrayList<Sarki>gecici = new ArrayList<>();
        if(ch.isEmpty())
        {
            gecici = newSarki;
        }
        else
        {
            for(Sarki s:newSarki)
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
}
