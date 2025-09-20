/*
 * File: CommandLineAction.java
 * Project: bproc-cli
 * Last modified: 2025-09-14 17:57
 *
 * This file: CommandLineAction.java is part of BProC-CLI project.
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

package com.jugubell.bproccli.cli;

/**
 * Enumeration of possible CLI actions
 * @author Jugurtha Bellagh
 */
public enum CommandLineAction {
    PARSE_ERROR,
    DUPLICATE_ARGS,
    INVALID_OPTION,
    INVALID_IN_PATH,
    INVALID_OUT_PATH,
    NO_IN_ARGS,
    SHOW_HELP,
    SHOW_VERSION,
    SHOW_INSTRSET,
    VERIFY_SYNTAX,
    GENERATE_CODE,
    WRITE_CODE,
    NOTHING,
    ABORT
}
