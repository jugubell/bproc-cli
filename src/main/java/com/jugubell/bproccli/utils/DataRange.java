/*
 * File: DataRange.java
 * Project: bproc-cli
 * Last modified: 2025-09-14 17:57
 *
 * This file: DataRange.java is part of BProC-CLI project.
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
 * Class for defining a data range in RAM
 * @author Jugurtha Bellagh
 */
public class DataRange {
    private int start;
    private int end;

    /**
     * Constructor of {@link DataRange}
     * @param start the start address of the range in <code>int</code>
     * @param end the end address of the range in <code>int</code>
     */
    public DataRange (int start, int end) {
        this.start = start;
        this.end = end;
    }

    // Setters
    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    // Getters
    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
