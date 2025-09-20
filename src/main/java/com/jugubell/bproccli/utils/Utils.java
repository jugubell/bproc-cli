/*
 * File: Utils.java
 * Project: bproc-cli
 * Last modified: 2025-09-14 17:57
 *
 * This file: Utils.java is part of BProC-CLI project.
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

import com.jugubell.bproccli.cli.PathType;
import com.jugubell.bproccli.console.ConsoleColor;
import com.jugubell.bproccli.console.Log;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Static class utility class for various tasks
 * @author Jugurtha Bellagh
 */
public class Utils {
    /**
     * Method checking a validity of a string as hexadecimal number
     * @param str hex number as <code>String</code>
     * @return boolean true if hex number is valid
     */
    public static boolean isValidHexNumber(String str) {
        return !str.isEmpty() && str.toUpperCase().matches("^[0-9A-F]*$");
    }

    /**
     * Checks if an address is in indirect mode by checking
     * ig the address is wrapped in []
     * Only 12bits address allowed
     * It uses {@link #is12bitsHexData(String)}
     * @param str address as <code>String</code>
     * @return boolean true if it is wrapped in []
     */
    public static boolean isIndirect(String str) {
        if(str.startsWith("[") && str.endsWith("]")) {
            return is12bitsHexData(str.toUpperCase());
        } else {
            return false;
        }
    }

    /**
     * Checks if a string a 16 bits hexadecimal number
     * Accepts prefix: 0x, 0X
     * Accepts suffix: h, H
     * @param hex the hexadecimal number
     * @return boolean true if it is a 16 bits hexadecimal number
     */
    public static boolean is16bitsHexData(String hex) {
        if(hex.startsWith("0X") ^ hex.endsWith("H")) {
            String hexPure = hex.replace("0X", "").replace("H", "");
            return hexPure.length() == 4 && isValidHexNumber(hexPure);
        } else {
            return false;
        }
    }

    /**
     * Checks if a string a 12 bits hexadecimal number
     * Accepts prefix: 0x, 0X
     * Accepts suffix: h, H
     * @param hex the hexadecimal number
     * @return boolean true if it is a 12 bits hexadecimal number
     */
    public static boolean is12bitsHexData(String hex) {
        if(hex.startsWith("0X") ^ hex.endsWith("H")) {
            String hexPure = hex.replace("0X", "").replace("H", "");
            return hexPure.length() == 3 && isValidHexNumber(hexPure);
        } else {
            return false;
        }
    }

    /**
     * Returns the address of a given data declaration annotated with .data syntax
     * Uses {@link #trimAddress(String)} to trim the address.
     * @param data the data line as <code>String</code>
     * @return the address as <code>String</code>
     */
    public static String getDataAddress(String data) {
        return trimAddress(data.split(" ")[1]);
    }

    /**
     * Returns the data of a given data declaration annotated with .data syntax
     * Uses {@link #trimAddress(String)} to trim the data.
     * @param data the data line as <code>String</code>
     * @return the data as <code>String</code>
     */
    public static String getDataData(String data) {
        return trimAddress(data.split(" ")[2]);
    }

    /**
     * Trims and address from suffix, prefix, and indirect [] annotation
     * @param address the address to be trimmed as <code>String</code>
     * @return trimmed address as <code>String</code>
     */
    public static String trimAddress(String address) {
        return address.trim().toUpperCase().replace("0X", " ").replace("H", "").replace("[", "").replace("]", "");
    }

    /**
     * Returns the address in integer of a given data declaration annotated with .data syntax.
     * Uses {@link #getDataAddress(String)} to get a clean address
     * then converts it to an integer.
     * @param data the data line as <code>String</code>
     * @return address in <code>int</code>
     */
    public static int getDataAddressInt(String data) {
        return Integer.decode("0x" + Utils.getDataAddress(data));
    }

    /**
     * Cleans the label by removing the ':'
     * @param str the label as <code>String</code>
     * @return clean label as <code>String</code>
     */
    public static String cleanLabel(String str) {
        return str.trim().replace(":", "");
    }

    /**
     * Gets the pointed label by the JMP instruction
     * @param str the code line of the JMP instruction
     * @return the uppercased label as <code>String</code>
     */
    public static String getLabelFromJmp(String str) {
        return str.trim().split(" ")[1].toUpperCase();
    }

    /**
     * Gets the path type according to the custom path types
     * defined in the enumeration {@link PathType}
     * @param pth path to be checked as <code>String</code>
     * @return the path type as {@link PathType}
     */
    public static PathType checkPath(String pth) {

        List<String> allowedExt = Collections.singletonList("bpasm");

        try {
            Path path = Paths.get(pth);

            // check for allowed extensions
            if(allowedExt.contains(pth.substring(pth.lastIndexOf(".")+1)) ) {
                // check if the file already exists
                if(Files.exists(path) && Files.isRegularFile(path)) {
                    return PathType.FILE_EXISTS;
                } else {
                    return PathType.INVALID;
                }
            } else {
                return PathType.INVALID;
            }

        } catch (InvalidPathException e) {
            Log.error("[ERROR] Invalid file/dir path error: " + e.getMessage());
            return PathType.INVALID;
        }
    }
    public static PathType checkPath(String pth, boolean read) {

        List<String> allowedExt;

        if(read)
            allowedExt = Collections.singletonList("bpasm");
        else
            allowedExt = Arrays.asList("data", "vhd", "hex", "txt", "v");

        try {
            Path path = Paths.get(pth);

            // check for allowed extensions
            if (Files.exists(path) && Files.isDirectory(path)) {
                return PathType.DIRECTORY;

            // if there is no extension, consider a directory
            } else if(allowedExt.contains(pth.substring(pth.lastIndexOf(".")+1))) {
                // check if the file already exists
                if(Files.exists(path) && Files.isRegularFile(path)) {
                    return PathType.FILE_EXISTS;
                } else {
                    return PathType.FILE_NEW;
                }
            } else {
                return PathType.INVALID;
            }

        } catch (InvalidPathException e) {
            Log.error("[ERROR] Invalid file/dir path error: " + e.getMessage());
            return PathType.INVALID;
        }
    }

    /**
     * Converts a file hex content to a binary format.
     * Every line of the file content should be a hexadecimal number.
     * @param file as <code>List</code> of <code>String</code>
     * @return a <code>List</code> of <code>String</code> in binary format
     */
    public static List<String> hexFile2binFile(List<String> file) {
        List<String> binFile = new ArrayList<>();
        for (String l: file) {
            int binary_int = Integer.parseInt(l, 16);
            String binary = String.format("%16s", Integer.toBinaryString(binary_int)).replace(' ', '0');
            binFile.add(binary);
        }
        return binFile;
    }

    /**
     * Checks if the system is not Windows for use in {@link Log}
     * with {@link ConsoleColor}
     * @return boolean false if the system is Windows
     */
    public static boolean supportsColor() {
        return !System.getProperty("os.name").toLowerCase().contains("win");
    }

}
