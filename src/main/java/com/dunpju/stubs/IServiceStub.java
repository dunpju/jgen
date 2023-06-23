package com.dunpju.stubs;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class IServiceStub {

    private String outPackage;
    private List<String> imports = new ArrayList<>();
    private String className;
    private String modelName;

    public String stub() {
        String tpl = """
                package %PACKAGE%;
                                
                import com.baomidou.mybatisplus.extension.service.IService;
                %IMPORTS%
                                
                public interface %CLASS_NAME% extends IService<%MODEL_NAME%> {
                                
                }""";
        tpl = tpl.replaceAll("%PACKAGE%", this.outPackage);
        tpl = tpl.replaceAll("%IMPORTS%", String.join("\n", this.imports));
        tpl = tpl.replaceAll("%CLASS_NAME%", this.className);
        tpl = tpl.replaceAll("%MODEL_NAME%", this.modelName);
        return tpl;
    }
}
