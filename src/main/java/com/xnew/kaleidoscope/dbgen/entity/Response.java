package com.xnew.kaleidoscope.dbgen.entity;

import lombok.Data;

/**
 * @ClassName Response
 * @Description TODO
 * @Author 13503
 * @Date 2020/3/29 14:48
 * @Version 1.0
 **/
@Data
public class Response {

    private Integer code;

    private String msg;

    private Object data;

    public Response(Object data){
        this.code = 0;
        this.msg = "";
        this.data = data;
    }

    public Response(Integer code,String msg,Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
