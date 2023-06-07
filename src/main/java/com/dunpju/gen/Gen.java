package com.dunpju.gen;

import com.dunpju.enumgen.EnumGen;
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

    public static void enums(String outPackage, String input, String outDir) {
        EnumGen enumGen = new EnumGen();
        enumGen.setOutPackage(outPackage);
        enumGen.setInput(input);
        enumGen.setOutDir(outDir);
        enumGen.run();
    }
}
