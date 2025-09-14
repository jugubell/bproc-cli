import java.util.*;

/**
 * Small utility for handling command line interface
 * It pasrses the arguments at the instanciation
 */
public class CommandLine {
    private String sourceFile;
    private String outputFile;
    private CommandLineAction inAction = CommandLineAction.NOTHING;
    private CommandLineAction outAction = CommandLineAction.NOTHING;
    private CommandLineOption option = CommandLineOption.BIN;
    private final String[] args;

    final List<String> HELP_ARGS = Arrays.asList("-h", "help", "--help");
    final List<String> VERSION_ARGS = Arrays.asList("-v", "version", "--version");
    final List<String> ACTION_IN_ARGS = Arrays.asList("-s", "-g");
    final List<String> ACTION_OUT_ARGS = Arrays.asList("-o", "-ow");
    final List<String> OPTION_ARGS = Arrays.asList("--hex", "--bin", "--hexv3", "--vhdl", "--vrlg");

    private final HashMap<String, String> pairArgs = new HashMap<>();
    private String optArg;

    /**
     * Constructor of the class
     * @param args arguments of the command
     */
    public CommandLine(String[] args) {
        this.args = args;
        this.parseArgs();
    }

    // Getters
    public String getSourceFile() {
        return this.sourceFile;
    }

    public String getOutputFile() {
        return this.outputFile;
    }

    public CommandLineAction getInAction() {
        return this.inAction;
    }

    public CommandLineAction getOutAction() {
        return this.outAction;
    }

    public CommandLineOption getOption() {
        return this.option;
    }

    /**
     * Arguments parser, called at instanciation
     */
    private void parseArgs() {
        // verify if arguments exists
        if(this.args != null) {
            int argsLen = this.args.length;
            // handling one argument configuration
            if(argsLen == 1) {
                if(this.HELP_ARGS.contains(args[0])) { // help arg
                    this.inAction = CommandLineAction.SHOW_HELP;
                }
                if(this.VERSION_ARGS.contains(args[0])) { // version arg
                    this.inAction = CommandLineAction.SHOW_VERSION;
                }

            // handling 2 to 5 arguments
            } else if (argsLen > 1 && argsLen <= 5) {
                // grouping args into pairs and looks for duplicate actions
                for (int i = 0; i <= (double) (argsLen / 2); i+=2) {
                    if(!this.pairArgs.containsKey(this.args[i])) {
                        this.pairArgs.put(this.args[i], this.args[i+1]);
                    } else {
                        this.inAction = CommandLineAction.PARSE_ERROR;
                        return;
                    }
                }

                // verifying if there is an input action provided
                if(!Collections.disjoint(this.pairArgs.keySet(), this.ACTION_IN_ARGS)) {
                    // handling -s action
                    if(this.pairArgs.containsKey("-s")) {
                        // verifying file path validity
                        if(Utils.checkPath(this.pairArgs.get("-s")) == PathType.FILE_EXISTS) {
                            this.inAction = CommandLineAction.VERIFY_SYNTAX;
                            this.sourceFile = this.pairArgs.get("-s");
                        } else {
                            this.inAction = CommandLineAction.INVALID_IN_PATH;
                        }
                    }

                    // handling -g action
                    if(this.pairArgs.containsKey("-g")) {
                        // verifying file path validity
                        if(Utils.checkPath(this.pairArgs.get("-g")) == PathType.FILE_EXISTS) {
                            this.inAction = CommandLineAction.GENERATE_CODE;
                            this.sourceFile = this.pairArgs.get("-g");
                        } else {
                            this.inAction = CommandLineAction.INVALID_IN_PATH;
                        }
                    }

                    // handling -o action
                    if(this.pairArgs.containsKey("-o")) {
                        // checks if the file already exists : emits an overwrite warning
                        if(Utils.checkPath(this.pairArgs.get("-o"), false) == PathType.FILE_EXISTS) {
                            Scanner scanner = new Scanner(System.in);
                            Log.warning("[WARNING] The file already exists, would you like to overwrite it? (Y/n): ");

                            String answer = scanner.nextLine().trim();

                            if (!answer.equals("Y")) {
                                this.outAction = CommandLineAction.ABORT;
                            }

                            this.outAction = CommandLineAction.WRITE_CODE;
                            this.outputFile = this.pairArgs.get("-o");

                        // verifying file/directory path validity
                        } else if(Utils.checkPath(this.pairArgs.get("-o"), false) == PathType.FILE_NEW || Utils.checkPath(this.pairArgs.get("-o"), false) == PathType.DIRECTORY) {
                            this.outAction = CommandLineAction.WRITE_CODE;
                            this.outputFile = this.pairArgs.get("-o");
                        } else {
                            this.outAction = CommandLineAction.INVALID_OUT_PATH;
                        }
                    }

                    // handling -ow overwrite action
                    if(this.pairArgs.containsKey("-ow")) {
                        if(Utils.checkPath(this.pairArgs.get("-ow"), false) == PathType.FILE_NEW || Utils.checkPath(this.pairArgs.get("-ow"), false) == PathType.FILE_EXISTS || Utils.checkPath(this.pairArgs.get("-ow"), false) == PathType.DIRECTORY) {
                            this.outAction = CommandLineAction.WRITE_CODE;
                            this.outputFile = this.pairArgs.get("-ow");
                        } else {
                            this.outAction = CommandLineAction.INVALID_OUT_PATH;
                        }
                    }
                } else {
                    this.inAction = CommandLineAction.NO_IN_ARGS;
                }

                // handling the option argument
                if(argsLen % 2 == 1) {
                    String opt = this.args[argsLen-1];
                    if(this.OPTION_ARGS.contains(opt)) {
                        switch (opt) {
                            case "--hex":
                                this.option = CommandLineOption.HEX;
                                break;
                            case "--hexv3":
                                this.option = CommandLineOption.HEXV3;
                                break;
                            case "--vhdl":
                                this.option = CommandLineOption.VHDL;
                                break;
                            case "--vrlg":
                                this.option = CommandLineOption.VERILOG;
                                break;
                            default:
                                this.option = CommandLineOption.BIN;
                                break;
                        }
                    } else {
                        this.inAction = CommandLineAction.INVALID_OPTION;
                    }
                }

            } else {
                this.inAction = CommandLineAction.PARSE_ERROR;
                this.outAction = CommandLineAction.PARSE_ERROR;
            }
        } else {
            this.inAction = CommandLineAction.PARSE_ERROR;
            this.outAction = CommandLineAction.PARSE_ERROR;
        }
    }
}