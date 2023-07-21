package io.dunpju.gen;

import io.dunpju.stubs.ControllerStub;
import io.dunpju.utils.StrUtil;
import lombok.Data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

@Data
public class ControllerGen implements IGen {
    private String outDir;
    private String outPackage;
    private String importResponse;
    private String requestMappingValue;
    private String className;
    private boolean shieldExistedOut;

    @Override
    public void run() throws SQLException {
        ControllerStub controllerStub = new ControllerStub();
        controllerStub.setOutPackage(this.outPackage);
        if (null != this.importResponse) {
            controllerStub.setImportResponseClass(this.importResponse);
        }
        if (null == this.requestMappingValue) {
            String className = this.className.replaceAll("Controller", "");
            this.requestMappingValue = "/" + StrUtil.lowerFirst(className);
        }
        controllerStub.setRequestMappingValue(this.requestMappingValue);
        controllerStub.setClassName(this.className);
        String stub = controllerStub.stub();
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
            System.out.println(outClassFile + " generate successful");
        } else {
            if (!shieldExistedOut) {
                System.out.println(outClassFile + " already existed");
            }
        }
    }

    public ControllerGen setOutDir(String outDir) {
        this.outDir = outDir;
        return this;
    }

    public ControllerGen setOutPackage(String outPackage) {
        this.outPackage = outPackage;
        return this;
    }

    public ControllerGen setImportResponse(String importResponse) {
        this.importResponse = importResponse;
        return this;
    }

    public ControllerGen setClassName(String className) {
        this.className = className;
        return this;
    }

    public ControllerGen setShieldExistedOut(boolean shieldExistedOut) {
        this.shieldExistedOut = shieldExistedOut;
        return this;
    }

    public ControllerGen setRequestMappingValue(String requestMappingValue) {
        return this;
    }
}
