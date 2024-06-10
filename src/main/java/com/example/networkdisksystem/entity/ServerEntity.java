package com.example.networkdisksystem.entity;

import lombok.Data;

import java.util.List;

@Data
public class ServerEntity {
  
  List<ServerDto> serverDtoList;
  
  @Data
  public static class ServerDto {
    
    /**
     * 判断是否宕机 0：宕机 1：在线.
     */
    private int downFlag;
    
    /**
     * zookeeper状态 lender/flower.
     */
    private String zk;

    /**
     * CPU.
     */
    private String cpu;

    /**
     * 内存.
     */
    private String internal;

    /**
     * 磁盘.
     */
    private String disc;
  } 
}
