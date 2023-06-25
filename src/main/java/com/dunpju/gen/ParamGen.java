package com.dunpju.gen;

import com.dunpju.stubs.ParamAddStub;
import com.dunpju.stubs.ParamEditStub;
import com.dunpju.stubs.ParamListStub;
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

    @Override
    public void run() throws SQLException {
        ParamAddStub paramAddStub = new ParamAddStub();
        paramAddStub.setOutPackage(this.outPackage);
        paramAddStub.setClassName(this.addClassName);
        String stub = paramAddStub.stub();
        String outClassFile = this.outDir + "/" + paramAddStub.getClassName() + ".java";
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
        ParamEditStub paramEditStub = new ParamEditStub();
        paramEditStub.setOutPackage(this.outPackage);
        paramEditStub.setClassName(this.editClassName);
        paramEditStub.setExtendsClassName(paramAddStub.getClassName());
        stub = paramEditStub.stub();
        outClassFile = this.outDir + "/" + paramEditStub.getClassName() + ".java";
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
        }
        ParamListStub paramListStub = new ParamListStub();
        paramListStub.setOutPackage(this.outPackage);
        paramListStub.setClassName(this.listClassName);
        stub = paramListStub.stub();
        outClassFile = this.outDir + "/" + paramListStub.getClassName() + ".java";
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
        }
    }
}
