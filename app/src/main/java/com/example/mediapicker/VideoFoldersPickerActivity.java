package com.example.mediapicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class VideoFoldersPickerActivity extends AppCompatActivity {
    public GridView gridView;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_folders_picker);
       FolderModel.folderModels = new ArrayList<FolderModel>();

       new ResolveVideoFolders(this).execute();

       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               String folderPath = FolderModel.folderModels.get(position).folderPath;
               Intent intent = new Intent(getApplicationContext(),VideoPickerActivity.class);

               intent.putExtra("FolderPath",folderPath);
               startActivity(intent);

           }
       });
    }


    public class ResolveVideoFolders extends AsyncTask<Void,Void,Void> implements com.example.mediapicker.ResolveVideoFolders {
        private Context context;
        HashMap<String,FolderModel>folderModelHashMap;
        public  FolderGridViewAdapter gridViewAdapter;
        public ResolveVideoFolders(Context context)
        {
            folderModelHashMap = new HashMap<>();
            this.context = context;
        }
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            gridView = findViewById(R.id.activity_video_folder_picker_gridview);
           gridViewAdapter = new FolderGridViewAdapter(context);

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(Void... voids) {
            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String[] projection = new String[]{
                    MediaStore.Video.Media.DATA
            };
            String sortOrder = MediaStore.Video.Media.DEFAULT_SORT_ORDER;
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor= contentResolver.query(
                    uri,
                    projection,
                    null,
                    null,
                    sortOrder);
            if(cursor == null)
            {
                showToast("Error Loading Videos");
            }
            else {

                if (cursor.getCount() < 1) {
                    showToast("No Videos");
                } else {
                    int dataColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);

                    while (cursor.moveToNext()) {
                        String curVideoPath = cursor.getString(dataColumnIndex);
                        addToHashMap(curVideoPath);

                    }
                   for(HashMap.Entry entry : folderModelHashMap.entrySet())
                   {
                       FolderModel.folderModels.add((FolderModel) entry.getValue());
                   }

                }
            }


            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gridView.setAdapter(gridViewAdapter);
                }
            });
        }


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        private void addToHashMap(final String path)
        {
            String[] pathVariables = path.split("/");
            final String folderName = pathVariables[pathVariables.length-2];
            String folderPath ="";
            for(int i=0;i<pathVariables.length-1;i++)
            {
                if(i>0) folderPath+="/";
                folderPath+=pathVariables[i];
            }
            int folderVideoCount =1;

            if(folderModelHashMap.containsKey(folderName))
            {
             folderVideoCount = folderModelHashMap.get(folderName).folderVideoCount+1;
            }
            folderModelHashMap.put(folderName,new FolderModel(folderName,folderPath,folderVideoCount));
        }

        private void showToast(final String msg)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
