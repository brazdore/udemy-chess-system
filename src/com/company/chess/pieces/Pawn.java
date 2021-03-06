package com.company.chess.pieces;

import com.company.boardgame.Board;
import com.company.boardgame.BoardException;
import com.company.boardgame.Position;
import com.company.chess.rules.ChessMatch;
import com.company.chess.rules.ChessPiece;
import com.company.chess.rules.Color;

public class Pawn extends ChessPiece {

    private ChessMatch chessMatch;

    public Pawn(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public String toString() {
        return "P";
    }

    private boolean canMove(Position position) throws BoardException {
        ChessPiece p = (ChessPiece) getBoard().getPiece(position);
        return p == null || p.getColor() != getColor();
    }

    @Override
    public boolean[][] possibleMoves() throws BoardException {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);

        if (getColor() == Color.WHITE) {


            // Above
            p.setValues(position.getRow() - 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // Above 1st
            p.setValues(position.getRow() - 2, position.getColumn());
            Position p2 = new Position(position.getRow() - 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // Left-Up
            p.setValues(position.getRow() - 1, position.getColumn() - 1);
            if (getBoard().positionExists(p) && canMove(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // Right-Up
            p.setValues(position.getRow() - 1, position.getColumn() + 1);
            if (getBoard().positionExists(p) && canMove(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // En passant
            if (position.getRow() == 3) {
                Position left = new Position(position.getRow(), position.getColumn() - 1);
                if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().getPiece(left) == chessMatch.getEnPassantVulnerability()) {
                    mat[left.getRow() - 1][left.getColumn()] = true;
                }
                Position right = new Position(position.getRow(), position.getColumn() + 1);
                if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().getPiece(right) == chessMatch.getEnPassantVulnerability()) {
                    mat[right.getRow() - 1][right.getColumn()] = true;
                }
            }

        } else if (getColor() == Color.BLACK) {

            // Above
            p.setValues(position.getRow() + 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // Above 1st
            p.setValues(position.getRow() + 2, position.getColumn());
            Position p2 = new Position(position.getRow() + 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // Left-Up

            p.setValues(position.getRow() + 1, position.getColumn() + 1);
            if (getBoard().positionExists(p) && canMove(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // Right-Up

            p.setValues(position.getRow() + 1, position.getColumn() - 1);
            if (getBoard().positionExists(p) && canMove(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // En passant
            if (position.getRow() == 4) {
                Position left = new Position(position.getRow(), position.getColumn() - 1);
                if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().getPiece(left) == chessMatch.getEnPassantVulnerability()) {
                    mat[left.getRow() + 1][left.getColumn()] = true;
                }
                Position right = new Position(position.getRow(), position.getColumn() + 1);
                if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().getPiece(right) == chessMatch.getEnPassantVulnerability()) {
                    mat[right.getRow() + 1][right.getColumn()] = true;
                }
            }
        }
        return mat;
    }


}
