package com.example.mediapicker;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;

public class VideoModel{
    private Bitmap thumbNail;
    private String uri;
    private String displayName;
    private int duration;


    public VideoModel(String uri,String displayName,int duration,Bitmap thumbNail)
    {
        this.displayName=displayName;
        this.uri=uri;
        this.duration=duration;
        this.thumbNail = thumbNail;
    }

    public int getDuration() {
        return duration;
    }

    public String getDisplayName() {
        return displayName;
    }
    public String getUri() {
        return uri;
    }
    public Bitmap getThumbNail()
    {
        return thumbNail;
    }
}