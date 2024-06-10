package com.example.networkdisksystem.service.serviceImpl;

import com.example.networkdisksystem.API.HadoopApi;
import com.example.networkdisksystem.config.AdminConfig;
import com.example.networkdisksystem.config.FileConfig;
import com.example.networkdisksystem.entity.AdminEntity;
import com.example.networkdisksystem.entity.FileShareEntity;
import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.mapper.*;
import com.example.networkdisksystem.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminConfig adminConfig;

    @Autowired
    private HttpSession session;

    @Autowired
    private AdminMapper adminMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private FileShareMapper fileShareMapper;
    
    @Autowired
    private FolderMapper folderMapper;
    
    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private StringRedisTemplate template;
    
    @Autowired
    private FileConfig fileConfig;

    @Override
    public boolean adminLogin(String username, String password) {
        if(username.equals(adminConfig.getUsername())&&password.equals(adminConfig.getPassword())){
            updateSession();
            return true;
        }
        return false;
    }

  @Override
  public void changeCondition(int uid, int condition) {
      //禁用用户
      if(condition == 1){
        //删除用户redis中的状态
        template.delete("userCondition"+uid);
        //改变用户表中的状态
        userMapper.changeCondition(uid,"0");
      } else {
        userMapper.changeCondition(uid,"1");
      }
      //更新session信息
      updateSession();
  }

  @Transactional
  @Override
  public int deleteUser(int uid) {
    String username = userMapper.getUserById(uid).getUsername();
    //删除用户HDFS上的文件
    String hdfsUploadPath = fileConfig.getHdfsUploadPath()+username;
    try {
      HadoopApi hadoopApi = new HadoopApi();
      hadoopApi.Del(hdfsUploadPath);
    } catch (Exception e){
      log.info("用户文件删除失败！！！");
      return 0;
    }
    //删除用户文件分享中中的文件
    List<FileShareEntity.FileShareEntityInput> fileShareList = 
        fileShareMapper.getFileShareListByUid(uid);
    if(!fileShareList.isEmpty()){
      for(FileShareEntity.FileShareEntityInput fileShareEntityInput: fileShareList){
        //删除redis中的数据
        template.delete("Time:"+fileShareEntityInput.getShareCode());
      }
    }
    fileShareMapper.deleteFileShareByUid(uid);
    //删除文件夹表中的数据
    folderMapper.deleteFolderByUid(uid);
    //删除文件表中的数据
    fileMapper.deleteFileByUid(uid);
    //删除用户表信息
    userMapper.deleteUserByUid(uid);
    //更新session信息
    updateSession();
    return 1;
  }

  @Override
  public AdminEntity.UserDetail catUserDetail(int uid) {
    AdminEntity.UserDetail userDetail = new AdminEntity.UserDetail();
    Users user = userMapper.getUserById(uid);
    BeanUtils.copyProperties(user,userDetail);
    int fileCount = adminMapper.countByUid("file_table", uid);
    int folderCount = adminMapper.countByUid("mkdir_table", uid);
    int fileShareCount = adminMapper.countByUid("file_share", uid);
    userDetail.setFileCount(fileCount);
    userDetail.setFolderCount(folderCount);
    userDetail.setFileShareCount(fileShareCount);
    return userDetail;
  }

  @Override
  public int userLevelChange(int uid, String level) {
    String totalSize = "0.00 GB";
    String totalSizeByte = "0";
    switch (level){
      case "0": 
        totalSize = "1.00 GB";
        totalSizeByte = "1073741824";
        break;
      case "1":
        totalSize = "5.00 GB";
        totalSizeByte = "5368709120";
        break;
      case "2":
        totalSize = "10.00 GB";
        totalSizeByte = "10737418240";
        break;
      case "3":
        totalSize = "50.00 GB";
        totalSizeByte = "53687091200";
        break;
      case "4":
        totalSize = "75.00 GB";
        totalSizeByte = "80530636800";
        break;
      case "5":
        totalSize = "100.00 GB";
        totalSizeByte = "107374182400";
        break;
    }
    return userMapper.updateVipLevel(uid,level,totalSize,totalSizeByte);
  }

  /**
   * 获取admin实体类
   */
  private AdminEntity getAdminEntity(){
    AdminEntity adminEntity = (AdminEntity) session.getAttribute("admin");
    if(!ObjectUtils.isEmpty(adminEntity)){
      return adminEntity;
    } else {
      adminEntity = new AdminEntity();
      List<Users> usersList = adminMapper.getAllUsers(100);
      adminEntity.setUsersList(usersList);
      adminEntity.setCountUser(adminMapper.countUser());
      adminEntity.setUsedUser(adminMapper.countUsedUser());
      adminEntity.setForbiddenUser(adminMapper.countForbiddenUser());
      adminEntity.setVIPCount(adminMapper.countVIP());
      adminEntity.setOnlineUsers(template.keys("userCondition*").size());
    }
    return adminEntity;
  }

  /**
   * 更新session信息
   */
  public void updateSession(){
    AdminEntity adminEntity = getAdminEntity();
    List<Users> usersList = adminMapper.getAllUsers(100);
    adminEntity.setUsersList(usersList);
    adminEntity.setUsedUser(adminMapper.countUsedUser());
    adminEntity.setVIPCount(adminMapper.countVIP());
    adminEntity.setForbiddenUser(adminMapper.countForbiddenUser());
    adminEntity.setOnlineUsers(template.keys("userCondition*").size());
    session.setAttribute("admin",adminEntity);
  }
}
