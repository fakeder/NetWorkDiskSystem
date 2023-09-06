package com.example.networkdisksystem.service.serviceImpl;

import com.example.networkdisksystem.service.VerifyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class VerifyServiceImpl implements VerifyService {

    @Resource
    JavaMailSender sender;
    @Resource
    StringRedisTemplate template;

    @Value("${spring.mail.username}")
    String from;

    @Override
    public void sendMailCode(String mail) {//发送验证码
        SimpleMailMessage message=new SimpleMailMessage();
        Random random=new Random();//随机数
        int code=random.nextInt(899999)+100000;//899999内的随机数加上100000保证验证码code是六位数
        System.out.println("验证码为："+code);
        //将验证码放入Redis数据库中，设置过期时间为五分钟
        template.opsForValue().set("verify:code:"+mail,""+code,5,TimeUnit.MINUTES);
        //编写发送邮件
        message.setSubject("[xxx网盘系统]您的注册验证码");
        message.setText("您的注册验证码为:"+code+",五分钟内有效,请及时完成注册!若非本人操作,请忽略。");
        message.setTo(mail);
        message.setFrom(from);
        sender.send(message);
    }

    @Override
    public boolean doVerify(String mail, String code) {//验证邮箱和验证码是否正确

        System.out.println("===============");
        String email=template.opsForValue().get("verify:code:"+mail);//通过前段传递的邮箱获取验证码
        if(email==null)return false;
        if(!email.equals(code))return false;//验证验证码是否正确
        template.delete("verify:code:"+mail);//验证成功后删除缓存中的验证码
        return true;
    }
}
