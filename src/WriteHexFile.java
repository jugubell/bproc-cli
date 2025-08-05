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
    private List<String> binFile = new ArrayList<>();
    private List<String> outFile = new ArrayList<>();
    private String filePath;

    public WriteHexFile(String filePath, List<String> hexFile) {
        this.hexFile = hexFile;
        this.filePath = filePath;
    }

    public void writeMemFile(boolean isBinary) {


        Path path = Paths.get(this.filePath);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("-", "_").replace(":", "_");

        if(Files.exists(path) && Files.isDirectory(path)) {
            path = path.resolve("program" + timestamp + ".data");
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
            this.outFile.clear();

            if(isBinary) {
                this.hexFile2binFile();
                this.outFile = this.binFile;
//                Files.write(path, this.binFile, StandardCharsets.US_ASCII);
            } else {
                this.outFile = this.hexFile;
//                Files.write(path, this.hexFile, StandardCharsets.US_ASCII);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.US_ASCII)) {

                for (int i = 0; i < this.outFile.size(); i++) {
                    writer.write(this.outFile.get(i));
                    if (i < this.outFile.size() - 1)
                        writer.newLine();
                }
            }


        } catch (IOException e) {
            Log.error("[ERROR] An error was occurred.");
            e.printStackTrace();
        }
    }



    private void hexFile2binFile() {
        for (String s : this.hexFile) {
            int binary_int = Integer.parseInt(s, 16);
            String binary = String.format("%16s", Integer.toBinaryString(binary_int)).replace(' ', '0');
            this.binFile.add(binary);
        }
    }




}
