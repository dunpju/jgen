package com.dunpju.orm;

import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.ibatis.jdbc.SQL;

import java.util.HashMap;
import java.util.Map;

public class Builder {
    SQL sql;
    Map<String, Object> map;
    Map<String, Object> parameters;

    public Builder() {
        this.sql = new SQL();
        this.parameters = new HashMap<>();
    }

    public Builder UPDATE(String table) {
        this.sql.UPDATE(table);
        return this;
    }

    public Builder SET(String sets) {
        this.sql.SET(sets);
        return this;
    }

    public Builder SET(String... sets) {
        this.sql.SET(sets);
        return this;
    }

    public Builder INSERT_INTO(String tableName) {
        this.sql.INSERT_INTO(tableName);
        return this;
    }

    public Builder VALUES(String columns, String values) {
        this.sql.VALUES(columns, values);
        return this;
    }

    public Builder INTO_COLUMNS(String... columns) {
        this.sql.INTO_COLUMNS(columns);
        return this;
    }

    public Builder INTO_VALUES(String... values) {
        this.sql.INTO_VALUES(values);
        return this;
    }

    public Builder SELECT(String columns) {
        this.sql.SELECT(columns);
        return this;
    }

    public Builder SELECT(String... columns) {
        this.sql.SELECT(columns);
        return this;
    }

    public Builder SELECT_DISTINCT(String columns) {
        this.sql.SELECT_DISTINCT(columns);
        return this;
    }

    public Builder SELECT_DISTINCT(String... columns) {
        this.sql.SELECT_DISTINCT(columns);
        return this;
    }

    public Builder DELETE_FROM(String table) {
        this.sql.DELETE_FROM(table);
        return this;
    }

    public Builder FROM(Class<?> table) {
        this.sql.FROM(table.getAnnotation(TableName.class).value());
        return this;
    }

    public Builder FROM(String table) {
        this.sql.FROM(table);
        return this;
    }

    public Builder FROM(String... tables) {
        this.sql.FROM(tables);
        return this;
    }

    public Builder JOIN(String join) {
        this.sql.JOIN(join);
        return this;
    }

    public Builder JOIN(String... joins) {
        this.sql.JOIN(joins);
        return this;
    }

    public Builder INNER_JOIN(String join) {
        this.sql.INNER_JOIN(join);
        return this;
    }

    public Builder INNER_JOIN(String... joins) {
        this.sql.INNER_JOIN(joins);
        return this;
    }

    public Builder LEFT_OUTER_JOIN(String join) {
        this.sql.LEFT_OUTER_JOIN(join);
        return this;
    }

    public Builder LEFT_OUTER_JOIN(String... joins) {
        this.sql.LEFT_OUTER_JOIN(joins);
        return this;
    }

    public Builder RIGHT_OUTER_JOIN(String join) {
        this.sql.RIGHT_OUTER_JOIN(join);
        return this;
    }

    public Builder RIGHT_OUTER_JOIN(String... joins) {
        this.sql.RIGHT_OUTER_JOIN(joins);
        return this;
    }

    public Builder OUTER_JOIN(String join) {
        this.sql.OUTER_JOIN(join);
        return this;
    }

    public Builder OUTER_JOIN(String... joins) {
        this.sql.OUTER_JOIN(joins);
        return this;
    }

    public Builder WHERE(String column, String operator, Object value) {
        this.sql.WHERE(String.format("%s %s #{%s}", column, operator, column));
        this.parameters.put(column, value);
        return this;
    }

    public Builder OR() {
        this.sql.OR();
        return this;
    }

    public Builder AND() {
        this.sql.AND();
        return this;
    }

    public Builder GROUP_BY(String columns) {
        this.sql.GROUP_BY(columns);
        return this;
    }

    public Builder GROUP_BY(String... columns) {
        this.sql.GROUP_BY(columns);
        return this;
    }

    public Builder HAVING(String conditions) {
        this.sql.HAVING(conditions);
        return this;
    }

    public Builder HAVING(String... conditions) {
        this.sql.HAVING(conditions);
        return this;
    }

    public Builder ORDER_BY(String columns) {
        this.sql.ORDER_BY(columns);
        return this;
    }

    public Builder ORDER_BY(String... columns) {
        this.sql.ORDER_BY(columns);
        return this;
    }

    public Builder LIMIT(String variable) {
        this.sql.LIMIT(variable);
        return this;
    }

    public Builder LIMIT(int value) {
        this.sql.LIMIT(value);
        return this;
    }

    public Builder OFFSET(String variable) {
        this.sql.OFFSET(variable);
        return this;
    }

    public Builder OFFSET(long value) {
        return this.OFFSET(String.valueOf(value));
    }

    public Builder FETCH_FIRST_ROWS_ONLY(String variable) {
        this.sql.FETCH_FIRST_ROWS_ONLY(variable);
        return this;
    }

    public Builder FETCH_FIRST_ROWS_ONLY(int value) {
        this.sql.FETCH_FIRST_ROWS_ONLY(value);
        return this;
    }

    public Builder OFFSET_ROWS(String variable) {
        this.sql.OFFSET_ROWS(variable);
        return this;
    }

    public Builder OFFSET_ROWS(long value) {
        return this.OFFSET_ROWS(String.valueOf(value));
    }

    public Builder ADD_ROW() {
        this.sql.ADD_ROW();
        return this;
    }

    public Builder BETWEEN(String column, Object begin, Object end) {
        this.WHERE(column, "BETWEEN", String.format("%s AND %s", begin, end));
//        this.sql.WHERE(String.format("%s BETWEEN %s AND %s", column, begin, end));
        return this;
    }

    public Builder LIKE(String column, Object value) {
        this.WHERE(column, "LIKE", value);
        return this;
    }

    public Map<String, Object> map() {
        this.parameters.put("_sql", this.sql.toString());
        Map<String, Object> map = this.parameters;
        this.parameters = new HashMap<>();
        this.sql = new SQL();
        return map;
    }

    public String toString() {
        return this.sql.toString();
    }
}
