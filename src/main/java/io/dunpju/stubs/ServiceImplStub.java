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
                                
                import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
                import org.springframework.stereotype.Service;
                %IMPORTS%
                                
                @Service
                public class %CLASS_NAME% extends ServiceImpl<%MAPPER_NAME%, %MODEL_NAME%> implements %SERVICE_NAME% {
                    
                }
                """;
        tpl = tpl.replaceAll("%PACKAGE%", this.outPackage);
        tpl = tpl.replaceAll("%IMPORTS%", String.join("\n", this.imports));
        tpl = tpl.replaceAll("%CLASS_NAME%", this.className);
        tpl = tpl.replaceAll("%MAPPER_NAME%", this.mapperName);
        tpl = tpl.replaceAll("%MODEL_NAME%", this.modelName);
        tpl = tpl.replaceAll("%SERVICE_NAME%", this.serviceName);
        //tpl = tpl.replaceAll("%VO_NAME%", this.voName);
        //tpl = tpl.replaceAll("%UPPER_FIRST_DAO_NAME%", this.upperFirstDaoName);
        //tpl = tpl.replaceAll("%DAO_NAME%", this.daoName);
        //tpl = tpl.replaceAll("%UPPER_FIRST_ENTITY_NAME%", this.upperFirstEntityName);
        //tpl = tpl.replaceAll("%ENTITY_NAME%", this.entityName);
        //tpl = tpl.replaceAll("%ENTITY_PRIMARY_KEY_TYPE%", this.entityPrimaryKeyType != null ? this.entityPrimaryKeyType : "Long");
        //tpl = tpl.replaceAll("%ENTITY_PRIMARY_KEY%", this.entityPrimaryKey != null ? this.entityPrimaryKey : "id");
        //tpl = tpl.replaceAll("%UPPER_FIRST_ENTITY_PRIMARY_KEY%", this.upperFirstEntityPrimaryKey != null ? this.upperFirstEntityPrimaryKey : "Id");
        return tpl;
    }
}
