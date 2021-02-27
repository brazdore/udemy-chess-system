package com.company.chess.rules;

import com.company.boardgame.Board;
import com.company.boardgame.BoardException;
import com.company.boardgame.Piece;
import com.company.boardgame.Position;
import com.company.chess.pieces.King;
import com.company.chess.pieces.Rook;

import java.util.ArrayList;
import java.util.List;

public class ChessMatch {

    private Board board;


    public ChessMatch() throws BoardException, ChessException {
        board = new Board(8, 8);
        initialSetup();
    }

    public ChessPiece[][] getPieces() throws BoardException {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.getPiece(i, j);
            }
        }
        return mat;
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) throws BoardException, ChessException {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        Piece capturedPiece = makeMove(source, target);
        return (ChessPiece) capturedPiece;
    }

    private Piece makeMove(Position source, Position target) throws BoardException {
        Piece p = board.removePiece(source);
        Piece captured = board.removePiece(target);
        board.placePiece(p,target);
        return captured;
    }

    private void validateSourcePosition(Position position) throws BoardException, ChessException {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("There is no piece at source position.");
        }
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) throws ChessException, BoardException {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
    }

    private void initialSetup() throws BoardException, ChessException {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new King(board, Color.WHITE));
        placeNewPiece('a', 3, new Rook(board, Color.WHITE));
        placeNewPiece('a', 4, new Rook(board, Color.WHITE));
        placeNewPiece('b', 5, new Rook(board, Color.WHITE));
        placeNewPiece('b', 6, new Rook(board, Color.WHITE));
        placeNewPiece('b', 7, new Rook(board, Color.WHITE));
        placeNewPiece('b', 8, new King(board, Color.WHITE));

        placeNewPiece('g', 8, new Rook(board, Color.BLACK));
        placeNewPiece('g', 6, new Rook(board, Color.BLACK));
        placeNewPiece('g', 5, new Rook(board, Color.BLACK));
        placeNewPiece('g', 4, new Rook(board, Color.BLACK));
        placeNewPiece('h', 3, new Rook(board, Color.BLACK));
        placeNewPiece('h', 2, new King(board, Color.BLACK));
        placeNewPiece('h', 1, new King(board, Color.BLACK));
        placeNewPiece('h', 7, new King(board, Color.BLACK));
    }

    public Board getBoard() {
        return this.board;
    }


}
