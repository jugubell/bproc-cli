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

import com.jugubell.bproccli.utils.Constants;
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
        for(String line: Constants.HELP) {
            System.out.println(line);
        }
    }

    public static void version() {
        System.out.println("BProC compiler - " + Constants.VERSION);
    }

    public static void instructionSet() {
        for(String line: Constants.INSTRUCTIONSET) {
            System.out.println(line);
        }
    }
}
