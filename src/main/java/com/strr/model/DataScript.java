package com.strr.model;

/**
 * 脚本
 */
public class DataScript {
    /**
     * 数据源
     */
    private String datasource;

    /**
     * 执行语句
     */
    private String sql;

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
