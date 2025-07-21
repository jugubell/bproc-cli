public class ProgramMetadata {
    private int programMemoryUsage;
    private int dataMemoryUsage;
    private int firstDataMemory;
    private boolean hasDataDeclaration;
    private int startIndex;
    private DataRange programDataRange;
    private DataRange dataDataRange;

//    private int

    //setters
    public void setProgramMemoryUsage(int programMemoryUsage) {
        this.programMemoryUsage = programMemoryUsage;
    }

    public void setDataMemoryUsage(int dataMemoryUsage) {
        this.dataMemoryUsage = dataMemoryUsage;
    }

    public void setFirstDataMemory(int firstDataMemory) {
        this.firstDataMemory = firstDataMemory;
    }

    public void setHasDataDeclaration(boolean hasDataDeclaration) {
        this.hasDataDeclaration = hasDataDeclaration;
    }

    public void setProgramDataRange(DataRange programDataRange) {
        this.programDataRange = programDataRange;
    }

    public void setDataDataRange(DataRange dataDataRange) {
        this.dataDataRange = dataDataRange;
    }

    //getters
    public int getProgramMemoryUsage() { return this.programMemoryUsage; }
    public int getDataMemoryUsage() { return this.dataMemoryUsage; }
    public int getFirstDataMemory() { return this.firstDataMemory; }
    public boolean getHasDataDeclaration() { return this.hasDataDeclaration; }

    public DataRange getProgramDataRange() {
        return programDataRange;
    }

    public DataRange getDataDataRange() {
        return dataDataRange;
    }
}
