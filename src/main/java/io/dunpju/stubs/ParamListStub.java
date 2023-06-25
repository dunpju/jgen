package io.dunpju.stubs;

public class ParamListStub {
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
                    private String keyword;
                    private long page;
                    private long pageSize;
                }
                """;
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
