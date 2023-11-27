package com.example.networkdisksystem.service.serviceImpl;

import com.example.networkdisksystem.entity.FileShareEntity;
import com.example.networkdisksystem.mapper.FileShareMapper;
import com.example.networkdisksystem.service.FileShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.networkdisksystem.util.DateToString;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
public class FileShareServiceImpl implements FileShareService {
  @Autowired
  StringRedisTemplate template;

  @Autowired
  FileShareMapper mapper;

  @Override
  public boolean fileShareCheck(int uid, int fid) {
    FileShareEntity.FileShareEntityInput sharedByUIdAndFid = mapper.getSharedByUIdAndFid(uid, fid);
    if(Objects.isNull(sharedByUIdAndFid)){
      return true;
    }else {
      return false;
    }
  }

  @Override
  public String FileShared(int uid, int fileId, String filename, String fileSize, int expirationTime, int downloadType, int downloadNumber) {
    //当前时间
    Date now=new Date();
    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    System.out.println("当前日期："+df.format(now));
    Date endTime;
    int end;
    //截止时间
    if(expirationTime==1){
        endTime=new Date(now.getTime() + 24 * 60 * 60 * 1000);
        end=1;
    }else if(expirationTime == 2){
        endTime=new Date(now.getTime() + 7* 24 * 60 * 60 * 1000);
        end=7;
    }else if(expirationTime == 3) {
      endTime = new Date(now.getTime() + 30L * 24 * 60 * 60 * 1000);
      end=30;
    }else {
      endTime=null;
      end=0;
    }
    //文件信息录入文件分享表
    mapper.insertFileShareTable(uid,fileId,filename,fileSize,now,endTime,downloadNumber,expirationTime,downloadType);
    //获取文件分享id
    int shareId = mapper.getSharedByUIdAndFid(uid, fileId).getShareId();
    //随机数
    Random random=new Random();
    int code=random.nextInt(899999)+100000;//899999内的随机数加上100000保证code是六位数
    String sharedCode=shareId+uid+""+code;//sheradId+uid+随机六位数
    //将分享码放入redis中
    if(end!=0){
      template.opsForValue().set("Time:"+sharedCode,""+fileId,end, TimeUnit.DAYS);
    }else {
      template.opsForValue().set("Time:"+sharedCode,""+fileId);
    }
    //将分享码插入表中
    mapper.setShareCodeById(shareId,sharedCode);
    return sharedCode;
  }

  @Override
  public List<FileShareEntity.FileShareEntityOutput> showFileShare(int uid) {
    //查询用户的文件分享信息
    List<FileShareEntity.FileShareEntityInput> list = mapper.getFileShareByUid(uid);
    return InToOut(list);
  }

  @Override
  public List<FileShareEntity.FileShareEntityOutput> findFileShare(int uid, String find) {
    List<FileShareEntity.FileShareEntityInput> list = mapper.findFileShare(uid, find);
    return InToOut(list);
  }

  @Override
  public int stopSharing(int shareId, String shareCode) {
    //删除redis中的数据
    template.delete("Time:"+shareCode);
    //修改状态
    mapper.changeCondition(shareId,"1");
    return 1;
  }

  @Override
  public int delShare(int shareId) {
    return mapper.delShareById(shareId);
  }


  /**
   * 将FileShareEntityInput转化成FileShareEntityOutput
   * @param list FileShareEntityInput
   * @return FileShareEntityOutput
   */
  public List<FileShareEntity.FileShareEntityOutput> InToOut(List<FileShareEntity.FileShareEntityInput> list){
    //返回展示在页面上的文件分享信息
    List<FileShareEntity.FileShareEntityOutput> fileShareEntityOutputList;

    fileShareEntityOutputList=list.stream().map(item ->{
      FileShareEntity.FileShareEntityOutput fileShareEntityOutput=new FileShareEntity.FileShareEntityOutput();
      //id
      fileShareEntityOutput.setShareId(item.getShareId());
      //分享码
      fileShareEntityOutput.setShareCode(item.getShareCode());
      //文件名
      fileShareEntityOutput.setFileName(item.getFileName());
      //文件大小
      fileShareEntityOutput.setFileSize(item.getFileSize());
      //开始时间
      fileShareEntityOutput.setStartTime(DateToString.StringData(item.getStartTime()));
      //过期时间
      if(item.getTimeFlag()==4){
        fileShareEntityOutput.setExpirationTime("永久");
      }else {
        fileShareEntityOutput.setExpirationTime(DateToString.StringData(item.getExpirationTime()));
      }
      //状态
      if(item.getCondition()==0){
        fileShareEntityOutput.setCondition("分享中");
        fileShareEntityOutput.setConditionClass("mb-0 badge badge-success");
        fileShareEntityOutput.setButtonText("停止分享");
        fileShareEntityOutput.setButtonClass("mb-0 badge badge-warning toltiped");
      }else {
        fileShareEntityOutput.setCondition("已过期");
        fileShareEntityOutput.setConditionClass("mb-0 badge badge-danger");
        fileShareEntityOutput.setButtonText("删除记录");
        fileShareEntityOutput.setButtonClass("mb-0 badge badge-danger toltiped");
      }
      //下载次数
      if(item.getDownloadFlag()==0){
        fileShareEntityOutput.setDownloadNumber("无限");
      }else {
        fileShareEntityOutput.setDownloadNumber(""+item.getDownloadNumber());
      }
      //被下载次数
      fileShareEntityOutput.setNumberOfDownload(item.getNumberOfDownload());
      return fileShareEntityOutput;
    }).collect(Collectors.toList());

    return fileShareEntityOutputList;
  }
}
