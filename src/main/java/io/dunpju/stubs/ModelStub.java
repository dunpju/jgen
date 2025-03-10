package io.dunpju.stubs;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.jdbc.DatabaseMetaDataWrapper;
import com.baomidou.mybatisplus.generator.type.TypeRegistry;
import io.dunpju.gen.ITypeConvert;
import io.dunpju.gen.TypeConvert;
import io.dunpju.utils.CamelizeUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ModelStub {

    private String outPackage;
    private final Set<String> imports = new HashSet<>();
    private String tableName;
    private String tableDescription;
    private String className;
    private String fieldDescriptionStub =  "";
    private String tableFieldStub =  "";
    private String tableIdStub =  "";
    private String propertyStub =  "";
    private final StringBuffer field = new StringBuffer();
    private final StringBuffer property = new StringBuffer();
    private final StringBuffer to_str = new StringBuffer();
    private ConfigBuilder configBuilder;
    private Map<String, DatabaseMetaDataWrapper.Column> columnsInfo;
    private TypeRegistry typeRegistry;
    private Map<String, TypeConvert> propertyTypeConvertMap;
    private Map<String, ITypeConvert> propertyITypeConvertMap;
    private List<String> updatedField;
    private List<String> alreadyField;

    private boolean isForceUpdate = false;

    public ModelStub() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("stubs/model_field_description.stub")) {
            if (Objects.nonNull(inputStream)) {
                // 读取文本文件
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    fieldDescriptionStub += line + "\n";
                }
            } else {
                throw new RuntimeException("stubs/model_field_description.stub not found");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("stubs/model_table_field.stub")) {
            if (Objects.nonNull(inputStream)) {
                // 读取文本文件
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    tableFieldStub += line + "\n";
                }
            } else {
                throw new RuntimeException("stubs/model_table_field.stub not found");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("stubs/model_table_id.stub")) {
            if (Objects.nonNull(inputStream)) {
                // 读取文本文件
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    tableIdStub += line + "\n";
                }
            } else {
                throw new RuntimeException("stubs/model_table_id.stub not found");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("stubs/model_property.stub")) {
            if (Objects.nonNull(inputStream)) {
                // 读取文本文件
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    propertyStub += line + "\n";
                }
            } else {
                throw new RuntimeException("stubs/model_property.stub not found");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String stub() {
        StringBuilder tpl = new StringBuilder();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("stubs/model.stub")) {
            if (Objects.nonNull(inputStream)) {
                // 读取文本文件
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    tpl.append(line).append("\n");
                }
            } else {
                throw new RuntimeException("stubs/model.stub not found");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.processProperty();
        tpl = new StringBuilder(tpl.toString().replaceAll("%PACKAGE%", this.outPackage));
        tpl = new StringBuilder(tpl.toString().replaceAll("%FIELD%", this.field.toString()));
        if (!Objects.equals(this.className, "Message")) {
            this.imports.add("import io.dunpju.annotations.Message;");
            tpl = new StringBuilder(tpl.toString().replaceAll("%MESSAGE%", "Message"));
        } else {
            tpl = new StringBuilder(tpl.toString().replaceAll("%MESSAGE%", "io.dunpju.annotations.Message"));
        }
        tpl = new StringBuilder(tpl.toString().replaceAll("%PROPERTY%", this.property.toString()));
        tpl = new StringBuilder(tpl.toString().replaceAll("%IMPORTS%", String.join("\n", this.imports)));
        tpl = new StringBuilder(tpl.toString().replaceAll("%TABLE_NAME%", this.tableName));
        tpl = new StringBuilder(tpl.toString().replaceAll("%TABLE_DESCRIPTION%", this.tableDescription));
        tpl = new StringBuilder(tpl.toString().replaceAll("%CLASS_NAME%", this.className));
        tpl = new StringBuilder(tpl.toString().replaceAll("%TO_STRING%", this.to_str.toString()));
        return tpl.toString();
    }

    private void processProperty() {
        int i = 0;
        for (String key : columnsInfo.keySet()) {
            String columnName = columnsInfo.get(key).getName();
            String camelCasePropertyName = CamelizeUtil.toCamelCase(columnName);

            if (! this.isForceUpdate) {
                if (CollectionUtils.isNotEmpty(this.alreadyField)) {
                    if (! this.alreadyField.contains(camelCasePropertyName)) {
                        if (CollectionUtils.isNotEmpty(this.updatedField)) {
                            if (! this.updatedField.contains(columnName)) {
                                continue;
                            }
                        }
                    }
                }
            }

            TableInfo tableInfo = new TableInfo(configBuilder, tableName);
            TableField.MetaInfo metaInfo = new TableField.MetaInfo(columnsInfo.get(key), tableInfo);
            IColumnType iColumnType = typeRegistry.getColumnType(metaInfo);
            if (!Objects.equals(this.className, "Message")) {
                this.fieldDescriptionStub = this.fieldDescriptionStub.replaceAll("%MESSAGE%", "Message");
            } else {
                this.fieldDescriptionStub = this.fieldDescriptionStub.replaceAll("%MESSAGE%", "io.dunpju.annotations.Message");
            }
            if (i == 0) {
                this.property.append(this.fieldDescriptionStub.replaceAll("%FIELD_DESCRIPTION%", columnsInfo.get(key).getRemarks().replaceAll("\r|\n", "")).replaceAll(" ", ""));
            } else {
                this.property.append(this.fieldDescriptionStub.replaceAll("%FIELD_DESCRIPTION%", columnsInfo.get(key).getRemarks().replaceAll("\r|\n", "")));
            }

            if (columnsInfo.get(key).isPrimaryKey()) {
                this.property.append(this.tableIdStub.replaceAll("%TABLE_PRIMARY_KEY%", columnsInfo.get(key).getName()));
            } else {
                this.property.append(this.tableFieldStub.replaceAll("%TABLE_FIELD%", columnsInfo.get(key).getName()));
            }

            String propertyStub = this.propertyStub;
            String getType = iColumnType.getType();

            if (this.propertyITypeConvertMap != null && this.propertyITypeConvertMap.get(getType) != null) {
                TypeConvert typeConvert = (this.propertyITypeConvertMap.get(getType)).handle(getType, columnName);
                if (typeConvert != null) {
                    getType = typeConvert.getTarget();
                    if (null != typeConvert.getPkg() && !typeConvert.getPkg().equals("")) {
                        this.imports.add("import " + typeConvert.getPkg() + ";");
                    }
                }
            } else if (this.propertyTypeConvertMap != null && this.propertyTypeConvertMap.get(getType) != null) {
                TypeConvert typeConvert = this.propertyTypeConvertMap.get(getType);
                getType = typeConvert.getTarget();
                if (null != typeConvert.getPkg() && !typeConvert.getPkg().equals("")) {
                    this.imports.add("import " + typeConvert.getPkg() + ";");
                }
            }

            propertyStub = propertyStub.replaceAll("%PROPERTY_TYPE%", getType);
            propertyStub = propertyStub.replaceAll("%PROPERTY_NAME%", camelCasePropertyName);
            this.property.append(propertyStub);
            if (iColumnType.getPkg() != null) {
                this.imports.add("import " + iColumnType.getPkg() + ";");
            }

            if (i == 0) {
                this.to_str.append(String.format("\"%s = \" + %s +\n", camelCasePropertyName, camelCasePropertyName));
                this.field.append(columnsInfo.get(key).getName());
            } else {
                if (columnsInfo.size() - 1 > i) {
                    this.to_str.append(String.format("            \", %s = \" + %s +\n", camelCasePropertyName, camelCasePropertyName));
                } else {
                    this.to_str.append(String.format("            \", %s = \" + %s +", camelCasePropertyName, camelCasePropertyName));
                }
                this.field.append("        ").append(columnsInfo.get(key).getName());
            }
            if (i < columnsInfo.size() - 1) {
                this.field.append(",");
                this.field.append("\n");
            }
            i++;
        }
    }

    public void setOutPackage(String outPackage) {
        this.outPackage = outPackage;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setTableDescription(String tableDescription) {
        this.tableDescription = tableDescription;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public String getOutPackage() {
        return outPackage;
    }

    public void setConfigBuilder(ConfigBuilder configBuilder) {
        this.configBuilder = configBuilder;
    }

    public void setColumnsInfo(Map<String, DatabaseMetaDataWrapper.Column> columnsInfo) {
        this.columnsInfo = columnsInfo;
    }

    public void setTypeRegistry(TypeRegistry typeRegistry) {
        this.typeRegistry = typeRegistry;
    }

    public void setPropertyTypeConvertMap(Map<String, TypeConvert> propertyTypeConvertMap) {
        this.propertyTypeConvertMap = propertyTypeConvertMap;
    }

    public void setPropertyITypeConvertMap(Map<String, ITypeConvert> propertyITypeConvertMap) {
        this.propertyITypeConvertMap = propertyITypeConvertMap;
    }

    public void setUpdatedField(List<String> updatedField) {
        this.updatedField = updatedField;
    }

    public void setAlreadyField(List<String> alreadyField) {
        this.alreadyField = alreadyField;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.isForceUpdate = forceUpdate;
    }
}
