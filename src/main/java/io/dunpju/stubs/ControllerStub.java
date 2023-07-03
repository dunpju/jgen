package io.dunpju.stubs;

import lombok.Data;

@Data
public class ControllerStub {
    String outPackage;
    String importResponseClass = "io.dunpju.controller.Response";
    String requestMappingValue;
    String className;

    public String stub() {
        String tpl = """
                package %PACKAGE%;
                                
                import %IMPORT_RESPONSE_CLASS%;
                import org.springframework.web.bind.annotation.*;
                                
                @RestController
                @RequestMapping("%REQUEST_MAPPING_VALUE%")
                public class %CLASS_NAME% {
                    //@GetMapping("list")
                    public Response<?> list() {
                        return Response.success();
                    }
                                
                    //@PostMapping("add")
                    public Response<?> add() {
                        return Response.success();
                    }
                                
                    //@PutMapping("edit")
                    public Response<?> edit() {
                        return Response.success();
                    }
                                
                    //@GetMapping("details")
                    public Response<?> details() {
                        return Response.success();
                    }
                                
                    //@DeleteMapping("delete")
                    public Response<?> delete() {
                        return Response.success();
                    }
                }
                """;
        tpl = tpl.replaceAll("%PACKAGE%", this.outPackage);
        tpl = tpl.replaceAll("%IMPORT_RESPONSE_CLASS%", this.importResponseClass);
        tpl = tpl.replaceAll("%REQUEST_MAPPING_VALUE%", this.requestMappingValue);
        tpl = tpl.replaceAll("%CLASS_NAME%", this.className);
        return tpl;
    }
}
