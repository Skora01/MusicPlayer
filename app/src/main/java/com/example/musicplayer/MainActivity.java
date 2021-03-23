package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.os.Environment.getExternalStorageState;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.lvsongs);
        runtimePermission();
    }

    public void runtimePermission()
    {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                })
                .check();
    }

    public ArrayList<File> findSongs(File file) {
        ArrayList<File> songs = new ArrayList<>();
        File[] files = file.listFiles();
        if(files != null)
        {
            for (File singleFile : files) {
                if (singleFile.isDirectory()) {
                    songs.addAll(findSongs(singleFile));
                } else {
                    if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith("wav")) {
                        songs.add(singleFile);
                    }
                }
            }
        }
        return songs;
    }

    public void displaySongs()
    {
        final ArrayList<File> songsFound = findSongs(Environment.getExternalStorageDirectory());
        int numberOfSongs = songsFound.size();
        items = new String[numberOfSongs];
        for (int i = 0; i < numberOfSongs; i++) {
            items[i] = songsFound.get(i)
                    .getName()
                    .replace(".mp3","")
                    .replace("wav","");
        }
        ItemAdapter mItemAdapter = new ItemAdapter(this,items);
        listView.setAdapter(mItemAdapter);
    }
}