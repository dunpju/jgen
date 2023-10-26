package io.dunpju.gen;

import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.jdbc.DatabaseMetaDataWrapper;
import com.baomidou.mybatisplus.generator.type.TypeRegistry;
import io.dunpju.stubs.EntityStub;
import lombok.Data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@Data
public class EntityGen implements IGen{
    private String outDir;
    private String outPackage;
    private String classDesc;
    private String className;
    private String tableName;
    private Map<String, DatabaseMetaDataWrapper.Column> columnsInfo;
    private ConfigBuilder configBuilder;
    private TypeRegistry typeRegistry;
    private Map<String, TypeConvert> propertyTypeConvertMap;
    private String entityPrimaryKey;
    private String entityPrimaryKeyType;
    private boolean shieldExistedOut;
    private String createTimeInit;
    private String createTime;
    private String updateTime;
    private String deleteTime;
    @Override
    public void run() throws SQLException {
        EntityStub entityStub = new EntityStub();
        entityStub.setOutPackage(this.outPackage);
        entityStub.setClassDesc(this.classDesc);
        entityStub.setClassName(this.className);
        entityStub.setTableName(this.tableName);
        entityStub.setColumnsInfo(this.columnsInfo);
        entityStub.setConfigBuilder(this.configBuilder);
        entityStub.setTypeRegistry(this.typeRegistry);
        entityStub.setPropertyTypeConvertMap(this.propertyTypeConvertMap);
        if (this.createTimeInit != null) {
            entityStub.setCreateTimeInit(this.createTimeInit);
        }
        if (this.createTime != null) {
            entityStub.setCreateTime(this.createTime);
        }
        if (this.updateTime != null) {
            entityStub.setUpdateTime(this.updateTime);
        }
        if (this.deleteTime != null) {
            entityStub.setDeleteTime(this.deleteTime);
        }
        String stub = entityStub.stub();
        this.entityPrimaryKey = entityStub.getEntityPrimaryKey();
        this.entityPrimaryKeyType = entityStub.getEntityPrimaryKeyType();
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
