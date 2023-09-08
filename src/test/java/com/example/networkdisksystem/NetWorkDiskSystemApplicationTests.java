package com.example.networkdisksystem;

import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.mapper.FileMapper;
import com.example.networkdisksystem.mapper.FolderMapper;
import com.example.networkdisksystem.mapper.UserMapper;
import org.apache.kerby.util.Hex;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.util.List;

@SpringBootTest
class NetWorkDiskSystemApplicationTests {

    @Resource
    UserMapper mapper;

    @Resource
    FileMapper fileMapper;

    @Resource
    FolderMapper folderMapper;

    @Test
    void contextLoads() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("123456");
        System.out.println(encoder.matches("123456", encode));
       /* Users users=mapper.getUsersByUsername("梁志超");
        System.out.println(users.toString());*/

    }

    @Test
    void file(){
        //fileMapper.addFile(1,"a.txt");
        //fileMapper.getFileById(1).forEach(System.out::println);
        //fileMapper.deleteFile(1,"a.txt");
        //mapper.register("fake","123456","fake","123456789@11.com");
        //System.out.println(mapper.checkEmail("123456789@1.com"));
        //System.out.println(mapper.checkUserName("fakwe"));
        /*String aaa="aaa";
        aaa=aaa+null;
        System.out.println(aaa);*/
        fileMapper.getFileByIdAndFileName(9,"a").forEach(System.out::println);
    }
    @Test
    void test(){
        int upFolderIdByMid = folderMapper.getUpFolderIdByMid(1);
        System.out.println(upFolderIdByMid);
    }


}
