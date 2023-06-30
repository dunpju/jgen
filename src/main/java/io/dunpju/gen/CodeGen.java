package io.dunpju.gen;

import io.dunpju.stubs.CodeStub;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * 输出类文件，绝对路径
     */
    private String outClassFile;
    private String importClass;
    private List<String> files;

    @Override
    public void run() {
        try {
            this.scan(this.yamlDir);
            Yaml yaml = new Yaml();
            CodeStub codeStub = new CodeStub();
            codeStub.setOutPackage(this.outPackage);

            File file = new File(this.outClassFile);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            String fileName = file.getName();

            String regex = "(.*)\\.(java)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(fileName);
            String className = null;
            if (matcher.find()) {
                className = matcher.group(1);
            }

            codeStub.setClassName(className);
            codeStub.setCodeClass(this.importClass);
            for (String filename : this.files) {
                InputStream inputStream = new FileInputStream(filename);
                Map<String, Object> map = yaml.load(inputStream);
                regex = "(\\d+)\\.(.*)";
                pattern = Pattern.compile(regex);
                matcher = pattern.matcher(filename);
                if (matcher.find()) {
                    String name = matcher.group(1);
                    codeStub.setCodeMessage(map);
                    codeStub.setCurrentCode(Long.parseLong(name));
                    codeStub.buildProperty();
                }
            }
            String stub = codeStub.stub();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.outClassFile));
            bufferedWriter.write(stub);
            bufferedWriter.flush();
            System.out.println(this.outClassFile + " generate successful");
        } catch (IOException e) {
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
        files.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String regex = "(\\d+)\\.(.*)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher1 = pattern.matcher(o1);
                String group1 = null;
                if (matcher1.find()) {
                    group1 = matcher1.group(1);
                }
                Matcher matcher2 = pattern.matcher(o2);
                String group2 = null;
                if (matcher2.find()) {
                    group2 = matcher2.group(1);
                }
                assert group1 != null;
                assert group2 != null;
                return (int) (Long.parseLong(group1) - Long.parseLong(group2));
            }
        });

        this.files = files;
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

    public void setYamlDir(String yamlDir) {
        this.yamlDir = yamlDir;
    }

    public void setOutPackage(String outPackage) {
        this.outPackage = outPackage;
    }

    public void setOutClassFile(String outClassFile) {
        this.outClassFile = outClassFile;
    }

    public void setImportClass(String importClass) {
        this.importClass = importClass;
    }
}
