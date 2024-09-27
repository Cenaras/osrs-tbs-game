package hotscape.framework;

public record Position(int row, int col) {

    public Position {
        if (row < 0 || row > 7) {
            throw new IllegalArgumentException("Row must be between 0 and 7");
        }
        if (col < 0 || col > 7) {
            throw new IllegalArgumentException("Col must be between 0 and 7");
        }
    }


    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Position other)) {
            return false;
        }
        return row == other.row && col == other.col;
    }

}
