package io.dunpju.gen;

import io.dunpju.stubs.MapperXmlStub;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

@Setter
public class MapperXmlGen implements IGen {
    private String fileName;
    private String namespace;
    private String outDir;
    @Override
    public void run() throws SQLException {
        MapperXmlStub mapperXmlStub = new MapperXmlStub();
        mapperXmlStub.setNamespace(this.namespace);
        String stub = mapperXmlStub.stub();
        String outFile = this.outDir + "/" + fileName + ".xml";
        File file = new File(outFile);
        if (!file.exists()) {
            try {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outFile));
                bufferedWriter.write(stub);
                bufferedWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
