package io.dunpju.gen;

import io.dunpju.stubs.EnumStub;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;

public class EnumGen implements IGen {
    private final String separator = "" + ModelGen.separatorChar;
    /**
     * 输出package
     */
    private String outPackage;
    /**
     * 输出目录，绝对路径
     */
    private String outDir;
    private String input;

    @Override
    public void run() {
        try {
            EnumStub enumStub = new EnumStub();
            enumStub.setInput(this.input);
            enumStub.setOutPackage(this.outPackage);
            String stub = enumStub.stub();
            String outClassFile = this.outDir + ModelGen.separatorChar + enumStub.getClassName() + ".java";
            File file = new File(outClassFile);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outClassFile));
                bufferedWriter.write(stub);
                bufferedWriter.flush();
                System.out.println(outClassFile + " generate successful");
            } else {
                System.out.println(outClassFile + " already existed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public EnumGen setOutPackage(String outPackage) {
        this.outPackage = outPackage;
        return this;
    }

    public EnumGen setOutDir(String outDir) {
        if (outDir.contains("\\")) {
            outDir = outDir.replaceAll("\\\\", Matcher.quoteReplacement(this.separator));
        } else if (outDir.contains("/")){
            outDir = outDir.replaceAll("/", Matcher.quoteReplacement(this.separator));
        }
        this.outDir = outDir;
        return this;
    }

    public EnumGen setInput(String input) {
        this.input = input;
        return this;
    }
}
