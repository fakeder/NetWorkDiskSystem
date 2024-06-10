package com.example.networkdisksystem.mapper;

import com.example.networkdisksystem.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {

    /**
     * 获取用户list
     * @return 用户list
     */
    List<Users> getAllUsers(int limit);

    /**
     * 统计用户数量
     */
    int countUser();

    /**
     * 启用数量
     */
    int countUsedUser();

    /**
     * 禁用总量
     */
    int countForbiddenUser();

    /**
     * 会员
     */
    int countVIP();

    /**
      * 统计用户文件、文件夹个数
      * @param table 表名
      * @param uid 用户名
      * @return 统计数量
      */
    int countByUid(@Param("table") String table,@Param("uid") int uid);
}
