package com.example.networkdisksystem.service.serviceImpl;

import com.example.networkdisksystem.config.FileConfig;
import com.example.networkdisksystem.config.FileImage;
import com.example.networkdisksystem.entity.FileEntity;
import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.mapper.FileMapper;
import com.example.networkdisksystem.mapper.UserMapper;
import com.example.networkdisksystem.service.FileService;
import com.example.networkdisksystem.util.DateToString;
import com.example.networkdisksystem.util.SizeChange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Resource
    FileMapper fileMapper;

    @Resource
    UserMapper userMapper;

    @Autowired
    FileConfig fileConfig;

    @Autowired
    FileImage fileImage;


    @Transactional
    @Override
    public int pushFile(int mid, String filename, String FileTempPath, String FileSavePath,
                        String fileSize,long fileSizeByte,long usedSize,int uid) {//文件上传
        //文件表中插入数据
        fileMapper.addFile(mid,filename,fileSize,fileSizeByte,uid,new Date());
        //更新用户表信息
        String usedsize = SizeChange.formatFileSize(usedSize);
        userMapper.updateUsedSizeByUid(uid,usedsize,usedSize);
        //获取文件id
        int fid = fileMapper.getFileIdByMidAndFileName(mid, filename);

        //将文件复制到用户文件路径下
            //将文件重命名为 /用户文件存储路径/用户名/fid
        FileSavePath=FileSavePath+fid;
        try {
            /*File tempFile=new File(FileTempPath);
            File saveFile=new File(FileSavePath);
            tempFile.renameTo(saveFile);*/
            Files.copy(Paths.get(FileTempPath), Paths.get(FileSavePath));
            System.out.println("文件上传成功！文件上传路径："+FileSavePath);
        } catch (Exception e) {
            System.out.println("文件上传的过程中发生异常");
            e.printStackTrace();
            throw new RuntimeException("文件上传的过程中发生异常");
        }finally {
            // 指定要删除的文件路径
            String filePath = FileTempPath;
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
        }
        return 1;
    }


    @Override
    public List<FileEntity.FileInputEntity> getFileNamesByMid(int mid) {//文件一览
        List<FileEntity.FileInputEntity> fileNames = fileMapper.getFileByMid(mid);
        return fileNames;
    }

    @Override
    public int deleteFile(int fid, String filePath, Users user) {//文件删除
        //1.删除服务器上的文件
        // 指定要删除的文件路径
        filePath = filePath+fid;
        // 创建一个新的File对象
        File deleteFile = new File(filePath);
        // 检查文件是否存在
        if (deleteFile.exists()) {
          // 删除文件
          deleteFile.delete();
          log.info("文件已成功删除.");
        } else {
          log.warn("文件不存在.");
        }
        //2.更新用户表中的UsedSize
        FileEntity.FileInputEntity file= fileMapper.getFileById(fid);
        long userSizeByte = user.getUsedSizeByte();
        long fileSizeByte = file.getFileSizeByte();
        //已用大小-文件大小
        userSizeByte=userSizeByte-fileSizeByte;
        //long -> string / 字节 -> B
        String usedSize=SizeChange.formatFileSize(userSizeByte);
        userMapper.updateUsedSizeByUid(user.getUid(),usedSize,userSizeByte);
        //3.删除表中数据
        fileMapper.deleteFile(fid);
        return 1;
    }

    @Override
    public int rename(String filename, int fid,int mid) {//修改文件名
        List<FileEntity.FileInputEntity> files = fileMapper.getFileByMid(mid);
        for (FileEntity.FileInputEntity file:files){
            if(filename.equals(file.getFileName())){
                return 0;
            }
        }
        return fileMapper.rename(filename,fid);
    }


    @Override
    public void downloadToClient(HttpServletRequest request, HttpServletResponse response,String filePath,int fid) {
        String filePathName = filePath + fid;
        String fileName = fileMapper.getFileNameByFid(fid);
        BufferedInputStream bins = null;
        BufferedOutputStream bouts = null;
        try {
            //同一个窗口下载多次，清除空白流
            response.reset();
            File file = new File(filePathName);
            if (!file.exists()) {
                log.error("要下载的文件不存在：{}", filePathName);
                return;
            }
            bins = new BufferedInputStream(new FileInputStream(filePathName));
            bouts = new BufferedOutputStream(response.getOutputStream());
            String userAgent = request.getHeader("USER-AGENT").toLowerCase();
            // 如果是火狐浏览器
            if (userAgent.contains("firefox")) {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } else {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }
            //设置发送到客户端的响应的内容类型
            response.setContentType("application/download");
            //指定客户端下载的文件的名称
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            int len;
            byte[] bytes = new byte[1024];
            while ((len = bins.read(bytes)) != -1) {
                bouts.write(bytes, 0, len);
            }
            //刷新流
            bouts.flush();
            log.info("下载完成");
        } catch (IOException e) {
            log.error("下载文件异常:{}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<FileEntity.FileOutputEntity> fileInputChangeToOutput(List<FileEntity.FileInputEntity> list) {
        List<FileEntity.FileOutputEntity> fileOutputEntityList;
        fileOutputEntityList=list.stream().map(item->{
            FileEntity.FileOutputEntity fileOutput=new FileEntity.FileOutputEntity();
            //fid
            fileOutput.setFid(item.getFid());
            //uid
            fileOutput.setUid(item.getUid());
            //mid
            fileOutput.setMid(item.getMid());
            //文件名
            fileOutput.setFileName(item.getFileName());
            //开始时间
            fileOutput.setStartTime(DateToString.StringData(item.getStartTime()));
            //修改时间
            if(!ObjectUtils.isEmpty(item.getLastModifiedTime())){
                fileOutput.setLastModifiedTime(DateToString.StringData(item.getLastModifiedTime()));
            }
            //文件大小 (B)
            fileOutput.setFileSize(item.getFileSize());
            //文件大小 (字节)
            fileOutput.setFileSizeByte(item.getFileSizeByte());
            //文件类型
            String[] str=item.getFileName().split("\\.");
            if(str.length<2){
                fileOutput.setType("未知");
            }else {
                fileOutput.setType(str[str.length - 1]);
            }
            //文件图片
            fileOutput.setFileImage(fileImage.getPath(str[str.length-1]));
            return fileOutput;
        }).collect(Collectors.toList());
        return fileOutputEntityList;
    }
}
