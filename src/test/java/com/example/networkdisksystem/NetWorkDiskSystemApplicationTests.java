package com.example.networkdisksystem;

import com.example.networkdisksystem.entity.FileShareEntity;
import com.example.networkdisksystem.mapper.FileMapper;
import com.example.networkdisksystem.mapper.FileShareMapper;
import com.example.networkdisksystem.mapper.FolderMapper;
import com.example.networkdisksystem.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
        /*int upFolderIdByMid = folderMapper.getUpFolderIdByMid(1);
        System.out.println(upFolderIdByMid);*/
      LocalDateTime now=LocalDateTime.now();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
      System.out.println(now.format(formatter));
      System.out.println();

      Date date=new Date();
      SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      System.out.println("今天的日期："+df.format(date));
      System.out.println("两天前的日期：" + df.format(new Date(date.getTime() - 2 * 24 * 60 * 60 * 1000)));
      System.out.println("三天后的日期：" + df.format(new Date(date.getTime() + 30L * 24 * 60 * 60 * 1000)));
      System.out.println(1+2+""+3);
    }

    @Resource
    RedisTemplate<String,String> redisTemplate;

    @Resource
    FileShareMapper fileShareMapper;

    @Test
    void MyBatisXMLTest(){
      fileShareMapper.getFileShareByUid(9).forEach(System.out::println);
      FileShareEntity.FileShareEntityInput sharedById = fileShareMapper.getSharedByUIdAndFid(9,58);
      SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      FileShareEntity.FileShareEntityOutput fileShareEntityOutput=new FileShareEntity.FileShareEntityOutput();
      BeanUtils.copyProperties(sharedById,fileShareEntityOutput);
      fileShareEntityOutput.setStartTime(df.format(sharedById.getStartTime()));
      fileShareEntityOutput.setExpirationTime(df.format(sharedById.getExpirationTime()));
      System.out.println(fileShareEntityOutput);
      //fileShareMapper.insertFileShareTable(0,0,"","",new Date(),new Date(),1 ,1 ,1);
      //fileShareMapper.setShareCodeById(3,"123456789");
    }


}
