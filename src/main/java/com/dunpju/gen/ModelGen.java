package com.dunpju.gen;

import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.jdbc.DatabaseMetaDataWrapper;
import com.baomidou.mybatisplus.generator.type.TypeRegistry;
import com.dunpju.stubs.ModelStub;
import com.dunpju.utils.CamelizeUtil;
import com.dunpju.utils.StrUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ModelGen implements IGen {
    private String outPackage;
    /**
     * 输出目录，绝对路径
     */
    private String outDir;
    private String outMapperXmlDir;
    private String tableName;
    private String tablePrefix = "";
    private DataSourceConfig dataSourceConfig;
    private DatabaseMetaDataWrapper databaseMetaDataWrapper;
    private Map<String, DatabaseMetaDataWrapper.Column> columnsInfo;
    private ConfigBuilder configBuilder;
    private TypeRegistry typeRegistry;
    private ResultSet tableResultSet;

    public ModelGen Builder(String url, String username, String password) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.dataSourceConfig = new DataSourceConfig.Builder(url, username, password).build();
        this.databaseMetaDataWrapper = new DatabaseMetaDataWrapper(dataSourceConfig.getConn(), dataSourceConfig.getSchemaName());
        DatabaseMetaData metaData = dataSourceConfig.getConn().getMetaData();
        PackageConfig packageInfo = new PackageConfig.Builder().build();
        StrategyConfig strategy = new StrategyConfig.Builder().build();
        TemplateConfig template = new TemplateConfig.Builder().build();
        GlobalConfig globalConfig = new GlobalConfig.Builder().build();
        InjectionConfig injection = new InjectionConfig();

        this.typeRegistry = new TypeRegistry(globalConfig);

        this.configBuilder = new ConfigBuilder(packageInfo, dataSourceConfig, strategy, template, globalConfig, injection);

        this.tableResultSet = metaData.getTables(dataSourceConfig.getConn().getCatalog(), dataSourceConfig.getConn().getCatalog(), null, new String[]{"TABLE"});
        return this;
    }

    @Override
    public void run() throws SQLException {
        while (this.tableResultSet.next()) {
            String tableName = null;
            if (this.tableName != null && !this.tableName.equals("all")) {
                tableName = this.tableName;
            } else {
                tableName = tableResultSet.getString("TABLE_NAME");
            }
            Map<String, DatabaseMetaDataWrapper.Column> columnsInfo = this.databaseMetaDataWrapper.getColumnsInfo(
                    this.dataSourceConfig.getConn().getCatalog(),
                    this.dataSourceConfig.getConn().getCatalog(),
                    tableName, true);
            DatabaseMetaDataWrapper.Table table = this.databaseMetaDataWrapper.getTableInfo(tableName);

            if (tableName.contains(this.tablePrefix)) {
                String className = StrUtil.upperFirst(CamelizeUtil.toCamelCase(tableName.replaceAll(this.tablePrefix, "")));

                ModelStub modelStub = new ModelStub();
                modelStub.setOutPackage(this.outPackage);
                modelStub.setClassName(className);
                modelStub.setTableName(tableName);
                modelStub.setTableDescription(table.getRemarks());
                modelStub.setColumnsInfo(columnsInfo);
                modelStub.setConfigBuilder(this.configBuilder);
                modelStub.setTypeRegistry(this.typeRegistry);
                String stub = modelStub.stub();
                String outClassFile = this.outDir + "/" + className + ".java";
                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outClassFile));
                    bufferedWriter.write(stub);
                    bufferedWriter.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                String[] outPackageSplit = this.outPackage.split("\\.");
                ArrayList<String> outPackageArray = new ArrayList<>();
                for (int i = 0; i < outPackageSplit.length - 1; i++) {
                    outPackageArray.add(outPackageSplit[i]);
                }
                outPackageArray.add("mapper");
                String mapperPackage = String.join(".", outPackageArray);
                System.out.println(mapperPackage);
                MapperGen mapperGen = new MapperGen();
                mapperGen.setOutPackage(mapperPackage);
                List<String> imports = new ArrayList<>();
                imports.add("import " + String.format("%s.%s", this.outPackage, className) + ";");
                System.out.println(imports);
                mapperGen.setImports(imports);
                mapperGen.setClassName(className + "Mapper");
                mapperGen.setModelName(className);
                File file = new File(this.outDir);
                mapperGen.setOutDir(file.getParentFile() + "/mapper");
                mapperGen.run();
            }
        }
    }

    public ModelGen setOutPackage(String outPackage) {
        this.outPackage = outPackage;
        return this;
    }

    public ModelGen setOutDir(String outDir) {
        this.outDir = outDir;
        return this;
    }

    public ModelGen setOutMapperXmlDir(String outMapperXmlDir) {
        this.outMapperXmlDir = outMapperXmlDir;
        return this;
    }

    public ModelGen setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public ModelGen setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
        return this;
    }
}
