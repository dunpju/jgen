package io.dunpju.stubs;

import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.jdbc.DatabaseMetaDataWrapper;
import com.baomidou.mybatisplus.generator.type.TypeRegistry;
import io.dunpju.utils.CamelizeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityStub {
    private String outPackage;
    private final List<String> imports = new ArrayList<>();
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
    public String stub() {
        String tpl = """
                package %PACKAGE%;
                                
                import io.dunpju.annotations.Message;
                import io.dunpju.entity.BaseEntity;
                import io.dunpju.entity.IFlag;
                import lombok.Data;
                                
                %IMPORTS%
                                
                @Data
                @Message(value = "%CLASS_DESC%")
                public class %CLASS_NAME% extends BaseEntity {
                                
                    public enum FLAG implements IFlag {
                        Delete,
                        Update;
                    }
                                
                    %PROPERTY%
                }
                """;
        this.processProperty();
        tpl = tpl.replaceAll("%PACKAGE%", this.outPackage);
        tpl = tpl.replaceAll("%IMPORTS%", String.join("\n", this.imports));
        tpl = tpl.replaceAll("%CLASS_DESC%", this.classDesc);
        tpl = tpl.replaceAll("%CLASS_NAME%", this.className);
        tpl = tpl.replaceAll("%PROPERTY%", this.property.toString());
        return tpl;
    }

    private void processProperty() {
        int i = 0;
        for (String key : columnsInfo.keySet()) {
            TableInfo tableInfo = new TableInfo(configBuilder, tableName);
            TableField.MetaInfo metaInfo = new TableField.MetaInfo(columnsInfo.get(key), tableInfo);
            IColumnType iColumnType = typeRegistry.getColumnType(metaInfo);
            if (i == 0) {
                this.property.append(this.messageStub.replaceAll("%FIELD_DESCRIPTION%", columnsInfo.get(key).getRemarks()).replaceAll(" ", ""));
            } else {
                this.property.append(this.messageStub.replaceAll("%FIELD_DESCRIPTION%", columnsInfo.get(key).getRemarks()));
            }
            String camelCasePropertyName = CamelizeUtil.toCamelCase(columnsInfo.get(key).getName());
            if (columnsInfo.get(key).isPrimaryKey()) {
                this.entityPrimaryKey = camelCasePropertyName;
                this.entityPrimaryKeyType = iColumnType.getType();
            }
            String propertyStub = this.propertyStub;
            propertyStub = propertyStub.replaceAll("%PROPERTY_TYPE%", iColumnType.getType());
            propertyStub = propertyStub.replaceAll("%PROPERTY_NAME%", camelCasePropertyName);
            this.property.append(propertyStub);
            if (iColumnType.getPkg() != null && !iColumnType.getPkg().equals("")) {
                this.imports.add("import " + iColumnType.getPkg() + ";");
            }
            i++;
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

    public String getEntityPrimaryKey() {
        return entityPrimaryKey;
    }

    public String getEntityPrimaryKeyType() {
        return entityPrimaryKeyType;
    }
}
