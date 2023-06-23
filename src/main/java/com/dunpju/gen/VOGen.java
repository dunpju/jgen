package com.dunpju.gen;

import com.dunpju.stubs.VOStub;
import lombok.Data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

@Data
public class VOGen implements IGen{
    private String outDir;
    private String outPackage;
    private String className;
    @Override
    public void run() throws SQLException {
        VOStub voStub = new VOStub();
        voStub.setOutPackage(this.outPackage);
        voStub.setClassName(this.className);
        String stub = voStub.stub();
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
