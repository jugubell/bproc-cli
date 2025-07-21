import com.sun.source.tree.Tree;

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
    private List<String> data = new ArrayList<>();


    public Compile(List<String> code, TreeMap<Integer, LineType> codeLineType, ProgramMetadata programMetadata) {
        this.code = code;
        this.codeLineType = codeLineType;
        this.programMetadata = programMetadata;
        this.generateProgramMemory();
    }
// random change for BProC - Main repos sync test
//    public List<String> compileForLogisim() {
//
//        List<String> hexFile = new ArrayList<>();
//
//        int memoryIndex = 0;
//
//        // header
//        hexFile.add("v3.0 hex words addressed");
//        //program
//        if(this.programMetadata.getHasDataDeclaration()) {
//
//        }
//        for (int i = 0; i < 0xff0; i++) {
//
//            if(this.instr.contains(this.codeLineType.get(i))) {
//
//            }
//        }
//
//    }

    public void generateProgramMemory() {

        for (int i = 0; i < this.code.size(); i++) {
            if(this.instr.contains(this.codeLineType.get(i))) {
                this.program.add(Integer.toHexString(this.instSet.get(this.code.get(i)).getOpCode()));
            }
        }

    }
}


