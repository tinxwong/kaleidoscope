package com.xnew.kaleidoscope.dbgen.service;

import com.xnew.common.base.Asserts;
import com.xnew.common.exception.IllegalException;
import com.xnew.kaleidoscope.dbgen.entity.Column;
import com.xnew.kaleidoscope.dbgen.entity.DBMsg;
import com.xnew.kaleidoscope.dbgen.response.DBGenErrorCode;
import com.xnew.kaleidoscope.dbgen.util.MapUtils;

import com.xnew.kaleidoscope.dbgen.util.MpGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @ClassName DBGenService
 * @Description TODO
 * @Author tinx
 * @Date 2019/3/15 0015 下午 12:03
 * @Version 1.0
 **/
@Service
public class DBGenService {

    private Logger logger = LoggerFactory.getLogger(DBGenService.class);

    private final static String SQL_SUF = "?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&autoReconnect=true&failOverReadOnly=false";

    /**
     * @return
     * @Author tinx
     * @Description //连接数据库
     * @Date 下午 1:51 2019/3/15 0015
     * @Param
     */
    public boolean connectDB(DBMsg dbMsg) {
        boolean isConnection = false;
        Connection connection = (Connection) MapUtils.getValue(dbMsg.getDatabasename());

        try {
            if (connection != null && !connection.isClosed()) {
                logger.info("数据库已在链接状态！");
                return true;
            }
            connection = DriverManager.getConnection(getUrl(dbMsg), dbMsg.getUser(), dbMsg.getPwd());
            MapUtils.put(dbMsg.getDatabasename(), connection);
            logger.info("数据库链接成功！");
            isConnection = true;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new IllegalException(DBGenErrorCode.DB_CONNECTION_NULL);
        }
        return isConnection;
    }

    /**
     * 判断链接状态
     *
     * @param databasename
     * @return
     */
    public boolean connectionStatus(String databasename) {
        Connection connection = (Connection) MapUtils.getValue(databasename);

        try {
            if (connection != null && !connection.isClosed()) {
                logger.info("数据库已在链接状态！");
                return true;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new IllegalException(DBGenErrorCode.DB_CONNECTION_NULL);
        }
        return false;
    }

    /**
     * 获取数据库链接
     *
     * @param dbMsg
     * @return
     */
    private String getUrl(DBMsg dbMsg) {
        StringBuilder url = new StringBuilder("jdbc:mysql://").append(dbMsg.getUrl()).append(":").append(dbMsg.getPort()).append("/").append(dbMsg.getDatabasename()).append(SQL_SUF);
        logger.info("url:" + url);
        return url.toString();
    }

    /**
     * @return
     * @Author tinx
     * @Description //检查数据库链接
     * @Date 下午 1:51 2019/3/15 0015
     * @Param
     */
    public boolean checkConnection(DBMsg dbMsg) {
        boolean isConnection = true;
        Connection connection = (Connection) MapUtils.getValue(dbMsg.getDatabasename());
        try {
            if (connection == null && connection.isClosed()) {
                isConnection = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            isConnection = false;
        }
        return isConnection;
    }

    /**
     * @return
     * @Author tinx
     * @Description //执行创建表语句
     * @Date 下午 1:54 2019/3/15 0015
     * @Param
     */
    public boolean createTable(DBMsg dbMsg) {
        boolean isSuccess = false;
        if (!checkConnection(dbMsg)) {
            connectDB(dbMsg);
        }
        Connection connection = (Connection) MapUtils.getValue(dbMsg.getDatabasename());
        if (checkTableIfExist(connection, dbMsg.getTablename())) {
            throw new IllegalException(DBGenErrorCode.DB_TABLE_IS_EXIST);
        }
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            int count = stmt.executeUpdate(dbMsg.getSql());
            isSuccess = true;
            logger.info("执行sql语句={},count={}", dbMsg.getSql(), count);
        } catch (SQLException e) {
            logger.error("执行sql语句失败!" + dbMsg.getSql());
            logger.error(e.getMessage());
            throw new IllegalException(DBGenErrorCode.DB_CREATE_TABLE_ERROR);
        }
        return isSuccess;
    }

    public boolean checkTableIfExist(Connection connection, String tablename) {
        String sql = String.format("select count(*) from `%s` where 1=1", tablename);
        logger.info("检查表是否存在");
        PreparedStatement pStat = null;
        ResultSet rs = null;
        boolean isExist = false;
        try {
            pStat = connection.prepareStatement(sql);
            rs = pStat.executeQuery();
            isExist = true;
        } catch (SQLException e) {
            isExist = false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (pStat != null) {
                    pStat.close();
                    pStat = null;
                }
            } catch (SQLException e) {
                rs = null;
                pStat = null;
                logger.error("释放数据库资源异常:" + e.getMessage());
            }

        }
        return isExist;
    }

    public boolean genCode(DBMsg dbMsg) {
        String url = getUrl(dbMsg);
        dbMsg.setUrl(url);
        String creater = dbMsg.getCreater();
        if (StringUtils.isBlank(creater)) {
            creater = "xnew";
        }
        if (StringUtils.isNotBlank(dbMsg.getSql())) {
            try {
                createTable(dbMsg);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }

        }
        MpGenerator.generator(dbMsg);
        return true;
    }


    /**
     * 获取所有表
     *
     * @param dbMsg
     * @return
     */
    public List<String> getDbAllTable(DBMsg dbMsg) {

        Connection connection = (Connection) MapUtils.getValue(dbMsg.getDatabasename());
        List<String> tables = new ArrayList<String>();
        try {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet rs = dbMetaData.getTables(null, null, null, new String[]{"TABLE"});
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    public List<Column> getTableColumn(DBMsg dbMsg) {
        String sql = "select * from " + dbMsg.getTablename();
        Connection connection = (Connection) MapUtils.getValue(dbMsg.getDatabasename());
        List<Column> columns = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            for (int i = 1; i < columnCount + 1; i++) {
                Column column = new Column();
                column.setName(meta.getColumnName(i));
                column.setType(meta.getColumnTypeName(i));
                column.setLength(meta.getColumnDisplaySize(i));
                columns.add(column);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columns;
    }

    public boolean addTableColumn(Column column) {

        Asserts.notNull(column.getTableName(), DBGenErrorCode.DB_TABLE_NAME_IS_NULL);
        boolean isSuccess = false;
        Integer length = column.getLength();
        String type = column.getType();
        if (length != null) {
            type = type + "(" + length.intValue() + ")";
        }
        String dft = column.getDft();
        if (StringUtils.isNotBlank(dft)) {
            dft = "default " + dft;
        }
        String ifNull = "";
        if (column.isIfNull()) {
            ifNull = "null";
        } else {
            ifNull = "not null";
        }
        String sql = String.format("alter table %s add column %s %s %s %s comment '%s'", column.getTableName(), column.getName(), type, dft, ifNull, column.getComment());
        if (StringUtils.isNotBlank(column.getAfter())) {
            sql += " after " + column.getAfter();
        }
        Connection connection = (Connection) MapUtils.getValue(column.getDatabaseName());
        if (!checkTableIfExist(connection, column.getTableName())) {
            throw new IllegalException(DBGenErrorCode.DB_TABLE_IS_NOT_EXIST);
        }
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            int count = stmt.executeUpdate(sql);
            isSuccess = true;
            logger.info("执行sql语句={},count={}", sql, count);
        } catch (SQLException e) {
            logger.error("执行sql语句失败!" + sql);
            logger.error(e.getMessage());
            throw new IllegalException(DBGenErrorCode.DB_CREATE_TABLE_ERROR);
        }
        return isSuccess;
    }


    public boolean addTableColumnBySql(String databaseName, String tableName, String sql) {
        boolean isSuccess = false;
        Connection connection = (Connection) MapUtils.getValue(databaseName);
        if (!checkTableIfExist(connection, tableName)) {
            throw new IllegalException(DBGenErrorCode.DB_TABLE_IS_EXIST);
        }
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            int count = stmt.executeUpdate(sql);
            isSuccess = true;
            logger.info("执行sql语句={},count={}", sql, count);
            stmt.close();
        } catch (SQLException e) {
            logger.error("执行sql语句失败!" + sql);
            logger.error(e.getMessage());
            throw new IllegalException(DBGenErrorCode.DB_TABLE_ADD_COLUMN_FAIL);
        }
        return isSuccess;
    }

    public boolean dropTableColumn(String databaseName, String tableName, String column) {
        String sql = String.format("alter table %s drop column %s", tableName, column);
        boolean isSuccess = false;
        Connection connection = (Connection) MapUtils.getValue(databaseName);
        if (!checkTableIfExist(connection, tableName)) {
            throw new IllegalException(DBGenErrorCode.DB_TABLE_IS_EXIST);
        }
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            int count = stmt.executeUpdate(sql);
            isSuccess = true;
            logger.info("执行sql语句={},count={}", sql, count);
            stmt.close();
        } catch (SQLException e) {
            logger.error("执行sql语句失败!" + sql);
            logger.error(e.getMessage());
            throw new IllegalException(DBGenErrorCode.DB_TABLE_DROP_COLUMN_FAIL);
        }
        return isSuccess;
    }

    public boolean dropTable(String databaseName, String tableName) {
        String sql = String.format("drop table %s", tableName);
        boolean isSuccess = false;
        Connection connection = (Connection) MapUtils.getValue(databaseName);
        if (!checkTableIfExist(connection, tableName)) {
            throw new IllegalException(DBGenErrorCode.DB_TABLE_IS_EXIST);
        }
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            int count = stmt.executeUpdate(sql);
            isSuccess = true;
            logger.info("执行sql语句={},count={}", sql, count);
            stmt.close();
        } catch (SQLException e) {
            logger.error("执行sql语句失败!" + sql);
            logger.error(e.getMessage());
            throw new IllegalException(DBGenErrorCode.DB_TABLE_DROP_COLUMN_FAIL);
        }
        return isSuccess;
    }

    public HashMap getSelectSearch(DBMsg dbMsg) {
        Connection connection = (Connection) MapUtils.getValue(dbMsg.getDatabasename());
        List<Column> columns = new ArrayList<>();
        List<HashMap> rows = new ArrayList<>();
        HashMap result = new HashMap();
        try {
            PreparedStatement ps = connection.prepareStatement(dbMsg.getSql());
            ResultSet rs = ps.executeQuery();

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            for (int i = 1; i < columnCount + 1; i++) {
                Column column = new Column();
                column.setName(meta.getColumnName(i));
                column.setType(meta.getColumnTypeName(i));
                column.setLength(meta.getColumnDisplaySize(i));
                columns.add(column);
            }
            if (CollectionUtils.isNotEmpty(columns)) {
                while (rs.next()) {
                    HashMap row = new LinkedHashMap();
                    for (Column c : columns) {
                        String columnName = c.getName();
                        row.put(columnName, rs.getString(columnName));
                    }
                    rows.add(row);
                }
            }
            result.put("columns", columns);
            result.put("rows", rows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
