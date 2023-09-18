package io.dunpju.stubs;

import io.dunpju.utils.CamelizeUtil;
import io.dunpju.utils.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnumStub {
    String input;
    String outPackage;
    String className;
    String flag;
    String classDesc;
    String propertyStub = "    %PROPERTY%(%CODE%, \"%MESSAGE%\")";
    StringBuffer enumProperty = new StringBuffer();

    public String stub() {
        this.parseInput();
        this.buildFlags();
        String tpl = "package %PACKAGE%;\n" +
                "\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.LinkedHashMap;\n" +
                "import java.util.List;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "/**\n" +
                " * %CLASS_DESC%\n" +
                " */\n" +
                "public enum %CLASS_NAME% {\n" +
                this.enumProperty +
                "\n" +
                "    private final int code;\n" +
                "    private final String message;\n" +
                "\n" +
                "    %CLASS_NAME%(int code, String message) {\n" +
                "        this.code = code;\n" +
                "        this.message = message;\n" +
                "    }\n" +
                "    \n" +
                "    public int getCode() {\n" +
                "        return code;\n" +
                "    }\n" +
                "    \n" +
                "    public String getMessage() {\n" +
                "        return message;\n" +
                "    }\n" +
                "    \n" +
                "    public static %CLASS_NAME% get(int code) throws Exception {\n" +
                "        for (%CLASS_NAME% e : %CLASS_NAME%.values()) {\n" +
                "            if (e.code == code) {\n" +
                "                return e;\n" +
                "            }\n" +
                "        }\n" +
                "        throw new Exception(String.format(\"%d不存在枚举%s中\", code, %CLASS_NAME%.class.getSimpleName()));\n" +
                "    }\n" +
                "    \n" +
                "    public static List<Map<String, Object>> select() {\n" +
                "        List<Map<String, Object>> list = new ArrayList<>();\n" +
                "        for (%CLASS_NAME% e : %CLASS_NAME%.values()) {\n" +
                "            Map<String, Object> m = new LinkedHashMap<>();\n" +
                "            m.put(\"id\", e.getCode());\n" +
                "            m.put(\"title\", e.getMessage());\n" +
                "            list.add(m);\n" +
                "        }\n" +
                "        return list;\n" +
                "    }\n" +
                "    \n" +
                "    public static List<Map<String, Object>> select(String key, String title) {\n" +
                "        List<Map<String, Object>> list = new ArrayList<>();\n" +
                "        for (%CLASS_NAME% e : %CLASS_NAME%.values()) {\n" +
                "            Map<String, Object> m = new LinkedHashMap<>();\n" +
                "            m.put(key, e.getCode());\n" +
                "            m.put(title, e.getMessage());\n" +
                "            list.add(m);\n" +
                "        }\n" +
                "        return list;\n" +
                "    }\n" +
                "\n" +
                "}";
        tpl = tpl.replaceAll("%PACKAGE%", this.outPackage);
        tpl = tpl.replaceAll("%CLASS_DESC%", this.classDesc);
        tpl = tpl.replaceAll("%CLASS_NAME%", this.className);
        return tpl;
    }

    @Data
    @AllArgsConstructor
    static
    class Cm {
        String property;
        Integer code;
        String message;
    }

    private void parseInput() {
        Pattern patten = Pattern.compile("(\\s+)");
        Matcher matcher = patten.matcher(this.input);
        if (matcher.find()) {
            this.input = matcher.replaceAll(" ");
        }
        String[] inputArr = this.input.split(" ");
        this.className = StrUtil.upperFirst(CamelizeUtil.toCamelCase(("Enum_" + inputArr[0].replaceAll("name=", "")).toLowerCase())).trim();
        this.flag = inputArr[1].replaceAll("flag=", "");
    }

    private void buildFlags() {
        String[] flagArr = this.flag.split(":");
        String flagPrev = flagArr[0];
        String flagLater = flagArr[1];
        this.classDesc = flagPrev;
        String[] flagLaterArr = flagLater.split(",");
        List<Cm> cms = new ArrayList<>();
        for (String f : flagLaterArr) {
            if (!f.equals("")) {
                String[] fms = f.split("-");
                String preFm = "";
                String property = "";
                Integer code = null;
                String message = "";
                for (String fm : fms) {
                    if (Objects.equals(property, "") && !Objects.equals(fm, "")) {
                        property = fm;
                    } else if (!property.equals("") && code == null && !Objects.equals(fm, "")) {
                        if (preFm.equals("")) {
                            code = Integer.parseInt(String.format("-%s", fm));
                        } else {
                            code = Integer.parseInt(fm);
                        }
                    } else if (!property.equals("") && code != null && message.equals("")) {
                        message = fm;
                    }
                    preFm = fm;
                }
                cms.add(new Cm(property, code, message));
            }
        }
        int i = 0;
        for (Cm c : cms) {
            String cc = this.propertyStub;
            cc = cc.replaceAll("%PROPERTY%", c.property.trim().toUpperCase());
            cc = cc.replaceAll("%CODE%", String.valueOf(c.code));
            cc = cc.replaceAll("%MESSAGE%", c.message.trim());
            if (i < cms.size() - 1) {
                this.enumProperty.append(cc).append(",");
            } else {
                this.enumProperty.append(cc).append(";");
            }
            this.enumProperty.append("\n");
            i++;
        }
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setOutPackage(String outPackage) {
        this.outPackage = outPackage;
    }

    public String getClassName() {
        return className;
    }
}
