package com.dunpju.errcode;

import com.dunpju.gen.IGen;
import com.dunpju.stubs.CodeStub;
import lombok.Data;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class CodeGen implements IGen {
    /**
     * yaml文件目录
     */
    private String yamlDir;
    /**
     * 输出package
     */
    private String outPackage;
    /**
     * 输出目录
     */
    private String outDir;
    /**
     * 输出类
     */
    private String outClass;
    @Override
    public void run() {
        try {
            this.scan(this.yamlDir);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void scan(String dir) throws FileNotFoundException {
        File file = new File(dir);
        if (!file.exists()) {
            return;
        }
        List<String> files = new ArrayList<>();
        getFileNames(file, files);
        System.out.println(files);
        Yaml yaml = new Yaml();
        CodeStub codeStub = new CodeStub();
        codeStub.setCodeClass("ErrCode");
        for (String filename : files) {
            InputStream inputStream = new FileInputStream(filename);
            Map<String,Object> map =  yaml.load(inputStream);
            System.out.println(map);
            String regex = "(\\d+)\\.(.*)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(filename);
            if (matcher.find()) {
                String name = matcher.group(1);
                codeStub.setCodeMessage(map);
                codeStub.setCurrentCode(Long.parseLong(name));
                codeStub.buildProperty();
            }
        }
        String stub = codeStub.stub();
        System.out.println(stub);
    }

    private void getFileNames(File file, List<String> fileNames) {
        File[] files = file.listFiles();
        assert files != null;
        for (File f : files) {
            if (f.isDirectory()) {
                getFileNames(f, fileNames);
            } else {
                fileNames.add(f.getAbsolutePath());
            }
        }
    }
}
