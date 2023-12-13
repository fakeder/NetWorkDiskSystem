package com.example.networkdisksystem.mapper;

import com.example.networkdisksystem.entity.Users;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return 用户类
     */
    Users getUsersByUsername(String username);

    /**
     * 注册.
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     * @return
     */
    int register(@Param("username") String username,
                 @Param("password") String password,
                 @Param("email") String email);

    /**
     * 注册时使用，设置用户目录id
     * @param mid 目录id
     * @param uid 用户id
     * @return
     */
    int setMkdirId(@Param("mid") int mid,@Param("uid") int uid);


    /**
     * 更新已使用大小
     * @param uid uid
     * @param usedSize 已使用大小 (B)
     * @param usedSizeByte 已使用大小 (字节)
     * @return
     */
    int updateUsedSizeByUid(@Param("uid")int uid,@Param("usedSize") String usedSize,@Param("usedSizeByte") long usedSizeByte);

    /**
     * 统计注册邮箱个数（判断是否重复）
     * @param email 邮箱
     * @return 注册邮箱个数
     */
    int checkEmail(String email);

    /**
     * 统计用户名个数（判断是否重复）
     * @param username 用户名
     * @return 用户名个数
     */
    int checkUserName(String username);

    /**
     * 根据邮箱获取用户信息
     * @param email 邮箱
     * @return 用户信息
     */
    Users getUserByEmail(String email);

    /**
     * 根据uid重新设置密码
     * @param uid uid
     * @param password 新密码
     * @return 1:成功执行  0:执行失败
     */
    int updatePassword(@Param("uid") int uid,@Param("password") String password);

    /**
     * 通过uid获取用户
     * @param uid uid
     * @return Users
     */
    Users getUserById(int uid);
}
