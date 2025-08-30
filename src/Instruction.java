/**
 * Class for handling instruction form
 */
public class Instruction {
    private String name;
    private boolean hasOperand;
    private Integer opCode;
    private boolean isHWinst;

    /**
     * Constructor of the {@link Instruction} with hardware instruction defaulted to true.
     * @param name
     * @param hasOperand
     * @param opCode
     */
    public Instruction(String name, boolean hasOperand, Integer opCode) {
        this.name = name;
        this.hasOperand = hasOperand;
        this.opCode = opCode;
        this.isHWinst = true;
    }

    /**
     * Constructor of the {@link Instruction} with control of instruction type (SW/HW).
     * @param name
     * @param hasOperand
     * @param opCode
     * @param isHWinst
     */
    public Instruction(String name, boolean hasOperand, Integer opCode, boolean isHWinst) {
        this.name = name;
        this.hasOperand = hasOperand;
        this.opCode = opCode;
        this.isHWinst = isHWinst;
    }

    //getters
    public String getName() { return this.name; }
    public boolean getHasOperand() { return this.hasOperand; }
    public Integer getOpCode() { return this.opCode; }

}
