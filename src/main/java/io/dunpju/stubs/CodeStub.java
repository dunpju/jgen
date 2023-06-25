package io.dunpju.stubs;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CodeStub {

    String outPackage;
    String codeClass;
    String className;
    StringBuffer property = new StringBuffer();
    String constProperty = "    public final static %SHORT_CODE_CLASS% %PROPERTY% = new %SHORT_CODE_CLASS%(%CODE%, \"%MESSAGE%\");\n";
    Map<String, Object> codeMessage = new HashMap<>();
    Long currentCode;

    public String stub() {
        String tpl = "package %PACKAGE%;\n" +
                "\n" +
                "import %CODE_CLASS%;\n" +
                "\n" +
                "public class %CLASS_NAME% {\n" +
                this.property.toString() + "\n" +
                "}\n";
        tpl = tpl.replaceAll("%PACKAGE%", this.outPackage);
        tpl = tpl.replaceAll("%CODE_CLASS%", this.codeClass);
        tpl = tpl.replaceAll("%CLASS_NAME%", this.className);
        return tpl;
    }

    public void buildProperty() {
        Long currentCode = this.currentCode;
        String[] shortCodeClassArr = this.codeClass.split("\\.");
        String shortCodeClass = shortCodeClassArr[shortCodeClassArr.length - 1];
        for (String key : this.codeMessage.keySet()) {
            String property = this.constProperty;
            property = property.replaceAll("%SHORT_CODE_CLASS%", shortCodeClass);
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

    public void setCurrentCode(Long currentCode) {
        this.currentCode = currentCode;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setOutPackage(String outPackage) {
        this.outPackage = outPackage;
    }
}
