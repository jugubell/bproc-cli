//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {
    public static void main(String[] args) {

        CommandLine cli = new CommandLine(args);
        CommandLineAction inAction = cli.getInAction();
        CommandLineAction outAction = cli.getOutAction();

        boolean inActionCorrect = false;
        boolean outActionCorrect = false;

        switch (inAction) {
            case SHOW_HELP:
                Log.help();
                break;
            case SHOW_VERSION:
                Log.version();
                break;
            case PARSE_ERROR:
                Log.error("[ERROR] Argument non-valid. Show help with command: java -jar BProC.jar help");
                break;
            case NO_IN_ARGS:
                Log.error("[ERROR] Input file not provided. Show help with command: java -jar BProC.jar help");
                break;
            case INVALID_IN_PATH:
                Log.error("[ERROR] Invalid input file path. Show help with command: java -jar BProC.jar help");
                break;
            case DUPLICATE_ARGS:
                Log.error("[ERROR] Duplicate arguments found. Show help with command: java -jar BProC.jar help");
                break;
            case INVALID_OPTION:
                Log.error("[ERROR] Option provided is invalid. Show help with command: java -jar BProC.jar help");
                break;
            case NOTHING:
                Log.error("[ERROR] Invalid arguments. Show help with command: java -jar BProC.jar help");
                break;
            case VERIFY_SYNTAX:
                inActionCorrect = true;
                break;
            case GENERATE_CODE:
                inActionCorrect = true;
                break;
            default:
                Log.error("[ERROR] Invalid out argument.");
        }

        switch (outAction) {
            case INVALID_OUT_PATH:
                Log.error("[ERROR] Invalid output file or directory path. Show help with command: java -jar BProC.jar help");
                break;
            case PARSE_ERROR:
                Log.error("[ERROR] Argument non-valid. Show help with command: java -jar BProC.jar help");
                break;
            case NOTHING:
                outActionCorrect = true;
                break;
            case WRITE_CODE:
                outActionCorrect = true;
                break;
            default:
                Log.error("[ERROR] Invalid out argument.");
        }

        if(inActionCorrect && outActionCorrect) {
            String sourceFilePath = cli.getSourceFile();
            String outputFilePath = cli.getOutputFile();
            CommandLineOption option = cli.getOption();

            Globals globals = new Globals();
            globals.setMemFilePath(sourceFilePath);

            ReadAssemblerFile asmFile = new ReadAssemblerFile(sourceFilePath);
            boolean readFileStaus = asmFile.readFile();

            if(readFileStaus) {
                VerifySyntax verifySyntax = new VerifySyntax(asmFile.getTrimmedData());

                if(Arrays.asList(CommandLineAction.VERIFY_SYNTAX, CommandLineAction.GENERATE_CODE).contains(inAction)) {

                    if(verifySyntax.isSyntaxCorrect()) {

                        Compile compile = new Compile(verifySyntax.getCode(), verifySyntax.getCodeLineType(), verifySyntax.getProgramMetadata());
                        List<String> outFile = new ArrayList<>();

                        if(option == CommandLineOption.HEXV3) {
                            outFile = compile.getHexFileLogisim();
                        } else if(option == CommandLineOption.HEX) {
                            outFile = compile.getMemFile(true);
                        } else if(option == CommandLineOption.VHDL) {
                            outFile = compile.getHexFileVhdl();
                        } else {
                            outFile = compile.getMemFile(false);
                        }

                        if(inAction == CommandLineAction.GENERATE_CODE) {
                            for (String s: outFile) {
                                System.out.println(s);
                            }
                            Log.info("[INFO BProC-CLI] Done generating code.");
                        }

                        if(outAction == CommandLineAction.WRITE_CODE) {
                            WriteHexFile writeHexFile = new WriteHexFile(outputFilePath, outFile, option);
                            writeHexFile.writeMemFile();
                            Log.info("[INFO BProC-CLI] File written successfully.");
                        }
                    }
                }
            }
        }
    }
}