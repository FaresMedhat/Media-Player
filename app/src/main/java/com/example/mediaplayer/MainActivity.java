package com.example.mediaplayer;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton btn_play , btn_pause , btn_stop ;
    SeekBar sb_audio ;

    int sb_audio_progress;
Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.song);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        Runnable liveSeekBar = new Runnable() {
            public void run() {
                boolean playing = mediaPlayer.isPlaying();
                if(mediaPlayer!=null&&mediaPlayer.isPlaying()) {
                    sb_audio.setProgress(mediaPlayer.getCurrentPosition());
                    handler.postDelayed(this, 50);
                }
            }
        };

btn_play = findViewById(R.id.start_play);
 btn_play.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
         mediaPlayer.start();
         handler.post(liveSeekBar);
     }
 });
btn_pause = findViewById(R.id.pause_play);
btn_pause.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        mediaPlayer.pause();
        handler.removeCallbacks(liveSeekBar);
    }
});
btn_stop  = findViewById(R.id.stop_play);
btn_stop.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        mediaPlayer.stop();
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sb_audio_progress = 0;
        handler.removeCallbacks(liveSeekBar);

    }
});
sb_audio = findViewById(R.id.sb_audio);
        sb_audio.setMax(mediaPlayer.getDuration());
sb_audio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        sb_audio_progress = i ;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.seekTo(sb_audio_progress);
    }
});



    }
}