package com.xnew.kaleidoscope.dbgen.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName DatabaseMsg
 * @Description TODO
 * @Author tinx
 * @Date 2019/3/15 0015 下午 12:02
 * @Version 1.0
 **/
@Data
public class DBMsg implements Serializable {

    private static final long serialVersionUID = 2415666486540389324L;

    private String url;
    private String databasename;
    private String port;
    private String user;
    private String pwd;
    private String sql;
    private String model;
    private String creater;
    private String tablename;
    private String driver;
    private List<String> sw;
    private List<String> enums;
    private List<String> enumsType;
}
