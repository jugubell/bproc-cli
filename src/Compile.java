import com.sun.source.tree.Tree;

import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class Compile {

    private List<String> code;
    private TreeMap<Integer, LineType> codeLineType;
    private ProgramMetadata programMetadata;

    private final List<LineType> instr = Arrays.asList(LineType.INSTR, LineType.INSTR_I, LineType.INSTR_nI);
    private TreeMap<String, Instruction> instSet = new Globals().getInstset();
    private List<String> program = new ArrayList<>();
    private TreeMap<Integer, String> data = new TreeMap<>();

    private List<String> hexFileContent = new ArrayList<>();
    private List<String> hexFileLogisim = new ArrayList<>();
//    private file hexFile = new ArrayList<>();


    public Compile(List<String> code, TreeMap<Integer, LineType> codeLineType, ProgramMetadata programMetadata) {
        this.programMetadata = programMetadata;
        this.code = code;
        this.codeLineType = codeLineType;
        this.generateProgramMemory();
        this.generateDataMemory();
        this.generateHexFileContent();
    }

    public List<String> getLogisimHexFileContent() {
        this.compileForLogisim();
        return this.hexFileLogisim;
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

    private void generateProgramMemory() {
        for (int i = 0; i < this.code.size(); i++) {
            if(this.instr.contains(this.codeLineType.get(i))) {
                this.program.add(Integer.toHexString(this.instSet.get(this.getInstruction(this.code.get(i))).getOpCode()));
            }
        }
    }

    private void generateDataMemory() {
        for (int i = 0; i < this.code.size(); i++) {
            if(this.codeLineType.get(i) == LineType.DATA) {
                this.data.put(this.getDataAddressInt(this.code.get(i)), this.getDataData(this.code.get(i)));
            }
        }
    }

    private String getDataAddress(String data) {
        return this.trimAddress(data.split(" ")[1]);
    }

    private int getDataAddressInt(String data) {
        return Integer.decode("0x" + this.trimAddress(data.split(" ")[1]));
    }

    private String getDataData(String data) {
        return this.trimAddress(data.split(" ")[2]);
    }

    private String trimAddress(String address) {
        return address.trim().toUpperCase().replace("0X", " ").replace("H", "");
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


