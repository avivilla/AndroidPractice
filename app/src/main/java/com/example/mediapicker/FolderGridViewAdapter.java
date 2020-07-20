package com.example.mediapicker;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class FolderGridViewAdapter extends BaseAdapter {
    private Context context;
    public FolderGridViewAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount() {
        return FolderModel.folderModels.size();
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
        FolderModel folder= FolderModel.folderModels.get(position);
        if(convertView == null )
        {
            LayoutInflater layoutInflater =  LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.folder_model_adapter_card,null);
            TextView folderNameView =  convertView.findViewById(R.id.folder_model_adapter_card_folder_name);
            TextView folderCountView = convertView.findViewById(R.id.folder_model_adapter_card_count);

            folderNameView.setText(folder.folderName);
            folderCountView.setText(Integer.toString(folder.folderVideoCount) + " items");
            convertView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT,(getDeviceHeight()/3)));
        }

        return convertView;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
