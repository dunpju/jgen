package io.dunpju.stubs;

import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.jdbc.DatabaseMetaDataWrapper;
import com.baomidou.mybatisplus.generator.type.TypeRegistry;
import io.dunpju.utils.CamelizeUtil;
import io.dunpju.utils.StrUtil;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DaoStub {
    private String outPackage;
    private List<String> imports;
    private String className;
    private String mapperName;
    private String modelName;
    private String entityName;
    private String tablePrimaryKey;
    private String entityPrimaryKey;
    private String entityPrimaryKeyType;
    private String cameEntityPrimaryKey;
    private String voName;
    private final StringBuffer createPropertyMapper = new StringBuffer();
    private String tableName;
    private ConfigBuilder configBuilder;
    private Map<String, DatabaseMetaDataWrapper.Column> columnsInfo;
    private TypeRegistry typeRegistry;

    public String stub() {
        String tpl = """
                package %PACKAGE%;
                                
                import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
                import com.baomidou.mybatisplus.core.toolkit.Wrappers;
                import io.dunpju.entity.IFlag;
                import io.dunpju.orm.BaseDao;
                import io.dunpju.orm.Paged;
                import lombok.EqualsAndHashCode;
                import org.springframework.context.annotation.Scope;
                import org.springframework.stereotype.Repository;
                import java.util.List;
                %IMPORTS%
                                
                @Repository
                @Scope("prototype")
                @EqualsAndHashCode(callSuper=false)
                public class %CLASS_NAME% extends BaseDao<%MAPPER_NAME%, %MODEL_NAME%> {
                    
                    public %CLASS_NAME%() {
                        this.model = new %MODEL_NAME%();
                    }
                    
                    // @see https://baomidou.com/pages/f84a74/#%E6%8F%92%E5%85%A5%E6%88%96%E6%9B%B4%E6%96%B0%E7%9A%84%E5%AD%97%E6%AE%B5%E6%9C%89-%E7%A9%BA%E5%AD%97%E7%AC%A6%E4%B8%B2-%E6%88%96%E8%80%85-null
                    private LambdaUpdateWrapper<%MODEL_NAME%> Wrappers() {
                        if (this.model.get%ENTITY_PRIMARY_KEY%() == null) {
                            throw new RuntimeException("%ENTITY_PRIMARY_KEY%不能为null");
                        }
                        this.wrapper = Wrappers.<%MODEL_NAME%>lambdaUpdate().
                                eq(%MODEL_NAME%::get%ENTITY_PRIMARY_KEY%, this.model.get%ENTITY_PRIMARY_KEY%());
                        return this.wrapper;
                    }
                                
                    public void setData(%ENTITY_NAME% entity) {
                        IFlag flag = entity.Flag();
                        if (null != flag) { // 编辑
                            model = this.model().SELECT("*").WHERE(%MODEL_NAME%.FIELD.%TABLE_PRIMARY_KEY%, "=", entity.get%ENTITY_PRIMARY_KEY%()).first(%MODEL_NAME%.class);
                            if (null == model || model.get%ENTITY_PRIMARY_KEY%() == null) {
                                throw new RuntimeException("%MODEL_NAME%数据不存在");
                            }
                            if (%ENTITY_NAME%.FLAG.Delete == flag) {
                                // TODO::映射入库字段
                            } else if (%ENTITY_NAME%.FLAG.Update == flag) {
                                // TODO::映射入库字段
                            }
                        } else { // 新增
                            //TODO::修改入库字段
                            %CREATE_PROPERTY_MAPPER%
                        }
                    }
                                
                    public %ENTITY_PRIMARY_KEY_TYPE% Add() {
                        this.baseMapper.insert(this.model);
                        return this.model.get%ENTITY_PRIMARY_KEY%();
                    }
                                
                    public int Update() {
                        if (null != this.wrapper){
                            return this.baseMapper.update(null, this.wrapper);
                        } else {
                            return this.baseMapper.updateById(this.model);
                        }
                    }
                                
                    public %MODEL_NAME% getBy%ENTITY_PRIMARY_KEY%(%ENTITY_PRIMARY_KEY_TYPE% %CAME_ENTITY_PRIMARY_KEY%) {
                        return this.model().SELECT("*").WHERE(%MODEL_NAME%.FIELD.%TABLE_PRIMARY_KEY%, "=", %CAME_ENTITY_PRIMARY_KEY%).first(%MODEL_NAME%.class);
                    }
                                
                    public List<%MODEL_NAME%> getBy%ENTITY_PRIMARY_KEY%s(List<Object> %CAME_ENTITY_PRIMARY_KEY%s) {
                        return this.model().SELECT("*").WHERE_IN(%MODEL_NAME%.FIELD.%TABLE_PRIMARY_KEY%, %CAME_ENTITY_PRIMARY_KEY%s).get(%MODEL_NAME%.class);
                    }
                                
                    public int deleteBy%ENTITY_PRIMARY_KEY%(%ENTITY_PRIMARY_KEY_TYPE% %ENTITY_PRIMARY_KEY%) {
                        return this.baseMapper.deleteById(%ENTITY_PRIMARY_KEY%);
                    }
                                
                    public Paged<%MODEL_NAME%> getList(long page, long pageSize) {
                        return this.model().paginate(page, pageSize, %MODEL_NAME%.class);
                    }
                }
                """;
        this.processProperty();
        tpl = tpl.replaceAll("%PACKAGE%", this.outPackage);
        tpl = tpl.replaceAll("%IMPORTS%", String.join("\n", this.imports));
        tpl = tpl.replaceAll("%CLASS_NAME%", this.className);
        tpl = tpl.replaceAll("%MAPPER_NAME%", this.mapperName);
        tpl = tpl.replaceAll("%MODEL_NAME%", this.modelName);
        tpl = tpl.replaceAll("%ENTITY_NAME%", this.entityName);
        tpl = tpl.replaceAll("%TABLE_PRIMARY_KEY%", this.tablePrimaryKey != null ? this.tablePrimaryKey : "id");
        tpl = tpl.replaceAll("%ENTITY_PRIMARY_KEY%", this.entityPrimaryKey != null ? this.entityPrimaryKey : "id");
        tpl = tpl.replaceAll("%CREATE_PROPERTY_MAPPER%", this.createPropertyMapper.toString());
        tpl = tpl.replaceAll("%ENTITY_PRIMARY_KEY_TYPE%", this.entityPrimaryKeyType != null ? this.entityPrimaryKeyType : "Long");
        tpl = tpl.replaceAll("%CAME_ENTITY_PRIMARY_KEY%", this.cameEntityPrimaryKey != null ? this.cameEntityPrimaryKey : "Id");
        //tpl = tpl.replaceAll("%VO_NAME%", this.voName);
        return tpl;
    }

    private void processProperty() {
        int i = 0;
        for (String key : columnsInfo.keySet()) {
            TableInfo tableInfo = new TableInfo(configBuilder, tableName);
            TableField.MetaInfo metaInfo = new TableField.MetaInfo(columnsInfo.get(key), tableInfo);
            IColumnType iColumnType = typeRegistry.getColumnType(metaInfo);
            if (columnsInfo.get(key).isPrimaryKey()) {
                this.tablePrimaryKey = columnsInfo.get(key).getName();
                this.entityPrimaryKey = StrUtil.upperFirst(CamelizeUtil.toCamelCase(columnsInfo.get(key).getName()));
                this.cameEntityPrimaryKey = CamelizeUtil.toCamelCase(columnsInfo.get(key).getName());
                this.entityPrimaryKeyType = iColumnType.getType();
            }
            String camelCasePropertyName = CamelizeUtil.toCamelCase(columnsInfo.get(key).getName());
            String setStub = "this.model.set%FIELD_NAME%(entity.get%FIELD_NAME%());\n";
            setStub = setStub.replaceAll("%FIELD_NAME%", StrUtil.upperFirst(camelCasePropertyName));
            if (i > 0) {
                setStub = "            " + setStub;
            }
            this.createPropertyMapper.append(setStub);
            i++;
        }
    }
}
