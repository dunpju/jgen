package io.dunpju.stubs;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
public class MapperStub {
    private String outPackage;
    private List<String> imports = new ArrayList<>();
    private String className;
    private String modelName;

    public String stub() {
        String tpl = """
                  package %PACKAGE%;
                  
                  import io.dunpju.orm.IMapper;
                  import org.apache.ibatis.annotations.Mapper;
                  %IMPORTS%
                  
                  @Mapper
                  public interface %CLASS_NAME% extends IMapper<%MODEL_NAME%> {
                  }""";
        tpl = tpl.replaceAll("%PACKAGE%", this.outPackage);
        tpl = tpl.replaceAll("%IMPORTS%", String.join("\n", this.imports));
        tpl = tpl.replaceAll("%CLASS_NAME%", this.className);
        tpl = tpl.replaceAll("%MODEL_NAME%", this.modelName);
        return tpl;
    }
}
