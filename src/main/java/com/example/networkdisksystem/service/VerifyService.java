package com.example.networkdisksystem.service;

public interface VerifyService {
    void sendMailCode(String mail);

    boolean doVerify(String mail,String code);
}
