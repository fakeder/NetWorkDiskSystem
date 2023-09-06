package com.example.networkdisksystem.service.serviceImpl;

import com.example.networkdisksystem.entity.FolderEntity;
import com.example.networkdisksystem.mapper.FolderMapper;
import com.example.networkdisksystem.service.FileService;
import com.example.networkdisksystem.service.FolderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {
    @Resource
    FolderMapper mapper;

    @Resource
    FileService service;

    @Override
    public int CreateFolder(String folderName, int mid, int uid) {
        List<FolderEntity> folders = mapper.getFolderByUpMkdirId(mid);
        int i=1;
        String f_name=folderName;
        boolean flag=true;
        while(flag&&folders.size()>0){
            for (FolderEntity folder:folders){
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
        return mapper.insertMkdir(uid, folderName, mid);
    }

    @Override
    public List<FolderEntity> getFolderByUpFolderId(int UpFolderId) {
        return mapper.getFolderByUpMkdirId(UpFolderId);
    }

    @Override
    public int deleteFolderByMid(int mid) {
        //判断文件夹下面是否有文件
        int FileSize = service.getFileNamesByMid(mid).size();
        int FolderSize = mapper.getFolderByUpMkdirId(mid).size();
        if(FileSize+FolderSize>0)return 0;
        //删除文件夹
        return mapper.delete(mid);
    }

    @Override
    public int folderRename(int mid, String folderName, int UpFolderId) {
        List<FolderEntity> folders= mapper.getFolderByUpMkdirId(UpFolderId);
        int i=1;
        String f_name=folderName;
        boolean flag=true;
        while(flag&&folders.size()>0){
            for (FolderEntity folder:folders){
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
       return mapper.rename(mid,folderName);
    }
}
