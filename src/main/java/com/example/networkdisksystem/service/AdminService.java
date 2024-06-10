package com.example.networkdisksystem.service;

import com.example.networkdisksystem.entity.AdminEntity;

public interface AdminService {
    /**
     * 验证管理员账号密码
     * @param username 用户名
     * @param password 密码
     */
    boolean adminLogin(String username,String password);

  /**
   * 改变用户状态
   * @param uid 用户id
   * @param condition 状态
   */
  void changeCondition(int uid,int condition);

  /**
   * 删除用户
   *
   * @param uid uid
   * @return 1:删除成功 0:删除失败
   */
  int deleteUser(int uid);

  /**
   * 查询用户详细信息.
   * @param uid uid
   * @return 用户详细信息
   */
  AdminEntity.UserDetail catUserDetail(int uid);

  /**
   * 更改用户会员等级.
   * @param uid uid
   * @param level 会员等级
   */
  int userLevelChange(int uid, String level);
}
