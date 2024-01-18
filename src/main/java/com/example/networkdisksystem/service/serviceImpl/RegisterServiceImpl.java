package com.example.networkdisksystem.service.serviceImpl;

import com.example.networkdisksystem.API.HadoopApi;
import com.example.networkdisksystem.config.FileConfig;
import com.example.networkdisksystem.mapper.FolderMapper;
import com.example.networkdisksystem.mapper.UserMapper;
import com.example.networkdisksystem.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Resource
    UserMapper mapper;

    @Resource
    FolderMapper folderMapper;

    @Autowired
    FileConfig fileConfig;

    @Transactional
    @Override
    public void register(String username, String password, String email) {
        String filepath="/"+username;

        password=new BCryptPasswordEncoder().encode(password);
        //向用户表中插入数据
        mapper.register(username,password,email);
        //获取用户id
        int uid = mapper.getUsersByUsername(username).getUid();
        //文件夹表中插入数据
        folderMapper.insertMkdir(uid,filepath,-1,new Date());
        //获取文件夹id
        int mid = folderMapper.getMidByUidAndUpMkdirId(uid, -1);
        //向用户表中插入文件夹id
        mapper.setMkdirId(mid,uid);

        //在服务器上创建文件夹
        File file=new File(fileConfig.getUserFilePath()+username);
        file.mkdirs();
    }
}
