package com.company.chess.rules;

import com.company.boardgame.Position;

public class ChessPosition {

    protected char column;
    protected int row;

    public ChessPosition(){}

    public ChessPosition(char column, int row) {
        this.column = column;
        this.row = row;
    }



}
