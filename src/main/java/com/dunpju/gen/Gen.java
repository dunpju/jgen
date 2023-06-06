package com.dunpju.gen;

import com.dunpju.errcode.CodeGen;

public class Gen {
    public static void code(String yamlDir) {
        CodeGen code = new CodeGen();
        code.setYamlDir(yamlDir);
        code.run();
    }
}
