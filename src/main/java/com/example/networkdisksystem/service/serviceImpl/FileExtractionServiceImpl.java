package com.example.networkdisksystem.service.serviceImpl;

import com.example.networkdisksystem.config.FileConfig;
import com.example.networkdisksystem.config.FileImage;
import com.example.networkdisksystem.entity.FileExtractionEntity;
import com.example.networkdisksystem.entity.FileShareEntity;
import com.example.networkdisksystem.entity.Users;
import com.example.networkdisksystem.mapper.FileShareMapper;
import com.example.networkdisksystem.mapper.UserMapper;
import com.example.networkdisksystem.service.FileExtractionService;
import com.example.networkdisksystem.util.DateToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FileExtractionServiceImpl implements FileExtractionService {

    @Autowired
    StringRedisTemplate template;

    @Autowired
    FileImage fileImage;

    @Autowired
    FileShareMapper fileShareMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public FileExtractionEntity checkShareCode(String shareCode) {
        FileExtractionEntity fileExtraction=new FileExtractionEntity();
        //从redis中获取fid
        String fid = template.opsForValue().get("Time:"+shareCode);
        //没有获取到数据
        if(Objects.isNull(fid)){
            fileExtraction.setCode(404);
            fileExtraction.setReason("提取码不存在");
            return fileExtraction;
        }
        Integer Fid= Integer.valueOf(fid);
        //获取文件分享信息
        FileShareEntity.FileShareEntityInput fileShareEntityInput =
            fileShareMapper.getFileShareEntityInputByShareCodeAndFid(shareCode, Fid);
        //状态为已过期
        if(fileShareEntityInput.getCondition()==1){
            //删除redis中的数据
            template.delete("Time:"+shareCode);
            fileExtraction.setCode(405);
            fileExtraction.setReason("提取码已过期");
            return fileExtraction;
        }
        //fid
        fileExtraction.setFid(Fid);
        //文件名
        fileExtraction.setFileName(fileShareEntityInput.getFileName());
        //文件图片
        String[] type = fileShareEntityInput.getFileName().split("\\.");
        fileExtraction.setFileImage(fileImage.getPath(type[type.length-1]));
        //用户名
        int uid = fileShareEntityInput.getUid();
        Users user = userMapper.getUserById(uid);
        fileExtraction.setUsername(user.getUsername());
        //用户头像
        fileExtraction.setUserImage(null);
        //文件大小
        fileExtraction.setFileSize(fileShareEntityInput.getFileSize());
        //到期时间
        String time=DateToString.StringData(fileShareEntityInput.getExpirationTime());
        fileExtraction.setTime(time);
        //code
        fileExtraction.setCode(200);

        return fileExtraction;
    }
}
