package com.example.networkdisksystem.util;

import com.example.networkdisksystem.entity.FileEntity;
import com.example.networkdisksystem.entity.FolderEntity;

import java.util.List;

public class Naming {
    /**
     * 文件命名（重复）
     * @param fileNames 目录下的所以文件名
     * @param oldFilename 文件名
     * @return 新的文件名
     */
    public static String fileNaming(List<FileEntity.FileInputEntity> fileNames,String oldFilename){
        int i=1;
        boolean flag=true;
        String filename=oldFilename;
        while(flag&&fileNames.size()>0){
            for(FileEntity.FileInputEntity fileEntity:fileNames){
                if(filename.equals(fileEntity.getFileName())){
                    String[] f=oldFilename.split("\\.");
                    String fn=f[0];
                    //将这种文件a.b.c.pdf 连接成==》a.b.c
                    for(int j=1;j<f.length-1;j++){
                        fn=fn+"."+f[i];
                    }
                    //添加文件后缀a.xxx 没有后则不添加
                    if(f.length>=2){
                        filename=fn+"("+i+")."+f[f.length-1];
                    } else {
                        filename=fn+"("+i+")";
                    }
                    i++;
                    flag=true;
                    break;
                }else {
                    flag=false;
                }
            }
        }
        return filename;
    }


    /**
     * 目录重命名（重复）
     * @param folders 当前目录下的目录list
     * @param folderName 目录名
     * @return 变更目录名
     */
    public static String folderNaming(List<FolderEntity.FolderEntityInput> folders,String folderName){
        int i=1;
        String f_name=folderName;
        boolean flag=true;
        while(flag&&folders.size()>0){
            for (FolderEntity.FolderEntityInput folder:folders){
                if(folderName.equals(folder.getFolderName())){
                    folderName=f_name+"("+i+")";
                    i++;
                    flag=true;
                    break;
                }else {
                    flag=false;
                }
            }
        }
        return folderName;
    }
}
