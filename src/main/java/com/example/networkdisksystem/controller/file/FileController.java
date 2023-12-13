package com.example.networkdisksystem.controller.file;

import com.example.networkdisksystem.config.FileConfig;
import com.example.networkdisksystem.entity.FileEntity;
import com.example.networkdisksystem.entity.R;
import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.mapper.UserMapper;
import com.example.networkdisksystem.service.FileService;
import com.example.networkdisksystem.util.Naming;
import com.example.networkdisksystem.util.SizeChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.List;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileService service;

    @Autowired
    FileConfig fileConfig;

    @Autowired
    UserMapper userMapper;

    @Bean("multipartResolver") //注意这里Bean的名称是固定的，必须是multipartResolver
    public CommonsMultipartResolver commonsMultipartResolver(){
        CommonsMultipartResolver resolver=new CommonsMultipartResolver();
        resolver.setMaxInMemorySize(1024*1024*1024*10);//最大10GB文件大小
        resolver.setDefaultEncoding("UTF-8");//设置编码格式
        return resolver;
    }

    //上传文件
    @RequestMapping(value = "/fileUpload",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public R upload(@RequestParam("file") MultipartFile file, HttpSession session) throws IOException, URISyntaxException, InterruptedException {
        System.out.println("=============fileUpload=============");
        Users user = (Users) session.getAttribute("user");
        user = userMapper.getUserById(user.getUid());
        int mid = (int) session.getAttribute("mid");
        //判断文件大小
        long size = file.getSize();//上传文件大小
        String fileSize=SizeChange.formatFileSize(size);
        System.out.println("上传文件大小:"+fileSize+"  字节大小"+size);
        System.out.println("用户已用容量:"+user.getUsedSize()+"  字节大小:"+user.getUsedSizeByte());
        System.out.println("用户总容量:"+user.getTotalSize()+"  字节大小:"+user.getTotalSizeByte());
        long usedSize;
        if(user.getTotalSizeByte()< user.getUsedSizeByte()+size) {
            System.out.println("该用户网盘容量已满！");
            return new R(400, "网盘容量已满，请购买网盘容量或充值VIP");
        }else {
            usedSize=user.getUsedSizeByte()+size;
        }
        //获取文件名
        String oldFilename = file.getOriginalFilename();
        //判断文件名是否重复
        List<FileEntity.FileInputEntity> fileNames = service.getFileNamesByMid(mid);
        String filename= Naming.fileNaming(fileNames,oldFilename);
        //文件上传到服务器地址
        String filePath=fileConfig.getWindowsUploadPath();
        File fileObject=new File(filePath+filename);
        file.transferTo(fileObject);
        System.out.println("文件名："+file.getOriginalFilename());
        System.out.println("用户上传的文件保存到："+fileObject.getAbsolutePath());

        String widowsFilePath=fileObject.getAbsolutePath();
        String HDFSFilePath=fileConfig.getHdfsUploadPath()+user.getUsername()+"/";
        //上传文件
        try{
            service.pushFile(mid, filename, widowsFilePath, HDFSFilePath, fileSize,size,usedSize, user.getUid());
            return new R(200,"文件上传成功！");
        } catch (Exception e){
            return new R(500,"文件上传失败!");
        }finally {
            session.setAttribute("user",user);
        }
    }

    //下载文件
    @RequestMapping(value = "/download",method = RequestMethod.GET)
    @ResponseBody
    public void download(@RequestParam("fid") int fid,
                      HttpSession session,
                      HttpServletRequest request,
                      HttpServletResponse response){
        System.out.println("fid:"+fid);
        Users user = (Users) session.getAttribute("user");
        String username = user.getUsername();
        //hdfs文件名拼接
        String HDFSFilePath=fileConfig.getHdfsUploadPath()+username+"/";
        //将文件从hdfs下载到服务器端
        String fileName = service.pullFile(HDFSFilePath, fid);
        //将文件从服务器端下载到客户端
        service.downloadToClient(request,response,fileName);
    }

    //删除文件
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    @ResponseBody
    public R delete(@RequestParam("fid") int fid,HttpSession session){
        Users user = (Users) session.getAttribute("user");
        user=userMapper.getUserById(user.getUid());
        String username = user.getUsername();
        String HDFSFilePath=fileConfig.getHdfsUploadPath()+username+"/";
        try {
            service.deleteFile(fid, HDFSFilePath, user);
            return new R(200, "删除成功！");
        }catch (Exception e){
            return new R(500, "删除失败！");
        }finally {
            session.setAttribute("user",user);
        }
    }

    //修改文件名
    @RequestMapping(value = "/rename",method = RequestMethod.POST)
    @ResponseBody
    public R rename(@RequestParam("filename") String filename,@RequestParam("Fid") int fid,HttpSession session){
        System.out.println("=======重命名=========");
        System.out.println("重命名的文件名："+filename+"   文件id"+fid);
        if(ObjectUtils.isEmpty(filename))return new R(401,"文件名不能为空！");
        int mid = (int) session.getAttribute("mid");
        int flag = service.rename(filename, fid, mid);
        if(flag==1) return new R(200,"修改成功！");
        else return new R(400,"文件名重复");
    }

    /**
     * 文件提取 直接下载文件
     * @param fid fid
     * @param username 用户名
     * @param request
     * @param response
     */
    @RequestMapping(value = "/fileExtractionDownload",method = RequestMethod.GET)
    @ResponseBody
    public void fileExtractionDownload(@RequestParam("fid") int fid,
                                       @RequestParam("username") String username,
                                       HttpServletRequest request,
                                       HttpServletResponse response){
        //hdfs文件名拼接
        String HDFSFilePath=fileConfig.getHdfsUploadPath()+username+"/";
        //将文件从hdfs下载到服务器端
        String fileName = service.pullFile(HDFSFilePath, fid);
        //将文件从服务器端下载到客户端
        service.downloadToClient(request,response,fileName);
    }
}
