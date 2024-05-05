package com.example.networkdisksystem.mapper;

import com.example.networkdisksystem.entity.Users;
import org.apache.ibatis.annotations.Mapper;

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
}
