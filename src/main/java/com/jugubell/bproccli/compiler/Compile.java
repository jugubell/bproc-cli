/*
 * File: Compile.java
 * Project: bproc-cli
 * Last modified: 2025-09-14 17:57
 *
 * This file: Compile.java is part of BProC-CLI project.
 *
 * BProC-CLI is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 2 of the License,
 * or (at your option) any later version.
 *
 * BProC-CLI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BProC-CLI. If not, see <https://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2025 Jugurtha Bellagh
 */

package com.jugubell.bproccli.compiler;

import com.jugubell.bproccli.utils.Globals;
import com.jugubell.bproccli.utils.Instruction;
import com.jugubell.bproccli.utils.LineType;
import com.jugubell.bproccli.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/**
 * Compile utility
 * <p>It compiles the assembler code into binary/hexadecimal</p>
 * @author Jugurtha Bellagh
 */
public class Compile {

    private List<String> code;
    private TreeMap<Integer, LineType> codeLineType;
    private ProgramMetadata programMetadata;

    private final List<LineType> instr = Arrays.asList(LineType.INSTR, LineType.INSTR_I, LineType.INSTR_nI, LineType.JUMP);
    private TreeMap<String, Instruction> instSet = new Globals().getInstset();
    private List<String> program = new ArrayList<>();
    private TreeMap<Integer, String> data = new TreeMap<>();
    private TreeMap<String, Integer> labelIndex = new TreeMap<>();

    private List<String> hexFileContent = new ArrayList<>();
    private List<String> hexFileCompiled = new ArrayList<>();

    public Compile(List<String> code, TreeMap<Integer, LineType> codeLineType, ProgramMetadata programMetadata) {
        this.programMetadata = programMetadata;
        this.code = code;
        this.codeLineType = codeLineType;
        this.generateProgramMemory();
        this.generateDataMemory();
        this.generateHexFileContent();
    }

    // Getters
    public List<String> getHexFileHexV3() {
        this.compileForHexV3File();
        return this.hexFileCompiled;
    }

    public List<String> getHexFileVhdl() {
        this.compileForVhdlFile();
        return this.hexFileCompiled;
    }

    public List<String> getHexFileVerilog() {
        this.compileForVerilogFile();
        return this.hexFileCompiled;
    }

    /**
     * Compiles into hexadecimal or binary according to isHex
     * then return the file
     * @param isHex boolean true for getting in hexadecimal format, if false binary format is used
     * @return the bin or hex file as <code>List</code>
     */
    public List<String> getHexFileHex(boolean isHex) {
        this.compileForHexFile(isHex);
        return this.hexFileCompiled;
    }

    /**
     * Getting a <code>List</code> of code line type of every line
     * @return <code>TreeMap</code> of line index, line type.
     */
    public TreeMap<Integer, LineType> getCodeLineType() {
        return this.codeLineType;
    }

    /**
     * Generating the Hex file content.
     * <p>The key method of compilation, it converts the program and data into an hex file.
     * It looks if there is a data declaration, then it reserves the data range according
     * to the lowest address of the declared data. The remaining range from the first address
     * is for the program.
     * The range overlap and allowed data declaration are handled in {@link VerifySyntax} class.
     * It writes the {@link #hexFileContent} attribute.</p>
     */
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

        //write the file
        this.hexFileContent.clear();
        this.hexFileContent = hexFile;
    }

    /**
     * Generates the Hex V3 file format from {@link #hexFileContent}
     */
    private void compileForHexV3File() {
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

        this.hexFileCompiled.clear();
        this.hexFileCompiled = hexFile;
    }

    /**
     * Generate a signal RAM init for VHDL code from {@link #hexFileContent}
     */
    private void compileForVhdlFile() {
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
        hexFile.add("");

        this.hexFileCompiled.clear();
        this.hexFileCompiled = hexFile;
    }

    private void compileForVerilogFile() {
        List<String> hexFile = new ArrayList<>();
        String hexCode = "";

        hexFile.add("// program RAM");
        hexFile.add("initial begin");
        hexFile.add("    integer i;");
        hexFile.add("    for(i = 0; i < 4096; i = i + 1) begin");
        hexFile.add("        ram[i] = 16'h0000;");
        hexFile.add("    end");
        hexFile.add("");
        for (int i = 0; i < this.hexFileContent.size(); i++) {
            hexCode = this.hexFileContent.get(i);
            if(!hexCode.equals("0000")) {
                hexFile.add("    ram[" + i + "] = 16'h" + hexCode.toUpperCase() + ";");
            }
        }

        hexFile.add("end");
        hexFile.add("");

        this.hexFileCompiled.clear();
        this.hexFileCompiled = hexFile;
    }

    /**
     * Generate an hex or binary file from {@link #hexFileContent}
     * @param isHex boolean true for hexadecimal format, if false binary is used
     */
    private void compileForHexFile(boolean isHex) {
        this.hexFileCompiled.clear();
        if(isHex) {
            this.hexFileCompiled.addAll(this.hexFileContent);
        } else {
            this.hexFileCompiled.addAll(Utils.hexFile2binFile(this.hexFileContent));
        }
    }

    /**
     * Generating the labels index for the JMP instruction.
     */
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
        this.labelIndex.clear();
        this.labelIndex = labelIndex;
    }

    /**
     * Program generation
     * <p>It generates the program by converting the instructions into the opcodes.
     * For the JMP instruction, it will be converted into the BIN opcode where
     * the address of the BIN is the label index.<br/>
     * This method has to generate the label indexes first with {@link #generateLabelIndex()}.
     * It writes on {@link #program} field.</p>
     */
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
                    opCode = this.instSet.get("BIN").getOpCode() + this.labelIndex.get(Utils.getLabelFromJmp(this.code.get(i)));
                }
                this.program.add(Integer.toHexString(opCode));
            }
        }
    }

    /**
     * Data generation
     * <p>It generates the data range by putting the declared data into the right address
     * relatively.
     * It writes on the {@link #data} field</p>
     */
    private void generateDataMemory() {
        for (int i = 0; i < this.code.size(); i++) {
            if(this.codeLineType.get(i) == LineType.DATA) {
                this.data.put(Utils.getDataAddressInt(this.code.get(i)), Utils.getDataData(this.code.get(i)));
            }
        }
    }

    /**
     * Extracting the used instruction in a given line by trimming the syntax.
     * @param line the code line in <code>String</code>
     * @return the name of the instruction in a <code>String</code>
     */
    private String getInstruction(String line) {
        String instr = line.split(" ")[0].toUpperCase();
        if(!instr.isEmpty() && this.instSet.containsKey(instr)) {
            return instr;
        } else {
            return "";
        }
    }
}


