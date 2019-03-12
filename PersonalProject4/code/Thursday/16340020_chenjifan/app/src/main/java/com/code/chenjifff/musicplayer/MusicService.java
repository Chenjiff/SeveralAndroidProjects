package com.code.chenjifff.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class MusicService extends Service {
    public MediaPlayer mediaPlayer;

    public MusicBinder musicBinder = new MusicBinder();

    public class MusicBinder extends Binder {
        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @NonNull Parcel reply, int flags) throws RemoteException {
            switch (code) {
                //play
                case 0:
                    mediaPlayer.start();
                    break;
                //pause
                case 1:
                    mediaPlayer.pause();
                    break;
                //stop
                case 2:
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                case 3:
                    mediaPlayer.seekTo(data.readInt());
                case 4:
                    reply.writeInt(mediaPlayer.getCurrentPosition());
                default:
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(Environment.getExternalStorageDirectory() + "/Music/山高水长.mp3");
            mediaPlayer.prepare();
            mediaPlayer.seekTo(0);
            mediaPlayer.setLooping(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return musicBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
