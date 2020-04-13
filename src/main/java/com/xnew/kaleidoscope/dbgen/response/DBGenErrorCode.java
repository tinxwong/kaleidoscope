package com.xnew.kaleidoscope.dbgen.response;

import com.xnew.common.response.status.StatusCode;

public enum DBGenErrorCode implements StatusCode {

    DB_CONNECTION_NULL(20000001,"数据库没有链接！","DB_CONNECTION_NULL"),
    DB_CREATE_TABLE_ERROR(20000002,"创建表失败！","DB_CREATE_TABLE_ERROR"),
    DB_TABLE_IS_EXIST(20000003,"创建失败，表已经存在!","DB_TABLE_IS_EXIST"),
    DB_TABLE_NAME_IS_NULL(20000004,"创建失败，没有指定表名!","DB_TABLE_NAME_IS_NULL"),
    DB_TABLE_IS_NOT_EXIST(20000005,"表不存在!","DB_TABLE_IS_NOT_EXIST"),
    DB_TABLE_ADD_COLUMN_FAIL(20000006,"添加表字段失败!","DB_TABLE_ADD_COLUMN_FAIL"),
    DB_TABLE_DROP_COLUMN_FAIL(20000007,"删除表字段失败!","DB_TABLE_DROP_COLUMN_FAIL");

    private int code;
    private String msg;
    private String name;

    DBGenErrorCode(int code, String msg, String name){
        this.code = code;
        this.msg = msg;
        this.name =  name;
    }

    @Override
    public String getModuleName() {
        return "表生成工具组件";
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String getName() {
        return name;
    }
}
