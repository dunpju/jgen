package com.dunpju.model;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.jdbc.DatabaseMetaDataWrapper;
import com.baomidou.mybatisplus.generator.type.TypeRegistry;
import com.dunpju.gen.IGen;
import com.dunpju.stubs.ModelStub;
import com.dunpju.utils.CamelizeUtil;
import com.dunpju.utils.StrUtil;
import lombok.Data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@Data
public class ModelGen implements IGen {
    private String outPackage;
    /**
     * 输出目录，绝对路径
     */
    private String outDir;
    private String tableName;
    private String tablePrefix;
    private String tableRemarks;
    private Map<String, DatabaseMetaDataWrapper.Column> columnsInfo;
    private ConfigBuilder configBuilder;
    private TypeRegistry typeRegistry;
    @Override
    public void run() {
        if (this.tableName.contains(this.tablePrefix)) {
            String className = StrUtil.upperFirst(CamelizeUtil.toCamelCase(this.tableName.replaceAll(this.tablePrefix, "")));

            ModelStub modelStub = new ModelStub();
            modelStub.setOutPackage(this.outPackage);
            modelStub.setClassName(className);
            modelStub.setTableName(this.tableName);
            modelStub.setTableDescription(this.tableRemarks);
            modelStub.setColumnsInfo(this.columnsInfo);
            modelStub.setConfigBuilder(this.configBuilder);
            modelStub.setTypeRegistry(this.typeRegistry);
            String stub = modelStub.stub();
            String outClassFile = this.outDir + "/" + className + ".java";
            File file = new File(outClassFile);
            if (!file.exists()) {
                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outClassFile));
                    bufferedWriter.write(stub);
                    bufferedWriter.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
