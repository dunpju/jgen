package io.dunpju.stubs;

import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.jdbc.DatabaseMetaDataWrapper;
import com.baomidou.mybatisplus.generator.type.TypeRegistry;
import io.dunpju.gen.ITypeConvert;
import io.dunpju.gen.TypeConvert;
import io.dunpju.utils.CamelizeUtil;

import java.util.*;

public class ModelStub {

    private String outPackage;
    private final Set<String> imports = new HashSet<>();
    private String tableName;
    private String tableDescription;
    private String className;
    private String fieldDescriptionStub = "    @%MESSAGE%(\"%FIELD_DESCRIPTION%\")\n";
    private String tableFieldStub = "    @TableField(\"`%TABLE_FIELD%`\")\n";
    private final String tableIdStub = "    @TableId(value = \"`%TABLE_PRIMARY_KEY%`\", type = IdType.AUTO)\n";
    private final String propertyStub = "    private %PROPERTY_TYPE% %PROPERTY_NAME%;\n";
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

    public String stub() {
        String tpl = """
                package %PACKAGE%;
                                
                import com.baomidou.mybatisplus.annotation.IdType;
                import com.baomidou.mybatisplus.annotation.TableField;
                import com.baomidou.mybatisplus.annotation.TableId;
                import com.baomidou.mybatisplus.annotation.TableName;
                import io.dunpju.orm.BaseField;
                import io.dunpju.orm.BaseModel;
                import io.dunpju.orm.Join;
                import lombok.Data;
                import lombok.EqualsAndHashCode;
                                
                import java.io.Serial;
                %IMPORTS%
                                
                @TableName("%TABLE_NAME%")
                @%MESSAGE%(value = "%TABLE_DESCRIPTION%")
                @Data
                @EqualsAndHashCode(callSuper=false)
                public class %CLASS_NAME% extends BaseModel {
                    
                    public enum FIELD implements BaseField {
                        %FIELD%;
                    }
                    
                    @TableField(select = false)
                    private final static String _tableName = "%TABLE_NAME%";
                    
                    @Serial
                    @TableField(select = false)
                    private static final long serialVersionUID = 1L;
                    
                    public String tableName() {
                        return %CLASS_NAME%._tableName;
                    }
                    
                    public static Join AS(String alias) {
                        Join join = new Join();
                        join.setTable(%CLASS_NAME%._tableName);
                        join.AS(alias);
                        return join;
                    }
                    
                    public static String ON(String first, String operator, String second, String... other) {
                        Join join = new Join();
                        join.setTable(%CLASS_NAME%._tableName);
                        return join.ON(first, operator, second, other);
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
        if (!Objects.equals(this.className, "Message")) {
            this.imports.add("import io.dunpju.annotations.Message;");
            tpl = tpl.replaceAll("%MESSAGE%", "Message");
        } else {
            tpl = tpl.replaceAll("%MESSAGE%", "io.dunpju.annotations.Message");
        }
        tpl = tpl.replaceAll("%PROPERTY%", this.property.toString());
        tpl = tpl.replaceAll("%IMPORTS%", String.join("\n", this.imports));
        tpl = tpl.replaceAll("%TABLE_NAME%", this.tableName);
        tpl = tpl.replaceAll("%TABLE_DESCRIPTION%", this.tableDescription.replaceAll("\r|\n", ""));
        tpl = tpl.replaceAll("%CLASS_NAME%", this.className);
        tpl = tpl.replaceAll("%TO_STRING%", this.to_str.toString());
        return tpl;
    }

    private void processProperty() {
        int i = 0;
        for (String key : columnsInfo.keySet()) {
            String columnName = columnsInfo.get(key).getName();
            String camelCasePropertyName = CamelizeUtil.toCamelCase(columnName);

            if (! this.isForceUpdate) {
                if (! this.alreadyField.isEmpty()) {
                    if (! this.alreadyField.contains(camelCasePropertyName)) {
                        if (! this.updatedField.contains(columnName)) {
                            continue;
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
