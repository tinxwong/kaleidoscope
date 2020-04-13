package com.xnew.kaleidoscope.dbgen.entity;

import lombok.Data;

/**
 * @ClassName Column
 * @Description TODO
 * @Author 13503
 * @Date 2020/3/29 14:08
 * @Version 1.0
 **/
@Data
public class Column {

    private String name;
    private Integer length;
    private String type;
    private String comment;
    private String after;
    private String dft;
    private boolean ifNull;
    private String tableName;
    private String databaseName;
}
