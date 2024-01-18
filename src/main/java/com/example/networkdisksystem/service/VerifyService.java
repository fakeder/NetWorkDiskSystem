package com.example.networkdisksystem.service;

public interface VerifyService {

  /**
   * 向邮箱发送验证码
   * @param mail 邮箱
   */
    void sendMailCode(String mail);

  /**
   * 验证邮箱验证码是否有效
   * @param mail 邮箱
   * @param code 验证码
   * @return
   */
    int doVerify(String mail,String code);

  /**
   * 删除验证码
   * @param mail 邮箱
   */
  void deleteCode(String mail);
}
