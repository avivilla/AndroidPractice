package com.example.mediapicker;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<VideoModel> videoModels;
    final Size THUMBNAIL_SIZE = new Size(30,30);
//    private final int THUMBNAIL_SIZE = 64;
    public GridViewAdapter(Context context,ArrayList<VideoModel>videoModels)
    {

        this.context =context;
        this.videoModels= videoModels;
    }
    @Override
    public int getCount() {
        return videoModels.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VideoModel video = videoModels.get(position);

        if(convertView == null)
        {
            LayoutInflater layoutInflater =  LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.video_model_adapter_card,null);        ImageView imageView = convertView.findViewById(R.id.video_model_adapter_card_image);
            TextView displayNameView = convertView.findViewById(R.id.video_model_adapter_card_display_name);
            TextView durationView = convertView.findViewById(R.id.video_model_adapter_card_duration);

            imageView.setImageBitmap(video.getThumbNail());
            displayNameView.setText(video.getDisplayName());
            durationView.setText(getDurationInFormat(video.getDuration()));
            convertView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT,(getDeviceHeight()/3)));

        }

         return convertView;
    }
    public int getDeviceHeight()
    {
        WindowManager windowManager = (WindowManager)    context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        try {
            display.getRealSize(size);
        } catch (NoSuchMethodError err) {
            display.getSize(size);
        }
//        int width = size.x;
        int height = size.y;
        return  height;
    }
    private String getDurationInFormat(int duration)
    {
        duration = Math.round(duration/1000);
        return Integer.toString((int) Math.floor(duration/60))+":"+Integer.toString(duration%60);
    }


}
