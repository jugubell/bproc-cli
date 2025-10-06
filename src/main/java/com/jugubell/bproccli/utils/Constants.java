/*
 * File: Constants.java
 * Project: bproc-cli
 * Last modified: 2025-10-06 12:00
 *
 * This file: Constants.java is part of BProC-CLI project.
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

package com.jugubell.bproccli.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Static class to store constants of the project
 */
public class Constants {
    // version
    public static final String VERSION = "v1.0";

    // help
    public static final List<String> HELP = Arrays.asList(
        "*****************************************************",
        "**************** Help for BProC CLI *****************",
        "*****************************************************",
        "",
        "Description:",
        "    BProC is a simple command line interface for compiling assembler files '.bpasm'",
        "    for basic processors intended for education use.",
        "    Please find related info about the project on: https://github.com/jugubell/bproc-cli",
        "    This project is free and open source, and it is licensed under the GPLv2 license.",
        "",
        "Usage:",
        "    bproc [-action <inputFile (*.bpasm)>] [optional -output <outputFileName/outputDirectory>] [optional --option <compileType>]",
        "Note:",
        "    If you are using the .jar file, instead of the bproc command, use:",
        "    java -jar bproc-cli.jar [-action <inputFile (*.bpasm)>] [optional -output <outputFileName/outputDirectory>] [optional --option <compileType>]",
        "",
        "Arguments:",
        "    inputFile (*.bpasm): A valid assembler file absolute or relative path with '.bpasm' extension.",
        "    outputFileName/outputDirectory: A valid output directory, if the name file is not provided, it will be generated.",
        "    compileType: Type of compilation : default 'binary'",
        "",
        "Actions:",
        "    -s : verify syntax.",
        "    -g : verify syntax, compile then generate the compiled code.",
        "    -o : verify syntax, compile and save the compiled code to a file",
        "",
        "Options:",
        "    --bin   : compiles to binary [value by default]",
        "    --hex   : compiles to hexadecimal",
        "    --hexv3 : compiles to hexadecimal version 3 format (compatible for Logisim RAM)",
        "    --vhdl  : compiles to a portion of VHDL RAM initialization signal",
        "    --vrlg  : compiles to a portion of Verilog RAM initial bloc",
        "",
        "Other arguments:",
        "    --help, -h, help        : shows this help.",
        "    --version, -v, version  : shows the version.",
        "    --instruction-set, --is : shows the supported instruction set."
    );

    // Instruction set
    public static final List<String> INSTRUCTIONSET = Arrays.asList(
        "BProC compiler supported instruction set:",
        "-------------------------------------------------------------------------------------------------------------------",
        "| Instr. | Op code | Type     | Needs an oprd. | Description                                                      |",
        "|--------|---------|----------|----------------|------------------------------------------------------------------|",
        "| AND    | 0x0000  | Hardware | Yes            | Logic *'AND'* between operand and accumulator A                  |",
        "| ADD    | 0x1000  | Hardware | Yes            | Addition of the address' value of the operand and value of A     |",
        "| LDA    | 0x2000  | Hardware | Yes            | Load address' value of the operand into A                        |",
        "| STA    | 0x3000  | Hardware | Yes            | Store the value of A into the address of the operand             |",
        "| BIN    | 0x4000  | Hardware | Yes            | Unconditional branch of program counter to the address           |",
        "| BSA    | 0x5000  | Hardware | Yes            | Subroutine branch stored in address $x + 1                       |",
        "| ISZ    | 0x6000  | Hardware | Yes            | Increment value of addr. and skip next instr. if result=0        |",
        "| CLA    | 0x7800  | Hardware | No             | Clear accumulator A                                              |",
        "| CLE    | 0x7400  | Hardware | No             | Clear flag E (carry out)                                         |",
        "| LNA    | 0x7200  | Hardware | No             | Logic `NOT` of accumulator A (A = Ā)                             |",
        "| LNE    | 0x7100  | Hardware | No             | Logic `NOT` of flag E (E = Ē)                                    |",
        "| SRA    | 0x7080  | Hardware | No             | Shift right of the accumulator A through the flag E              |",
        "| SLA    | 0x7040  | Hardware | No             | Shift left of the accumulator A through the flag E               |",
        "| INC    | 0x7020  | Hardware | No             | Increment accumulator A                                          |",
        "| SPA    | 0x7010  | Hardware | No             | Skip the next instr. if accumulator A > 0                        |",
        "| SNA    | 0x7008  | Hardware | No             | Skip the next instr. if accumulator A < 0                        |",
        "| SZA    | 0x7004  | Hardware | No             | Skip the next instr. if accumulator A = 0                        |",
        "| SZE    | 0x7002  | Hardware | No             | Skip the next instr. if flag E = 0                               |",
        "| HLT    | 0x7001  | Hardware | No             | Halt the program until hardware reset                            |",
        "| RIR    | 0xF800  | Hardware | No             | Read input register (GPIO in)                                    |",
        "| WOR    | 0xF400  | Hardware | No             | Write output register (GPIO out)                                 |",
        "| SFI    | 0xF200  | Hardware | No             | Skip the next instr. if flag FGI = 1 (Input reading flag)        |",
        "| SFO    | 0xF100  | Hardware | No             | Skip the next instr. if flag FGO = 0 (Output writing flag)       |",
        "| LDD*   | 0xF080  | Hardware | Yes            | Load data in immediate addressing mode                           |",
        "| JMP    | 0x0000  | Software | No             | Jump to label in assembly code                                   |",
        "-------------------------------------------------------------------------------------------------------------------",
        "* Not yet supported (instead, use .data declaration combined with LDA)",
        "",
        "Keywords:",
        "------------------------------------------------------------------------------------------------------------------",
        "| .data                       | Data declaration keyword                                                         |",
        "| start:                      | Program start label keyword                                                      |",
        "| 0x prefix or h suffix       | Hexadecimal numbers                                                              |",
        "------------------------------------------------------------------------------------------------------------------",
        "",
        "Credit: the instruction set is mostly inspired from a PhD lab work at the University of Blida.",
        ""
    );
}
