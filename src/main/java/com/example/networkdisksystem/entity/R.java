package com.example.networkdisksystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class R {
    int code;
    String reason;

    public String getReason() {
        return reason;
    }

    public int getCode() {
        return code;
    }
}
