package com.example.mediapicker;

import java.util.ArrayList;

public class FolderModel {
    public String folderName;
    public String folderPath;
    public int folderVideoCount;
    public FolderModel(String folderName,String folderPath,int folderVideoCount){
        this.folderName = folderName;
        this.folderPath = folderPath;
        this.folderVideoCount = folderVideoCount;
    }
    public static ArrayList<FolderModel>folderModels;
}
