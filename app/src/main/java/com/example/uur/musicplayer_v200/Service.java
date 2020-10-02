package com.example.uur.musicplayer_v200;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by UĞUR on 27.04.2018.
 */

public class Service extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new ViewFactory(this.getApplicationContext(), intent));
    }
}
