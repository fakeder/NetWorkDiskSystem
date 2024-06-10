package com.example.networkdisksystem.API;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.IOException;

public class HadoopApi {
    static FileSystem hdfs=null;
    static{
        Configuration conf=new Configuration();
        conf.set("fs.defaultFS","hdfs://Hadoop102:8020");
        System.setProperty("HADOOP_USER_NAME","fake");
        try {
            hdfs= FileSystem.get(conf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void MkdirFlie(String path) throws IOException{ //创建文件夹
        Path src=new Path(path);
        hdfs.mkdirs(src);
        System.out.println("文件夹创建成功！");
    }

    public void Del(String HDFSFilePath) throws IOException { //删除文件
        Path del=new Path(HDFSFilePath);
        hdfs.delete(del,true);
        System.out.println("删除成功");
    }
    public void Uplaod(String windowsFilePath,String HDFSFilePath) throws IOException { //将本地window文件上传到hdfs上
        Path src=new Path(windowsFilePath);
        Path dst=new Path(HDFSFilePath);
        hdfs.copyFromLocalFile(src,dst);
        System.out.println("上传成功");
    }

    public void downlaod(String HDFSFilePath,String windowsFilePath) throws IOException {
        Path src=new Path(HDFSFilePath);
        Path dst=new Path(windowsFilePath);
        //hdfs.copyToLocalFile(src,dst);
        hdfs.copyToLocalFile(false,src,dst,true);
        System.out.println("下载成功！");
    }

    /**
     * 复制（将文件从hdfs上下载到服务器临时目录，在将临时目录上的文件上传到hdfs）
     * @param HDFSFilePath 原路径
     * @param windowsFilePath 临时路径
     * @param newHDFSFilePath 新路径
     * @throws IOException
     */
    public void copy(String HDFSFilePath,String windowsFilePath,String newHDFSFilePath) throws IOException {
        //从hdfs先下载到服务器临时目录
        downlaod(HDFSFilePath,windowsFilePath);
        //从到服务器临时目录上传到hdfs
        Uplaod(windowsFilePath,newHDFSFilePath);
        //删除服务器临时目录文件
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
}
