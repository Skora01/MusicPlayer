package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    ImageButton implay,imff,imfr,imshuffle,imrepeat;
    TextView txtsong,txtstart,txtend;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;
    String songName;
    ArrayList<File> songs;
    int position;

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
        txtend= (TextView) findViewById(R.id.txtend);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songs");
        String sname = bundle.getString("songName");
        position = bundle.getInt("position",0);
        txtsong.setSelected(true);
        Uri uri = Uri.parse(songs.get(position).toString());
        songName = songs.get(position).getName();
        txtsong.setText(songName);
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();

        implay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {
                    implay.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                    mediaPlayer.pause();
                }
                else
                {
                    implay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                    mediaPlayer.start();
                }
            }
        });
    }
}