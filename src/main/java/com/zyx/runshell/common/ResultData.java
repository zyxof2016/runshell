package com.zyx.runshell.common;

import lombok.Data;

import java.io.Serializable;
@Data
public class ResultData <E> implements Serializable {
    private E data;
    private Integer code = 200;
    private String errMsg;
    private Boolean success = true;

    public ResultData() {
    }

    public ResultData(E data) {
        this.data = data;
    }

    public ResultData(Integer code, String errMsg) {
        this.success = false;
        this.code = code;
        this.errMsg = errMsg;
    }
}
