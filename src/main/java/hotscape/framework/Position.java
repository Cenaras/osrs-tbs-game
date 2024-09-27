package hotscape.framework;

public record Position(int row, int col) {

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
