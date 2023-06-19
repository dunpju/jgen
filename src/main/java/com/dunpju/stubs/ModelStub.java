package com.dunpju.stubs;

import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.jdbc.DatabaseMetaDataWrapper;
import com.baomidou.mybatisplus.generator.type.TypeRegistry;
import com.dunpju.utils.CamelizeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelStub {

    private String outPackage;
    private final List<String> imports = new ArrayList<>();
    private String tableName;
    private String tableDescription;
    private String className;
    private final String fieldDescriptionStub = "    @Message(\"%FIELD_DESCRIPTION%\")\n";
    private final String tableIdStub = "    @TableId(value = \"%TABLE_PRIMARY_KEY%\", type = IdType.AUTO)\n";
    private final String propertyStub = "    private %PROPERTY_TYPE% %PROPERTY_NAME%;\n";
    private final StringBuffer field = new StringBuffer();
    private final StringBuffer property = new StringBuffer();
    private final StringBuffer to_str = new StringBuffer();
    private ConfigBuilder configBuilder;
    private Map<String, DatabaseMetaDataWrapper.Column> columnsInfo;
    private TypeRegistry typeRegistry;

    public String stub() {
        String tpl = """
                package %PACKAGE%;
                                
                import com.baomidou.mybatisplus.annotation.IdType;
                import com.baomidou.mybatisplus.annotation.TableId;
                import com.baomidou.mybatisplus.annotation.TableName;
                import com.dunpju.annotations.Message;
                import com.dunpju.orm.BaseField;
                import com.dunpju.orm.BaseModel;
                import lombok.Data;
                                
                import java.io.Serial;
                %IMPORTS%
                                
                @TableName("%TABLE_NAME%")
                @Message(value = "%TABLE_DESCRIPTION%")
                @Data
                public class %CLASS_NAME% extends BaseModel {
                    
                    @Serial
                    private static final long serialVersionUID = 1L;
                    
                    public enum FIELD implements BaseField {
                        %FIELD%;
                    }
                                
                    %PROPERTY%
                    @Override
                    public String toString() {
                        return "%CLASS_NAME%{" +
                            %TO_STRING%
                        "}";
                    }
                }""";
        this.processProperty();
        tpl = tpl.replaceAll("%PACKAGE%", this.outPackage);
        tpl = tpl.replaceAll("%FIELD%", this.field.toString());
        tpl = tpl.replaceAll("%PROPERTY%", this.property.toString());
        tpl = tpl.replaceAll("%IMPORTS%", String.join("\n", this.imports));
        tpl = tpl.replaceAll("%TABLE_NAME%", this.tableName);
        tpl = tpl.replaceAll("%TABLE_DESCRIPTION%", this.tableDescription);
        tpl = tpl.replaceAll("%CLASS_NAME%", this.className);
        tpl = tpl.replaceAll("%TO_STRING%", this.to_str.toString());
        return tpl;
    }

    private void processProperty() {
        int i = 0;
        for (String key : columnsInfo.keySet()) {
            TableInfo tableInfo = new TableInfo(configBuilder, tableName);
            TableField.MetaInfo metaInfo = new TableField.MetaInfo(columnsInfo.get(key), tableInfo);
            IColumnType iColumnType = typeRegistry.getColumnType(metaInfo);
            if (i == 0) {
                this.property.append(this.fieldDescriptionStub.replaceAll("%FIELD_DESCRIPTION%", columnsInfo.get(key).getRemarks()).replaceAll(" ", ""));
            } else {
                this.property.append(this.fieldDescriptionStub.replaceAll("%FIELD_DESCRIPTION%", columnsInfo.get(key).getRemarks()));
            }
            if (columnsInfo.get(key).isPrimaryKey()) {
                this.property.append(this.tableIdStub.replaceAll("%TABLE_PRIMARY_KEY%", columnsInfo.get(key).getName()));
            }
            String camelCasePropertyName = CamelizeUtil.toCamelCase(columnsInfo.get(key).getName());
            String propertyStub = this.propertyStub;
            propertyStub = propertyStub.replaceAll("%PROPERTY_TYPE%", iColumnType.getType());
            propertyStub = propertyStub.replaceAll("%PROPERTY_NAME%", camelCasePropertyName);
            this.property.append(propertyStub);
            if (iColumnType.getPkg() != null) {
                this.imports.add("import " + iColumnType.getPkg() + ";");
            }

            if (i == 0) {
                this.to_str.append(String.format("\"%s = \" + %s +\n", camelCasePropertyName, camelCasePropertyName));
                this.field.append(key);
            } else {
                if (columnsInfo.size() - 1 > i) {
                    this.to_str.append(String.format("            \", %s = \" + %s +\n", camelCasePropertyName, camelCasePropertyName));
                } else {
                    this.to_str.append(String.format("            \", %s = \" + %s +", camelCasePropertyName, camelCasePropertyName));
                }
                this.field.append("        ").append(key);
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

    public void setConfigBuilder(ConfigBuilder configBuilder) {
        this.configBuilder = configBuilder;
    }

    public void setColumnsInfo(Map<String, DatabaseMetaDataWrapper.Column> columnsInfo) {
        this.columnsInfo = columnsInfo;
    }

    public void setTypeRegistry(TypeRegistry typeRegistry) {
        this.typeRegistry = typeRegistry;
    }
}
