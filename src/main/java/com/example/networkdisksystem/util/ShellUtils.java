package com.example.networkdisksystem.util;

import com.example.networkdisksystem.config.AdminConfig;
import com.example.networkdisksystem.config.HadoopConfig;
import com.example.networkdisksystem.config.ShellConfig;
import com.example.networkdisksystem.entity.ServerEntity;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Component
public class ShellUtils {
  @Autowired
  AdminConfig adminConfig;
  
  @Autowired
  HadoopConfig hadoopConfig;

  /**
   * 
   * @param username 初始化SSH连接参数
   * @param password 初始化SSH连接参数
   * @param servers Hadoop集群中的服务器IP地址列表
   * @return
   */
  public  ServerEntity serverDetail(String username,String password,String[] servers) {

    ServerEntity serverEntity = new ServerEntity();
    
    TestCallable callable1 = new TestCallable(username, password, servers[0]);
    FutureTask<ServerEntity.ServerDto> ft1 =new FutureTask<>(callable1);
    new Thread(ft1).start();
    
    
    TestCallable callable2 = new TestCallable(username, password, servers[1]);
    FutureTask<ServerEntity.ServerDto> ft2 =new FutureTask<>(callable2);
    new Thread(ft2).start();
    
    
    TestCallable callable3 = new TestCallable(username, password, servers[2]);
    FutureTask<ServerEntity.ServerDto> ft3 =new FutureTask<>(callable3);
    new Thread(ft3).start();
    
    List<ServerEntity.ServerDto> serverDtoList = new ArrayList<>();
    
    try {
     serverDtoList.add(ft1.get());
     serverDtoList.add(ft2.get());
     serverDtoList.add(ft3.get());
     serverEntity.setServerDtoList(serverDtoList);
    }catch (Exception e){
     e.printStackTrace();
    }
    return serverEntity;
  }
  
  public static class TestCallable implements Callable<ServerEntity.ServerDto> {
    public final String username;
    public final String password ;
    public final String server;
    
    TestCallable(String username,String password ,String server){
      this.username=username;
      this.password=password;
      this.server=server;
    }

    @Override
    public ServerEntity.ServerDto call() throws Exception {
      ServerEntity.ServerDto serverDto = new ServerEntity.ServerDto();
      try {
        // 创建SSH会话
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, server, 22);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        String line;
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");

        // 执行sar命令获取cpu使用情况
        channelExec.setCommand(ShellConfig.CPU);
        channelExec.connect();
        InputStream cpuStream = channelExec.getInputStream();
        BufferedReader cpuReader = new BufferedReader(new InputStreamReader(cpuStream));
        System.out.println("========="+server+"========");
        System.out.println("=======CPU===========");
        int cpu=0;
        while ((line = cpuReader.readLine()) != null) {
          // 解析cpu命令输出，提取cpu的使用情况信息
          cpu++;
          if(cpu == 9) {
            System.out.println(line);
            String[] s = line.split(" ");
            System.out.println("空闲CPU:"+s[s.length-1]);
            float a=Float.parseFloat(s[s.length-1]);
            System.out.println("使用CPU:"+String.format("%.2f",100-a));
            serverDto.setCpu(String.format("%.2f",100-a));
          }
          // 根据具体的输出格式进行解析和处理
        }
        cpuReader.close();
        channelExec.disconnect();

        channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(ShellConfig.INTERNAL);
        channelExec.connect();
        InputStream internalStream = channelExec.getInputStream();
        BufferedReader internalReader = new BufferedReader(new InputStreamReader(internalStream));
        System.out.println("=======内存===========");
        while ((line = internalReader.readLine()) != null) {
          // 解析free -h命令输出，提取内存的使用情况信息
          line=internalReader.readLine();
          String s1=line.replaceAll("\\s+", " ");
          String[] s2 = s1.split(" ");
          System.out.println(line);
          System.out.println(s2[1]);
          System.out.println(s2[2]);
          System.out.println(s2[3]);
          float total=Float.parseFloat(s2[1].substring(0,s2[1].length()-1));
          float used=Float.parseFloat(s2[3].substring(0,s2[3].length()-1));
          String internal= String.format("%.2f",(total-used)/total*100);
          System.out.println("内存使用率:"+internal+"%");
          serverDto.setInternal(internal);
          break;
        }
        internalReader.close();
        channelExec.disconnect();

        // 执行df命令获取磁盘使用情况
        channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(ShellConfig.DESC);
        channelExec.connect();
        InputStream dfStream = channelExec.getInputStream();
        BufferedReader dfReader = new BufferedReader(new InputStreamReader(dfStream));
        System.out.println("=======磁盘===========");
        int i =0;
        while ((line = dfReader.readLine()) != null) {
          // 解析df命令输出，提取磁盘使用情况信息
          i++;
          if(i==6) {
            System.out.println(line);
            String[] split = line.replaceAll("\\s+", " ").split(" ");
            System.out.println(split[4]);
            serverDto.setDisc(split[4].replace("%",""));
            break;
          }
        }
        dfReader.close();
        channelExec.disconnect();
        channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(ShellConfig.ZOOKEEPER);
        channelExec.connect();
        InputStream zkStream = channelExec.getInputStream();
        BufferedReader zkReader = new BufferedReader(new InputStreamReader(zkStream));
        System.out.println("=======zk===========");
        int z=0;
        while ((line = zkReader.readLine()) != null) {
          // 解析命令输出
          z++;
          if(z==2) {
            System.out.println("zk++++++++++"+line);
            System.out.println(line.split(" ")[1]);
            serverDto.setZk(line.split(" ")[1]);
            break;
          }
          // 根据具体的输出格式进行解析和处理
        }
        zkReader.close();
        channelExec.disconnect();
        /*
        // 执行dstat --top-cpu 1 10命令获取最占cpu的前十条进程
        channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(ShellConfig.PROCESS);
        channelExec.connect();
        InputStream processStream = channelExec.getInputStream();
        BufferedReader processReader = new BufferedReader(new InputStreamReader(processStream));
        System.out.println("=======进程===========");
        while ((line = processReader.readLine()) != null) {
          // 解析命令输出
          System.out.println(line);
          // 根据具体的输出格式进行解析和处理
        }
        processReader.close();
        channelExec.disconnect();
        */
        // 关闭SSH会话
        session.disconnect();
        serverDto.setDownFlag(1);
      } catch (Exception e) {
        serverDto.setDownFlag(0);
        e.printStackTrace();
      }
      return serverDto;
    }
  }
}
