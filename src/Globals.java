import java.util.ArrayList;
import java.util.TreeMap;

public class Globals {
    private String assemblyFilePath = "";
    private String logisimHexFilePath = "";
    private String memFilePath = "";
    private final TreeMap<String, Instruction> INSTSET = new TreeMap<>();

    public Globals() {
        this.INSTSET.put("AND", new Instruction("AND", true, 0x0000));
        this.INSTSET.put("ADD", new Instruction("ADD", true, 0x1000));
        this.INSTSET.put("LDA", new Instruction("LDA", true, 0x2000));
        this.INSTSET.put("STA", new Instruction("STA", true, 0x3000));
        this.INSTSET.put("BIN", new Instruction("BIN", true, 0x4000));
        this.INSTSET.put("BSA", new Instruction("BSA", true, 0x5000));
        this.INSTSET.put("ISZ", new Instruction("ISZ", true, 0x6000));
        this.INSTSET.put("CLA", new Instruction("CLA", false, 0x7800));
        this.INSTSET.put("CLE", new Instruction("CLE", false, 0x7400));
        this.INSTSET.put("CMA", new Instruction("CMA", false, 0x7200));
        this.INSTSET.put("CME", new Instruction("CME", false, 0x7100));
        this.INSTSET.put("CIR", new Instruction("CIR", false, 0x7080));
        this.INSTSET.put("CIL", new Instruction("CIL", false, 0x7040));
        this.INSTSET.put("INC", new Instruction("INC", false, 0x7020));
        this.INSTSET.put("SPA", new Instruction("SPA", false, 0x7010));
        this.INSTSET.put("SNA", new Instruction("SNA", false, 0x7008));
        this.INSTSET.put("SZA", new Instruction("SZA", false, 0x7004));
        this.INSTSET.put("SZE", new Instruction("SZE", false, 0x7002));
        this.INSTSET.put("HLT", new Instruction("HLT", false, 0x7001));
        this.INSTSET.put("INP", new Instruction("INP", false, 0xF800));
        this.INSTSET.put("OUT", new Instruction("OUT", false, 0xF400));
        this.INSTSET.put("SFI", new Instruction("SFI", false, 0xF200));
        this.INSTSET.put("SFO", new Instruction("SFO", false, 0xF100));
        this.INSTSET.put("LDI", new Instruction("LDI", false, 0xF080));
        this.INSTSET.put("JMP", new Instruction("LDI", false, 0x0000, false));
    }

    public void setAssemblyFilePath(String filePath) {
        this.assemblyFilePath = filePath;
    }
    public void setLogisimHexFilePath(String filePath) { this.logisimHexFilePath = filePath; }
    public void setMemFilePath(String filePath) { this.memFilePath = filePath; }

    public String getAssemblyFilePath() {
        return this.assemblyFilePath;
    }

    public String getLogisimHexFilePath() {
        return this.logisimHexFilePath;
    }

    public String getMemFilePath() {
        return this.memFilePath;
    }

    public TreeMap<String, Instruction> getInstset() {
        return this.INSTSET;
    }


}
