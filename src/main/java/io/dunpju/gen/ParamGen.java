package io.dunpju.gen;

import io.dunpju.stubs.ParamAddStub;
import io.dunpju.stubs.ParamEditStub;
import io.dunpju.stubs.ParamListStub;
import lombok.Data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

@Data
public class ParamGen implements IGen {
    private String outDir;
    private String outPackage;
    private String addClassName = "AddParam";
    private String editClassName = "EditParam";
    private String listClassName = "ListParam";
    private boolean shieldExistedOut;

    @Override
    public void run() throws SQLException {
        ParamAddStub paramAddStub = new ParamAddStub();
        paramAddStub.setOutPackage(this.outPackage);
        paramAddStub.setClassName(this.addClassName);
        String stub = paramAddStub.stub();
        String outClassFile = this.outDir + ModelGen.separatorChar + paramAddStub.getClassName() + ".java";
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
        ParamEditStub paramEditStub = new ParamEditStub();
        paramEditStub.setOutPackage(this.outPackage);
        paramEditStub.setClassName(this.editClassName);
        paramEditStub.setExtendsClassName(paramAddStub.getClassName());
        stub = paramEditStub.stub();
        outClassFile = this.outDir + ModelGen.separatorChar + paramEditStub.getClassName() + ".java";
        file = new File(outClassFile);
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
        ParamListStub paramListStub = new ParamListStub();
        paramListStub.setOutPackage(this.outPackage);
        paramListStub.setClassName(this.listClassName);
        stub = paramListStub.stub();
        outClassFile = this.outDir + ModelGen.separatorChar + paramListStub.getClassName() + ".java";
        file = new File(outClassFile);
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
    }
}
