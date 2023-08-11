package io.dunpju.gen;

import java.sql.SQLException;

public class Gen {
    public static CodeGen code() {
        return new CodeGen();
    }

    public static void enums(String outPackage, String input, String outDir) {
        EnumGen enumGen = new EnumGen();
        enumGen.setOutPackage(outPackage);
        enumGen.setInput(input);
        enumGen.setOutDir(outDir);
        enumGen.run();
    }

    public static EnumGen enums() {
        return new EnumGen();
    }

    public static ModelGen model() throws SQLException, ClassNotFoundException {
        return new ModelGen();
    }

    public static ControllerGen controller() {
        return new ControllerGen();
    }
}
