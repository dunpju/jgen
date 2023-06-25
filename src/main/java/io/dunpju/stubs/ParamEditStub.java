package io.dunpju.stubs;

public class ParamEditStub {
    private String outPackage;
    private String className;
    private String extendsClassName;

    public String stub() {
        String tpl = """
                package %PACKAGE%;
                                
                import lombok.Data;
                import lombok.EqualsAndHashCode;
                                
                @Data
                @EqualsAndHashCode(callSuper=false)
                public class %CLASS_NAME% extends %EXTENDS_CLASS_NAME% {
                }""";
        tpl = tpl.replaceAll("%PACKAGE%", this.outPackage);
        tpl = tpl.replaceAll("%CLASS_NAME%", this.className);
        tpl = tpl.replaceAll("%EXTENDS_CLASS_NAME%", this.extendsClassName);
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

    public void setExtendsClassName(String extendsClassName) {
        this.extendsClassName = extendsClassName;
    }
}
