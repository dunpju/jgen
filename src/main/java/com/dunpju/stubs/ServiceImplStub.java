package com.dunpju.stubs;

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
        return tpl;
    }
}
