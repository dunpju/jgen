package io.dunpju.orm;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.dunpju.annotations.ColumnCamel;
import io.dunpju.annotations.ColumnCustom;
import io.dunpju.annotations.ColumnSnake;
import io.dunpju.utils.CamelizeUtil;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Builder<M extends IMapper<T>, T extends BaseModel> {
    protected SQL sql;
    protected Map<String, Object> parameters;
    protected String table;
    protected String tableAlias;
    protected String _columns = null;
    protected M baseMapper;
    protected T model;

    public Builder(M baseMapper, T t) {
        this.sql = new SQL();
        this.parameters = new HashMap<>();
        this.baseMapper = baseMapper;
        this.model = t;
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

    public Builder<M, T> SELECT(BaseField... columns) {
        List<String> c = new ArrayList<>();
        for (BaseField b : columns) {
            c.add(b.toString());
        }
        this._columns = String.join(",", c);
        this.sql.SELECT(this._columns);
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

    public Builder<M, T> AS(String alias) {
        this.tableAlias = alias;
        this.table = this.table.replaceAll("\\s+AS\\s+\\w+", "");
        this.table = String.format("%s AS %s", this.table, alias);
        return this;
    }

    public String ON(String first, String operator, String second, String... other) {
        return String.format(" %s ON %s %s %s %s", this.table, first, operator, second, String.join(" ", other));
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
        String columnStr = column.toString().replaceAll("\\.", "_");
        String parameterName = String.format("%s_%d", columnStr, this.parameters.size());
        this.sql.WHERE(String.format("%s %s #{%s}", column, operator, parameterName));
        this.parameters.put(parameterName, value);
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

    public Builder<M, T> GROUP_BY(Object columns) {
        this.sql.GROUP_BY(columns.toString());
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
            String parameterName = String.format("%s_%d", column, this.parameters.size());
            String key = String.format("#{%s}", parameterName);
            inStr.add(key);
            if (value.size() - 1 > i) {
                inStr.add(",");
            }
            this.parameters.put(parameterName, v);
            i++;
        }
        inStr.add(")");
        this.sql.WHERE(String.join(" ", inStr));
        return this;
    }

    public Builder<M, T> WHERE_NULL(Object column) {
        String parameterName = String.format("%s_%d", column.toString(), this.parameters.size());
        this.sql.WHERE(String.format("%s %s #{%s}", column, "IS", parameterName));
        this.parameters.put(parameterName, "NULL");
        return this;
    }

    public Builder<M, T> BETWEEN(String column, Object first, Object second) {
        String beginName = String.format("%s_%d", column, this.parameters.size());
        this.parameters.put(beginName, first);
        String endName = String.format("%s_%d", column, this.parameters.size());
        this.parameters.put(endName, second);
        this.sql.WHERE(String.format("%s BETWEEN #{%s} AND #{%s}", column, beginName, endName));
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

    private void columnHandle(Class<?> objectClass) {
        if (this._columns == null || this._columns.equals("")) {
            ColumnSnake columnSnake = objectClass.getAnnotation(ColumnSnake.class);
            ColumnCamel columnCamel = objectClass.getAnnotation(ColumnCamel.class);
            ColumnCustom columnCustom = objectClass.getAnnotation(ColumnCustom.class);

            List<String> fieldName = new ArrayList<>();

            Field[] fields = objectClass.getDeclaredFields();

            if (columnSnake != null || Arrays.toString(objectClass.getInterfaces()).contains(IColumnSnake.class.getName())) {
                for (Field field : fields) {
                    TableField tableField = field.getAnnotation(TableField.class);
                    TableId tableId = field.getAnnotation(TableId.class);
                    if (tableField != null) {
                        if (!Modifier.isStatic(field.getModifiers())) {
                            if (tableField.select()) {
                                fieldName.add(tableField.value());
                            }
                        }
                    } else if (tableId != null) {
                        if (!Modifier.isStatic(field.getModifiers())) {
                            if (!tableId.value().equals("")) {
                                fieldName.add(tableId.value());
                            }
                        }
                    } else {
                        if (!Modifier.isStatic(field.getModifiers())) {
                            fieldName.add(CamelizeUtil.camelToSnake(field.getName()));
                        }
                    }
                }
            } else if (columnCamel != null || Arrays.toString(objectClass.getInterfaces()).contains(IColumnCamel.class.getName())) {
                for (Field field : fields) {
                    TableField tableField = field.getAnnotation(TableField.class);
                    TableId tableId = field.getAnnotation(TableId.class);
                    if (tableField != null) {
                        if (!Modifier.isStatic(field.getModifiers())) {
                            if (tableField.select()) {
                                fieldName.add(tableField.value());
                            }
                        }
                    } else if (tableId != null) {
                        if (!Modifier.isStatic(field.getModifiers())) {
                            if (!tableId.value().equals("")) {
                                fieldName.add(tableId.value());
                            }
                        }
                    } else {
                        if (!Modifier.isStatic(field.getModifiers())) {
                            fieldName.add(CamelizeUtil.toCamelCase(field.getName()));
                        }
                    }
                }
            } else if (columnCustom != null || Arrays.toString(objectClass.getInterfaces()).contains(IColumnCustom.class.getName())) {
                for (Field field : fields) {
                    TableField tableField = field.getAnnotation(TableField.class);
                    TableId tableId = field.getAnnotation(TableId.class);
                    if (tableField != null) {
                        if (!Modifier.isStatic(field.getModifiers())) {
                            if (tableField.select()) {
                                fieldName.add(tableField.value());
                            }
                        }
                    } else if (tableId != null) {
                        if (!Modifier.isStatic(field.getModifiers())) {
                            if (!tableId.value().equals("")) {
                                fieldName.add(tableId.value());
                            }
                        }
                    } else {
                        if (!Modifier.isStatic(field.getModifiers())) {
                            fieldName.add(field.getName());
                        }
                    }
                }
            } else {
                for (Field field : fields) {
                    TableField tableField = field.getAnnotation(TableField.class);
                    TableId tableId = field.getAnnotation(TableId.class);
                    if (tableField != null) {
                        if (!Modifier.isStatic(field.getModifiers())) {
                            if (tableField.select()) {
                                fieldName.add(tableField.value());
                            }
                        }
                    } else if (tableId != null) {
                        if (!Modifier.isStatic(field.getModifiers())) {
                            if (!tableId.value().equals("")) {
                                fieldName.add(tableId.value());
                            }
                        }
                    } else {
                        if (!Modifier.isStatic(field.getModifiers())) {
                            fieldName.add(field.getName());
                        }
                    }
                }
            }
            if (fieldName.size() > 0) {
                this.SELECT(String.join(",", fieldName));
            } else {
                this.SELECT("*");
            }
        }
    }

    /**
     * 获取第一条数据
     */
    public <E> E first(Class<E> objectClass) {
        this.columnHandle(objectClass);

        this.sql.LIMIT(1);

        Map<String, Object> map = this.baseMapper.first(this.map(this.toSql()));
        if (map != null) {
            Map<String, Object> camelCaseKeyMap = new HashMap<>();
            map.forEach((k,v) -> camelCaseKeyMap.put(StrUtil.toCamelCase(k), v));
            return JSONObject.parseObject(JSONObject.toJSONString(camelCaseKeyMap), objectClass);
        }
        return null;
    }

    /**
     * 获取数据集合
     */
    public <E> List<E> get(Class<E> objectClass) {

        this.columnHandle(objectClass);

        List<Map<String, Object>> list = this.baseMapper.get(this.map(this.toSql()));
        if (!list.isEmpty()) {
            List<E> result = new ArrayList<>();
            for (Map<String, Object> map : list) {
                Map<String, Object> camelCaseKeyMap = new HashMap<>();
                map.forEach((k,v) -> camelCaseKeyMap.put(StrUtil.toCamelCase(k), v));
                result.add(JSONObject.parseObject(JSONObject.toJSONString(camelCaseKeyMap), objectClass));
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
        this.sql.SELECT("count(*) _count_");
        Map<String, Object> map = this.baseMapper.query(this.map(this.toSql()));
        if (null != map) {
            return (Long) map.get("_count_");
        }
        return 0L;
    }

    public <E> Paged<E> paginate(long current, long size, Class<E> objectClass) {

        this.columnHandle(objectClass);

        long limit = (current - 1) * size;
        this.sql.LIMIT(String.format("%s,%s", limit, size));

        String _sql_ = this.toSql();
        Map<String, Object> countMap = this.parameters;
        Pattern patten = Pattern.compile("(SELECT\\s+.*\n)");
        Matcher matcher = patten.matcher(_sql_);
        if (matcher.find()) {
            countMap.put("_sql_", matcher.replaceFirst("SELECT count(*) _count_ \n").replaceAll(String.format(" LIMIT %s,%s", limit, size), ""));
        }
        List<Map<String, Object>> countResult = this.baseMapper.count(countMap);
        long total = 0L;
        if (countResult != null && countResult.size() > 0) {
            if (countResult.size() > 1) {
                total = countResult.size();
            } else {
                total = (Long) countResult.get(0).get("_count_");
            }
        }

        List<E> result = new ArrayList<>();
        List<Map<String, Object>> list = this.baseMapper.get(this.map(_sql_));
        if (!list.isEmpty()) {
            for (Map<String, Object> map : list) {
                Map<String, Object> camelCaseKeyMap = new HashMap<>();
                map.forEach((k,v) -> camelCaseKeyMap.put(StrUtil.toCamelCase(k), v));
                result.add(JSONObject.parseObject(JSONObject.toJSONString(camelCaseKeyMap), objectClass));
            }
            return new Paged<>(total, current, size, result);
        }
        return new Paged<>(total, current, size, result);
    }

    public String toString() {
        return this.sql.toString();
    }
}
