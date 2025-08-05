public class Instruction {
    private String name;
    private boolean hasOperand;
    private Integer opCode;
    private boolean isHWinst;

    public Instruction(String name, boolean hasOperand, Integer opCode) {
        this.name = name;
        this.hasOperand = hasOperand;
        this.opCode = opCode;
        this.isHWinst = true;
    }

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
