package io.dunpju.gen;

import io.dunpju.stubs.IServiceStub;
import lombok.Data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Data
public class IServiceGen implements IGen {
    private String outDir;
    private String outPackage;
    private List<String> imports;
    private String className;
    private String modelName;
    @Override
    public void run() throws SQLException {
        IServiceStub iServiceStub = new IServiceStub();
        iServiceStub.setOutPackage(this.outPackage);
        iServiceStub.setImports(this.imports);
        iServiceStub.setClassName(this.className);
        iServiceStub.setModelName(this.modelName);
        String stub = iServiceStub.stub();
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
    }
}
