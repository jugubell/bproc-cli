import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ReadAssemblerFile {
    private String filePath = "";
    private List<String> trimmedData = new ArrayList<>();
    private List<String> fileData = new ArrayList<>();

    public ReadAssemblerFile(String filePath) {
        this.filePath = filePath;
    }

    public boolean readFile() {
            try {
                File asmFile = new File(this.getFilePath());
                Scanner asmScanner = new Scanner(asmFile);

                String fileName = asmFile.getName();
                String fileExtension = fileName.substring(fileName.lastIndexOf(".")+1);

                if(fileExtension.equals("bpasm")) {
                    if(asmScanner.hasNextLine()) {
                        while (asmScanner.hasNextLine()) {
                            this.fileData.add(asmScanner.nextLine());

    //                        System.out.println(data);
                        }
                    } else {
                        Log.warning("[WARNING] The file provided is empty.");
                        asmScanner.close();
                        return false;
                    }
                } else {
                    Log.error("[ERROR] Your provided file extension is wrong. Please choose a .bpasm file.");
                    return false;
                }

                asmScanner.close();

                this.trimFile();

                return true;

            } catch (FileNotFoundException e) {
                Log.error("[ERROR] An error was occurred!");
                e.printStackTrace();

                return false;
            }
        }

        public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

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

    public List<String> getTrimmedData() {
        return this.trimmedData;
    }
}
