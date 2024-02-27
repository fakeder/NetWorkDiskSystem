package com.example.networkdisksystem.service.serviceImpl;

import com.example.networkdisksystem.API.HadoopApi;
import com.example.networkdisksystem.config.FileConfig;
import com.example.networkdisksystem.config.FileImage;
import com.example.networkdisksystem.entity.FileEntity;
import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.mapper.FileMapper;
import com.example.networkdisksystem.mapper.UserMapper;
import com.example.networkdisksystem.service.FileService;
import com.example.networkdisksystem.util.DateToString;
import com.example.networkdisksystem.util.Naming;
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
    public int pushFile(int mid, String filename, String windowsFilePath, String HDFSFilePath,
                        String fileSize,long fileSizeByte,long usedSize,int uid) {//文件上传
        //文件表中插入数据
        fileMapper.addFile(mid,filename,fileSize,fileSizeByte,uid,new Date());
        //更新用户表信息
        String usedsize = SizeChange.formatFileSize(usedSize);
        userMapper.updateUsedSizeByUid(uid,usedsize,usedSize);
        //获取文件id
        int fid = fileMapper.getFileIdByMidAndFileName(mid, filename);

        //将文件上传到HDFS上
            //将文件重命名为 /HDFS/用户名/fid
        HDFSFilePath=HDFSFilePath+fid;
        HadoopApi hadoopApi=new HadoopApi();
        try {
            hadoopApi.Uplaod(windowsFilePath,HDFSFilePath);
            System.out.println("文件上传成功！文件上传到hdfs路径："+HDFSFilePath);
        } catch (Exception e) {
            System.out.println("文件上传到HDFS的过程中发生异常");
            e.printStackTrace();
            throw new RuntimeException("文件上传到HDFS的过程中发生异常");
        }finally {
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
        }
        return 1;
    }

    @Override
    public String pullFile(String HDFSFilePath,int fid) {//文件下载
        String fileName= fileMapper.getFileNameByFid(fid);
        HDFSFilePath=HDFSFilePath+fid;
        String windowsFilePath=fileConfig.getWindowsUploadPath()+fileName;
        HadoopApi hadoopApi=new HadoopApi();
        try {
            hadoopApi.downlaod(HDFSFilePath,windowsFilePath);
            System.out.println("hdfs文件下载到服务器路径："+windowsFilePath);
        } catch (IOException e) {
            System.out.println("hdfs文件下载到服务器失败！");
            e.printStackTrace();
            throw new RuntimeException("hdfs文件下载到服务器失败！");
        }
        return fileName;
    }

    @Override
    public List<FileEntity.FileInputEntity> getFileNamesByMid(int mid) {//文件一览
        List<FileEntity.FileInputEntity> fileNames = fileMapper.getFileByMid(mid);
        return fileNames;
    }

    @Override
    public int deleteFile(int fid, String HDFSFilePath, Users user) {//文件删除
        //删除HDFS上的文件
        FileEntity.FileInputEntity file= fileMapper.getFileById(fid);
        HDFSFilePath=HDFSFilePath+fid;
        System.out.println(HDFSFilePath+"================");
        HadoopApi hadoopApi=new HadoopApi();
        try {
            hadoopApi.Del(HDFSFilePath);
            System.out.println("hdfs文件删除成功！被删除文件为："+HDFSFilePath);
        } catch (IOException e) {
            System.out.println("hdfs文件删除失败！被删除文件为："+HDFSFilePath);
            e.printStackTrace();
            throw new RuntimeException("hdfs文件删除失败！被删除文件为："+HDFSFilePath);
        }
        //更新用户表中的UsedSize
        long userSizeByte = user.getUsedSizeByte();
        long fileSizeByte = file.getFileSizeByte();
        //已用大小-文件大小
        userSizeByte=userSizeByte-fileSizeByte;
        //long -> string / 字节 -> B
        String usedSize=SizeChange.formatFileSize(userSizeByte);
        userMapper.updateUsedSizeByUid(user.getUid(),usedSize,userSizeByte);
        //删除表中数据
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
    public void downloadToClient(HttpServletRequest request, HttpServletResponse response,String fileName) {
        String filePath = fileConfig.getWindowsUploadPath();
        String filePathName = filePath + fileName;
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
        } finally {
            try {
                if (bouts != null) {
                    bouts.close();
                }
                if (bins != null) {
                    bins.close();
                }
                // 创建一个新的File对象
                File file = new File(filePathName);
                // 检查文件是否存在
                if (file.exists()) {
                    // 删除文件
                    file.delete();
                    System.out.println("下载文件已成功删除.");
                } else {
                    System.out.println("文件不存在.");
                }
            } catch (IOException e) {
                log.error("关闭流异常", e);
                e.printStackTrace();
            }
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

    @Override
    public int removeMidByFid(int fid, int mid) {
        return fileMapper.removeMidByFid(fid,mid);
    }
}
