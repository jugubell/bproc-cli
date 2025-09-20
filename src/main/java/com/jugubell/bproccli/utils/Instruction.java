/*
 * File: Instruction.java
 * Project: bproc-cli
 * Last modified: 2025-09-14 17:57
 *
 * This file: Instruction.java is part of BProC-CLI project.
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

/**
 * Class for handling instruction form
 * @author Jugurtha Bellagh
 */
public class Instruction {
    private final String name;
    private final boolean hasOperand;
    private final Integer opCode;
    private final boolean isHWinst;

    /**
     * Constructor of the {@link Instruction} with hardware instruction defaulted to true.
     * @param name identifier of the instruction
     * @param hasOperand boolean true if it needs an operand
     * @param opCode the binary code of the operation supplied as an <code>Integer</code>
     */
    public Instruction(String name, boolean hasOperand, Integer opCode) {
        this.name = name;
        this.hasOperand = hasOperand;
        this.opCode = opCode;
        this.isHWinst = true;
    }

    /**
     * Constructor of the {@link Instruction} with control of instruction type (SW/HW).
     * @param name identifier of the instruction
     * @param hasOperand boolean true if it needs an operand
     * @param opCode the binary code of the operation supplied as an <code>Integer</code>
     * @param isHWinst boolean true is it a harware instruction, false if it is a software instruction
     */
    public Instruction(String name, boolean hasOperand, Integer opCode, boolean isHWinst) {
        this.name = name;
        this.hasOperand = hasOperand;
        this.opCode = opCode;
        this.isHWinst = isHWinst;
    }

    //getters
    public String getName() { return this.name; }
    public boolean getHasOperand() { return this.hasOperand; }
    public Integer getOpCode() { return this.opCode; }

}
