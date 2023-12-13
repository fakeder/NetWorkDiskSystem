package com.example.networkdisksystem.util;

import java.text.DecimalFormat;

//文件大小转换 字节->B,KB,MB,GB,TB
public class SizeChange {
    public static String formatFileSize(long size) {
        if(size==0)return "0.00 B";
        String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int unitIndex = 0;
        long sz=1;
        long ss=size;
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
            sz*=1024;
        }
        sz=sz*size;
        float SZ=ss-sz;
        for(int i=0;i<unitIndex;i++){
            SZ=SZ/1024;
        }

        float SIZE=size+SZ;
        DecimalFormat df = new DecimalFormat("#.00");
        String roundedNum = df.format(SIZE);
        return String.format("%s %s", roundedNum, units[unitIndex]);
    }

    public static long formatFileSizeReverse(String size){
        if(size==null||size==""){
            System.out.println("传入参数为空");
            return -1;
        }
        String[] s = size.split(" ");
        String[] sz=s[0].split("\\.");
        Long sz1 = Long.valueOf(sz[0]);
        Long sz2 = Long.valueOf(sz[1]);
        Long FileSize=0l;
        switch (s[1]){
            case "B":FileSize=sz1+sz2/100;break;
            case "KB":FileSize=sz1*1024+sz2*1024/100;break;
            case "MB":FileSize=sz1*1024*1024+sz2*1024*1024/100;break;
            case "GB":FileSize=sz1*1024*1024*1024+sz2*1024*1024*1024/100;break;
            case "TB":FileSize=sz1*1024*1024*1024*1024+sz2*1024*1024*1024*1024/100;break;
        }
        return FileSize;
    }

    public static void main(String[] args) {
        //System.out.println(formatFileSizeReverse("1.00 GB"));
        System.out.println(formatFileSize(0));
        //System.out.println(formatFileSize(1073741824));
        //System.out.println(500f/1024/1024);
    }
}
