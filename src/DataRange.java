/**
 * Class for defining a data range in RAM
 */
public class DataRange {
    private int start;
    private int end;

    /**
     * Constructor of {@link DataRange}
     * @param start the start address of the range in <code>int</code>
     * @param end the end address of the range in <code>int</code>
     */
    DataRange (int start, int end) {
        this.start = start;
        this.end = end;
    }

    // Setters
    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    // Getters
    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
