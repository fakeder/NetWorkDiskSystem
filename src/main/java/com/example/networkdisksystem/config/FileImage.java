package com.example.networkdisksystem.config;

import lombok.Data;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class FileImage {
    private static Map<String,String> imageMapping=new HashMap<>();
    private static final String Path="../static/assets/images/my_images/";
    static {
        imageMapping.put("png",Path+"图片.png");
        imageMapping.put("jpg",Path+"图片.png");
        imageMapping.put("jpeg",Path+"图片.png");
        imageMapping.put("csv",Path+"excel.png");
        imageMapping.put("xls",Path+"excel.png");
        imageMapping.put("xlsx",Path+"excel.png");
        imageMapping.put("mp3",Path+"mp3.png");
        imageMapping.put("mp4",Path+"mp4.png");
        imageMapping.put("pdf",Path+"pdf.png");
        imageMapping.put("ppt",Path+"ppt.png");
        imageMapping.put("txt",Path+"txt.png");
        imageMapping.put("doc",Path+"word.png");
        imageMapping.put("docx",Path+"word.png");
        imageMapping.put("zip",Path+"zip.png");
    }
    public String getPath(String index){
        String path=imageMapping.get(index);
        if(Objects.isNull(path)){
            return Path+"默认.png";
        }else {
            return path;
        }
    }
}
