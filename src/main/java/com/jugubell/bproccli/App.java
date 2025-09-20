/*
 * File: App.java
 * Project: bproc-cli
 * Last modified: 2025-09-14 17:38
 *
 * This file: App.java is part of BProC-CLI project.
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

package com.jugubell.bproccli;

import java.util.Arrays;
import java.util.List;

import com.jugubell.bproccli.cli.CommandLine;
import com.jugubell.bproccli.cli.CommandLineAction;
import com.jugubell.bproccli.cli.CommandLineOption;
import com.jugubell.bproccli.compiler.Compile;
import com.jugubell.bproccli.compiler.VerifySyntax;
import com.jugubell.bproccli.console.Log;
import com.jugubell.bproccli.files.ReadAssemblerFile;
import com.jugubell.bproccli.files.WriteHexFile;

/**
 * App class
 * The entrypoint of the program
 * @author Jugurtha Bellagh
 */
public class App {
    public static void main(String[] args) {

        CommandLine cli = new CommandLine(args);
        CommandLineAction inAction = cli.getInAction();
        CommandLineAction outAction = cli.getOutAction();

        boolean inActionCorrect = false;
        boolean outActionCorrect = false;

        switch (inAction) {
            case SHOW_HELP:
                Log.help();
                break;
            case SHOW_VERSION:
                Log.version();
                break;
            case SHOW_INSTRSET:
                Log.instructionSet();
                break;
            case PARSE_ERROR:
                Log.error("[ERROR] Argument non-valid. Show help with command: java -jar BProC.jar help");
                break;
            case NO_IN_ARGS:
                Log.error("[ERROR] Input file not provided. Show help with command: java -jar BProC.jar help");
                break;
            case INVALID_IN_PATH:
                Log.error("[ERROR] Invalid input file path. Show help with command: java -jar BProC.jar help");
                break;
            case DUPLICATE_ARGS:
                Log.error("[ERROR] Duplicate arguments found. Show help with command: java -jar BProC.jar help");
                break;
            case INVALID_OPTION:
                Log.error("[ERROR] Option provided is invalid. Show help with command: java -jar BProC.jar help");
                break;
            case NOTHING:
                Log.error("[ERROR] Invalid arguments. Show help with command: java -jar BProC.jar help");
                break;
            case VERIFY_SYNTAX:
            case GENERATE_CODE:
                inActionCorrect = true;
                break;
            default:
                Log.error("[ERROR] Invalid out argument.");
        }

        switch (outAction) {
            case INVALID_OUT_PATH:
                Log.error("[ERROR] Invalid output file or directory path. Show help with command: java -jar BProC.jar help");
                break;
            case PARSE_ERROR:
                Log.error("[ERROR] Argument non-valid. Show help with command: java -jar BProC.jar help");
                break;
            case NOTHING:
            case WRITE_CODE:
                outActionCorrect = true;
                break;
            case ABORT:
                Log.warning("[WARNING] Operation aborted by user.");
                return;
            default:
                Log.error("[ERROR] Invalid out argument.");
        }

        if(inActionCorrect && outActionCorrect) {
            String sourceFilePath = cli.getSourceFile();
            String outputFilePath = cli.getOutputFile();
            CommandLineOption option = cli.getOption();

            ReadAssemblerFile asmFile = new ReadAssemblerFile(sourceFilePath);
            boolean readFileStaus = asmFile.readFile();

            if(readFileStaus) {
                VerifySyntax verifySyntax = new VerifySyntax(asmFile.getTrimmedData());

                if(Arrays.asList(CommandLineAction.VERIFY_SYNTAX, CommandLineAction.GENERATE_CODE).contains(inAction)) {

                    if(verifySyntax.isSyntaxCorrect()) {

                        Compile compile = new Compile(verifySyntax.getCode(), verifySyntax.getCodeLineType(), verifySyntax.getProgramMetadata());
                        List<String> outFile;

                        if(option == CommandLineOption.HEXV3) {
                            outFile = compile.getHexFileHexV3();
                        } else if(option == CommandLineOption.HEX) {
                            outFile = compile.getHexFileHex(true);
                        } else if(option == CommandLineOption.VHDL) {
                            outFile = compile.getHexFileVhdl();
                        } else if(option == CommandLineOption.VERILOG) {
                            outFile = compile.getHexFileVerilog();
                        } else {
                            outFile = compile.getHexFileHex(false);
                        }

                        if(outFile != null) {
                            if(inAction == CommandLineAction.GENERATE_CODE) {
                                for (String s: outFile) {
                                    System.out.println(s);
                                }
                                Log.info("[INFO BProC-CLI] Done generating code.");
                            }

                            if(outAction == CommandLineAction.WRITE_CODE) {
                                WriteHexFile writeHexFile = new WriteHexFile(outputFilePath, outFile, option);
                                if(writeHexFile.writeFile())
                                    Log.info("[INFO BProC-CLI] File written successfully.");
                            }
                        } else {
                            Log.error("[ERROR] Output file not generated.");
                        }
                    }
                }
            }
        }

    }
}