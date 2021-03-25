package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.VerifiedInputEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    ImageButton implay, imff, imfr, imshuffle, imrepeat;
    TextView txtsong, txtstart, txtend;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;
    String songName;
    ArrayList<File> songs;
    int position;
    Thread threadSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        implay = (ImageButton) findViewById(R.id.implay);
        imff = (ImageButton) findViewById(R.id.imff);
        imfr = (ImageButton) findViewById(R.id.imfr);
        imshuffle = (ImageButton) findViewById(R.id.imshuffle);
        imrepeat = (ImageButton) findViewById(R.id.imrepeat);
        txtsong = (TextView) findViewById(R.id.txtsong);
        txtstart = (TextView) findViewById(R.id.txtstart);
        txtend = (TextView) findViewById(R.id.txtend);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        threadSeekBar = new Thread()
        {
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;
                while(currentPosition < totalDuration)
                {
                    try {
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        seekBar.setMax(mediaPlayer.getDuration());
        threadSeekBar.start();
        String endTime = convertTime(mediaPlayer.getDuration());
        txtend.setText(endTime);
        Handler handler = new Handler();
        int delay = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = convertTime(mediaPlayer.getCurrentPosition());
                txtstart.setText(currentTime);
                handler.postDelayed(this,delay);
            }
        },delay);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songs");
        String sname = bundle.getString("songName");
        position = bundle.getInt("position", 0);
        txtsong.setSelected(true);
        Uri uri = Uri.parse(songs.get(position).toString());
        songName = songs.get(position).getName();
        txtsong.setText(songName);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imff.performClick();
            }
        });

        implay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    implay.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                    mediaPlayer.pause();
                } else {
                    implay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                    mediaPlayer.start();
                }
            }
        });

        imff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = (position + 1) % songs.size();
                Uri updatedUri = Uri.parse(songs.get(position).toString());
                songName = songs.get(position).getName();
                txtsong.setText(songName);
                mediaPlayer = MediaPlayer.create(getApplicationContext(), updatedUri);
                mediaPlayer.start();
                implay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
            }
        });

        imfr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position - 1) < 0) ? (songs.size() - 1) : (position - 1);
                Uri updatedUri = Uri.parse(songs.get(position).toString());
                songName = songs.get(position).getName();
                txtsong.setText(songName);
                mediaPlayer = MediaPlayer.create(getApplicationContext(),updatedUri);
                mediaPlayer.start();
                implay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
            }
        });
    }

    public String convertTime(int duration) {
        int min = duration/1000/60;
        int sec = duration/1000%60;
        String time = min + ":";
        if(sec < 10)
        {
            time += "0";
        }
        time += sec;
        return time;
    }
}