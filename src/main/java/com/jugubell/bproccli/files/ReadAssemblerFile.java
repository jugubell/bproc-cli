/*
 * File: ReadAssemblerFile.java
 * Project: bproc-cli
 * Last modified: 2025-09-14 17:57
 *
 * This file: ReadAssemblerFile.java is part of BProC-CLI project.
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

package com.jugubell.bproccli.files;

import com.jugubell.bproccli.cli.PathType;
import com.jugubell.bproccli.console.Log;
import com.jugubell.bproccli.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class for the assembler file reading.
 * @author Jugurtha Bellagh
 */
public class ReadAssemblerFile {
    private String filePath;
    private final List<String> trimmedData = new ArrayList<>();
    private final List<String> fileData = new ArrayList<>();

    /**
     * Constructor for the {@link ReadAssemblerFile} class.
     * @param filePath path of the file to read as <code>String</code>
     */
    public ReadAssemblerFile(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Method for reading the file, the extension should be '.bpasm'
     * Writes the file content into {@link #fileData}
     * Uses {@link #trimFile()} to trim the file content.
     * @return boolean true if read success
     */
    public boolean readFile() {
        try {
            File asmFile = new File(this.getFilePath());
            Scanner asmScanner = new Scanner(asmFile);

            // getting the file name and file extension
            String fileName = asmFile.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".")+1);

            // checks the extension
            if(Utils.checkPath(this.getFilePath()) == PathType.FILE_EXISTS) {
                // checks if the file has content
                if(asmScanner.hasNextLine()) {
                    // writing the file content into fileData field
                    while (asmScanner.hasNextLine()) {
                        this.fileData.add(asmScanner.nextLine());
                    }

                // aborting if file empty
                } else {
                    Log.warning("[WARNING] The file provided is empty.");
                    asmScanner.close();
                    return false;
                }

            // aborting if file extension is not correct
            } else {
                Log.error("[ERROR] Your provided file doesn't exist or the extension is wrong. Please choose a .bpasm file.");
                return false;
            }

            // closing the opened file and trimming the content into trimmedData by trimFile();
            asmScanner.close();
            this.trimFile();
            return true;

        // throws exception if file not opened
        } catch (FileNotFoundException e) {
            Log.error("[ERROR] An error was occurred: " + e.getMessage());
            return false;
        }
    }

    // Getters
    public String getFilePath() {
        return this.filePath;
    }

    public List<String> getTrimmedData() {
        return this.trimmedData;
    }

    // Setters
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    /**
     * Trims the file content and store the trimmed content
     * and stores the trimmed data in {@link #trimmedData}.
     * It removes comments (starting with ';')
     * It removes extra spaces.
     * Uppercases everything.
     */
    public void trimFile() {
        for(String fileDatum: this.fileData) {
            String trimmedLine;

            int commentIndex = fileDatum.indexOf(";");

            if(commentIndex != -1) {
                trimmedLine = fileDatum.substring(0, commentIndex);
            } else {
                trimmedLine = fileDatum;
            }

            this.trimmedData.add(trimmedLine.trim().toUpperCase());
        }
    }
}
