/*
 * File: Log.java
 * Project: bproc-cli
 * Last modified: 2025-09-14 17:57
 *
 * This file: Log.java is part of BProC-CLI project.
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

package com.jugubell.bproccli.console;

import com.jugubell.bproccli.utils.Globals;
import com.jugubell.bproccli.utils.Utils;
import com.jugubell.bproccli.cli.CommandLine;

/**
 * Static class for logging texts, info, warnings and errors.
 * It logs also the version and help for the CLI: {@link CommandLine}
 * @author Jugurtha Bellagh
 */
public class Log {
    public static void text(String message) {
        if(Utils.supportsColor())
            System.out.println(ConsoleColor.DEFAULT + message);
        else
            System.out.println(message);
    }

    public static void info(String message) {
        if(Utils.supportsColor())
            System.out.println(ConsoleColor.INFO + message + ConsoleColor.DEFAULT);
        else
            System.out.println(message);

    }

    public static void warning(String message) {
        if(Utils.supportsColor())
            System.out.println(ConsoleColor.WARNING + message + ConsoleColor.DEFAULT);
        else
            System.out.println(message);
    }

    public static void error(String message) {
        if(Utils.supportsColor())
            System.out.println(ConsoleColor.ERROR + message + ConsoleColor.DEFAULT);
        else
            System.out.println(message);
    }

    public static void help() {
        System.out.println("*****************************************************");
        System.out.println("**************** Help for BProC CLI *****************");
        System.out.println("*****************************************************");
        System.out.println();
        System.out.println("Description:");
        System.out.println("    BProC is a simple command line interface for compiling assembler files '.bpasm'");
        System.out.println("    for basic processors intended for education use.");
        System.out.println("    Please find related info about the project on: https://github.com/jugubell/bproc-cli");
        System.out.println("    This project is free and open source, and it is licensed under the GPLv2 license.");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("    bproc [-action <inputFile (*.bpasm)>] [optional -output <outputFileName/outputDirectory>] [optional --option <compileType>]");
        System.out.println("Note:");
        System.out.println("    If you are using the .jar file, instead of the bproc command, use:");
        System.out.println("    java -jar bproc-cli.jar [-action <inputFile (*.bpasm)>] [optional -output <outputFileName/outputDirectory>] [optional --option <compileType>]");
        System.out.println();
        System.out.println("Arguments:");
        System.out.println("    inputFile (*.bpasm): A valid assembler file absolute or relative path with '.bpasm' extension.");
        System.out.println("    outputFileName/outputDirectory: A valid output directory, if the name file is not provided, it will be generated.");
        System.out.println("    compileType: Type of compilation : default 'binary'");
        System.out.println();
        System.out.println("Actions:");
        System.out.println("    -s : verify syntax.");
        System.out.println("    -g : verify syntax, compile then generate the compiled code.");
        System.out.println("    -o : verify syntax, compile and save the compiled code to a file");
        System.out.println();
        System.out.println("Options:");
        System.out.println("    --bin   : compiles to binary [value by default]");
        System.out.println("    --hex   : compiles to hexadecimal");
        System.out.println("    --hexv3 : compiles to hexadecimal version 3 format (compatible for Logisim RAM)");
        System.out.println("    --vhdl  : compiles to a portion of VHDL RAM initialization signal");
        System.out.println("    --vrlg  : compiles to a portion of Verilog RAM initial bloc");
        System.out.println();
        System.out.println("Other arguments:");
        System.out.println("    --help, -h, help        : shows this help.");
        System.out.println("    --version, -v, version  : shows the version.");
        System.out.println("    --instruction-set, --is : shows the supported instruction set.");
    }

    public static void version() {
        System.out.println("BProC compiler - v1.0");
    }

    public static void instructionSet() {
//        Globals glb = new Globals();  // for future dynamic instruction set fetch
        System.out.println("BProC compiler supported instruction set:");
        System.out.println("-------------------------------------------------------------------------------------------------------------------");
        System.out.println("| Instr. | Op code | Type     | Needs an oprd. | Description                                                      |");
        System.out.println("|--------|---------|----------|----------------|------------------------------------------------------------------|");
        System.out.println("| AND    | 0x0000  | Hardware | Yes            | Logic *'AND'* between operand and accumulator A                  |");
        System.out.println("| ADD    | 0x1000  | Hardware | Yes            | Addition of the address' value of the operand and value of A     |");
        System.out.println("| LDA    | 0x2000  | Hardware | Yes            | Load address' value of the operand into A                        |");
        System.out.println("| STA    | 0x3000  | Hardware | Yes            | Store the value of A into the address of the operand             |");
        System.out.println("| BIN    | 0x4000  | Hardware | Yes            | Unconditional branch of program counter to the address           |");
        System.out.println("| BSA    | 0x5000  | Hardware | Yes            | Subroutine branch stored in address $x + 1                       |");
        System.out.println("| ISZ    | 0x6000  | Hardware | Yes            | Increment value of addr. and skip next instr. if result=0        |");
        System.out.println("| CLA    | 0x7800  | Hardware | No             | Clear accumulator A                                              |");
        System.out.println("| CLE    | 0x7400  | Hardware | No             | Clear flag E (carry out)                                         |");
        System.out.println("| LNA    | 0x7200  | Hardware | No             | Logic `NOT` of accumulator A (A = Ā)                             |");
        System.out.println("| LNE    | 0x7100  | Hardware | No             | Logic `NOT` of flag E (E = Ē)                                    |");
        System.out.println("| SRA    | 0x7080  | Hardware | No             | Shift right of the accumulator A through the flag E              |");
        System.out.println("| SLA    | 0x7040  | Hardware | No             | Shift left of the accumulator A through the flag E               |");
        System.out.println("| INC    | 0x7020  | Hardware | No             | Increment accumulator A                                          |");
        System.out.println("| SPA    | 0x7010  | Hardware | No             | Skip the next instr. if accumulator A > 0                        |");
        System.out.println("| SNA    | 0x7008  | Hardware | No             | Skip the next instr. if accumulator A < 0                        |");
        System.out.println("| SZA    | 0x7004  | Hardware | No             | Skip the next instr. if accumulator A = 0                        |");
        System.out.println("| SZE    | 0x7002  | Hardware | No             | Skip the next instr. if flag E = 0                               |");
        System.out.println("| HLT    | 0x7001  | Hardware | No             | Halt the program until hardware reset                            |");
        System.out.println("| RIR    | 0xF800  | Hardware | No             | Read input register (GPIO in)                                    |");
        System.out.println("| WOR    | 0xF400  | Hardware | No             | Write output register (GPIO out)                                 |");
        System.out.println("| SFI    | 0xF200  | Hardware | No             | Skip the next instr. if flag FGI = 1 (Input reading flag)        |");
        System.out.println("| SFO    | 0xF100  | Hardware | No             | Skip the next instr. if flag FGO = 0 (Output writing flag)       |");
        System.out.println("| LDD*   | 0xF080  | Hardware | Yes            | Load data in immediate addressing mode                           |");
        System.out.println("| JMP    | 0x0000  | Software | No             | Jump to label in assembly code                                   |");
        System.out.println("-------------------------------------------------------------------------------------------------------------------");
        System.out.println("* Not yet supported (instead, use .data declaration combined with LDA)");
        System.out.println();
        System.out.println("Keywords:");
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.println("| .data                       | Data declaration keyword                                                         |");
        System.out.println("| start:                      | Program start label keyword                                                      |");
        System.out.println("| 0x prefix or h suffix       | Hexadecimal numbers                                                              |");
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.println();
        System.out.println("Credit: the instruction set is mostly inspired from a PhD lab work at the University of Blida.");

    }

}
