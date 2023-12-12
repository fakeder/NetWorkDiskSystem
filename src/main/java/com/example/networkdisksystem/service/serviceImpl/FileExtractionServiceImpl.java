package com.example.networkdisksystem.service.serviceImpl;

import com.example.networkdisksystem.API.HadoopApi;
import com.example.networkdisksystem.config.FileConfig;
import com.example.networkdisksystem.config.FileImage;
import com.example.networkdisksystem.entity.FileExtractionEntity;
import com.example.networkdisksystem.entity.FileShareEntity;
import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.mapper.FileMapper;
import com.example.networkdisksystem.mapper.FileShareMapper;
import com.example.networkdisksystem.mapper.UserMapper;
import com.example.networkdisksystem.service.FileExtractionService;
import com.example.networkdisksystem.util.DateToString;
import com.example.networkdisksystem.util.Naming;
import com.example.networkdisksystem.util.SizeChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Service
public class FileExtractionServiceImpl implements FileExtractionService {

    @Autowired
    StringRedisTemplate template;

    @Autowired
    FileImage fileImage;

    @Autowired
    FileShareMapper fileShareMapper;

    @Autowired
    FileMapper fileMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public FileExtractionEntity checkShareCode(String shareCode) {
        FileExtractionEntity fileExtraction=new FileExtractionEntity();
        //从redis中获取fid
        String fid = template.opsForValue().get("Time:"+shareCode);
        //没有获取到数据
        if(Objects.isNull(fid)){
            fileExtraction.setCode(404);
            fileExtraction.setReason("提取码不存在");
            return fileExtraction;
        }
        Integer Fid= Integer.valueOf(fid);
        //获取文件分享信息
        FileShareEntity.FileShareEntityInput fileShareEntityInput =
            fileShareMapper.getFileShareEntityInputByShareCodeAndFid(shareCode, Fid);
        //状态为已过期
        if(fileShareEntityInput.getCondition()==1){
            //删除redis中的数据
            template.delete("Time:"+shareCode);
            fileExtraction.setCode(405);
            fileExtraction.setReason("提取码已过期");
            return fileExtraction;
        }
        //fid
        fileExtraction.setFid(Fid);
        //文件名
        fileExtraction.setFileName(fileShareEntityInput.getFileName());
        //文件图片
        String[] type = fileShareEntityInput.getFileName().split("\\.");
        fileExtraction.setFileImage(fileImage.getPath(type[type.length-1]));
        //用户名
        int uid = fileShareEntityInput.getUid();
        Users user = userMapper.getUserById(uid);
        fileExtraction.setUsername(user.getUsername());
        //用户头像
        fileExtraction.setUserImage(null);
        //文件大小
        fileExtraction.setFileSize(fileShareEntityInput.getFileSize());
        //到期时间
        String time=DateToString.StringData(fileShareEntityInput.getExpirationTime());
        fileExtraction.setTime(time);
        //code
        fileExtraction.setCode(200);

        return fileExtraction;
    }

    @Override
    public boolean updateShareNumberOfDownload(String shareCode, int fid) {
        //获取文件分享信息
        FileShareEntity.FileShareEntityInput fileShareEntity =
            fileShareMapper.getFileShareEntityInputByShareCodeAndFid(shareCode, fid);
        //判断状态，文件下载次数是否达到最大限定次数
        int condition = fileShareEntity.getCondition();
        if(condition==1){
            return false;
        }
        //限定下载次数
        int downloadNumber = fileShareEntity.getDownloadNumber();
        //已下载次数
        int numberOfDownload = fileShareEntity.getNumberOfDownload();
        //更新前判断 已下载次数>=限定下载次数
        if(numberOfDownload>=downloadNumber){
            //删除redis中数据
            template.delete("Time:"+shareCode);
            //更改状态为已过期
            fileShareMapper.changeCondition(fileShareEntity.getShareId(),1);
            return false;
        }
        numberOfDownload+=1;
        //更新已下载次数
        fileShareMapper.updateNumberOfDownload(numberOfDownload,fileShareEntity.getShareId());
        //更新后判断 已下载次数>=限定下载次数
        if(numberOfDownload>=downloadNumber) {
            //删除redis中数据
            template.delete("Time:" + shareCode);
            //更改状态为已过期
            fileShareMapper.changeCondition(fileShareEntity.getShareId(), 1);
        }
        return true;
    }

    @Override
    public int saveFile(String fileName, int mid, int uid, String fileSize,long usedSize,
                        String HDFSFilePath1,String HDFSFilePath2,String tempPath) {
        //文件表中插入信息
        fileMapper.addFile(mid,fileName,fileSize,uid,new Date());
        //更新用户表信息
        String usedsize = SizeChange.formatFileSize(usedSize);
        userMapper.updateUsedSizeByUid(uid,usedsize);
        //获取文件id
        int fid = fileMapper.getFileIdByMidAndFileName(mid, fileName);
        //将文件上传到HDFS上
        //将文件重命名为 /HDFS/用户名/fid
        HDFSFilePath1=HDFSFilePath1+fid;
        //hdfs文件复制
        HadoopApi hadoopApi=new HadoopApi();
        try {
            hadoopApi.copy(HDFSFilePath2, tempPath, HDFSFilePath1);
            System.out.println("文件已成功复制到hdfs上");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}
