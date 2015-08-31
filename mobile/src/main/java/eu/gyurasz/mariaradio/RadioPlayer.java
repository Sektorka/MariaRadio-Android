package eu.gyurasz.mariaradio;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.TimedMetaData;
import android.widget.Toast;

import java.io.IOException;

public class RadioPlayer extends IntentService implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnCompletionListener {

    private static final String ACTION_START = "eu.gyurasz.mariaradio.action.START";
    private static final String ACTION_STOP = "eu.gyurasz.mariaradio.action.STOP";
    private static final String EP_STREAM_URL = "eu.gyurasz.mariaradio.extra.STREAM_URL";

    private MediaPlayer mediaPlayer;

    private void initMediaPlayer(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(1, 1);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    public static void startActionPlay(Context context, String streamUrl) {
        Intent intent = new Intent(context, RadioPlayer.class);
        intent.setAction(ACTION_START);
        intent.putExtra(EP_STREAM_URL, streamUrl);
        context.startService(intent);
    }

    public static void startActionStop(Context context, String param1, String param2) {
        Intent intent = new Intent(context, RadioPlayer.class);
        intent.setAction(ACTION_STOP);
        context.startService(intent);
    }

    public RadioPlayer() {
        super("RadioPlayer");
        initMediaPlayer();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                final String streamUrl = intent.getStringExtra(EP_STREAM_URL);
                handleActionPlay(streamUrl);
            } else if (ACTION_STOP.equals(action)) {
                handleActionStop();
            }
        }
    }

    private void handleActionPlay(String streamUrl) {
        try {
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            else{

                mediaPlayer.setDataSource(streamUrl);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handleActionStop() {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(RadioPlayer.this, "Error: " + what + "/" + extra, Toast.LENGTH_LONG).show();
        System.out.println("Error: " + what + "/" + extra);
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //MainActivity.instance().getMainFragment().getIvPlay().setEnabled(true);
    }
}
