package io.dunpju.sqlMethod;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InsertOrUnionKeyUpdateMethod extends AbstractMethod {

    /**
     * @since 3.5.0
     */
    protected InsertOrUnionKeyUpdateMethod() {
        super("insertOrUnionKeyUpdate");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        // SQL 模板
        String sql = "<script>\nINSERT INTO %s (%s)\nVALUES\n" +
                "<foreach collection=\"list\" index=\"index\" item=\"item\" separator=\",\">\n %s \n</foreach>\n" +
                "ON DUPLICATE KEY UPDATE\n%s\n</script>";

        // 主键属性名（驼峰）
        String keyProperty = tableInfo.getKeyProperty();

        // 所有字段属性名（驼峰）：主键 + 其他字段
        List<String> allFieldNames = new ArrayList<>();
        allFieldNames.add(keyProperty);
        tableInfo.getFieldList().stream()
                .map(TableFieldInfo::getProperty)
                .forEach(allFieldNames::add);

        // 数据库字段名（蛇形）：用于 INSERT INTO 表 (列名)
        List<String> dbColumns = new ArrayList<>();
        dbColumns.add(tableInfo.getKeyColumn());  // 主键列名
        tableInfo.getFieldList().stream()
                .map(TableFieldInfo::getColumn)
                .forEach(dbColumns::add);

        // 生成 SQL 片段
        String fieldNames = String.join(", ", dbColumns);  // 数据库列名
        String insertFields = allFieldNames.stream()
                .map(field -> "#{item." + field + "}")  // 属性名占位符
                .collect(Collectors.joining(", "));
        insertFields = "(" + insertFields + ")";

        // 生成 UPDATE 部分
        String updateFields = tableInfo.getFieldList().stream()
                .map(field -> field.getColumn() + " = VALUES(" + field.getColumn() + ")")
                .collect(Collectors.joining(",\n"));

        // 拼接完整 SQL
        String sqlResult = String.format(sql,
                tableInfo.getTableName(),
                fieldNames,
                insertFields,
                updateFields);

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sqlResult, modelClass);
        return this.addUpdateMappedStatement(mapperClass, modelClass, "insertOrUnionKeyUpdate", sqlSource);
    }
}
