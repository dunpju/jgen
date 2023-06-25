package io.dunpju.gen;

import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.jdbc.DatabaseMetaDataWrapper;
import com.baomidou.mybatisplus.generator.type.TypeRegistry;
import io.dunpju.stubs.DaoStub;
import lombok.Data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Data
public class DaoGen implements IGen{
    private String outDir;
    private String outPackage;
    private List<String> imports;
    private String className;
    private String mapperName;
    private String modelName;
    private String entityName;
    private ConfigBuilder configBuilder;
    private Map<String, DatabaseMetaDataWrapper.Column> columnsInfo;
    private TypeRegistry typeRegistry;
    private String voName;
    private String tableName;
    @Override
    public void run() throws SQLException {
        DaoStub daoStub = new DaoStub();
        daoStub.setOutPackage(this.outPackage);
        daoStub.setImports(this.imports);
        daoStub.setClassName(this.className);
        daoStub.setMapperName(this.mapperName);
        daoStub.setModelName(this.modelName);
        daoStub.setEntityName(this.entityName);
        daoStub.setTableName(this.tableName);
        daoStub.setVoName(this.voName);
        daoStub.setColumnsInfo(this.columnsInfo);
        daoStub.setConfigBuilder(this.configBuilder);
        daoStub.setTypeRegistry(this.typeRegistry);
        String stub = daoStub.stub();
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
