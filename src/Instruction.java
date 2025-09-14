/**
 * Class for handling instruction form
 */
public class Instruction {
    private final String name;
    private final boolean hasOperand;
    private final Integer opCode;
    private final boolean isHWinst;

    /**
     * Constructor of the {@link Instruction} with hardware instruction defaulted to true.
     * @param name identifier of the instruction
     * @param hasOperand boolean true if it needs an operand
     * @param opCode the binary code of the operation supplied as an <code>Integer</code>
     */
    public Instruction(String name, boolean hasOperand, Integer opCode) {
        this.name = name;
        this.hasOperand = hasOperand;
        this.opCode = opCode;
        this.isHWinst = true;
    }

    /**
     * Constructor of the {@link Instruction} with control of instruction type (SW/HW).
     * @param name identifier of the instruction
     * @param hasOperand boolean true if it needs an operand
     * @param opCode the binary code of the operation supplied as an <code>Integer</code>
     * @param isHWinst boolean true is it a harware instruction, false if it is a software instruction
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
