package com.company.chess.pieces;

import com.company.boardgame.Board;
import com.company.chess.rules.ChessPiece;
import com.company.chess.rules.Color;

public class King extends ChessPiece {

    public King(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "K";
    }
}
