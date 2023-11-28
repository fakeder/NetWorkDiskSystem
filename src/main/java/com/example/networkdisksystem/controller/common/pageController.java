package com.example.networkdisksystem.controller.common;

import com.example.networkdisksystem.entity.FileEntity;
import com.example.networkdisksystem.entity.FolderEntity;
import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.mapper.FileMapper;
import com.example.networkdisksystem.mapper.FolderMapper;
import com.example.networkdisksystem.service.FileService;
import com.example.networkdisksystem.service.FolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
@RequestMapping("/common")
public class pageController {
    @Resource
    FileService service;

    @Resource
    FolderService folderService;

    @Resource
    FolderMapper mapper;

    @Resource
    FileMapper fileMapper;

    //主页面列表
    @RequestMapping(value = {"/index","/index.html"},method = RequestMethod.GET)
    public String index(HttpSession session, Model model,@RequestParam(value = "mid",defaultValue = "0") int Mid){
        Users user = (Users) session.getAttribute("user");
        int mid=(int) session.getAttribute("mid");
        /*int mid=0;
        //从session中获取，如果没有获取到就是登录后的第一次进入，重user中获取mid
        try{
            mid = (int) session.getAttribute("mid");
        }catch (Exception e){
            mid=user.getMid();
        }*/
        //判断闯过来的mid是否是默认值，如果不是默认值就是点击文件夹的URL传过来的，将mid赋值为URL传入的值
        if (Mid>0) mid=Mid;
        System.out.println("^=============index============^");
        System.out.println("用户id:"+user.getUid()+" 用户名:"+user.getUsername()+" 当前目录id:"+mid);
        //文件夹
        List<FolderEntity> folders = folderService.getFolderByUpFolderId(mid);
        //文件
        List<FileEntity> list = service.getFileNamesByMid(mid);
        model.addAttribute("folders",folders);
        model.addAttribute("list",list);
        model.addAttribute("user",user);

        folders.forEach(System.out::println);
        System.out.println(" ------------------------------ ");
        list.forEach(System.out::println);
        System.out.println("~==============================~");
        //将当前页面的mid放入session中
        session.setAttribute("mid",mid);
        return "index";
    }


    //返回上一级目录
    @RequestMapping(value = "/commonback",method = RequestMethod.GET)
    public String commonBack(HttpSession session){
        System.out.println("==========返回=============");
        //获取当前目录的mid
        int mid = (int) session.getAttribute("mid");
        //通过当前目录的mid获取上一级目录的mid
        int upFolderIdByMid = mapper.getUpFolderIdByMid(mid);
        System.out.println("上一级目录："+upFolderIdByMid);
        return "redirect:index?mid="+upFolderIdByMid;
    }

    //文件（夹）查询
    @RequestMapping(value = "/find",method = RequestMethod.GET)
    public String find(@RequestParam("find") String find,HttpSession session,Model model){
        System.out.println("========find=========");
        System.out.println("查询文件的文件名："+find);
        //如果传入""
        if(Objects.isNull(find)||find.equals("")){
          return "redirect:index";
        }
        Users user = (Users) session.getAttribute("user");
        int userId=user.getUid();
        //获取文件
        List<FileEntity> files = fileMapper.getFileByIdAndFileName(userId, find);
        //获取文件夹
        List<FolderEntity> folders = mapper.getFolderByIdAndFolderName(userId, find);

        model.addAttribute("user",user);
        model.addAttribute("folders",folders);
        model.addAttribute("list",files);

        return "index";
    }
}
