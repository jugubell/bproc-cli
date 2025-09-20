/*
 * File: WriteHexFile.java
 * Project: bproc-cli
 * Last modified: 2025-09-14 17:57
 *
 * This file: WriteHexFile.java is part of BProC-CLI project.
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
import com.jugubell.bproccli.cli.CommandLineOption;
import com.jugubell.bproccli.console.Log;
import com.jugubell.bproccli.utils.Utils;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.IOException;

/**
 * Class handling writing the hex, hex v3, bin and vhdl files
 * @author Jugurtha Bellagh
 */
public class WriteHexFile {

    private final List<String> hexFile;
    private final String filePath;
    private CommandLineOption option = CommandLineOption.BIN;

    /**
     * Constructor of {@link WriteHexFile}
     * @param filePath the file pr directory path of the file to write
     * @param hexFile the file to write as <code>{@literal List<String>}</code>
     * @param option the option as {@link CommandLineOption} of the file.
     */
    public WriteHexFile(String filePath, List<String> hexFile, CommandLineOption option) {
        this.hexFile = hexFile;
        this.filePath = filePath;
        this.option = option;
    }

    /**
     * Method writing the hex file
     * If a directory is provided as a file path, the file is automatically
     * named in the format : {@literal  program_<timestamp>.<extension>}
     * @return boolean true if path and write successful
     */
    public boolean writeFile() {
        try {
            // test and set path and generate timestamp
            Path path = Paths.get(this.filePath);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("-", "_").replace(":", "_");

            // if the file path provided is a directory, generate the file name and add it to the path
            if(Utils.checkPath(this.filePath, false) == PathType.DIRECTORY) {
                switch (option) {
                    case BIN:
                    case HEX:
                        path = path.resolve("program" + timestamp + ".data");
                        break;
                    case HEXV3:
                        path = path.resolve("program" + timestamp + ".hex");
                        break;
                    case VHDL:
                        path = path.resolve("program" + timestamp + ".vhd");
                        break;
                    case VERILOG:
                        path = path.resolve("program" + timestamp + ".v");
                        break;

                }
            }

            // writing process
            try {
                Log.info("[INFO] Writing file content on: " + path.getFileName());

                // writing
                try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.US_ASCII)) {
                    // loop throw the file content and write each line
                    for (int i = 0; i < this.hexFile.size(); i++) {
                        writer.write(this.hexFile.get(i));
                        // add new line only if it not the last line
                        if (i < this.hexFile.size() - 1)
                            writer.newLine();
                    }
                    return true;
                }

            // handle the file opening error
            } catch (IOException e) {
                Log.error("[ERROR] Writing file failed: " + e.getMessage());
                return false;
            }

        } catch (InvalidPathException e) {
            Log.error("[ERROR] Invalid path error: " + e.getMessage());
            return false;
        }
    }
}
