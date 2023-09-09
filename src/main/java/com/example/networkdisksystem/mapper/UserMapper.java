package com.example.networkdisksystem.mapper;

import com.example.networkdisksystem.entity.Users;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    Users getUsersByUsername(String username);

    @Insert("insert into user(username,password,email) values (#{username},#{password},#{email})")
    int register(@Param("username") String username,
                 @Param("password") String password,
                 @Param("email") String email);

    @Update("update user set mid=#{mid} where uid=#{uid}")
    int setMkdirId(@Param("mid") int mid,@Param("uid") int uid);

    @Update("update user set usedSize=#{usedSize} where uid=#{uid}")
    int updateUsedSizeByUid(@Param("uid")int uid,@Param("usedSize") String usedSize);

    @Select("select count(email) from user where email=#{email}")
    int checkEmail(String email);

    @Select("select count(username) from user where username=#{username}")
    int checkUserName(String username);

    @Select("select * from user where email=#{email}")
    Users getUserByEmail(String email);

    @Update("update user set password=#{password} where uid=#{uid}")
    int updatePassword(@Param("uid") int uid,@Param("password") String password);
}
