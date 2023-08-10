package io.dunpju.stubs;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CodeStub {

    String outPackage;
    String codeClass;
    List<String> imports;
    String className;
    String extendsClassName;
    StringBuffer property = new StringBuffer();
    String constProperty = "    public final static %SHORT_CODE_CLASS% %PROPERTY% = new %SHORT_CODE_CLASS%(%CODE%, \"%MESSAGE%\");\n";
    Map<String, Object> codeMessage = new HashMap<>();
    Long currentCode;

    public String stub() {
        String tpl = "package %PACKAGE%;\n" +
                "\n" +
                "%IMPORTS%\n" +
                "\n";
        if (this.extendsClassName != null) {
            tpl += "public class %CLASS_NAME% extends %EXTENDS_CLASS_NAME% {\n";
        } else {
            tpl += "public class %CLASS_NAME% {\n";
        }

        tpl += this.property.toString() + "\n" +
                "}\n";
        tpl = tpl.replaceAll("%PACKAGE%", this.outPackage);
        tpl = tpl.replaceAll("%IMPORTS%", String.join("\n", this.imports));
        tpl = tpl.replaceAll("%CLASS_NAME%", this.className);
        if (this.extendsClassName != null) {
            tpl = tpl.replaceAll("%EXTENDS_CLASS_NAME%", this.extendsClassName);
        }
        return tpl;
    }

    public void buildProperty() {
        Long currentCode = this.currentCode;
        for (String key : this.codeMessage.keySet()) {
            String property = this.constProperty;
            property = property.replaceAll("%SHORT_CODE_CLASS%", this.codeClass);
            property = property.replaceAll("%PROPERTY%", key.toUpperCase());
            Object message = null;
            if (this.codeMessage.get(key) instanceof LinkedHashMap) {
                if (((LinkedHashMap<?, ?>) this.codeMessage.get(key)).containsKey("code")) {
                    Object code = ((LinkedHashMap<?, ?>) this.codeMessage.get(key)).get("code");
                    if (code instanceof Integer) {
                        currentCode = ((Integer) code).longValue();
                    } else if (code instanceof Long) {
                        currentCode = (Long) code;
                    }
                }
                if (((LinkedHashMap<?, ?>) this.codeMessage.get(key)).containsKey("message")) {
                    message = ((LinkedHashMap<?, ?>) this.codeMessage.get(key)).get("message");
                }
            }
            if (null == message) {
                if (key.contains("_error")) {
                    message = key.replaceAll("_error", "") + "错误";
                } else {
                    message = key + "错误";
                }
            }

            property = property.replaceAll("%CODE%", String.valueOf(currentCode));
            property = property.replaceAll("%MESSAGE%", (String) message);
            this.property.append(property);
            currentCode++;
        }
    }

    public void setCodeMessage(Map<String, Object> codeMessage) {
        this.codeMessage = codeMessage;
    }

    public void setCodeClass(String codeClass) {
        this.codeClass = codeClass;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public void setCurrentCode(Long currentCode) {
        this.currentCode = currentCode;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setExtendsClassName(String extendsClassName) {
        this.extendsClassName = extendsClassName;
    }

    public void setOutPackage(String outPackage) {
        this.outPackage = outPackage;
    }
}
