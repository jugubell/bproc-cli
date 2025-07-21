public class Instruction {
    private String name;
    private boolean hasOperand;
    private Integer opCode;

    public Instruction(String name, boolean hasOperand, Integer opCode) {
        this.name = name;
        this.hasOperand = hasOperand;
        this.opCode = opCode;
    }

    //getters
    public String getName() { return this.name; }
    public boolean getHasOperand() { return this.hasOperand; }
    public Integer getOpCode() { return this.opCode; }

}
