package com.dunpju.gen;

import java.sql.SQLException;

public class Gen {
    public static void code(String yamlDir, String outPackage, String outClassFile, String importClass) {
        CodeGen code = new CodeGen();
        code.setYamlDir(yamlDir);
        code.setOutPackage(outPackage);
        code.setOutClassFile(outClassFile);
        code.setImportClass(importClass);
        code.run();
    }

    public static void enums(String outPackage, String input, String outDir) {
        EnumGen enumGen = new EnumGen();
        enumGen.setOutPackage(outPackage);
        enumGen.setInput(input);
        enumGen.setOutDir(outDir);
        enumGen.run();
    }

    public static ModelGen model() throws SQLException, ClassNotFoundException {
        return new ModelGen();
    }
}
