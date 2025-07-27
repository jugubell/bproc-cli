//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
//        System.out.printf("Hello and welcome!");

        Globals gbl = new Globals();
//
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the file path: ");
        gbl.setAssemblyFilePath(scanner.nextLine());
//
        Log.info("[INFO] The assembly file path provided is: " + gbl.getAssemblyFilePath());
//
        ReadAssemblerFile asmFile = new ReadAssemblerFile(gbl.getAssemblyFilePath());

        boolean fileStatus = asmFile.readFile();
        VerifySyntax verifySyntax = new VerifySyntax(asmFile.getTrimmedData());
//        TreeMap<Integer, LineType> hexFileContent = new TreeMap<>();
        if(fileStatus) {
            Log.info("[INFO] File read.");
            if(verifySyntax.isSyntaxCorrect()) {
                Compile compile = new Compile(verifySyntax.getCode(), verifySyntax.getCodeLineType(), verifySyntax.getProgramMetadata());
                List<String> hexFileLogisim = new ArrayList<>();
                List<String> vhdlFile = new ArrayList<>();

                hexFileLogisim = compile.getHexFileLogisim();
                vhdlFile = compile.getHexFileVhdl();

                for (String s : hexFileLogisim) {
                    System.out.println(s);
                }
                Log.info("[INFO] Done.");

                Log.info("[INFO] Generating vhdl hex file ...");
                for (String s : vhdlFile) {
                    System.out.println(s);
                }
                Log.info("[INFO] Done.");
            }
        } else {
            Log.error("[ERROR] File not read.");
        }

    }
}