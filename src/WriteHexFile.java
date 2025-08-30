import com.sun.org.apache.bcel.internal.generic.ARETURN;

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
import java.util.Scanner;

/**
 * Class handling writing the hex, hex v3, bin and vhdl files
 */
public class WriteHexFile {

    private List<String> hexFile;
    private String filePath;
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
