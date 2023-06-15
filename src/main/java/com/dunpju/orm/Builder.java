package com.dunpju.orm;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dunpju.utils.CamelizeUtil;
import org.apache.ibatis.jdbc.SQL;
import org.apache.tomcat.util.buf.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Builder<M extends BaseModel<T>, T> {
    SQL sql;
    Map<String, Object> parameters;
    String table;
    protected M baseMapper;

    public Builder(M baseMapper) {
        this.sql = new SQL();
        this.parameters = new HashMap<>();
        this.baseMapper = baseMapper;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Builder<M, T> UPDATE(String table) {
        this.table = table;
        this.sql.UPDATE(table);
        return this;
    }

    public Builder<M, T> SET(String sets) {
        this.sql.SET(sets);
        return this;
    }

    public Builder<M, T> SET(String... sets) {
        this.sql.SET(sets);
        return this;
    }

    public Builder<M, T> INSERT_INTO(String tableName) {
        this.table = tableName;
        this.sql.INSERT_INTO(this.table);
        return this;
    }

    public Builder<M, T> VALUES(String columns, String values) {
        this.sql.VALUES(columns, values);
        return this;
    }

    public Builder<M, T> INTO_COLUMNS(String... columns) {
        this.sql.INTO_COLUMNS(columns);
        return this;
    }

    public Builder<M, T> INTO_VALUES(String... values) {
        this.sql.INTO_VALUES(values);
        return this;
    }

    private String _columns = null;

    public Builder<M, T> SELECT(String columns) {
        this._columns = columns;
        this.sql.SELECT(columns);
        return this;
    }

    public Builder<M, T> SELECT(String... columns) {
        this._columns = String.join(",", columns);
        this.sql.SELECT(columns);
        return this;
    }

    public Builder<M, T> SELECT_DISTINCT(String columns) {
        this._columns = columns;
        this.sql.SELECT_DISTINCT(columns);
        return this;
    }

    public Builder<M, T> SELECT_DISTINCT(String... columns) {
        this._columns = String.join(",", columns);
        this.sql.SELECT_DISTINCT(columns);
        return this;
    }

    public Builder<M, T> DELETE_FROM(String table) {
        this.table = table;
        this.sql.DELETE_FROM(this.table);
        return this;
    }

    public Builder<M, T> FROM(Class<?> table) {
        this.table = table.getAnnotation(TableName.class).value();
        return this;
    }

    public Builder<M, T> FROM(String table) {
        this.table = table;
        return this;
    }

    public Builder<M, T> FROM(String... tables) {
        this.table = String.join(",", tables);
        return this;
    }

    public Builder<M, T> As(String alias) {
        this.table = String.format("%s AS %s", this.table, alias);
        return this;
    }

    public Builder<M, T> JOIN(String join) {
        this.sql.JOIN(join);
        return this;
    }

    public Builder<M, T> JOIN(String... joins) {
        this.sql.JOIN(joins);
        return this;
    }

    public Builder<M, T> INNER_JOIN(String join) {
        this.sql.INNER_JOIN(join);
        return this;
    }

    public Builder<M, T> INNER_JOIN(String... joins) {
        this.sql.INNER_JOIN(joins);
        return this;
    }

    public Builder<M, T> LEFT_OUTER_JOIN(String join) {
        this.sql.LEFT_OUTER_JOIN(join);
        return this;
    }

    public Builder<M, T> LEFT_OUTER_JOIN(String... joins) {
        this.sql.LEFT_OUTER_JOIN(joins);
        return this;
    }

    public Builder<M, T> RIGHT_OUTER_JOIN(String join) {
        this.sql.RIGHT_OUTER_JOIN(join);
        return this;
    }

    public Builder<M, T> RIGHT_OUTER_JOIN(String... joins) {
        this.sql.RIGHT_OUTER_JOIN(joins);
        return this;
    }

    public Builder<M, T> OUTER_JOIN(String join) {
        this.sql.OUTER_JOIN(join);
        return this;
    }

    public Builder<M, T> OUTER_JOIN(String... joins) {
        this.sql.OUTER_JOIN(joins);
        return this;
    }

    public Builder<M, T> WHERE(Object column, String operator, Object value) {
        this.sql.WHERE(String.format("%s %s #{%s}", column.toString(), operator, column));
        this.parameters.put(column.toString(), value);
        return this;
    }

    public Builder<M, T> OR() {
        this.sql.OR();
        return this;
    }

    public Builder<M, T> AND() {
        this.sql.AND();
        return this;
    }

    public Builder<M, T> GROUP_BY(String columns) {
        this.sql.GROUP_BY(columns);
        return this;
    }

    public Builder<M, T> GROUP_BY(String... columns) {
        this.sql.GROUP_BY(columns);
        return this;
    }

    public Builder<M, T> HAVING(String conditions) {
        this.sql.HAVING(conditions);
        return this;
    }

    public Builder<M, T> HAVING(String... conditions) {
        this.sql.HAVING(conditions);
        return this;
    }

    public Builder<M, T> ORDER_BY(String columns) {
        this.sql.ORDER_BY(columns);
        return this;
    }

    public Builder<M, T> ORDER_BY(String... columns) {
        this.sql.ORDER_BY(columns);
        return this;
    }

    public Builder<M, T> LIMIT(String variable) {
        this.sql.LIMIT(variable);
        return this;
    }

    public Builder<M, T> LIMIT(int value) {
        this.sql.LIMIT(value);
        return this;
    }

    public Builder<M, T> OFFSET(String variable) {
        this.sql.OFFSET(variable);
        return this;
    }

    public Builder<M, T> OFFSET(long value) {
        return this.OFFSET(String.valueOf(value));
    }

    public Builder<M, T> FETCH_FIRST_ROWS_ONLY(String variable) {
        this.sql.FETCH_FIRST_ROWS_ONLY(variable);
        return this;
    }

    public Builder<M, T> FETCH_FIRST_ROWS_ONLY(int value) {
        this.sql.FETCH_FIRST_ROWS_ONLY(value);
        return this;
    }

    public Builder<M, T> OFFSET_ROWS(String variable) {
        this.sql.OFFSET_ROWS(variable);
        return this;
    }

    public Builder<M, T> OFFSET_ROWS(long value) {
        return this.OFFSET_ROWS(String.valueOf(value));
    }

    public Builder<M, T> ADD_ROW() {
        this.sql.ADD_ROW();
        return this;
    }

    public Builder<M, T> WHERE_IN(Object column, List<Object> value) {
        List<String> inStr = new ArrayList<>();
        inStr.add(column.toString());
        inStr.add("IN");
        inStr.add("(");
        int i = 0;
        for (Object v : value) {
            String key = String.format("#{%s_%s}", column, i);
            inStr.add(key);
            if (value.size() - 1 > i) {
                inStr.add(",");
            }
            this.parameters.put(String.format("%s_%s", column, i), v);
            i++;
        }
        inStr.add(")");
        this.sql.WHERE(String.join(" ", inStr));
        return this;
    }

    public Builder<M, T> BETWEEN(String column, Object begin, Object end) {
        this.sql.WHERE(String.format("%s BETWEEN #{%s} AND #{%s}", column, "_begin_", "_end_"));
        this.parameters.put("_begin_", begin);
        this.parameters.put("_end_", end);
        return this;
    }

    public Builder<M, T> LIKE(String column, Object value) {
        this.WHERE(column, "LIKE", value);
        return this;
    }

    private Map<String, Object> map(String _sql_) {
        this.parameters.put("_sql_", _sql_);
        Map<String, Object> map = this.parameters;
        this.parameters = new HashMap<>();
        this._columns = null;
        this.sql = new SQL();
        return map;
    }

    public String toSql() {
        return this.sql.FROM(this.table).toString();
    }

    /**
     * 获取第一条数据
     */
    public <E> E first(Class<E> objectClass) {
        this.sql.LIMIT(1);
        Map<String, Object> map = this.baseMapper.first(this.map(this.toSql()));
        return JSONObject.parseObject(CamelizeUtil.toCamelCase(JSONObject.toJSONString(map)), objectClass);
    }

    /**
     * 获取数据集合
     */
    public <E> List<E> get(Class<E> objectClass) {
        List<Map<String, Object>> list = this.baseMapper.get(this.map(this.toSql()));
        if (!list.isEmpty()) {
            List<E> result = new ArrayList<>();
            for (Map<String, Object> map : list) {
                result.add(JSONObject.parseObject(CamelizeUtil.toCamelCase(JSONObject.toJSONString(map)), objectClass));
            }
            return result;
        }
        return null;
    }

    public BigDecimal sum(Object column) {
        this.sql.SELECT(String.format("sum(%s) _sum_", column));
        Map<String, Object> map = this.baseMapper.query(this.map(this.toSql()));
        if (null != map) {
            return (BigDecimal) map.get("_sum_");
        }
        return BigDecimal.valueOf(0);
    }

    public Long count() {
        this.sql.SELECT("count(1) _count_");
        Map<String, Object> map = this.baseMapper.query(this.map(this.toSql()));
        if (null != map) {
            return (Long) map.get("_count_");
        }
        return 0L;
    }

    public <E> Paged<E> paginate(long current, long size, Class<E> objectClass) {
        if (null == this._columns) {
            this._columns = "*";
            this.sql.SELECT(this._columns);
        }
        long limit = (current - 1) * size;
        this.sql.LIMIT(String.format("%s,%s", limit, size));

        String _sql_ = this.toSql();
        Map<String, Object> countMap = this.parameters;
        Pattern patten = Pattern.compile("(SELECT\\s+.*\n)");
        Matcher matcher = patten.matcher(_sql_);
        if (matcher.find()) {
            countMap.put("_sql_", matcher.replaceFirst("SELECT count(1) _count_ \n").replaceAll(String.format(" LIMIT %s,%s", limit, size), ""));
        }
        Map<String, Object> countResult = this.baseMapper.query(countMap);
        long total = (Long) countResult.get("_count_");
        List<Map<String, Object>> list = this.baseMapper.get(this.map(_sql_));
        if (!list.isEmpty()) {
            List<E> result = new ArrayList<>();
            for (Map<String, Object> map : list) {
                result.add(JSONObject.parseObject(CamelizeUtil.toCamelCase(JSONObject.toJSONString(map)), objectClass));
            }
            return new Paged<>(total, current, size, result);
        }
        return null;
    }

    public String toString() {
        return this.sql.toString();
    }
}
