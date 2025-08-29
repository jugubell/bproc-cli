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
    private String[] args;

    final List<String> HELP_ARGS = Arrays.asList("-h", "help", "--help");
    final List<String> VERSION_ARGS = Arrays.asList("-v", "version", "--version");
    final List<String> ACTION_IN_ARGS = Arrays.asList("-s", "-g");
    final List<String> ACTION_OUT_ARGS = Arrays.asList("-o");
    final List<String> OPTION_ARGS = Arrays.asList("--hex", "--bin", "--hexv3", "--vhdl");

    private HashMap<String, String> pairArgs = new HashMap<>();
    private String optArg;

    /**
     * Constructor of the class
     * @param args
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
                if(!Collections.disjoint(this.pairArgs.keySet(), this.ACTION_IN_ARGS)) { // verifies if
                    // handling -s action
                    if(this.pairArgs.containsKey("-s")) {
                        // verifying file path validity
                        if(Utils.checkPath(this.pairArgs.get("-s")) == PathType.FILE) {
                            this.inAction = CommandLineAction.VERIFY_SYNTAX;
                            this.sourceFile = this.pairArgs.get("-s");
                        } else {
                            this.inAction = CommandLineAction.INVALID_IN_PATH;
                        }
                    }

                    // handling -g action
                    if(this.pairArgs.containsKey("-g")) {
                        // verifying file path validity
                        if(Utils.checkPath(this.pairArgs.get("-g")) == PathType.FILE) {
                            this.inAction = CommandLineAction.GENERATE_CODE;
                            this.sourceFile = this.pairArgs.get("-g");
                        } else {
                            this.inAction = CommandLineAction.INVALID_IN_PATH;
                        }
                    }

                    // handling -o action
                    if(this.pairArgs.containsKey("-o")) {
                        // verifying file/directory path validity
                        if(Utils.checkPath(this.pairArgs.get("-o")) == PathType.FILE || Utils.checkPath(this.pairArgs.get("-o")) == PathType.DIRECTORY) {
                            this.outAction = CommandLineAction.WRITE_CODE;
                            this.outputFile = this.pairArgs.get("-o");
                        } else {
                            this.outAction = CommandLineAction.INVALID_OUT_PATH;
                        }
                    } else {
                        this.outAction = CommandLineAction.NOTHING;
                    }
                } else {
                    this.inAction = CommandLineAction.NO_IN_ARGS;
                }

                // handling the option argument
                if(argsLen % 2 == 1) {
                    String opt = this.args[argsLen-1];
                    if(this.OPTION_ARGS.contains(opt)) {
                        if(opt.equals("--hex")) {
                            this.option = CommandLineOption.HEX;
                        } else if(opt.equals("--hexv3")) {
                            this.option = CommandLineOption.HEXV3;
                        } else if(opt.equals("--vhdl")) {
                            this.option = CommandLineOption.VHDL;
                        } else {
                            this.option = CommandLineOption.BIN;
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