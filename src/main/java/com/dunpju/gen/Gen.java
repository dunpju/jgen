package com.dunpju.gen;

import com.dunpju.errcode.CodeGen;

public class Gen {
    public static void code(String yamlDir, String outPackage, String outClassFile, String importClass) {
        CodeGen code = new CodeGen();
        code.setYamlDir(yamlDir);
        code.setOutPackage(outPackage);
        code.setOutClassFile(outClassFile);
        code.setImportClass(importClass);
        code.run();
    }
}
