package io.dunpju.stubs;

import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
public class MapperStub {
    private String outPackage;
    private List<String> imports = new ArrayList<>();
    private String className;
    private String modelName;

    public String stub() {
        StringBuilder tpl = new StringBuilder();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("stubs/mapper.stub")) {
            if (Objects.nonNull(inputStream)) {
                // 读取文本文件
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    tpl.append(line).append("\n");
                }
            } else {
                throw new RuntimeException("stubs/mapper.stub not found");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tpl = new StringBuilder(tpl.toString().replaceAll("%PACKAGE%", this.outPackage));
        tpl = new StringBuilder(tpl.toString().replaceAll("%IMPORTS%", String.join("\n", this.imports)));
        tpl = new StringBuilder(tpl.toString().replaceAll("%CLASS_NAME%", this.className));
        tpl = new StringBuilder(tpl.toString().replaceAll("%MODEL_NAME%", this.modelName));
        return tpl.toString();
    }
}
