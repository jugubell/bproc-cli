import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/**
 * Compile utility
 * <p>It compiles the assembler code into binary/hexadecimal</p>
 */
public class Compile {

    private List<String> code;
    private TreeMap<Integer, LineType> codeLineType;
    private ProgramMetadata programMetadata;

    private final List<LineType> instr = Arrays.asList(LineType.INSTR, LineType.INSTR_I, LineType.INSTR_nI, LineType.JUMP);
    private TreeMap<String, Instruction> instSet = new Globals().getInstset();
    private List<String> program = new ArrayList<>();
    private TreeMap<Integer, String> data = new TreeMap<>();
    private TreeMap<String, Integer> labeIndex = new TreeMap<>();

    private List<String> hexFileContent = new ArrayList<>();
    private List<String> hexFileLogisim = new ArrayList<>();
    private List<String> hexFileVhdl = new ArrayList<>();
    private List<String> memFile = new ArrayList<>();

    public Compile(List<String> code, TreeMap<Integer, LineType> codeLineType, ProgramMetadata programMetadata) {
        this.programMetadata = programMetadata;
        this.code = code;
        this.codeLineType = codeLineType;
        this.generateProgramMemory();
        this.generateDataMemory();
        this.generateHexFileContent();
    }

    // Getters
    public List<String> getHexFileLogisim() {
        this.compileForLogisim();
        return this.hexFileLogisim;
    }

    public List<String> getHexFileVhdl() {
        this.compileForVhdl();
        return this.hexFileVhdl;
    }

    /**
     * Compiles into hexadecimal or binary according to isHex
     * then return the file
     * @param isHex
     * @return the bin or hex file as <code>List</code>
     */
    public List<String> getMemFile(boolean isHex) {
        this.compileForMemFile(isHex);
        return this.memFile;
    }

    public TreeMap<Integer, LineType> getCodeLineType() {
        return this.codeLineType;
    }

    private void generateHexFileContent() {

        List<String> hexFile = new ArrayList<>();

        int endOfProgram = 0;

        // is there .data declaration
        if(this.programMetadata.getHasDataDeclaration()) {
            endOfProgram = this.programMetadata.getFirstDataMemory()-1;
        } else {
            endOfProgram = 4095;
        }

        // write program
        for (int i = 0; i < endOfProgram + 1; i++) {
            if(i < this.programMetadata.getProgramMemoryUsage()) {
                hexFile.add(this.program.get(i));
            } else {
                hexFile.add("0000");
            }
        }

        // write data
        if(this.programMetadata.getHasDataDeclaration()) {
            for (int i = endOfProgram+1; i < 4096; i++) {
                hexFile.add(this.data.getOrDefault(i, "0000"));
            }
        }

        this.hexFileContent.clear();
        this.hexFileContent = hexFile;
    }

    private void compileForLogisim() {
        List<String> hexFile = new ArrayList<>();
        String[] line = new String[16];
        // header
        hexFile.add("v3.0 hex words addressed");

        for (int i = 0; i < 0xfff; i += 16) {
            for (int j = 0; j < 16; j++) {
                line[j] = this.hexFileContent.get(i+j);
            }
            hexFile.add(String.format("%03x", i) + ": " + String.join(" ", line));
        }

        this.hexFileLogisim.clear();
        this.hexFileLogisim = hexFile;
    }

    private void compileForVhdl() {
        List<String> hexFile = new ArrayList<>();
        String hexCode = "";

        hexFile.add("-- program RAM");
        hexFile.add("signal RAM: RAM_ARRAY_16b (0 to 4095) := (");
        for (int i = 0; i < this.hexFileContent.size(); i++) {
            hexCode = this.hexFileContent.get(i);
            if(!hexCode.equals("0000")) {
                hexFile.add("    " + i + " => x\"" + hexCode.toUpperCase() + "\",");
            }
        }

        hexFile.add("    others => x\"0000\"");
        hexFile.add(");");

        this.hexFileVhdl.clear();
        this.hexFileVhdl = hexFile;
    }

    private void compileForMemFile(boolean isHex) {
        this.memFile.clear();
        if(isHex) {
            this.memFile.addAll(this.hexFileContent);
        } else {
            this.memFile.addAll(Utils.hexFile2binFile(this.hexFileContent));
        }
    }

    private void generateLabelIndex() {
        int programIndex = 0;
        TreeMap<String, Integer> labelIndex = new TreeMap<>();
        for (int i = 0; i < this.code.size(); i++) {
            if(this.codeLineType.get(i) == LineType.LABEL || this.codeLineType.get(i) == LineType.START) {
                labelIndex.put(Utils.cleanLabel(this.code.get(i)), programIndex);
            }

            if(this.instr.contains(this.codeLineType.get(i))) {
                programIndex++;
            }

        }
        this.labeIndex.clear();
        this.labeIndex = labelIndex;
    }

    private void generateProgramMemory() {
        this.generateLabelIndex();
        for (int i = 0; i < this.code.size(); i++) {
            if(this.instr.contains(this.codeLineType.get(i))) {
                String line = this.code.get(i);
                Integer opCode = this.instSet.get(this.getInstruction(line)).getOpCode();
                boolean hasOperand = this.instSet.get(this.getInstruction(line)).getHasOperand();
                if(this.codeLineType.get(i) == LineType.INSTR_I) {
                    opCode = opCode + 0x8000;
                }
                if(hasOperand) {
                    opCode = opCode + Utils.getDataAddressInt(line);
                }
                if(this.codeLineType.get(i) == LineType.JUMP) {
                    opCode = this.instSet.get("BIN").getOpCode() + this.labeIndex.get(Utils.getLabelFromJmp(this.code.get(i)));
                }
                this.program.add(Integer.toHexString(opCode));
            }
        }
    }

    private void generateDataMemory() {
        for (int i = 0; i < this.code.size(); i++) {
            if(this.codeLineType.get(i) == LineType.DATA) {
                this.data.put(Utils.getDataAddressInt(this.code.get(i)), Utils.getDataData(this.code.get(i)));
            }
        }
    }

    private String getInstruction(String line) {
        String instr = line.split(" ")[0];
        if(!instr.isEmpty() && this.instSet.containsKey(instr)) {
            return instr;
        } else {
            return "";
        }
    }

}


