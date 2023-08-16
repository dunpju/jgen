package io.dunpju.stubs;

import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.jdbc.DatabaseMetaDataWrapper;
import com.baomidou.mybatisplus.generator.type.TypeRegistry;
import io.dunpju.utils.CamelizeUtil;
import io.dunpju.utils.StrUtil;

import java.util.*;

public class EntityStub {
    private String outPackage;
    private final Set<String> imports = new HashSet<>();
    private String classDesc;
    private String className;
    private String tableName;
    private final String messageStub = "    @Message(\"%FIELD_DESCRIPTION%\")\n";
    private final String propertyStub = "    private %PROPERTY_TYPE% %PROPERTY_NAME%;\n";
    private final StringBuffer property = new StringBuffer();
    private ConfigBuilder configBuilder;
    private Map<String, DatabaseMetaDataWrapper.Column> columnsInfo;
    private TypeRegistry typeRegistry;
    private String entityPrimaryKey;
    private String entityPrimaryKeyType;
    private Map<String, String> propertyTypeConvertMap;
    private String createTimeInitTemp = "this.set%CREATE_TIME%(%CREATE_TIME_INIT%);";
    private String updateTimeInitTemp = "this.set%UPDATE_TIME%(%UPDATE_TIME_INIT%);";
    private String createTimeInit = "LocalDateTime.now()";
    private String createTime = "create_time";
    private String updateTime = "update_time";
    private String deleteTime = "delete_time";

    public String stub() {
        String tpl = """
                package %PACKAGE%;
                                
                import io.dunpju.annotations.Message;
                import io.dunpju.entity.BaseEntity;
                import io.dunpju.entity.IFlag;
                import lombok.Data;
                import lombok.EqualsAndHashCode;
                                
                %IMPORTS%
                                
                @Data
                @Message(value = "%CLASS_DESC%")
                @EqualsAndHashCode(callSuper=false)
                public class %CLASS_NAME% extends BaseEntity {
                                
                    public enum FLAG implements IFlag {
                        Delete,
                        Update;
                    }
                    
                    public %CLASS_NAME%() {
                        %CREATE_TIME_INIT_TEMP%
                        %UPDATE_TIME_INIT_TEMP%
                    }
                                
                    %PROPERTY%
                }
                """;
        this.processProperty();
        tpl = tpl.replaceAll("%PACKAGE%", this.outPackage);
        tpl = tpl.replaceAll("%IMPORTS%", String.join("\n", this.imports));
        tpl = tpl.replaceAll("%CLASS_DESC%", this.classDesc.replaceAll("\r|\n", ""));
        tpl = tpl.replaceAll("%CLASS_NAME%", this.className);
        tpl = tpl.replaceAll("%CREATE_TIME_INIT_TEMP%", this.createTimeInitTemp);
        tpl = tpl.replaceAll("%UPDATE_TIME_INIT_TEMP%", this.updateTimeInitTemp);
        tpl = tpl.replaceAll("%PROPERTY%", this.property.toString());
        return tpl;
    }

    private void processProperty() {
        int i = 0;
        String camelCaseCreateTime = "";
        String camelCaseUpdateTime = "";
        for (String key : columnsInfo.keySet()) {
            TableInfo tableInfo = new TableInfo(configBuilder, tableName);
            TableField.MetaInfo metaInfo = new TableField.MetaInfo(columnsInfo.get(key), tableInfo);
            IColumnType iColumnType = typeRegistry.getColumnType(metaInfo);
            if (i == 0) {
                this.property.append(this.messageStub.replaceAll("%FIELD_DESCRIPTION%", columnsInfo.get(key).getRemarks().replaceAll("\r|\n", "")).replaceAll(" ", ""));
            } else {
                this.property.append(this.messageStub.replaceAll("%FIELD_DESCRIPTION%", columnsInfo.get(key).getRemarks().replaceAll("\r|\n", "")));
            }
            String columnName = columnsInfo.get(key).getName();
            String camelCasePropertyName = CamelizeUtil.toCamelCase(columnName);
            if (columnsInfo.get(key).isPrimaryKey()) {
                this.entityPrimaryKey = camelCasePropertyName;
                this.entityPrimaryKeyType = iColumnType.getType();
            }
            String propertyStub = this.propertyStub;
            String getType = iColumnType.getType();
            if (this.propertyTypeConvertMap.size() > 0) {
                if (this.propertyTypeConvertMap.containsKey(getType)) {
                    getType = this.propertyTypeConvertMap.get(getType);
                }
            }
            propertyStub = propertyStub.replaceAll("%PROPERTY_TYPE%", getType);
            propertyStub = propertyStub.replaceAll("%PROPERTY_NAME%", camelCasePropertyName);
            this.property.append(propertyStub);
            if (iColumnType.getPkg() != null && !iColumnType.getPkg().equals("")) {
                this.imports.add("import " + iColumnType.getPkg() + ";");
            }
            if (columnName.equals(this.createTime)) {
                camelCaseCreateTime = StrUtil.upperFirst(camelCasePropertyName);
                this.createTimeInitTemp = this.createTimeInitTemp.replaceAll("%CREATE_TIME%", camelCaseCreateTime);
                this.createTimeInitTemp = this.createTimeInitTemp.replaceAll("%CREATE_TIME_INIT%", this.createTimeInit);
            } else if (columnName.equals(this.updateTime)) {
                if (!camelCaseCreateTime.equals("")) {
                    camelCaseUpdateTime = StrUtil.upperFirst(camelCasePropertyName);
                    this.updateTimeInitTemp = this.updateTimeInitTemp.replaceAll("%UPDATE_TIME%", camelCaseUpdateTime);
                    this.updateTimeInitTemp = this.updateTimeInitTemp.replaceAll("%UPDATE_TIME_INIT%", String.format("this.get%s()", camelCaseCreateTime));
                }
            }
            i++;
        }
        if (camelCaseCreateTime.equals("")) {
            this.createTimeInitTemp = "";
        }
        if (camelCaseUpdateTime.equals("")) {
            this.updateTimeInitTemp = "";
        }
    }

    public void setOutPackage(String outPackage) {
        this.outPackage = outPackage;
    }

    public void setClassDesc(String classDesc) {
        this.classDesc = classDesc;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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

    public void setPropertyTypeConvertMap(Map<String, String> propertyTypeConvertMap) {
        this.propertyTypeConvertMap = propertyTypeConvertMap;
    }

    public String getEntityPrimaryKey() {
        return entityPrimaryKey;
    }

    public String getEntityPrimaryKeyType() {
        return entityPrimaryKeyType;
    }

    public void setCreateTimeInit(String createTimeInit) {
        this.createTimeInit = createTimeInit;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setDeleteTime(String deleteTime) {
        this.deleteTime = deleteTime;
    }
}
