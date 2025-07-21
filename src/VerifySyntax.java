import javax.sound.sampled.Line;
import java.util.*;

public class VerifySyntax {
    Globals glb = new Globals();
    TreeMap<String, Instruction> instSet = glb.getInstset();
    private TreeMap<Integer, LineType> codeLineType = new TreeMap<>();
    private ProgramMetadata programMetadata = new ProgramMetadata();
    private List<String> code;

    //constructor
    public VerifySyntax(List<String> code) {
        this.code = code;
    }

    public boolean isSyntaxCorrect() {
        int startIndex = -1;
        int lastDataIndex = -1;

        for(int i=0 ; i < this.code.size() ; i++) {
            String[] lineArray = this.code.get(i).split(" ");
            LineType lineType = this.whatLine(lineArray);

            if(lineType == LineType.START)
                startIndex = i;

            if(lineType == LineType.DATA && i > lastDataIndex)
                lastDataIndex = i;

            if(lineType == LineType.SYNTAX_ERROR) {
                this.codeLineType.clear();
                Log.error("[ERROR] Syntax error at line " + (i + 1) + ".");
                return false;
            }

            this.codeLineType.put(i, lineType);
        }


        if((startIndex == -1) && (lastDataIndex != -1)) {
            this.codeLineType.clear();
            Log.error("[ERROR] Data declared but 'start:' label is not found");
            return false;
        }

        if(lastDataIndex > startIndex) {
            this.codeLineType.clear();
            Log.error("[ERROR] Incorrect data declaration at line " + (lastDataIndex + 1) + ".");
            return false;
        }

        int duplicateLabel = this.searchDuplicates(LineType.LABEL);
        int duplicateData = this.searchDuplicates(LineType.DATA);
        int[] firstMemory = this.firstDataMemory();
        int programMemoryUsage = this.programMemoryUsage();
        int dataMemoryUsage = this.dataMemoryUsage();
        float totalUsed = (Math.round(((float) (programMemoryUsage + dataMemoryUsage) / 4096) * 1000f)) / 1000f;

        if(duplicateLabel > -1) {
            this.codeLineType.clear();
            Log.error("[ERROR] Duplicate label at " + (duplicateLabel) + ".");
            return false;
        }

        if(duplicateData > -1) {
            this.codeLineType.clear();
            Log.error("[ERROR] Duplicate data declaration at " + (duplicateData) + ".");
            return false;
        }

        if(firstMemory[0] != -1 &&  firstMemory[0] < 16) {
            this.codeLineType.clear();
            Log.error("[ERROR] Data declaration is not allowed in this range [000h - 00F] at line " + firstMemory[1] + ".");
            return false;
        }


        if(firstMemory[0] == -1 && programMemoryUsage > 4096) {
            this.codeLineType.clear();
            Log.info("[INFO] Program Memory Usage: " + programMemoryUsage + " bytes");
            Log.info("[INFO] Data Memory Usage: 0 byte");
            Log.info("[INFO] Total used: " + (programMemoryUsage/4096) + "%");
            Log.error("[ERROR] Your program exceeds RAM capacity.");
            return false;
        } else if (firstMemory[0] != -1 && programMemoryUsage > firstMemory[0]) {
            this.codeLineType.clear();
            Log.info("[INFO] Program Memory Usage: " + programMemoryUsage + " bytes");
            Log.info("[INFO] Data Memory Usage: " + dataMemoryUsage + " byte");
            Log.info("[INFO] Total used: " + totalUsed + "%");
            Log.error("[ERROR] Your program is overlapping the data declaration range. Please use a higher address memory for data declaration.");
            return false;
        }

        Log.info("[INFO] Program Memory Usage: " + programMemoryUsage + " bytes");
        Log.info("[INFO] Data Memory Usage: " + dataMemoryUsage + " byte");
        Log.info("[INFO] Total used: " + totalUsed + "%");
        Log.info("[INFO] Syntax correct.");

        return true;

    }

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

    private LineType isData(String[] str) {
        if(str.length == 3) {
            if(str[0].equals(".DATA") && is12bitsHexData(str[1]) && is16bitsHexData(str[2])) {
                return LineType.DATA;
            } else {
                return LineType.SYNTAX_ERROR;
            }
        } else {
            return LineType.SYNTAX_ERROR;
        }
    }


    public LineType whatLine(String[] str) {
        String strStr = String.join(" ", str);
        if(!strStr.isEmpty()) {
            if (this.isLabel(str) == LineType.LABEL) {
                if(this.isStart(str) == LineType.START) {
                    return LineType.START;
                } else {
                    return LineType.LABEL;
                }
            } else if(this.isData(str) == LineType.DATA) {
                return LineType.DATA;
            } else {
                if (this.instSet.containsKey(str[0])) {
                    if (str.length == 2 && this.instSet.get(str[0]).getHasOperand()) {
                        if (str[1].startsWith("0x") ^ str[1].endsWith("H")) {
                            String hexCode = str[1].replace("0x", "").replace("H", "");
                            if (this.isValidHexNumber(hexCode)) {
                                return LineType.INSTR_nI;
                            } else {
                                return LineType.SYNTAX_ERROR;
                            }
                        } else if((str[1].startsWith("[0x") && str[1].endsWith("]")) ^ (str[1].startsWith("[") && str[1].endsWith("H]"))) {
                            String hexCode = str[1].replace("0x", "").replace("H", "").replace("[", "").replace("]","");
                            if (this.isValidHexNumber(hexCode)) {
                                return LineType.INSTR_I;
                            } else {
                                return LineType.SYNTAX_ERROR;
                            }
                        } else {
                            return LineType.SYNTAX_ERROR;
                        }
                    } else if (str.length == 1 && !this.instSet.get(str[0]).getHasOperand()) {
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

    public boolean isValidHexNumber(String str) {
        return !str.isEmpty() && str.toUpperCase().matches("^[0-9A-F]*$");
    }

    public boolean isIndirect(String str) {
        if (str.length() == 4) {
            char leftDigit = str.charAt(0);
            char msbBin = Integer.toBinaryString((int) leftDigit).charAt(0);
            return msbBin == '1';
        } else {
            return false;
        }
    }

    private boolean compileCode() {
        return true;
    }

    private boolean is16bitsHexData(String hex) {
        if(hex.startsWith("0X") ^ hex.endsWith("H")) {
            String hexPure = hex.replace("0X", "").replace("H", "");
            return hexPure.length() == 4 && this.isValidHexNumber(hexPure);
        } else {
            return false;
        }
    }

    private boolean is12bitsHexData(String hex) {
        if(hex.startsWith("0X") ^ hex.endsWith("H")) {
            String hexPure = hex.replace("0X", "").replace("H", "");
            return hexPure.length() == 3 && this.isValidHexNumber(hexPure);
        } else {
            return false;
        }
    }

    private int searchDuplicates(LineType lineType) {
        int count = 0;

        LineType lineTypeTmp = lineType == LineType.DATA ? lineType : LineType.LABEL;
        String firstItem = "";

        for (int i=0 ; i < this.code.size() ; i++) {
            if (this.codeLineType.get(i) == lineTypeTmp) {
                if(count == 1) {
                    if(lineType == LineType.DATA) {
                        String[] dataArr = this.code.get(i).split(" ");
                        String data = dataArr[0] + " " + dataArr[1];
                        if(firstItem.equals(data)) {
                            return i+1;
                        }
                    } else {
                        if(firstItem.equals(this.code.get(i))) {
                            return i+1;
                        }
                    }
                }

                if(count == 0) {
                    if(lineType == LineType.DATA) {
                        String[] dataArr = this.code.get(i).split(" ");
                        firstItem = dataArr[0] + " " + dataArr[1];
                    } else {
                        firstItem = this.code.get(i);
                    }
                    count++;
                }
            }
        }
        return -1;
    }

    private int[] firstDataMemory() {
        int count = 0;
        int firstData = -1;
        int currentData = 0;
        int lineNumber = -1;
        for (int i = 0; i < this.code.size(); i++) {
            if(this.codeLineType.get(i) == LineType.DATA) {
                count++;
                currentData = Integer.parseInt(this.getDataAddress(this.code.get(i)), 16);
                if(firstData == -1 || currentData < firstData) {
                    firstData = currentData;
                    lineNumber = i;
                }
            }
        }


        return new int[]{firstData, lineNumber + 1};
    }

    private String getDataAddress(String data) {
        return this.trimAddress(data.split(" ")[1]);
    }

    private String trimAddress(String address) {
        return address.trim().toUpperCase().replace("0X", " ").replace("H", "");
    }

    private int programMemoryUsage() {
        int memoryCount = 0;

        for (int i = 0; i < this.code.size(); i++) {
            LineType currentLineType = this.codeLineType.get(i);
            List<LineType> memoryInstructions = Arrays.asList(LineType.INSTR, LineType.INSTR_I, LineType.INSTR_nI);

            if(memoryInstructions.contains(currentLineType)) {
                memoryCount++;
            }
        }

        return memoryCount;
    }

    private int dataMemoryUsage() {
        int memoryCount = 0;

        for (int i = 0; i < this.code.size(); i++) {
            LineType currentLineType = this.codeLineType.get(i);

            if(currentLineType == LineType.DATA) {
                memoryCount++;
            }
        }

        return memoryCount;
    }

    private void setProgramMetadata() {
        this.programMetadata.setDataMemoryUsage(this.programMemoryUsage());
    }

}
