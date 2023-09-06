package com.example.networkdisksystem.mapper;

import com.example.networkdisksystem.entity.FolderEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FolderMapper {

    @Insert("insert into mkdir_table(uid,FolderName,UpFolderId) values (#{uid},#{FolderName},#{UpFolderId})")
    int insertMkdir(@Param("uid") int uid,@Param("FolderName") String FolderName,@Param("UpFolderId") int UpFolderId);

    //仅在注册时使用UpFolderId=-1
    @Select("select * from mkdir_table where uid=#{uid} and UpFolderId=#{UpFolderId}")
    FolderEntity getFolderByUidAndUpMkdirId(@Param("uid") int uid, @Param("UpFolderId") int UpFolderId);

    @Select("select * from mkdir_table where UpFolderId=#{UpMkdirId}")
    List<FolderEntity> getFolderByUpMkdirId(int UpFolderId);

    @Delete("delete FROM mkdir_table where mid=#{mid}")
    int delete(int mid);

    @Update("update mkdir_table set FolderName=#{FolderName} where mid=#{mid}")
    int rename(@Param("mid") int mid,@Param("FolderName") String FolderName);

    @Select("select UpFolderId from mkdir_table where mid=#{mid}")
    int getUpFolderIdByMid(int mid);

    @Select("select * from mkdir_table where uid=#{uid} and FolderName like CONCAT('%',#{FolderName},'%')")
    List<FolderEntity> getFolderByIdAndFolderName(@Param("uid") int uid ,@Param("FolderName") String FolderName);
}
