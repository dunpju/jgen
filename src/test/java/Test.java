import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.dunpju.gen.Gen;
import com.yumi.db.system.mapper.UserMapper;
import com.yumi.db.system.service.IUserService;
import com.yumi.db.system.service.impl.UserServiceImpl;
import com.yumi.enums.EnumYesOrNo;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Types;
import java.util.Collections;
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
        Pattern patten = Pattern.compile("(\\s+)");
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
        System.out.println(EnumYesOrNo.NO.getCode());


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            FastAutoGenerator.create("jdbc:mysql://192.168.8.99:3306/test?characterEncoding=UTF-8", "root", "1qaz2wsx")
                    .globalConfig(builder -> {
                        builder.author("ffff") // 设置作者
                                .enableSwagger() // 开启 swagger 模式
                                .fileOverride() // 覆盖已生成文件
                                .outputDir("D:\\php\\jgen\\src\\test\\java"); // 指定输出目录
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
                        builder.addInclude("ts_user,ts_news") // 设置需要生成的表名
                                .addTablePrefix("ts_"); // 设置过滤表前缀
                    })
                    .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                    .execute();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
