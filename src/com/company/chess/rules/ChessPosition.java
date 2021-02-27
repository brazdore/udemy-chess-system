package com.company.chess.rules;

import com.company.boardgame.Position;

public class ChessPosition {

    private char column;
    private int row;

    public ChessPosition() {
    }

    public ChessPosition(char column, int row) throws ChessException {
        if (column < 'a' || column > 'h' || row < 1 || row > 8) {
            throw new ChessException("Error at Chess Position. Valid values are from a1 to h8.");
        }
        this.column = column;
        this.row = row;
    }

    public char getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    protected Position toPosition() {
        return new Position(8 - row, column - 'a');
    }

    protected static ChessPosition fromPosition(Position position) throws ChessException {
        return new ChessPosition((char) ('a' - position.getColumn()), 8 - position.getRow());
    }

    @Override
    public String toString() {
        return "" + column + row;
    }
}
