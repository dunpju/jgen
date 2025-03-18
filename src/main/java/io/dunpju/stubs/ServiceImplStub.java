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
public class ServiceImplStub {
    private String outPackage;
    private List<String> imports = new ArrayList<>();
    private String className;
    private String mapperName;
    private String modelName;
    private String primaryKey;
    private String primaryKeyType;
    private String serviceName;
    private String voName;
    private String upperFirstDaoName;
    private String daoName;
    private String upperFirstEntityName;
    private String entityName;
    private String entityPrimaryKey;
    private String entityPrimaryKeyType;
    private String upperFirstEntityPrimaryKey;

    public String stub() {
        StringBuilder tpl = new StringBuilder();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("stubs/service_impl.stub")) {
            if (Objects.nonNull(inputStream)) {
                // 读取文本文件
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    tpl.append(line).append("\n");
                }
            } else {
                throw new RuntimeException("stubs/service_impl.stub not found");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tpl = new StringBuilder(tpl.toString().replaceAll("%PACKAGE%", this.outPackage));
        tpl = new StringBuilder(tpl.toString().replaceAll("%IMPORTS%", String.join("\n", this.imports)));
        tpl = new StringBuilder(tpl.toString().replaceAll("%CLASS_NAME%", this.className));
        tpl = new StringBuilder(tpl.toString().replaceAll("%MAPPER_NAME%", this.mapperName));
        tpl = new StringBuilder(tpl.toString().replaceAll("%MODEL_NAME%", this.modelName));
        tpl = new StringBuilder(tpl.toString().replaceAll("%UPPER_PRIMARY_KEY%", CamelizeUtil.toCamelCase(this.primaryKey)));
        tpl = new StringBuilder(tpl.toString().replaceAll("%PRIMARY_KEY%", this.primaryKey));
        tpl = new StringBuilder(tpl.toString().replaceAll("%PRIMARY_KEY_TYPE%", this.primaryKeyType));
        tpl = new StringBuilder(tpl.toString().replaceAll("%SERVICE_NAME%", this.serviceName));
        //tpl = tpl.replaceAll("%VO_NAME%", this.voName);
        //tpl = tpl.replaceAll("%UPPER_FIRST_DAO_NAME%", this.upperFirstDaoName);
        //tpl = tpl.replaceAll("%DAO_NAME%", this.daoName);
        //tpl = tpl.replaceAll("%UPPER_FIRST_ENTITY_NAME%", this.upperFirstEntityName);
        //tpl = tpl.replaceAll("%ENTITY_NAME%", this.entityName);
        //tpl = tpl.replaceAll("%ENTITY_PRIMARY_KEY_TYPE%", this.entityPrimaryKeyType != null ? this.entityPrimaryKeyType : "Long");
        //tpl = tpl.replaceAll("%ENTITY_PRIMARY_KEY%", this.entityPrimaryKey != null ? this.entityPrimaryKey : "id");
        //tpl = tpl.replaceAll("%UPPER_FIRST_ENTITY_PRIMARY_KEY%", this.upperFirstEntityPrimaryKey != null ? this.upperFirstEntityPrimaryKey : "Id");
        return tpl.toString();
    }
}
