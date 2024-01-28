package tictactoe.util;

public enum BoardTileEnum {
    EMPTY(""),
    X("x"),
    O("o");

    private final String text;

    BoardTileEnum(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
