package com.example.mediapicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;

public class VideoPickerActivity extends AppCompatActivity {
    private ArrayList<VideoModel> videoModels ;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_picker);
        String folderPath = getIntent().getStringExtra("FolderPath");
        videoModels = new ArrayList<>();
       new GetVideoDataBackground(this,folderPath).execute();

    }

    public class GetVideoDataBackground extends AsyncTask<Void,Void,Void>{
        private Context context;
        private GridView gridView;
        GridViewAdapter gridViewAdapter;
        private String folderPath;
        public GetVideoDataBackground(Context context,String folderPath){
            this.context=context;
            this.folderPath = folderPath;
        }
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPreExecute(){

            gridView = findViewById(R.id.activity_video_picker_gridview);

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected Void doInBackground(Void... voids) {
                    Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media._ID
        };
        String selectionClause = MediaStore.Video.Media.DATA+" LIKE ?";
        String[] selectionArgs = new String[]{folderPath+"%"};
        String sortOrder = MediaStore.Video.Media.DEFAULT_SORT_ORDER;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor= contentResolver.query(
                uri,
                projection,
                selectionClause,
                selectionArgs,
                sortOrder);
        if(cursor == null)
        {
            showToast("Error Loading Videos");
        }
        else
        {

            if(cursor.getCount()<1)
            {
                showToast("No Videos");
            }
            else
            {
                int dataColumnIndex  = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                int displayColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                int durationColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
                int idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                while(cursor.moveToNext())
                {
                    String curVideoPath = cursor.getString(dataColumnIndex);
                    String curVideoDisplayName = cursor.getString(displayColumnIndex);
                    int curVideoDuration = cursor.getInt(durationColumnIndex);
                    long curVideoID = cursor.getLong(idColumnIndex);
                    BitmapFactory.Options bitmapFactory = new BitmapFactory.Options();
                    bitmapFactory.inSampleSize =1;
                    Bitmap thumbnail= MediaStore.Video.Thumbnails.getThumbnail(contentResolver,curVideoID,MediaStore.Video.Thumbnails.MINI_KIND,bitmapFactory);
                    videoModels.add(new VideoModel(curVideoPath,curVideoDisplayName,curVideoDuration,thumbnail));

                }

                gridViewAdapter = new GridViewAdapter(context,videoModels);
                gridView.setAdapter(gridViewAdapter);
            }
        }

            return null;
        }


        private void showToast(final String msg)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
                }
            });
        }
    }


}
