package com.dunpju.gen;

import com.dunpju.stubs.MapperStub;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Setter
public class MapperGen implements IGen {
    private String outPackage;
    private List<String> imports;
    private String className;
    private String modelName;
    private String outDir;
    private String outMapperXmlDir;

    @Override
    public void run() throws SQLException {
        MapperStub mapperStub = new MapperStub();
        mapperStub.setOutPackage(this.outPackage);
        mapperStub.setImports(this.imports);
        mapperStub.setClassName(this.className);
        mapperStub.setModelName(this.modelName);
        String stub = mapperStub.stub();
        String outClassFile = this.outDir + "/" + className + ".java";
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
        }
        MapperXmlGen mapperXmlGen = new MapperXmlGen();
        mapperXmlGen.setFileName(className);
        mapperXmlGen.setNamespace(String.format("%s.%s", this.outPackage, className));
        if (this.outMapperXmlDir == null) {
            this.outMapperXmlDir = String.format("%s/%s", this.outDir, "xml");
        }
        mapperXmlGen.setOutDir(this.outMapperXmlDir);
        mapperXmlGen.run();
    }
}
