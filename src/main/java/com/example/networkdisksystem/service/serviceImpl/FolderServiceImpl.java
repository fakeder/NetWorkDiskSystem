package com.example.networkdisksystem.service.serviceImpl;

import com.example.networkdisksystem.entity.FolderEntity;
import com.example.networkdisksystem.mapper.FolderMapper;
import com.example.networkdisksystem.service.FileService;
import com.example.networkdisksystem.service.FolderService;
import com.example.networkdisksystem.util.DateToString;
import com.example.networkdisksystem.util.Naming;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FolderServiceImpl implements FolderService {
    @Resource
    FolderMapper mapper;

    @Resource
    FileService service;

    @Override
    public int CreateFolder(String folderName, int mid, int uid) {
        List<FolderEntity.FolderEntityInput> folders = mapper.getFolderByUpMkdirId(mid);
        folderName= Naming.folderNaming(folders,folderName);
        return mapper.insertMkdir(uid, folderName, mid,new Date());
    }

    @Override
    public List<FolderEntity.FolderEntityInput> getFolderByUpFolderId(int UpFolderId) {
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
        List<FolderEntity.FolderEntityInput> folders= mapper.getFolderByUpMkdirId(UpFolderId);
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
       return mapper.rename(mid,folderName);
    }

    @Override
    public List<FolderEntity.FolderEntityOutput> FolderInputChangeToOutput(List<FolderEntity.FolderEntityInput> list) {
        List<FolderEntity.FolderEntityOutput> folderEntityOutputList;
        folderEntityOutputList=list.stream().map(itme->{
            FolderEntity.FolderEntityOutput folderEntityOutput=new FolderEntity.FolderEntityOutput();
            //mid
            folderEntityOutput.setMid(itme.getMid());
            //uid
            folderEntityOutput.setUid(itme.getUid());
            //上级目录id
            folderEntityOutput.setUpFolderId(itme.getUpFolderId());
            //目录名称
            folderEntityOutput.setFolderName(itme.getFolderName());
            //创建时间
            folderEntityOutput.setStartTime(DateToString.StringData(itme.getStartTime()));
            //最近操作时间
            if(!ObjectUtils.isEmpty(itme.getLastModifiedTime())){
                folderEntityOutput.setLastModifiedTime(DateToString.StringData(itme.getLastModifiedTime()));
            }
            return folderEntityOutput;
        }).collect(Collectors.toList());
        return folderEntityOutputList;
    }

    @Override
    public int getLastMid(int mid) {
        return mapper.getUpFolderIdByMid(mid);
    }

    @Override
    public String getFolderNameByMid(int mid) {
        return mapper.getFolderNameByMid(mid);
    }
}
