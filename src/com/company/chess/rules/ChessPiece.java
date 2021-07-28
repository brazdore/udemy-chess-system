package com.company.chess.rules;

import com.company.boardgame.Board;
import com.company.boardgame.BoardException;
import com.company.boardgame.Piece;
import com.company.boardgame.Position;

public abstract class ChessPiece extends Piece {

    private Color color;
    private int moveCount;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void increaseMoveCount() {
        moveCount++;
    }

    public void decreaseMoveCount() {
        moveCount--;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public ChessPosition getChessPosition() throws ChessException {
        return ChessPosition.fromPosition(position);
    }

    protected boolean isThereOpponentPiece(Position position) throws BoardException {
        ChessPiece p = (ChessPiece) getBoard().getPiece(position);
        return p != null && p.getColor() != color;
    }
}
