package io.dunpju.stubs;

import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.jdbc.DatabaseMetaDataWrapper;
import com.baomidou.mybatisplus.generator.type.TypeRegistry;
import io.dunpju.utils.CamelizeUtil;

import java.util.*;

public class ModelStub {

    private String outPackage;
    private final Set<String> imports = new HashSet<>();
    private String tableName;
    private String tableDescription;
    private String className;
    private String fieldDescriptionStub = "    @%MESSAGE%(\"%FIELD_DESCRIPTION%\")\n";
    private final String tableIdStub = "    @TableId(value = \"%TABLE_PRIMARY_KEY%\", type = IdType.AUTO)\n";
    private final String propertyStub = "    private %PROPERTY_TYPE% %PROPERTY_NAME%;\n";
    private final StringBuffer field = new StringBuffer();
    private final StringBuffer property = new StringBuffer();
    private final StringBuffer to_str = new StringBuffer();
    private ConfigBuilder configBuilder;
    private Map<String, DatabaseMetaDataWrapper.Column> columnsInfo;
    private TypeRegistry typeRegistry;
    private Map<String, String> propertyTypeConvertMap;

    public String stub() {
        String tpl = """
                package %PACKAGE%;
                                
                import com.baomidou.mybatisplus.annotation.IdType;
                import com.baomidou.mybatisplus.annotation.TableId;
                import com.baomidou.mybatisplus.annotation.TableName;
                import io.dunpju.orm.BaseField;
                import io.dunpju.orm.BaseModel;
                import lombok.Data;
                                
                import java.io.Serial;
                %IMPORTS%
                                
                @TableName("%TABLE_NAME%")
                @%MESSAGE%(value = "%TABLE_DESCRIPTION%")
                @Data
                public class %CLASS_NAME% extends BaseModel {
                    
                    public enum FIELD implements BaseField {
                        %FIELD%;
                    }
                    
                    @Serial
                    private static final long serialVersionUID = 1L;
                                
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
        if (!Objects.equals(this.className, "Message")) {
            this.imports.add("import io.dunpju.annotations.Message;");
            tpl = tpl.replaceAll("%MESSAGE%", "Message");
        } else {
            tpl = tpl.replaceAll("%MESSAGE%", "io.dunpju.annotations.Message");
        }
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
            if (!Objects.equals(this.className, "Message")) {
                this.fieldDescriptionStub = this.fieldDescriptionStub.replaceAll("%MESSAGE%", "Message");
            } else {
                this.fieldDescriptionStub = this.fieldDescriptionStub.replaceAll("%MESSAGE%", "io.dunpju.annotations.Message");
            }
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
            String getType = iColumnType.getType();
            if (this.propertyTypeConvertMap.size() > 0) {
                if (this.propertyTypeConvertMap.containsKey(getType)) {
                    getType = this.propertyTypeConvertMap.get(getType);
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

    public void setPropertyTypeConvertMap(Map<String, String> propertyTypeConvertMap) {
        this.propertyTypeConvertMap = propertyTypeConvertMap;
    }
}
