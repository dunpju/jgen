import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.jdbc.DatabaseMetaDataWrapper;
import com.baomidou.mybatisplus.generator.type.TypeRegistry;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.dunpju.gen.Gen;
import com.dunpju.model.ModelGen;
import com.dunpju.stubs.ModelStub;
import com.yumi.db.system.mapper.UserMapper;
import com.yumi.db.system.service.IUserService;
import com.yumi.db.system.service.impl.UserServiceImpl;
import com.yumi.enums.EnumYesOrNo;
import org.apache.ibatis.type.JdbcType;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        /*Gen.code("D:\\php\\jgen\\src\\test\\java\\ymal",
                "com.yumi.jerrcode",
                "D:\\php\\jgen\\src\\test\\java\\com\\yumi\\jerrcode\\ErrCode.java",
                "com.dunpju.errcode.Code");
        System.out.println("ddd");
        System.out.println(ErrCode.SUCCESS.getCode());
        System.out.println(ErrCode.SUCCESS.getMessage());*/


        /*Pattern patten = Pattern.compile("(\\s+)");
        String str = "name=yes_or_no   flag=是否:no-1-否,yes-2-是";
        System.out.println(str);
        Matcher matcher = patten.matcher(str);
        if (matcher.find()) {
            System.out.println(matcher.group());
            str = matcher.replaceAll(" ");
        }
        System.out.println(str);

        Gen.enums("com.yumi.enums",
                "name=yes_or_no   flag=是否:no-1-否,yes-2-是",
                "D:\\php\\jgen\\src\\test\\java\\com\\yumi\\enums");
        System.out.println(EnumYesOrNo.NO.getCode());*/


        // https://blog.csdn.net/weixin_42629433/article/details/124151645
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder("jdbc:mysql://192.168.8.99:3306/test?characterEncoding=UTF-8", "root", "1qaz2wsx").build();
            System.out.println(dataSourceConfig.getConn().getSchema());
            DatabaseMetaDataWrapper databaseMetaDataWrapper = new DatabaseMetaDataWrapper(dataSourceConfig.getConn(), dataSourceConfig.getSchemaName());
            DatabaseMetaData metaData = dataSourceConfig.getConn().getMetaData();

            PackageConfig packageInfo = new PackageConfig.Builder().build();
            StrategyConfig strategy = new StrategyConfig.Builder().build();
            TemplateConfig template = new TemplateConfig.Builder().build();
            GlobalConfig globalConfig = new GlobalConfig.Builder().build();
            InjectionConfig injection = new InjectionConfig();

            TypeRegistry typeRegistry = new TypeRegistry(globalConfig);

            ConfigBuilder configBuilder = new ConfigBuilder(packageInfo, dataSourceConfig, strategy, template, globalConfig, injection);

            ResultSet tableResultSet = metaData.getTables("test", "test", null, new String[]{"TABLE"});
            while (tableResultSet.next()) {
                String tableName = tableResultSet.getString("TABLE_NAME");
                Map<String, DatabaseMetaDataWrapper.Column> columnsInfo = databaseMetaDataWrapper.getColumnsInfo("test", "test", tableName,true);
                System.out.println("=============");
                DatabaseMetaDataWrapper.Table table = databaseMetaDataWrapper.getTableInfo(tableName);

                System.out.println(tableName);
                /*for (String key : columnsInfo.keySet()) {
                    System.out.println(columnsInfo.get(key).getName());
                    System.out.println(columnsInfo.get(key).getJdbcType());
                    System.out.println(columnsInfo.get(key).isPrimaryKey());
                    System.out.println(columnsInfo.get(key).getRemarks());
                    System.out.println(columnsInfo.get(key).isAutoIncrement());
                    System.out.println(columnsInfo.get(key).getDefaultValue());

                    // 设置字段的元数据信息
                    TableInfo tableInfo = new TableInfo(configBuilder, tableName);
                    TableField.MetaInfo metaInfo = new TableField.MetaInfo(columnsInfo.get(key), tableInfo);
                    IColumnType iColumnType = typeRegistry.getColumnType(metaInfo);
                    System.out.println(iColumnType.getType());
                    System.out.println(iColumnType.getPkg());
                    System.out.println();
                }*/

                ModelGen modelGen = new ModelGen();
                modelGen.setOutPackage("com.yumi.db.system.model");
                modelGen.setOutDir("E:\\share\\jgen\\src\\test\\java\\com\\yumi\\db\\system\\model");
                modelGen.setTableName(tableName);
                modelGen.setTableRemarks(table.getRemarks());
                modelGen.setTablePrefix("ts_");
                modelGen.setColumnsInfo(columnsInfo);
                modelGen.setConfigBuilder(configBuilder);
                modelGen.setTypeRegistry(typeRegistry);
                modelGen.run();
            }

            /*Map<String, DatabaseMetaDataWrapper.Column> columnsInfo = databaseMetaDataWrapper.getColumnsInfo("test", "test", "users",true);
            Assertions.assertNotNull(columnsInfo);
            DatabaseMetaDataWrapper.Column name = columnsInfo.get("user_name");
            Assertions.assertTrue(name.isNullable());
            System.out.println("=============");
            for (String key : columnsInfo.keySet()) {
                System.out.println(columnsInfo.get(key).getName());
                System.out.println(columnsInfo.get(key).getJdbcType());
                System.out.println(columnsInfo.get(key).isPrimaryKey());
                System.out.println(columnsInfo.get(key).getRemarks());
                System.out.println(columnsInfo.get(key).isAutoIncrement());
                System.out.println(columnsInfo.get(key).getDefaultValue());
                System.out.println(columnsInfo.get(key).getScale());
            }
            Assertions.assertEquals(JdbcType.VARCHAR, name.getJdbcType());
            Assertions.assertEquals(Integer.MAX_VALUE, name.getLength());*/

            /*FastAutoGenerator.create("jdbc:mysql://192.168.8.99:3306/test?characterEncoding=UTF-8", "root", "1qaz2wsx")
                    .globalConfig(builder -> {
                        builder.author("ffff") // 设置作者
                                .enableSwagger() // 开启 swagger 模式
                                .fileOverride() // 覆盖已生成文件
//                                .outputDir("D:\\php\\jgen\\src\\test\\java"); // 指定输出目录
                                .outputDir("E:\\share\\jgen\\src\\test\\java"); // 指定输出目录
                    })
                    .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                        int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                        if (typeCode == Types.SMALLINT) {
                            // 自定义类型转换
                            return DbColumnType.INTEGER;
                        }
                        return typeRegistry.getColumnType(metaInfo);

                    }))
                    .packageConfig(builder -> {
                        builder.parent("com.yumi.db") // 设置父包名
                                .moduleName("system"); // 设置父包模块名
//                                .pathInfo(Collections.singletonMap(OutputFile.xml, "D:\\php\\jgen\\src\\test\\java\\com\\yumi\\db")); // 设置mapperXml生成路径
                    })
                    .strategyConfig(builder -> {
                        builder.addInclude("users") // 设置需要生成的表名
                                .addTablePrefix("ts_"); // 设置过滤表前缀
                    })
                    .templateEngine(new FreemarkerTemplateEngine()); // 使用Freemarker引擎模板，默认的是Velocity引擎模板
//                    .execute();*/
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
