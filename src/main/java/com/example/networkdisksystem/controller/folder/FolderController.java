package com.example.networkdisksystem.controller.folder;

import com.example.networkdisksystem.entity.FolderEntity;
import com.example.networkdisksystem.entity.R;
import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.service.FileService;
import com.example.networkdisksystem.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/folder")
public class FolderController {

    @Resource
    FolderService folderService;

    //创建文件夹
    @RequestMapping(value = "/mkdir",method = RequestMethod.POST)
    @ResponseBody
    public R mkdir(@RequestParam("folder") String folder, HttpSession session){
        System.out.println("========mkdir=========");
        System.out.println("文件夹名称："+folder);
        int mid = (int) session.getAttribute("mid");
        Users user = (Users) session.getAttribute("user");
        int i = folderService.CreateFolder(folder, mid, user.getUid());
        if(i==1){
            return new R(200,"文件夹创建成功！");
        }else {
            return new R(400,"目录重复！");
        }
    }

    //删除文件夹
    @RequestMapping(value = "/deleteFolder",method = RequestMethod.POST)
    @ResponseBody
    public R deleteFolder(int mid){
        System.out.println("========deleteFolder==========");
        System.out.println("删除的文件夹id："+mid);
        int flag = folderService.deleteFolderByMid(mid);
        if(flag==0)return new R(400,"文件夹删除失败！该目录不为空！");
        return new R(200,"删除成功！");
    }

    //重命名文件夹
    @RequestMapping(value = "/folderRename",method = RequestMethod.POST)
    @ResponseBody
    public R folderRename(@RequestParam("mid") int mid,@RequestParam("folderName") String folderName,HttpSession session){
        System.out.println("==========folderRename=============");
        System.out.println("文件夹名称:"+folderName+"---mid:"+mid);
        int UpFolderId = (int) session.getAttribute("mid");
        int flag = folderService.folderRename(mid, folderName, UpFolderId);
        if(flag==1)return new R(200,"修改成功！");
        else return new R(400,"修改失败");
    }

    /**
     * 获取当前页面mid
     * @param session session
     * @return mid
     */
    @RequestMapping("/getMid")
    @ResponseBody
    public int getMid(HttpSession session){
        return (int) session.getAttribute("mid");
    }
    /**
     *根据id获取当前目录list
     * @param mid
     */
    @RequestMapping(value = "/folderShow",method = RequestMethod.GET)
    @ResponseBody
    public List<FolderEntity.FolderEntityOutput> fileShow(@RequestParam("mid") int mid){
        //目录
        List<FolderEntity.FolderEntityInput> folders = folderService.getFolderByUpFolderId(mid);
        //目录 DB录入/取得 -> 页面展示
        List<FolderEntity.FolderEntityOutput> outputList = folderService.FolderInputChangeToOutput(folders);
        return outputList;
    }

    /**
     * 根据mid获取上一级目录id
     * @param mid
     * @return
     */
    @RequestMapping("/getLastMid")
    @ResponseBody
    public int getLastMid(@RequestParam("mid") int mid){
        return folderService.getLastMid(mid);
    }

    @RequestMapping(value = "/getFolderName",method = RequestMethod.GET)
    @ResponseBody
    public String getFolderNameByMid(@RequestParam("mid") int mid){
        String folderName=folderService.getFolderNameByMid(mid);
        return folderName;
    }
}
