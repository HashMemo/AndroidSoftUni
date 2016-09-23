package com.example.asus.spotifydesign;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;

/**
 * Created by ASUS on 21.9.2016 г..
 */
public class MusicService extends Service{
    MediaPlayer player;
    private final IBinder mBinder = new MusicBinder();
    public class MusicBinder extends Binder {
        MusicService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MusicService.this;
        }
    }

    @Override
    public void onDestroy() {
        player.stop();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "Started", Toast.LENGTH_SHORT);
        player = new MediaPlayer();
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if(player.isPlaying()){
            player.stop();
        }else {
            try {
                player.setDataSource(intent.getStringExtra("songToPlay"));
                player.prepare();
                player.setLooping(true);
                player.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        double duration = (double) player.getDuration()/(60*1000);
        Log.d("Duration: ",String.valueOf(new DecimalFormat("##.##").format(duration)));
        Log.d("Transferred", intent.getStringExtra("songToPlay"));
        Log.d("Serive: ", "Started");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    public void onPause(){
        player.pause();
        double duration = (double) player.getDuration()/(60*1000);
        Log.d("Duration: ",String.valueOf(new DecimalFormat("##.##").format(duration)));

    }
    public void onPlay(){
        player.start();
    }
    public boolean isPlaying(){
        return player.isPlaying();
    }
    public void Reverse(){
        player.seekTo(player.getCurrentPosition()-2000);
    }
    public void Forward(){
        player.seekTo(player.getCurrentPosition()+2000);
    }
    public void stopMusicForNextOne(){
        player.stop();
    }
}
