package io.dunpju.stubs;

public class ParamAddStub {
    private String outPackage;
    private String className;

    public String stub() {
        String tpl = """
                package %PACKAGE%;
                                
                import lombok.Data;
                import lombok.EqualsAndHashCode;
                                
                @Data
                @EqualsAndHashCode(callSuper=false)
                public class %CLASS_NAME% {
                }""";
        tpl = tpl.replaceAll("%PACKAGE%", this.outPackage);
        tpl = tpl.replaceAll("%CLASS_NAME%", this.className);
        return tpl;
    }

    public void setOutPackage(String outPackage) {
        this.outPackage = outPackage;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
