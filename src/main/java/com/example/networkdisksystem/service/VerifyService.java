package com.example.networkdisksystem.service;

public interface VerifyService {
    void sendMailCode(String mail);

    int doVerify(String mail,String code);
}
