package com.yumi.db.system.filter;

import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.*;
import java.io.IOException;

// 参考资料：https://docs.spring.io/spring-framework/reference/core/beans/classpath-scanning.html#beans-scanning-filters
// 参考资料：https://blog.csdn.net/qq_39093474/article/details/124110591
public class MtyTypeFilter implements TypeFilter {
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        System.out.println("========Begin MtyTypeFilter===========");
        //获取当前类的注解的信息
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        System.out.println("annotationMetadata: " + annotationMetadata);
        //输出结果：annotationMetadata: com.example.test.bean.Color

        //获取当前正在扫描的类的类信息
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        System.out.println("classMetadata: " + classMetadata);
        //输出结果： classMetadata: com.example.test.bean.Color

        if (annotationMetadata.hasAnnotation(RequestMapping.class.getName())) {
            System.out.println("===有RequestMapping注解===");
            try {
                Class<?> fn = Class.forName(classMetadata.getClassName());
                // 获取类上的RequestMapping注解
                RequestMapping rm = fn.getAnnotation(RequestMapping.class);
                for (String v : rm.value()) {
                    System.out.println(v);
                }
                // 获取类方法上的RequestMapping注解
                for (MethodMetadata methodMetadata : annotationMetadata.getAnnotatedMethods(RequestMapping.class.getName())) {
                    String[] mrm = (String[]) methodMetadata.getAnnotationAttributes(RequestMapping.class.getName()).get("value");
                    for (String mv : mrm) {
                        System.out.println(mv);
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            System.out.println("===结束RequestMapping注解===");
        }


        //获取当前类资源(类的路径)
        Resource resource = metadataReader.getResource();
        System.out.println("resource: " + resource);
        //输出结果：resource: file [D:\idea\demo-02\target\classes\com\example\test\bean\Color.class]


        //获取类名
        String className = classMetadata.getClassName();
        System.out.println("className: " + className);
        //输出结果：className: com.example.test.bean.Color
        Class<?> forName = null;
        try {
            forName = Class.forName(className);
            if (Color.class.isAssignableFrom(forName)) {
                // 如果是Color的子类，就加载到IOC容器
                return true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("========End MtyTypeFilter===========");
        return false;
    }
}
