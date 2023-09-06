package com.example.networkdisksystem.mapper;

import com.example.networkdisksystem.entity.FileEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Insert("insert into file_table(mid,fileName,FileSize,uid) values (#{mid},#{fileName},#{FileSize},#{uid})")
    int addFile(@Param("mid") int mid,
                @Param("fileName") String fileName,
                @Param("FileSize")String fileSize,
                @Param("uid") int uid);

    @Results({
            @Result(column = "fid", property = "fid"),
            @Result(column = "mid", property = "mid"),
            @Result(column = "fileName", property = "fileName")
    })
    @Select("select * from file_table where mid=#{mid}")
    List<FileEntity> getFileByMid(int mid);

    @Select("select fid from file_table where mid=#{mid} and fileName=#{fileName}")
    int getFileIdByMidAndFileName(@Param("mid") int mid,@Param("fileName") String fileName);
    @Delete("delete from file_table where fid=#{fid}")
    int deleteFile(int fid);

    @Select("select fileName from file_table where fid=#{fid}")
    String getFileNameByFid(int fid);

    @Update("update file_table set fileName=#{filename} where fid=#{fid}")
    int rename(@Param("filename") String filename,@Param("fid") int fid);

    @Select("select * from file_table where uid=#{uid} and fileName like concat('%',#{fileName},'%')")
    List<FileEntity> getFileByIdAndFileName(@Param("uid") int uid,@Param("fileName") String fileNmae);
}
