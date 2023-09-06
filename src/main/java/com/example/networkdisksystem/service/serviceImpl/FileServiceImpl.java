package com.example.networkdisksystem.service.serviceImpl;

import com.example.networkdisksystem.API.HadoopApi;
import com.example.networkdisksystem.entity.FileEntity;
import com.example.networkdisksystem.mapper.FileMapper;
import com.example.networkdisksystem.mapper.UserMapper;
import com.example.networkdisksystem.service.FileService;
import com.example.networkdisksystem.util.SizeChange;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Resource
    FileMapper fileMapper;

    @Resource
    UserMapper userMapper;


    @Transactional
    @Override
    public int pushFile(int mid, String filename, String windowsFilePath, String HDFSFilePath,String fileSize,long usedSize,int uid) {//文件上传
        //文件表中插入数据
        fileMapper.addFile(mid,filename,fileSize,uid);
        //更新用户表信息
        String usedsize = SizeChange.formatFileSize(usedSize);
        userMapper.updateUsedSizeByUid(uid,usedsize);
        //获取文件id
        int fid = fileMapper.getFileIdByMidAndFileName(mid, filename);

        //将文件上传到HDFS上
            //获取文件后缀
        String[] f=filename.split("\\.");
            //将文件重命名为 /HDFS/用户名/fid.xxx
        HDFSFilePath=HDFSFilePath+fid+"."+f[f.length-1];
        HadoopApi hadoopApi=new HadoopApi();
        try {
            hadoopApi.Uplaod(windowsFilePath,HDFSFilePath);
        } catch (Exception e) {
            System.out.println("文件上传到HDFS的过程中发生异常");
            return 0;
        }
        // 指定要删除的文件路径
        String filePath = windowsFilePath;
        // 创建一个新的File对象
        File file = new File(filePath);
        // 检查文件是否存在
        if (file.exists()) {
            // 删除文件
            file.delete();
            System.out.println("文件已成功删除.");
        } else {
            System.out.println("文件不存在.");
        }
        return 1;
    }

    @Override
    public int pullFile(String HDFSFilePath,int fid) {//文件下载
        String fileName= fileMapper.getFileNameByFid(fid);
        String[] f=fileName.split("\\.");
        HDFSFilePath=HDFSFilePath+fid+"."+f[f.length-1];
        String windowsFilePath="F:/IOtest/HDFS/"+fileName;
        HadoopApi hadoopApi=new HadoopApi();
        try {
            hadoopApi.downlaod(HDFSFilePath,windowsFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("文件下载到"+windowsFilePath);
        return 1;
    }

    @Override
    public List<FileEntity> getFileNamesByMid(int mid) {//文件一览
        List<FileEntity> fileNames = fileMapper.getFileByMid(mid);
        return fileNames;
    }

    @Override
    public int deleteFile(int fid,String HDFSFilePath) {//文件删除
        //删除HDFS上的文件
        String fileName= fileMapper.getFileNameByFid(fid);
        String[] f=fileName.split("\\.");
        HDFSFilePath=HDFSFilePath+fid+"."+f[f.length-1];
        System.out.println(HDFSFilePath+"================");
        HadoopApi hadoopApi=new HadoopApi();
        try {
            hadoopApi.Del(HDFSFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //删除表中数据
        return fileMapper.deleteFile(fid);
    }

    @Override
    public int rename(String filename, int fid,int mid) {//修改文件名
        List<FileEntity> files = fileMapper.getFileByMid(mid);
        for (FileEntity file:files){
            if(filename.equals(file.getFileName())){
                return 0;
            }
        }
        return fileMapper.rename(filename,fid);
    }

}
