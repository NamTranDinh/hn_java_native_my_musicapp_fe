package com.aptech.mymusic.presentation.view.broadcast;

import static com.aptech.mymusic.presentation.view.service.MusicInteract.KEY_MUSIC_ACTION;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aptech.mymusic.presentation.view.service.MusicService;

public class MusicReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        if (!KEY_MUSIC_ACTION.equals(intent.getAction())) {
            return;
        }
        // Use to interact between foreground service and notification
        context.startService(new Intent(context, MusicService.class).putExtras(intent));
    }
}
