package io.dunpju.stubs;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ServiceImplStub {
    private String outPackage;
    private List<String> imports = new ArrayList<>();
    private String className;
    private String mapperName;
    private String modelName;
    private String serviceName;
    private String voName;
    private String upperFirstDaoName;
    private String daoName;
    private String upperFirstEntityName;
    private String entityName;
    private String entityPrimaryKey;
    private String entityPrimaryKeyType;
    private String upperFirstEntityPrimaryKey;

    public String stub() {
        String tpl = """
                package %PACKAGE%;
                                
                import cn.hutool.extra.spring.SpringUtil;
                import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
                import io.dunpju.orm.Paged;
                import lombok.Data;
                import org.springframework.stereotype.Service;
                import org.springframework.transaction.annotation.Transactional;
                %IMPORTS%
                                
                @Service
                public class %CLASS_NAME% extends ServiceImpl<%MAPPER_NAME%, %MODEL_NAME%> implements %SERVICE_NAME% {
                    
                    @Data
                    public static class ListParam {
                        private Long page;
                        private Long pageSize;
                    }
                    
                    public Paged<%MODEL_NAME%> getList(ListParam params) {
                        %UPPER_FIRST_DAO_NAME% %DAO_NAME% = SpringUtil.getBean(%UPPER_FIRST_DAO_NAME%.class);
                        return %DAO_NAME%.getList(params.getPage(), params.getPageSize());
                    }
                    
                    @Data
                    public static class AddParam {
                    }
                    
                    @Transactional(rollbackFor = Exception.class)
                    public void add(AddParam params) {
                        %UPPER_FIRST_DAO_NAME% %DAO_NAME% = SpringUtil.getBean(%UPPER_FIRST_DAO_NAME%.class);
                        %UPPER_FIRST_ENTITY_NAME% %ENTITY_NAME% = new %UPPER_FIRST_ENTITY_NAME%();
                        // TODO::填充业务
                        %DAO_NAME%.setData(%ENTITY_NAME%);
                        %DAO_NAME%.Add();
                    }
                    
                    @Data
                    public static class EditParam {
                    }
                    
                    @Transactional(rollbackFor = Exception.class)
                    public void edit(EditParam params) {
                        %UPPER_FIRST_DAO_NAME% %DAO_NAME% = SpringUtil.getBean(%UPPER_FIRST_DAO_NAME%.class);
                        %UPPER_FIRST_ENTITY_NAME% %ENTITY_NAME% = new %UPPER_FIRST_ENTITY_NAME%();
                        %UPPER_FIRST_ENTITY_NAME%.FLAG.Update.Apply(%ENTITY_NAME%);
                        // TODO::填充业务
                        %DAO_NAME%.setData(%ENTITY_NAME%);
                        %DAO_NAME%.Update();
                    }
                                
                    public %MODEL_NAME% details(%ENTITY_PRIMARY_KEY_TYPE% %ENTITY_PRIMARY_KEY%) {
                        %UPPER_FIRST_DAO_NAME% %DAO_NAME% = SpringUtil.getBean(%UPPER_FIRST_DAO_NAME%.class);
                        return %DAO_NAME%.getBy%UPPER_FIRST_ENTITY_PRIMARY_KEY%(%ENTITY_PRIMARY_KEY%);
                    }
                                
                    @Transactional(rollbackFor = Exception.class)
                    public void delete(%ENTITY_PRIMARY_KEY_TYPE% %ENTITY_PRIMARY_KEY%) {
                        %UPPER_FIRST_DAO_NAME% %DAO_NAME% = SpringUtil.getBean(%UPPER_FIRST_DAO_NAME%.class);
                        %UPPER_FIRST_ENTITY_NAME% %ENTITY_NAME% = new %UPPER_FIRST_ENTITY_NAME%();
                        %UPPER_FIRST_ENTITY_NAME%.FLAG.Delete.Apply(%ENTITY_NAME%);
                        %ENTITY_NAME%.set%UPPER_FIRST_ENTITY_PRIMARY_KEY%(%ENTITY_PRIMARY_KEY%);
                        %DAO_NAME%.setData(%ENTITY_NAME%);
                        %DAO_NAME%.Update();
                    }
                }
                """;
        tpl = tpl.replaceAll("%PACKAGE%", this.outPackage);
        tpl = tpl.replaceAll("%IMPORTS%", String.join("\n", this.imports));
        tpl = tpl.replaceAll("%CLASS_NAME%", this.className);
        tpl = tpl.replaceAll("%MAPPER_NAME%", this.mapperName);
        tpl = tpl.replaceAll("%MODEL_NAME%", this.modelName);
        tpl = tpl.replaceAll("%SERVICE_NAME%", this.serviceName);
        //tpl = tpl.replaceAll("%VO_NAME%", this.voName);
        tpl = tpl.replaceAll("%UPPER_FIRST_DAO_NAME%", this.upperFirstDaoName);
        tpl = tpl.replaceAll("%DAO_NAME%", this.daoName);
        tpl = tpl.replaceAll("%UPPER_FIRST_ENTITY_NAME%", this.upperFirstEntityName);
        tpl = tpl.replaceAll("%ENTITY_NAME%", this.entityName);
        tpl = tpl.replaceAll("%ENTITY_PRIMARY_KEY_TYPE%", this.entityPrimaryKeyType != null ? this.entityPrimaryKeyType : "Long");
        tpl = tpl.replaceAll("%ENTITY_PRIMARY_KEY%", this.entityPrimaryKey != null ? this.entityPrimaryKey : "id");
        tpl = tpl.replaceAll("%UPPER_FIRST_ENTITY_PRIMARY_KEY%", this.upperFirstEntityPrimaryKey != null ? this.upperFirstEntityPrimaryKey : "Id");
        return tpl;
    }
}
