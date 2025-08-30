/**
 * Static class for logging texts, info, warnings and errors.
 * It logs also the version and help for the CLI: {@link CommandLine}
 */
public class Log {
    public static void text(String message) {
        if(Utils.supportsColor())
            System.out.println(ConsoleColor.DEFAULT + message);
        else
            System.out.println(message);
    }

    public static void info(String message) {
        if(Utils.supportsColor())
            System.out.println(ConsoleColor.INFO + message + ConsoleColor.DEFAULT);
        else
            System.out.println(message);

    }

    public static void warning(String message) {
        if(Utils.supportsColor())
            System.out.println(ConsoleColor.WARNING + message + ConsoleColor.DEFAULT);
        else
            System.out.println(message);
    }

    public static void error(String message) {
        if(Utils.supportsColor())
            System.out.println(ConsoleColor.ERROR + message + ConsoleColor.DEFAULT);
        else
            System.out.println(message);
    }

    public static void help() {
        System.out.println("*** Help for BProC CLI ***");
        System.out.println();
        System.out.println("Description:");
        System.out.println("    BProC is a simple command line interface for compiling assembler files '.bpasm'");
        System.out.println("    for basic processors intended for education use.");
        System.out.println("    Please find the supported instruction set in: https://github.com/jugubell/bproc-cli");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("    java -jar bproc-cli.jar [-action <inputFile (*.bpasm)>] [optional -output <outputFileName/outputDirectory>] [optional --option <compileType (bin|hex|hexv3)>]");
        System.out.println();
        System.out.println("Actions:");
        System.out.println("    -s : verify syntax.");
        System.out.println("    -g : verify syntax, compile then generate the compiled code.");
        System.out.println("    -o : verify syntax, compile and save the compiled code to a file");
        System.out.println();
        System.out.println("Options:");
        System.out.println("    --bin : compiles to binary [value by default]");
        System.out.println("    --hex : compiles to hexadecimal");
        System.out.println("    --hexv3 : compiles to hexadecimal version 3 format (compatible for Logisim RAM)");
        System.out.println("    --vhdl : compiles to a portion of VHDL RAM initialization signal");
        System.out.println("    --vrlg : compiles to a portion of Verilog RAM initial bloc");
        System.out.println();
        System.out.println("Arguments:");
        System.out.println("    inputFile (*.bpasm): A valid assembler file absolute or relative path with '.bpasm' extension.");
        System.out.println("    outputFileName/outputDirectory: A valid output directory, if the name file is not provided, it will be generated.");
        System.out.println("    compileType [binary|hexv3]: Type of compilation : default 'binary'");
        System.out.println();
        System.out.println("Other arguments:");
        System.out.println("    help, -h, --help: shows this help.");
        System.out.println("    version, -v, --version: shows this help.");
    }

    public static void version() {
        System.out.println("BProC compiler - v1.0");
    }


}
