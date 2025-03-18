package io.dunpju.stubs;

import io.dunpju.utils.CamelizeUtil;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class IServiceStub {

    private String outPackage;
    private List<String> imports = new ArrayList<>();
    private String className;
    private String modelName;
    private String primaryKey;
    private String primaryKeyType;

    public String stub() {
        StringBuilder tpl = new StringBuilder();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("stubs/service.stub")) {
            if (Objects.nonNull(inputStream)) {
                // 读取文本文件
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    tpl.append(line).append("\n");
                }
            } else {
                throw new RuntimeException("stubs/service.stub not found");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tpl = new StringBuilder(tpl.toString().replaceAll("%PACKAGE%", this.outPackage));
        tpl = new StringBuilder(tpl.toString().replaceAll("%IMPORTS%", String.join("\n", this.imports)));
        tpl = new StringBuilder(tpl.toString().replaceAll("%CLASS_NAME%", this.className));
        tpl = new StringBuilder(tpl.toString().replaceAll("%MODEL_NAME%", this.modelName));
        tpl = new StringBuilder(tpl.toString().replaceAll("%UPPER_PRIMARY_KEY%", CamelizeUtil.toCamelCase(this.primaryKey)));
        tpl = new StringBuilder(tpl.toString().replaceAll("%PRIMARY_KEY%", this.primaryKey));
        tpl = new StringBuilder(tpl.toString().replaceAll("%PRIMARY_KEY_TYPE%", this.primaryKeyType));
        return tpl.toString();
    }
}
