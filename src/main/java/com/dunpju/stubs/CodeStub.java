package com.dunpju.stubs;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CodeStub {

    String constProperty = "    public final static %CODE_CLASS% %PROPERTY% = new %CODE_CLASS%(%CODE%, \"%MESSAGE%\");\n";
    Map<String, Object> codeMessage = new HashMap<>();
    String codeClass;
    Long currentCode;
    Long code;
    String message;
    StringBuffer property = new StringBuffer();

    public String stub() {
        return "package %PACKAGE%;\n" +
                "\n" +
                "import %CODE_PACKAGE%.%CODE_CLASS%;\n" +
                "\n" +
                "public class %CLASS% {\n" +
                this.property + "\n" +
                "}\n";
    }

    public String buildProperty() {
        Long currentCode = this.currentCode;
        for (String key : this.codeMessage.keySet()) {
            String property = this.constProperty;
            property = property.replaceAll("%CODE_CLASS%", this.codeClass);
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
            System.out.println(property);
            this.property.append(property);
            currentCode++;
        }
        return this.property.toString();
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
}
