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
                String unTablePrefix = tableName.replaceAll(this.tablePrefix, "");
                String className = StrUtil.upperFirst(CamelizeUtil.toCamelCase(unTablePrefix));

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
                ArrayList<String> mapperPackageArray = new ArrayList<>();
                ArrayList<String> entityPackageArray = new ArrayList<>();
                ArrayList<String> iServicePackageArray = new ArrayList<>();
                ArrayList<String> serviceImplPackageArray = new ArrayList<>();
                ArrayList<String> voPackageArray = new ArrayList<>();
                ArrayList<String> daoPackageArray = new ArrayList<>();
                for (int i = 0; i < outPackageSplit.length - 1; i++) {
                    mapperPackageArray.add(outPackageSplit[i]);
                    entityPackageArray.add(outPackageSplit[i]);
                    iServicePackageArray.add(outPackageSplit[i]);
                    serviceImplPackageArray.add(outPackageSplit[i]);
                    voPackageArray.add(outPackageSplit[i]);
                    daoPackageArray.add(outPackageSplit[i]);
                }
                mapperPackageArray.add("mapper");
                entityPackageArray.add("entity");
                iServicePackageArray.add("service");
                serviceImplPackageArray.add("service");
                serviceImplPackageArray.add("impl");
                voPackageArray.add("vo");
                voPackageArray.add(className);
                daoPackageArray.add("dao");

                String mapperPackage = String.join(".", mapperPackageArray);
                MapperGen mapperGen = new MapperGen();
                mapperGen.setOutPackage(mapperPackage);
                List<String> modelImports = new ArrayList<>();
                modelImports.add("import " + String.format("%s.%s", this.outPackage, className) + ";");
                mapperGen.setImports(modelImports);
                mapperGen.setClassName(className + "Mapper");
                mapperGen.setModelName(className);
                mapperGen.setOutMapperXmlDir(this.outMapperXmlDir);
                File file = new File(this.outDir);
                mapperGen.setOutDir(file.getParentFile() + "/mapper");
                mapperGen.run();

                VOGen voGen = new VOGen();
                voGen.setOutPackage(String.join(".", voPackageArray));
                voGen.setClassName(className + "VO");
                voGen.setOutDir(file.getParentFile() + "/vo/" + className);
                voGen.run();

                EntityGen entityGen = new EntityGen();
                entityGen.setOutPackage(String.join(".", entityPackageArray));
                entityGen.setClassDesc(table.getRemarks());
                entityGen.setClassName(className + "Entity");
                entityGen.setTableName(tableName);
                entityGen.setColumnsInfo(columnsInfo);
                entityGen.setConfigBuilder(this.configBuilder);
                entityGen.setTypeRegistry(this.typeRegistry);
                entityGen.setOutDir(file.getParentFile() + "/entity");
                entityGen.run();

                DaoGen daoGen = new DaoGen();
                daoGen.setOutPackage(String.join(".", daoPackageArray));
                List<String> daoImports = new ArrayList<>();
                daoImports.add("import " + String.format("%s.%s", entityGen.getOutPackage(), entityGen.getClassName()) + ";");
                daoImports.add("import " + String.format("%s.%s", mapperGen.getOutPackage(), mapperGen.getClassName()) + ";");
                daoImports.add("import " + String.format("%s.%s", this.outPackage, className) + ";");
                daoImports.add("import " + String.format("%s.%s", voGen.getOutPackage(), voGen.getClassName()) + ";");
                daoGen.setImports(daoImports);
                daoGen.setClassName(className + "Dao");
                daoGen.setMapperName(mapperGen.getClassName());
                daoGen.setModelName(className);
                daoGen.setEntityName(entityGen.getClassName());
                daoGen.setConfigBuilder(this.configBuilder);
                daoGen.setColumnsInfo(columnsInfo);
                daoGen.setTypeRegistry(this.typeRegistry);
                daoGen.setTableName(tableName);
                daoGen.setVoName(voGen.getClassName());
                daoGen.setOutDir(file.getParentFile() + "/dao");
                daoGen.run();

                IServiceGen iServiceGen = new IServiceGen();
                iServiceGen.setOutPackage(String.join(".", iServicePackageArray));
                iServiceGen.setImports(modelImports);
                iServiceGen.setClassName("I" + className + "Service");
                iServiceGen.setModelName(className);
                iServiceGen.setOutDir(file.getParentFile() + "/service");
                iServiceGen.run();

                ServiceImplGen serviceImplGen = new ServiceImplGen();
                serviceImplGen.setOutPackage(String.join(".", serviceImplPackageArray));
                modelImports.add("import " + String.format("%s.%s", mapperGen.getOutPackage(), mapperGen.getClassName()) + ";");
                modelImports.add("import " + String.format("%s.%s", iServiceGen.getOutPackage(), iServiceGen.getClassName()) + ";");
                modelImports.add("import " + String.format("%s.%s", voGen.getOutPackage(), voGen.getClassName()) + ";");
                serviceImplGen.setImports(modelImports);
                serviceImplGen.setClassName(className + "ServiceImpl");
                serviceImplGen.setMapperName(mapperGen.getClassName());
                serviceImplGen.setModelName(className);
                serviceImplGen.setServiceName(iServiceGen.getClassName());
                serviceImplGen.setVoName(voGen.getClassName());
                serviceImplGen.setUpperFirstDaoName(daoGen.getClassName());
                serviceImplGen.setDaoName(StrUtil.lowerFirst(daoGen.getClassName()));
                serviceImplGen.setUpperFirstEntityName(entityGen.getClassName());
                serviceImplGen.setEntityName(StrUtil.lowerFirst(entityGen.getClassName()));
                serviceImplGen.setEntityPrimaryKey(entityGen.getEntityPrimaryKey());
                serviceImplGen.setEntityPrimaryKeyType(entityGen.getEntityPrimaryKeyType());
                serviceImplGen.setUpperFirstEntityPrimaryKey(StrUtil.upperFirst(entityGen.getEntityPrimaryKey()));
                serviceImplGen.setOutDir(iServiceGen.getOutDir() + "/impl");
                serviceImplGen.run();
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
