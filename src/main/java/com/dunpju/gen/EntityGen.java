package com.dunpju.gen;

import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.jdbc.DatabaseMetaDataWrapper;
import com.baomidou.mybatisplus.generator.type.TypeRegistry;
import com.dunpju.stubs.EntityStub;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@Setter
public class EntityGen implements IGen{
    private String outDir;
    private String outPackage;
    private String classDesc;
    private String className;
    private String tableName;
    private Map<String, DatabaseMetaDataWrapper.Column> columnsInfo;
    private ConfigBuilder configBuilder;
    private TypeRegistry typeRegistry;
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
        String stub = entityStub.stub();
        String outClassFile = this.outDir + "/" + className + ".java";
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
        }
    }
}
