package com.example.networkdisksystem.service;

import com.example.networkdisksystem.entity.FolderEntity;

import java.util.List;

public interface FolderService {
    int CreateFolder(String folderName,int mid,int uid);

    List<FolderEntity> getFolderByUpFolderId(int UpFolderId);

    int deleteFolderByMid(int mid);

    int folderRename(int mid,String folderName,int UpFolderId);
}
