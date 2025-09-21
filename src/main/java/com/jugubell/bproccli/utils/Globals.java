/*
 * File: Globals.java
 * Project: bproc-cli
 * Last modified: 2025-09-14 17:57
 *
 * This file: Globals.java is part of BProC-CLI project.
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

import java.util.TreeMap;

/**
 * Globals stores the instruction set in a constant {@link #INSTSET}.
 * @author Jugurtha Bellagh
 */
public class Globals {
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
        this.INSTSET.put("LNA", new Instruction("LNA", false, 0x7200));
        this.INSTSET.put("LNE", new Instruction("LNE", false, 0x7100));
        this.INSTSET.put("SRA", new Instruction("SRA", false, 0x7080));
        this.INSTSET.put("SLA", new Instruction("SLA", false, 0x7040));
        this.INSTSET.put("INC", new Instruction("INC", false, 0x7020));
        this.INSTSET.put("SPA", new Instruction("SPA", false, 0x7010));
        this.INSTSET.put("SNA", new Instruction("SNA", false, 0x7008));
        this.INSTSET.put("SZA", new Instruction("SZA", false, 0x7004));
        this.INSTSET.put("SZE", new Instruction("SZE", false, 0x7002));
        this.INSTSET.put("HLT", new Instruction("HLT", false, 0x7001));
        this.INSTSET.put("RIR", new Instruction("RIR", false, 0xF800));
        this.INSTSET.put("WOR", new Instruction("WOR", false, 0xF400));
        this.INSTSET.put("SFI", new Instruction("SFI", false, 0xF200));
        this.INSTSET.put("SFO", new Instruction("SFO", false, 0xF100));
//        this.INSTSET.put("LDD", new Instruction("LDD", true, 0x0000, false)); // upcoming immediate addressing instruction // former opcode 0xF080 ( and was HW instr.)
        this.INSTSET.put("JMP", new Instruction("JMP", false, 0x0000, false));
    }

    public TreeMap<String, Instruction> getInstset() {
        return this.INSTSET;
    }

}
