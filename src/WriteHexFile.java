import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Scanner;

public class WriteHexFile {

    private List<String> hexFile;
    private String filePath;
    private CommandLineOption option = CommandLineOption.BIN;

    public WriteHexFile(String filePath, List<String> hexFile, CommandLineOption option) {
        this.hexFile = hexFile;
        this.filePath = filePath;
        this.option = option;
    }

    public void writeMemFile() {


        Path path = Paths.get(this.filePath);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("-", "_").replace(":", "_");

        if(Files.exists(path) && Files.isDirectory(path)) {
            switch (option) {
                case BIN:
                case HEX:
                    path = path.resolve("program" + timestamp + ".data");
                    break;
                case HEXV3:
                    path = path.resolve("program" + timestamp);
                    break;
                case VHDL:
                    path = path.resolve("program" + timestamp + ".vhd");
                    break;
            }
        }

        if(Files.exists(path)) {
            Scanner scanner = new Scanner(System.in);
            Log.warning("[WARNING] The file already exists, would you like to overwrite it? (Y/n): ");

            String answer = scanner.nextLine().trim();

            if (!answer.equals("Y")) {
                Log.warning("[WARNING] Operation aborted by user.");
                return;
            }
        }

        try {
            Log.info("[INFO] Writing file content ...");

            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.US_ASCII)) {

                for (int i = 0; i < this.hexFile.size(); i++) {
                    writer.write(this.hexFile.get(i));
                    if (i < this.hexFile.size() - 1)
                        writer.newLine();
                }
            }

        } catch (IOException e) {
            Log.error("[ERROR] An error was occurred.");
            e.printStackTrace();
        }
    }
}
