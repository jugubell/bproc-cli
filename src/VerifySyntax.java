import javax.sound.sampled.Line;
import java.util.*;

/**
 * The class handling the syntax verification of the code.
 */
public class VerifySyntax {
    Globals glb = new Globals();
    TreeMap<String, Instruction> instSet = glb.getInstset();
    private TreeMap<Integer, LineType> codeLineType = new TreeMap<>();
    private ProgramMetadata programMetadata = new ProgramMetadata();
    private List<String> code;
    private List<String> labels = new ArrayList<>();

    /**
     * Constructor of {@link VerifySyntax}
     * @param code a trimmed code from any comment or extra spaces as <code>List</code> of <code>String</code>
     */
    public VerifySyntax(List<String> code) {
        this.code = code;
    }

    // Getters

    /**
     * Getting the calculated program metadata.
     * @return program metadata as {@link ProgramMetadata} object.
     */
    public ProgramMetadata getProgramMetadata() {
        return this.programMetadata;
    }

    public List<String> getCode() {
        return this.code;
    }

    public TreeMap<Integer, LineType> getCodeLineType() {
        return this.codeLineType;
    }

    /**
     * Method to verify the syntax correctness.
     * It logs program metadata and Syntax OK, if true
     * It logs error, place of the error, type of the error if not.
     * It generates line type in a <code>TreeMap</code> with the same size as the {@link #code}
     * and stores it in {@link #codeLineType}.
     * The code is inspected line by line, and checks each line the corresponding line type
     * with the method {@link #whatLine(String[])}.
     * Checks for allowed data range declaration.
     * Checks for program and data ranges overlapping.
     * Checks for label start syntax requirement
     * Checks for duplicate labels
     * Checks for duplicate data declaration
     * Checks for general syntax guidelines.
     * If there is an error, it will be aborted, and returns false, with error logs.
     * @return boolean true if syntax is correct
     */
    public boolean isSyntaxCorrect() {
        int startIndex = -1;
        int lastDataIndex = -1;

        // retrieving every code line type
        for(int i=0 ; i < this.code.size() ; i++) {
            String[] lineArray = this.code.get(i).split(" ");
            LineType lineType = this.whatLine(lineArray);

            // stores the start index
            if(lineType == LineType.START)
                startIndex = i;

            // stores the last data index
            if(lineType == LineType.DATA && i > lastDataIndex)
                lastDataIndex = i;

            // checks if whatLine returned a syntax error
            if(lineType == LineType.SYNTAX_ERROR) {
                this.codeLineType.clear();
                Log.error("[ERROR] Syntax error at line " + (i + 1) + ".");
                return false;
            }

            // writing the correspondant data line type
            this.codeLineType.put(i, lineType);
        }

        // generated a list of labels
        this.fillLabelsList();

        // checking if JMP instruction is pointing to a wrong label, abort if it is.
        if(!this.labels.isEmpty()) {
            for (int i = 0; i < this.codeLineType.size(); i++) {
                if(this.codeLineType.get(i) == LineType.JUMP) {
                    if(!this.labels.contains(Utils.getLabelFromJmp(this.code.get(i)))) {
                        this.codeLineType.clear();
                        Log.error("[ERROR] Syntax error at line " + (i + 1) + ". The label doesn't exist.");
                        return false;
                    }
                }
            }
        }

        // checks if start: label is present when data declaration is made
        if((startIndex == -1) && (lastDataIndex != -1)) {
            this.codeLineType.clear();
            Log.error("[ERROR] Data declared but 'start:' label is not found");
            return false;
        }

        // checks if the start: label is written after data declaration
        if(lastDataIndex > startIndex) {
            this.codeLineType.clear();
            Log.error("[ERROR] Data is declared after 'start:' label at line: " + (lastDataIndex + 1) + ". The 'start:' laebl is at line: " + (startIndex + 1));
            return false;
        }

        // some local data for handling syntax
        int duplicateStart = this.searchDuplicates(LineType.START);
        int duplicateLabel = this.searchDuplicates(LineType.LABEL);
        int duplicateData = this.searchDuplicates(LineType.DATA);
        int[] firstMemory = this.firstDataMemory();
        int programMemoryUsage = this.programMemoryUsage();
        int dataMemoryUsage = this.dataMemoryUsage();
        float totalUsed = (Math.round(((float) (programMemoryUsage + dataMemoryUsage) / 4096) * 1000f)) / 1000f;

        // checks if there is a startL label duplication
        if(duplicateStart > -1) {
            this.codeLineType.clear();
            Log.error("[ERROR] Duplicate start: label at " + (duplicateStart) + ".");
            return false;
        }

        // check if there is a duplicated data
        if(duplicateLabel > -1) {
            this.codeLineType.clear();
            Log.error("[ERROR] Duplicate label at " + (duplicateLabel) + ".");
            return false;
        }

        // checks if there is any duplicated data declaration
        if(duplicateData > -1) {
            this.codeLineType.clear();
            Log.error("[ERROR] Duplicate data declaration at " + (duplicateData) + ".");
            return false;
        }

        // checks if data is declared in the allowed range
        if(firstMemory[0] != -1 &&  firstMemory[0] < 16) {
            this.codeLineType.clear();
            Log.error("[ERROR] Data declaration is not allowed in this range [000h - 00F] at line " + firstMemory[1] + ".");
            return false;
        }

        // checks if the program exceeds the memory capacity in program range
        if(firstMemory[0] == -1 && programMemoryUsage > 4096) {
            this.codeLineType.clear();
            Log.info("[INFO] Program Memory Usage: " + programMemoryUsage + " bytes");
            Log.info("[INFO] Data Memory Usage: 0 byte");
            Log.info("[INFO] Total used: " + (programMemoryUsage/4096) + "%");
            Log.error("[ERROR] Your program exceeds RAM capacity.");
            return false;

        // checks if there is an overlap of program and data ranges
        } else if (firstMemory[0] != -1 && programMemoryUsage > firstMemory[0]) {
            this.codeLineType.clear();
            Log.info("[INFO] Program Memory Usage: " + programMemoryUsage + " bytes");
            Log.info("[INFO] Data Memory Usage: " + dataMemoryUsage + " byte");
            Log.info("[INFO] Total used: " + totalUsed + "%");
            Log.error("[ERROR] Your program is overlapping the data declaration range. Please use a higher address memory for data declaration.");
            return false;
        }

        // if the method is not aborted, Logs the program memory usage and Syntax OK
        Log.info("[INFO] Program Memory Usage: " + programMemoryUsage + " bytes");
        Log.info("[INFO] Data Memory Usage: " + dataMemoryUsage + " byte");
        Log.info("[INFO] Total used: " + totalUsed + "%");
        Log.info("[INFO] Syntax correct.");

        // set the program metadata and return true
        this.setProgramMetadata();
        return true;
    }

    /**
     * Checks if the line is a label
     * @param label the code line as <code>String</code> array
     * @return a {@link LineType} as <code>LABEL</code> if true, otherwise <code>SYNTAX_ERROR</code>
     */
    public LineType isLabel(String[] label) {
        String labelStr = String.join(" ", label);
        int indexOfColumn = label[0].indexOf(":");
        if(!labelStr.isEmpty() && indexOfColumn != -1) {
            if(!String.join(" ", label).isEmpty() && !this.instSet.containsKey(label[0].replace(":", "")) && labelStr.substring(indexOfColumn+1).trim().isEmpty()) {
                return LineType.LABEL;
            } else {
                return LineType.SYNTAX_ERROR;
            }
        } else {
            return LineType.SYNTAX_ERROR;
        }
    }

    /**
     * Checks if the line is the label start:
     * @param str the code line as a <code>String</code> array
     * @return a {@link LineType} <code>START</code>, otherwise <code>SYNTAX_ERROR</code>
     */
    public LineType isStart(String[] str) {
        if(this.isLabel(str) == LineType.LABEL) {
            if(str[0].equals("START:"))
                return LineType.START;
            else
                return LineType.SYNTAX_ERROR;
        } else {
            return LineType.SYNTAX_ERROR;
        }
    }

    /**
     * Checks if it is a data declaration
     * @param str a code line as <code>String</code> array
     * @return a {@link LineType} <code>DATA</code>, otherwise <code>SYNTAX_ERROR</code>
     */
    private LineType isData(String[] str) {
        if(str.length == 3) {
            if(str[0].equals(".DATA") && Utils.is12bitsHexData(str[1]) && Utils.is16bitsHexData(str[2])) {
                return LineType.DATA;
            } else {
                return LineType.SYNTAX_ERROR;
            }
        } else {
            return LineType.SYNTAX_ERROR;
        }
    }

    /**
     * Returns what type of line of code is.
     * @param str a code line as a <code>String</code> array
     * @return a {@link LineType} according to the line type, otherwise <code>SYNTAX_ERROR</code>
     */
    public LineType whatLine(String[] str) {
        // convert the array into a String
        String strStr = String.join(" ", str);
        // checks if it is an empty line
        if(!strStr.isEmpty()) {
            // checks if it is a label
            if (this.isLabel(str) == LineType.LABEL) {
                // checks if the label is a start:
                if(this.isStart(str) == LineType.START) {
                    return LineType.START;
                } else {
                    return LineType.LABEL;
                }

            // checks if it is a dara declaration
            } else if(this.isData(str) == LineType.DATA) {
                return LineType.DATA;

            // handles others cases
            } else {
                // checks if the first word is in the instruction set
                if (this.instSet.containsKey(str[0])) {
                    // checks if it has 2 keywords and the first keyword is an instruction waiting for an operand
                    if (str.length == 2 && this.instSet.get(str[0]).getHasOperand()) {
                        // checks if the operand is formatted as direct address (?check Utils)
                        if (str[1].startsWith("0x") ^ str[1].endsWith("H")) {
                            // Direct Instruction
                            String hexCode = str[1].replace("0x", "").replace("H", "");
                            // checks if the address is a valid hexadecimal number
                            if (Utils.isValidHexNumber(hexCode)) {
                                return LineType.INSTR_nI;
                            } else {
                                return LineType.SYNTAX_ERROR;
                            }

                        // checks if the operand is formatted as an indirect address (?check Utils)
                        } else if ((str[1].startsWith("[0x") && str[1].endsWith("]")) ^ (str[1].startsWith("[") && str[1].endsWith("H]"))) {
                            // Indirect Instruction
                            String hexCode = str[1].replace("0x", "").replace("H", "").replace("[", "").replace("]", "");
                            // checks if the address is a valid hexadecimal number
                            if (Utils.isValidHexNumber(hexCode)) {
                                return LineType.INSTR_I;
                            } else {
                                return LineType.SYNTAX_ERROR;
                            }
                        } else {
                            return LineType.SYNTAX_ERROR;
                        }

                    // checks if the instruction is a JMP
                    } else if(this.isJump(str)) {
                        // JUMP instruction
                        return LineType.JUMP;
                    } else if (str.length == 1 && !this.instSet.get(str[0]).getHasOperand()) {
                        // Other instructions (no operand)
                        return LineType.INSTR;
                    } else {
                        return LineType.SYNTAX_ERROR;
                    }
                } else {
                    return LineType.SYNTAX_ERROR;
                }
            }
        } else {
            return LineType.EL;
        }
    }

    /**
     * Searches for duplication in the data declaration or duplicated labels
     * @param lineType the line type as {@link LineType}, may be LABEL or DATA
     * @return the duplicated data line index as an <code>int</code>. If no duplicate found, returns -1
     */
    private int searchDuplicates(LineType lineType) {

        // check if the type is allowed to check duplication
        if(Arrays.asList(LineType.DATA, LineType.LABEL, LineType.START).contains(lineType)) {
            int count = 0;
            List<String> dataList = new ArrayList<>();
            String firstItem = "";

            for (int i = 0; i < this.code.size(); i++) {
                if(this.codeLineType.get(i) == lineType) {
                    // process for DATA
                    switch (lineType) {
                        // Processing for DATA
                        case DATA:
                            // getting the duplication criteria for data : .data <add>
                            String[] dataArr = this.code.get(i).split(" ");
                            String data = dataArr[0] + " " + dataArr[1];

                            // checking if the data criteria already exists
                            if(dataList.contains(data)) {
                                return i+1;
                            } else {
                                dataList.add(data);
                            }
                            break;

                        // Processing for LABEL and START
                        case LABEL:
                        case START:
                            switch (count) {
                                // no occurrence process
                                case 0:
                                    // set the first occurrence in firstItem, and increment count
                                    firstItem = this.code.get(i);
                                    count++;
                                    break;

                                // occurred once process
                                case 1:
                                    // checks if the corresponding code line matches the first occurrence
                                    if(firstItem.equals(this.code.get(i))) {
                                        return i+1;
                                    }
                                    break;
                            }
                            break;
                    }
                }
            }

        }

        // no duplication or LineType not allowed for duplication check
        return -1;
    }

    /**
     * Returns the address of the first data declaration in <code>int</code>
     * with its corresponding line number in code.
     * The method support the use of data declaration in an unordered address.
     * @return an array of 2 <code>int</code> elements containing
     * in <code>[0]</code> the first data declaration address
     * in <code>[1]</code> its corresponding line number in code.
     */
    private int[] firstDataMemory() {

        int firstData = -1;
        int currentData;
        int lineNumber = -1;

        for (int i = 0; i < this.code.size(); i++) {
            // processing only data declaration lines
            if(this.codeLineType.get(i) == LineType.DATA) {
                // set the current data for comparaison
                currentData = Integer.parseInt(Utils.getDataAddress(this.code.get(i)), 16);

                // compare if the first data address is not yet set OR if current is less than the first
                if(firstData == -1 || currentData < firstData) {
                    // override the first address with the current, and set its line number
                    firstData = currentData;
                    lineNumber = i;
                }
            }
        }

        return new int[]{firstData, lineNumber + 1};
    }

    /**
     * Returning the program memory usage by counting how many instructions is used in the program
     * Counted line types are INSTR, INSTR_I, INSTR_nI, JUMP
     * @return memory usage as <code>int</code>.
     */
    private int programMemoryUsage() {
        int memoryCount = 0;

        for (int i = 0; i < this.code.size(); i++) {
            // current line type and countable line types
            LineType currentLineType = this.codeLineType.get(i);
            List<LineType> memoryInstructions = Arrays.asList(LineType.INSTR, LineType.INSTR_I, LineType.INSTR_nI, LineType.JUMP);

            // count if the line type corresponds
            if(memoryInstructions.contains(currentLineType)) {
                memoryCount++;
            }
        }

        return memoryCount;
    }

    /**
     * Returning data memory usage by counting how many data declarations are made
     * @return data memory usage as <code>int</code>.
     */
    private int dataMemoryUsage() {
        int memoryCount = 0;

        for (int i = 0; i < this.code.size(); i++) {
            LineType currentLineType = this.codeLineType.get(i);
            // count if the line type is data declaration
            if(currentLineType == LineType.DATA) {
                memoryCount++;
            }
        }

        return memoryCount;
    }

    /**
     * Set the program metadata
     * Sets:
     * <ul>
     *     <li>Program memory usage</li>
     *     <li>Data memory usage</li>
     *     <li>First data memory address</li>
     *     <li>Program address range</li>
     *     <li>Data address range</li>
     *     <li>If there is a data declaration</li>
     * </ul>
     */
    private void setProgramMetadata() {
        this.programMetadata.setDataMemoryUsage(this.dataMemoryUsage());
        this.programMetadata.setProgramMemoryUsage(this.programMemoryUsage());
        this.programMetadata.setFirstDataMemory(this.firstDataMemory()[0]);
        this.programMetadata.setDataDataRange(new DataRange(this.firstDataMemory()[0], 4095));
        this.programMetadata.setProgramDataRange(new DataRange(0, this.programMemoryUsage()));
        this.programMetadata.setHasDataDeclaration(this.hasDataDeclaration());
    }

    /**
     * Checking if code has data declaration: <code>.data</code> syntax.
     * @return boolean true if there is data declaration
     */
    private boolean hasDataDeclaration() {
        for (int i = 0; i < this.codeLineType.size(); i++) {
            if(this.codeLineType.get(i) == LineType.DATA) {
                return true;
            }
        }
        return false;
    }

    /**
     * Fills the <code>List</code> of labels used in the code.
     * It writes on {@link #labels} field.
     */
    private void fillLabelsList() {
        // checks if there is items in the list line type
        if(!this.codeLineType.isEmpty()) {
            for (int i = 0; i < this.codeLineType.size(); i++) {
                // add labels and 'start:' label
                if(this.codeLineType.get(i) == LineType.LABEL || this.codeLineType.get(i) == LineType.START) {
                    this.labels.add(Utils.cleanLabel(this.code.get(i)));
                }
            }
        }
    }

    /**
     * Checks if a line provided as an <code>Array</code> is a JMP instruction
     * @param str the code line as <code>Array</code>
     * @return boolean true if the code line is a JMP instruction.
     */
    private boolean isJump(String[] str) {
        if(str.length == 2) {
            return str[0].equals("JMP");
        } else {
            return false;
        }
    }
}
