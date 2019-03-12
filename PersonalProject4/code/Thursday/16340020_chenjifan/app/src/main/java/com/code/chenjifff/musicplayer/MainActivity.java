package com.code.chenjifff.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Observable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PathDashPathEffect;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{
    private ImageView playButton;
    private SeekBar seekBar;
    private CircleImageView circleImageView;
    private TextView currentDuration;
    private SimpleDateFormat simpleDateFormat;
    private IBinder iBinder;
    private int duration;
    private static String[] PERMISSIONS_STORAGE= {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBinder = service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iBinder = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleImageView = (CircleImageView) findViewById(R.id.cover_image);
        playButton = (ImageView) findViewById(R.id.play_button);
        playButton.setOnClickListener(this);
        playButton.setTag(0);
        ImageView stopButton = (ImageView) findViewById(R.id.stop_button);
        stopButton.setOnClickListener(this);
        ImageView exitButton = (ImageView) findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
        currentDuration = (TextView) findViewById(R.id.current_duration);

        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(this);

        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        //申请读写内存权限
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, 1);
        }
        else {
            String path = Environment.getExternalStorageDirectory() + "/Music/山高水长.mp3";
            bindViewsToMusic(path);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1) {
            String path = Environment.getExternalStorageDirectory() + "/Music/山高水长.mp3";
            bindViewsToMusic(path);
        }
    }

    public void bindViewsToMusic(String path) {
        //创建MediaMetadataRetriever
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        TextView musicName = (TextView) findViewById(R.id.music_name);
        musicName.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        TextView musicAuthor = (TextView) findViewById(R.id.music_author);
        musicAuthor.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        duration = Integer.valueOf(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        seekBar.setMax(duration);
        TextView totalDuration = (TextView) findViewById(R.id.total_duration);
        simpleDateFormat = new SimpleDateFormat("mm:ss");
        totalDuration.setText(simpleDateFormat.format(duration));
        byte[] pictureData = mediaMetadataRetriever.getEmbeddedPicture();
        Bitmap bitmap = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);
        circleImageView.setImageBitmap(bitmap);
        mediaMetadataRetriever.release();
    }

    PlayThread playThread = new PlayThread();

    int oneSecond = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case -1:
                    handler.removeCallbacksAndMessages(null);
                    break;
                default:
                    seekBar.setProgress(msg.arg1);
                    circleImageView.setRotation(circleImageView.getRotation() + 0.24f);
                    handler.postDelayed(playThread, 40);
                    currentDuration.setText(simpleDateFormat.format(msg.arg1));
            }
        }
    };

    class PlayThread extends Thread {
        @Override
        public void run() {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                iBinder.transact(4, data, reply, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            int currentMsc = reply.readInt();
            Message msg = handler.obtainMessage(1);
            msg.arg1 = currentMsc;
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.play_button:
                //暂停
                if((Integer)playButton.getTag() == 1) {
                    playButton.setImageResource(R.drawable.play);
                    playButton.setTag(0);
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    try {
                        iBinder.transact(1, data, reply, 0);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    //handler.obtainMessage(-1).sendToTarget();
                }
                //开始播放/继续播放
                else {
                    playButton.setImageResource(R.drawable.pause);
                    playButton.setTag(1);
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    try {
                        iBinder.transact(0, data, reply, 0);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    updateProgressBar();
                    //playThread.run();
                }
                break;
            case R.id.stop_button:
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    iBinder.transact(2, data, reply, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                seekBar.setProgress(0);
                playButton.setImageResource(R.drawable.play);
                playButton.setTag(0);
                circleImageView.setRotation(0);
                currentDuration.setText(simpleDateFormat.format(0));
                //handler.obtainMessage(-1).sendToTarget();
                break;
            case R.id.exit_button:
                unbindService(serviceConnection);
                this.finish();
                System.exit(0);
                break;
            default:
                break;
        }
    }

    public void updateProgressBar() {
        final Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 100; i++) {
                    if (i % 20 == 0) {
                        try {
                            Thread.sleep(500); //模拟下载的操作。
                        } catch (InterruptedException exception) {
                            if (!e.isDisposed()) {
                                e.onError(exception);
                            }
                        }
                        e.onNext(i);
                    }
                }
                e.onComplete();
            }

        });
        DisposableObserver<Integer> disposableObserver = new DisposableObserver<Integer>() {

            @Override
            public void onNext(Integer value) {
                Log.d("BackgroundActivity", "onNext=" + value);
                mTvDownloadResult.setText("Current Progress=" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("BackgroundActivity", "onError=" + e);
                mTvDownloadResult.setText("Download Error");
            }

            @Override
            public void onComplete() {
                Log.d("BackgroundActivity", "onComplete");
                mTvDownloadResult.setText("Download onComplete");
            }
        };
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(disposableObserver);
        mCompositeDisposable.add(disposableObserver);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser) {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInt(progress);
            try {
                iBinder.transact(3, data, reply, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            currentDuration.setText(simpleDateFormat.format(progress));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
