package com.example.networkdisksystem.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateChangeToString {
  public String StringData(Date date){
    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return df.format(date);
  }
}
