//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;


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
//        System.out.println(verifySyntax.isLabel(new String[] {"customlabel:", ""}));
//        System.out.println(verifySyntax.isLabel(new String[] {"customlabel", ""}));
//        System.out.println(verifySyntax.isValidHexNumber("ABCD"));
//        System.out.println(verifySyntax.isValidHexNumber("1234"));
//        System.out.println(verifySyntax.isValidHexNumber("aefe"));
//        System.out.println(verifySyntax.isValidHexNumber("fg12"));

        boolean fileStatus = asmFile.readFile();
        VerifySyntax verifySyntax = new VerifySyntax(asmFile.getTrimmedData());
//        TreeMap<Integer, LineType> hexFileContent = new TreeMap<>();
        if(fileStatus) {
            Log.info("[INFO] File read.");
            if(verifySyntax.isSyntaxCorrect()) {
                Compile compile = new Compile(verifySyntax.getCode(), verifySyntax.getCodeLineType(), verifySyntax.getProgramMetadata());
                List<String> hexFileContent = new ArrayList<>();
//                hexFileContent = compile.getLogisimHexFileContent();
//
//                for (int i = 0; i < hexFileContent.size(); i++) {
//                    System.out.println(hexFileContent.get(i));
//                }
                hexFileContent = compile.getLogisimHexFileContent();

                for (int i = 0; i < hexFileContent.size(); i++) {
                    System.out.println(hexFileContent.get(i));
                }
            }
        } else {
            Log.error("[ERROR] File not read.");
        }

//        for(int i=0 ; i < asmFile.getTrimmedData().size() ; i++) {
//            System.out.println("Line " + i + " : " + asmFile.getTrimmedData().get(i));
//            System.out.println("Line " + i + " : " + verifySyntax.whatLine(asmFile.getTrimmedData().get(i).trim().split("\\s+")));
//        }
//        File file = new File(filePath);



    }
}