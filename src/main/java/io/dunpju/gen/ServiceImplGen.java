package io.dunpju.gen;

import io.dunpju.stubs.ServiceImplStub;
import lombok.Data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Data
public class ServiceImplGen implements IGen {
    private String outDir;
    private String outPackage;
    private List<String> imports;
    private String className;
    private String modelName;
    private String primaryKey;
    private String primaryKeyType;
    private String mapperName;
    private String serviceName;
    private String voName;
    private String upperFirstDaoName;
    private String daoName;
    private String upperFirstEntityName;
    private String entityName;
    private String entityPrimaryKey;
    private String entityPrimaryKeyType;
    private String upperFirstEntityPrimaryKey;
    private boolean shieldExistedOut;
    /**
     * 继承的类, 如: com.baomidou.mybatisplus.extension.service.IService;
     */
    private String extendsClass;

    @Override
    public void run() throws SQLException {
        this.imports.add("import " + String.format("%s", extendsClass) + ";");
        ServiceImplStub serviceImplStub = new ServiceImplStub();
        serviceImplStub.setOutPackage(this.outPackage);
        serviceImplStub.setImports(this.imports);
        serviceImplStub.setClassName(this.className);
        serviceImplStub.setModelName(this.modelName);
        serviceImplStub.setPrimaryKey(this.primaryKey);
        serviceImplStub.setPrimaryKeyType(this.primaryKeyType);
        serviceImplStub.setMapperName(this.mapperName);
        serviceImplStub.setServiceName(this.serviceName);
        serviceImplStub.setVoName(this.voName);
        serviceImplStub.setUpperFirstDaoName(this.upperFirstDaoName);
        serviceImplStub.setDaoName(this.daoName);
        serviceImplStub.setUpperFirstEntityName(this.upperFirstEntityName);
        serviceImplStub.setEntityName(this.entityName);
        serviceImplStub.setEntityPrimaryKey(this.entityPrimaryKey);
        serviceImplStub.setEntityPrimaryKeyType(this.entityPrimaryKeyType);
        serviceImplStub.setUpperFirstEntityPrimaryKey(this.upperFirstEntityPrimaryKey);
        String[] extendsClassSplit = extendsClass.split("\\.");
        String extendsClassName = extendsClassSplit[extendsClassSplit.length - 1];
        serviceImplStub.setExtendsClass(extendsClassName);
        String stub = serviceImplStub.stub();
        String outClassFile = this.outDir + ModelGen.separatorChar + className + ".java";
        File file = new File(outClassFile);
        if (!file.exists()) {
            try {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outClassFile));
                bufferedWriter.write(stub);
                bufferedWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(outClassFile + " generate successful");
        } else {
            if (!shieldExistedOut) {
                System.out.println(outClassFile + " already existed");
            }
        }
    }
}
