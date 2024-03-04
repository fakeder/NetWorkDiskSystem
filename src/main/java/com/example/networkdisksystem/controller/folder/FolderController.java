package com.example.networkdisksystem.controller.folder;

import com.example.networkdisksystem.entity.FolderEntity;
import com.example.networkdisksystem.entity.R;
import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.service.FolderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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
     * 获取当前页面mid,目录名称，上级目录id
     * @param session session
     * @return mid
     */
    @RequestMapping(value = "/getMidAndFolderNameAndUpFolderId",method = RequestMethod.GET)
    @ResponseBody
    public FolderEntity.FolderEntityInput getMid(HttpSession session,@RequestParam(value = "mid",defaultValue = "0") int mid){
        if(mid ==0) mid= (int) session.getAttribute("mid");
        return folderService.getFolderNameAndUpFolderIdByMid(mid);
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
     * 移动目录
     * @param upFolderId
     * @param mid
     * @return
     */
    @RequestMapping(value = "/moveFolder",method = RequestMethod.POST)
    @ResponseBody
    public R moveFolder(@RequestParam("upFolderId") int upFolderId,
                        @RequestParam("mid") int mid){

        int flag=folderService.removeUpFolderIdByMid(upFolderId,mid);
        if(flag>0){
            return new R(200,"目录移动成功！");
        }else {
            return new R(500,"未知原因移动失败！");
        }
    }

    /**
     * 返回当前目录id下的所以目录id（包含当前目录id）
     * @param mid mid
     * @return mid_list
     */
    @RequestMapping(value = "/recursionDeleteFolder",method = RequestMethod.POST)
    @ResponseBody
    public List<Integer> recursionDeleteFolder(@RequestParam("mid") int mid){
        List<Integer> mid_list = folderService.getDeleteFolderIdList(mid);
        return mid_list;
    }

    /**
     * 批量删除目录
     * @param midList midList
     * @return
     */
    @RequestMapping(value = "/recursionDeleteFolderList",method = RequestMethod.POST)
    @ResponseBody
    public R recursionDeleteFolderList(@RequestParam("mid_list") String midList){
        System.out.println(midList);
        midList=midList.substring(1,midList.length()-1);
        System.out.println(midList);
        String[] numberStrings = midList.split(",");
        List<Integer>mid_list = new ArrayList<>();
        for (int i = 0; i < numberStrings.length; i++) {
            mid_list.add(Integer.parseInt(numberStrings[i]));
        }
        try {
            folderService.deleteFolderList(mid_list);
            return new R(200,"删除成功！");
        }catch (Exception e){
            System.err.println("目录递归删除失败");
            return new R(500,"删除失败！");
        }

    }
}
