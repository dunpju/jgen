package com.dunpju.stubs;

public class VOStub {
    private String outPackage;
    private String className;

    public String stub() {
        String tpl = """
                package %PACKAGE%;
                                
                import lombok.Data;
                                
                @Data
                public class %CLASS_NAME% {
                    
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
}
