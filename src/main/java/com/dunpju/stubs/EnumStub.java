package com.dunpju.stubs;

import com.dunpju.utils.CamelizeUtil;
import com.dunpju.utils.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
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
                "    public int getCode() {\n" +
                "        return code;\n" +
                "    }\n" +
                "    public String getMessage() {\n" +
                "        return message;\n" +
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
                String[] fm = f.split("-");
                cms.add(new Cm(fm[0], Integer.parseInt(fm[1]), fm[2]));
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
