package io.dunpju.gen;

import io.dunpju.stubs.MapperStub;
import lombok.Data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Data
public class MapperGen implements IGen {
    private String outPackage;
    private List<String> imports;
    private String className;
    private String modelName;
    private String outDir;
    private String outMapperXmlDir;
    private boolean shieldExistedOut;
    /**
     * 继承的类, 如: org.apache.ibatis.annotations.Mapper;
     */
    private String extendsClass;

    @Override
    public void run() throws SQLException {
        this.imports.add("import " + String.format("%s", extendsClass) + ";");
        MapperStub mapperStub = new MapperStub();
        mapperStub.setOutPackage(this.outPackage);
        mapperStub.setImports(this.imports);
        mapperStub.setClassName(this.className);
        mapperStub.setModelName(this.modelName);
        String[] extendsClassSplit = extendsClass.split("\\.");
        String extendsClassName = extendsClassSplit[extendsClassSplit.length - 1];
        mapperStub.setExtendsClass(extendsClassName);
        String stub = mapperStub.stub();
        String outClassFile = this.outDir + ModelGen.separatorChar + className + ".java";
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
            System.out.println(outClassFile + " generate successful");
        } else {
            if (!shieldExistedOut) {
                System.out.println(outClassFile + " already existed");
            }
        }
        MapperXmlGen mapperXmlGen = new MapperXmlGen();
        mapperXmlGen.setFileName(className);
        mapperXmlGen.setNamespace(String.format("%s.%s", this.outPackage, className));
        mapperXmlGen.setShieldExistedOut(this.shieldExistedOut);
        if (this.outMapperXmlDir == null) {
            this.outMapperXmlDir = String.format("%s%s%s", this.outDir, ModelGen.separatorChar, "xml");
        }
        mapperXmlGen.setOutDir(this.outMapperXmlDir);
        mapperXmlGen.run();
    }
}
