package com.example.networkdisksystem.service;

import com.example.networkdisksystem.entity.FolderEntity;

import java.util.List;

public interface FolderService {
    /**
     * 创建目录
     * @param folderName 目录名称
     * @param mid mid
     * @param uid uid
     * @return
     */
    int CreateFolder(String folderName,int mid,int uid);

    /**
     * 获取当前目录下的所以目录
     * @param UpFolderId 上级目录id
     * @return 目录List
     */
    List<FolderEntity.FolderEntityInput> getFolderByUpFolderId(int UpFolderId);

    /**
     * 删除目录
     * @param mid mid
     * @return
     */
    int deleteFolderByMid(int mid);

    /**
     * 目录重命名
     * @param mid mid
     * @param folderName 新目录名称
     * @param UpFolderId 上级目录id
     * @return
     */

    int folderRename(int mid,String folderName,int UpFolderId);


    /**
     * DB录入/取得 -> 页面展示
     * @param list
     * @return
     */
    List<FolderEntity.FolderEntityOutput> FolderInputChangeToOutput(List<FolderEntity.FolderEntityInput> list);

    /**
     * 根据mid获取目录名称
     * @param mid mid
     * @return 目录名称
     */
    FolderEntity.FolderEntityInput getFolderNameAndUpFolderIdByMid(int mid);
}
