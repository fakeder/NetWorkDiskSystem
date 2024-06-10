package com.example.networkdisksystem.config;

public class ShellConfig {

  /**
   * 查看CPU.
   */
  public static final String CPU = "sar -u 1 5";

  /**
   * 查看内存.
   */
  public static final String INTERNAL = "free -h";

  /**
   * 查看磁盘.
   */
  public static final String DESC = "df -h";

  /**
   * 获取最占cpu的前十条进程
   */
  public static final String PROCESS = "dstat --top-cpu 1 10";

  /**
   * 查看zookeeper状态.
   */
  public static final String ZOOKEEPER = "/opt/zookeeper/zookeeper-3.5.7/bin/zkServer.sh status";
}
