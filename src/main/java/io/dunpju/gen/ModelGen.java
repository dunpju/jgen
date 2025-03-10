package io.dunpju.gen;

import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.jdbc.DatabaseMetaDataWrapper;
import com.baomidou.mybatisplus.generator.type.TypeRegistry;
import io.dunpju.stubs.ModelStub;
import io.dunpju.utils.CamelizeUtil;
import io.dunpju.utils.ClassLoaderUtil;
import io.dunpju.utils.StrUtil;
import lombok.extern.log4j.Log4j;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;

public class ModelGen implements IGen {
    public static final char separatorChar = File.separatorChar;
    private String separator = "" + ModelGen.separatorChar;
    private String basePackage;
    private String packageName;
    private String baseDir;
    private String outMapperXmlDir;
    private boolean isAll;
    private Set<String> tableNames;
    private String tablePrefix = "";
    private DataSourceConfig dataSourceConfig;
    private DatabaseMetaDataWrapper databaseMetaDataWrapper;
    private Map<String, DatabaseMetaDataWrapper.Column> columnsInfo;
    private ConfigBuilder configBuilder;
    private TypeRegistry typeRegistry;
    private ResultSet tableResultSet;
    private final Map<String, TypeConvert> typeConvertMap;
    private final Map<String, ITypeConvert> iTypeConvertMap;
    private final List<String> updatedField;
    private final List<String> alreadyField;
    private boolean shieldExistedOut;
    private String createTimeInit;
    private String createTime;
    private String updateTime;
    private String deleteTime;

    private boolean isBuildDao = true;

    private boolean isForceUpdate = false;

    public ModelGen() {
        this.typeConvertMap = new HashMap<>();
        this.iTypeConvertMap = new HashMap<>();
        this.updatedField = new ArrayList<>();
        this.alreadyField = new ArrayList<>();
        this.tableNames = new HashSet<>();
    }

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

    private void build(String tableName) throws SQLException {
        Map<String, DatabaseMetaDataWrapper.Column> columnsInfo = this.databaseMetaDataWrapper.getColumnsInfo(
                this.dataSourceConfig.getConn().getCatalog(),
                this.dataSourceConfig.getConn().getCatalog(),
                tableName, true);
        DatabaseMetaDataWrapper.Table table = this.databaseMetaDataWrapper.getTableInfo(tableName);

        String unTablePrefix = tableName.replaceAll(this.tablePrefix, "");
        String className = StrUtil.upperFirst(CamelizeUtil.toCamelCase(unTablePrefix));

        String catalog = StrUtil.upperFirst(CamelizeUtil.toCamelCase(dataSourceConfig.getConn().getCatalog()));
        if (this.packageName != null && !this.packageName.equals("")) {
            catalog = this.packageName;
        }

        ModelStub modelStub = new ModelStub();
        modelStub.setOutPackage(this.basePackage + "." + catalog);
        modelStub.setClassName(className);
        modelStub.setTableName(tableName);
        modelStub.setTableDescription(table.getRemarks());
        modelStub.setColumnsInfo(columnsInfo);
        modelStub.setConfigBuilder(this.configBuilder);
        modelStub.setTypeRegistry(this.typeRegistry);
        modelStub.setPropertyTypeConvertMap(this.typeConvertMap);
        modelStub.setPropertyITypeConvertMap(this.iTypeConvertMap);
        modelStub.setUpdatedField(this.updatedField);
        modelStub.setForceUpdate(this.isForceUpdate);

        String outClassFile = this.baseDir + this.separator + catalog + this.separator + className + ".java";
        File file = new File(outClassFile);
        if (file.exists()) {
            try {
                Class<?> clazz = ClassLoaderUtil.load(this.basePackage + "." + catalog + "." + className);
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    this.alreadyField.add(field.getName());
                }
                modelStub.setAlreadyField(this.alreadyField);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        String stub = modelStub.stub();

        if (!file.exists() || this.isForceUpdate || !this.updatedField.isEmpty()) {
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
            if (this.isForceUpdate) {
                System.out.println(outClassFile + " force update successful");
            } else if (!this.updatedField.isEmpty()) {
                System.out.println(outClassFile + " update successful");
            } else {
                System.out.println(outClassFile + " generate successful");
            }
        } else {
            System.out.println(outClassFile + " already existed");
        }

        String[] basePackageSplit = this.basePackage.split("\\.");
        ArrayList<String> mapperPackageArray = new ArrayList<>();
        ArrayList<String> entityPackageArray = new ArrayList<>();
        ArrayList<String> iServicePackageArray = new ArrayList<>();
        ArrayList<String> serviceImplPackageArray = new ArrayList<>();
        ArrayList<String> voPackageArray = new ArrayList<>();
        ArrayList<String> daoPackageArray = new ArrayList<>();
        ArrayList<String> paramPackageArray = new ArrayList<>();
        for (int i = 0; i < basePackageSplit.length - 1; i++) {
            mapperPackageArray.add(basePackageSplit[i]);
            entityPackageArray.add(basePackageSplit[i]);
            iServicePackageArray.add(basePackageSplit[i]);
            serviceImplPackageArray.add(basePackageSplit[i]);
            voPackageArray.add(basePackageSplit[i]);
            daoPackageArray.add(basePackageSplit[i]);
            paramPackageArray.add(basePackageSplit[i]);
        }
        mapperPackageArray.add("mapper");
        mapperPackageArray.add(catalog);

        entityPackageArray.add("entity");
        entityPackageArray.add(catalog);

        iServicePackageArray.add("service");
        iServicePackageArray.add(catalog);

        serviceImplPackageArray.add("service");
        serviceImplPackageArray.add(catalog);
        serviceImplPackageArray.add("impl");

        voPackageArray.add("vo");
        voPackageArray.add(catalog);
        voPackageArray.add(className);

        daoPackageArray.add("dao");
        daoPackageArray.add(catalog);

        paramPackageArray.add("params");
        paramPackageArray.add(catalog);
        paramPackageArray.add(className + "Service");

        String mapperPackage = String.join(".", mapperPackageArray);
        MapperGen mapperGen = new MapperGen();
        mapperGen.setOutPackage(mapperPackage);
        List<String> modelImports = new ArrayList<>();
        modelImports.add("import " + String.format("%s.%s", modelStub.getOutPackage(), className) + ";");
        mapperGen.setImports(modelImports);
        mapperGen.setClassName(className + "Mapper");
        mapperGen.setModelName(className);
        mapperGen.setOutMapperXmlDir(this.outMapperXmlDir + this.separator + catalog);
        file = new File(this.baseDir);
        mapperGen.setOutDir(file.getParentFile() + this.separator + "mapper" + this.separator + catalog);
        mapperGen.setShieldExistedOut(shieldExistedOut);
        mapperGen.run();

        /*VOGen voGen = new VOGen();
        voGen.setOutPackage(String.join(".", voPackageArray));
        voGen.setClassName(className + "VO");
        voGen.setShieldExistedOut(this.shieldExistedOut);
        voGen.setOutDir(file.getParentFile() + this.separator + "vo" + this.separator + catalog + this.separator + className);
        voGen.run();*/

        if (this.isBuildDao) {
            EntityGen entityGen = new EntityGen();
            entityGen.setOutPackage(String.join(".", entityPackageArray));
            entityGen.setClassDesc(table.getRemarks());
            entityGen.setClassName(className + "Entity");
            entityGen.setTableName(tableName);
            entityGen.setColumnsInfo(columnsInfo);
            entityGen.setConfigBuilder(this.configBuilder);
            entityGen.setTypeRegistry(this.typeRegistry);
            entityGen.setPropertyTypeConvertMap(this.typeConvertMap);
            entityGen.setPropertyITypeConvertMap(this.iTypeConvertMap);
            entityGen.setShieldExistedOut(this.shieldExistedOut);
            entityGen.setCreateTimeInit(this.createTimeInit);
            entityGen.setCreateTime(this.createTime);
            entityGen.setUpdateTime(this.updateTime);
            entityGen.setDeleteTime(this.deleteTime);
            entityGen.setOutDir(file.getParentFile() + this.separator + "entity" + this.separator + catalog);
            entityGen.run();

            DaoGen daoGen = new DaoGen();
            daoGen.setOutPackage(String.join(".", daoPackageArray));
            List<String> daoImports = new ArrayList<>();
            daoImports.add("import " + String.format("%s.%s", entityGen.getOutPackage(), entityGen.getClassName()) + ";");
            daoImports.add("import " + String.format("%s.%s", mapperGen.getOutPackage(), mapperGen.getClassName()) + ";");
            daoImports.add("import " + String.format("%s.%s", modelStub.getOutPackage(), className) + ";");
            //daoImports.add("import " + String.format("%s.%s", voGen.getOutPackage(), voGen.getClassName()) + ";");
            daoGen.setImports(daoImports);
            daoGen.setClassName(className + "Dao");
            daoGen.setMapperName(mapperGen.getClassName());
            daoGen.setModelName(className);
            daoGen.setEntityName(entityGen.getClassName());
            daoGen.setConfigBuilder(this.configBuilder);
            daoGen.setColumnsInfo(columnsInfo);
            daoGen.setTypeRegistry(this.typeRegistry);
            daoGen.setShieldExistedOut(this.shieldExistedOut);
            daoGen.setTableName(tableName);
            //daoGen.setVoName(voGen.getClassName());
            daoGen.setOutDir(file.getParentFile() + this.separator + "dao" + this.separator + catalog);
            daoGen.run();
        }

        IServiceGen iServiceGen = new IServiceGen();
        iServiceGen.setOutPackage(String.join(".", iServicePackageArray));
        iServiceGen.setImports(modelImports);
        iServiceGen.setClassName("I" + className + "Service");
        iServiceGen.setModelName(className);
        iServiceGen.setShieldExistedOut(this.shieldExistedOut);
        iServiceGen.setOutDir(file.getParentFile() + this.separator + "service" + this.separator + catalog);
        iServiceGen.run();

        /*ParamGen paramGen = new ParamGen();
        paramGen.setShieldExistedOut(this.shieldExistedOut);
        paramGen.setOutPackage(String.join(".", paramPackageArray));
        paramGen.setOutDir(file.getParentFile() + this.separator + "params" + this.separator + catalog + this.separator + className + "Service");
        paramGen.run();*/

        ServiceImplGen serviceImplGen = new ServiceImplGen();
        serviceImplGen.setOutPackage(String.join(".", serviceImplPackageArray));
        List<String> serviceImplImports = new ArrayList<>();
//        serviceImplImports.add("import " + String.format("%s.%s", daoGen.getOutPackage(), daoGen.getClassName()) + ";");
//        serviceImplImports.add("import " + String.format("%s.%s", entityGen.getOutPackage(), entityGen.getClassName()) + ";");
        serviceImplImports.add("import " + String.format("%s.%s", mapperGen.getOutPackage(), mapperGen.getClassName()) + ";");
        serviceImplImports.add("import " + String.format("%s.%s", modelStub.getOutPackage(), className) + ";");
//        serviceImplImports.add("import " + String.format("%s.%s", paramGen.getOutPackage(), paramGen.getAddClassName()) + ";");
//        serviceImplImports.add("import " + String.format("%s.%s", paramGen.getOutPackage(), paramGen.getEditClassName()) + ";");
//        serviceImplImports.add("import " + String.format("%s.%s", paramGen.getOutPackage(), paramGen.getListClassName()) + ";");
        serviceImplImports.add("import " + String.format("%s.%s", iServiceGen.getOutPackage(), iServiceGen.getClassName()) + ";");
        //serviceImplImports.add("import " + String.format("%s.%s", voGen.getOutPackage(), voGen.getClassName()) + ";");
        serviceImplGen.setImports(serviceImplImports);
        serviceImplGen.setClassName(className + "ServiceImpl");
        serviceImplGen.setMapperName(mapperGen.getClassName());
        serviceImplGen.setModelName(className);
        serviceImplGen.setServiceName(iServiceGen.getClassName());
        //serviceImplGen.setVoName(voGen.getClassName());
        //serviceImplGen.setUpperFirstDaoName(daoGen.getClassName());
        //serviceImplGen.setDaoName(StrUtil.lowerFirst(daoGen.getClassName()));
        //serviceImplGen.setUpperFirstEntityName(entityGen.getClassName());
        //serviceImplGen.setEntityName(StrUtil.lowerFirst(entityGen.getClassName()));
        //serviceImplGen.setEntityPrimaryKey(entityGen.getEntityPrimaryKey());
        //serviceImplGen.setEntityPrimaryKeyType(entityGen.getEntityPrimaryKeyType());
        //serviceImplGen.setUpperFirstEntityPrimaryKey(StrUtil.upperFirst(entityGen.getEntityPrimaryKey()));
        serviceImplGen.setShieldExistedOut(this.shieldExistedOut);
        serviceImplGen.setOutDir(iServiceGen.getOutDir() + this.separator + "impl");
        serviceImplGen.run();
    }

    @Override
    public void run() throws SQLException {

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("stubs/model.stub")) {
            if (inputStream != null) {
                // 读取文本文件
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } else {
                System.out.println("文件未找到: ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!this.tableNames.isEmpty()) {
            for (String tn : this.tableNames) {
                this.build(tn);
            }
        } else if (this.isAll) {
            this.tableNames = new HashSet<>();
            while (this.tableResultSet.next()) {
                String tableName = tableResultSet.getString("TABLE_NAME");
                if (!this.tablePrefix.equals("")) {
                    if (tableName.contains(this.tablePrefix)) {
                        this.tableNames.add(tableName);
                    }
                } else {
                    this.tableNames.add(tableName);
                }
            }
            for (String tn : this.tableNames) {
                this.build(tn);
            }
        }
    }

    public ModelGen setBasePackage(String basePackage) {
        this.basePackage = basePackage + ".model";
        return this;
    }

    public ModelGen setBaseDir(String baseDir) {
        if (baseDir.contains("\\")) {
            baseDir = baseDir.replaceAll("\\\\", Matcher.quoteReplacement(this.separator));
        } else if (baseDir.contains("/")) {
            baseDir = baseDir.replaceAll("/", Matcher.quoteReplacement(this.separator));
        }
        this.baseDir = baseDir + this.separator + "model";
        return this;
    }

    public ModelGen setOutMapperXmlDir(String outMapperXmlDir) {
        if (outMapperXmlDir.contains("\\")) {
            outMapperXmlDir = outMapperXmlDir.replaceAll("\\\\", Matcher.quoteReplacement(this.separator));
        } else if (outMapperXmlDir.contains("/")) {
            outMapperXmlDir = outMapperXmlDir.replaceAll("/", Matcher.quoteReplacement(this.separator));
        }
        this.outMapperXmlDir = outMapperXmlDir;
        return this;
    }

    public ModelGen setTableName(String tableName, String... moreTableName) {
        this.tableNames.add(tableName);
        if (moreTableName.length > 0) {
            this.tableNames.addAll(Arrays.asList(moreTableName));
        }
        return this;
    }

    public ModelGen setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
        return this;
    }

    public ModelGen typeConvert(String source, String target) {
        this.typeConvertMap.put(source, new TypeConvert(source, target, ""));
        return this;
    }

    public ModelGen typeConvert(String source, TypeConvert target) {
        this.typeConvertMap.put(source, target);
        return this;
    }

    public ModelGen typeConvert(String source, ITypeConvert target) {
        this.iTypeConvertMap.put(source, target);
        return this;
    }

    public ModelGen updatedField(String field, String... moreField) {
        this.updatedField.add(field);
        if (moreField.length > 0) {
            this.updatedField.addAll(Arrays.asList(moreField));
        }
        return this;
    }

    public ModelGen setShieldExistedOut(boolean shieldExistedOut) {
        this.shieldExistedOut = shieldExistedOut;
        return this;
    }

    public ModelGen setAll(boolean all) {
        this.isAll = all;
        return this;
    }

    public ModelGen setCreateTimeInit(String createTimeInit) {
        this.createTimeInit = createTimeInit;
        return this;
    }

    public ModelGen setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public ModelGen setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public ModelGen setDeleteTime(String deleteTime) {
        this.deleteTime = deleteTime;
        return this;
    }

    public ModelGen setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public ModelGen setBuildDao(boolean is) {
        isBuildDao = is;
        return this;
    }

    public ModelGen setForceUpdate(boolean is) {
        isForceUpdate = is;
        return this;
    }
}
