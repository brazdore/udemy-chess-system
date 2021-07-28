package com.company.chess.rules;

import com.company.boardgame.Board;
import com.company.boardgame.BoardException;
import com.company.boardgame.Piece;
import com.company.boardgame.Position;
import com.company.chess.pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {

    private Board board;
    private int turn;
    private Color currentPlayer;
    private boolean check;
    private boolean checkmate;
    private ChessPiece enPassantVulnerability;
    private ChessPiece promoted;

    private List<Piece> piecesOnBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();


    public ChessMatch() throws BoardException, ChessException {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public int getTurn() {
        return turn;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckmate() {
        return checkmate;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
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

    public ChessPiece getEnPassantVulnerability() {
        return enPassantVulnerability;
    }

    public ChessPiece getPromoted() {
        return promoted;
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) throws BoardException, ChessException {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("You cannot put yourself in check.");
        }

        ChessPiece movedPiece = (ChessPiece) board.getPiece(target);

        // Promotion
        promoted = null;
        if (movedPiece instanceof Pawn) {
            if (movedPiece.getColor() == Color.WHITE && target.getRow() == 0 || movedPiece.getColor() == Color.BLACK && target.getRow() == 7) {
                promoted = (ChessPiece) board.getPiece(target);
                promoted = replacePromotedPiece("Q");
            }
        }


        check = testCheck(opponent(currentPlayer));

        if (testCheckmate(opponent(currentPlayer))) {
            checkmate = true;
        } else { /// Só passa o turno de não for mate.
            nextTurn();
        }

        // En passant
        if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
            enPassantVulnerability = movedPiece;
        } else {
            enPassantVulnerability = null;
        }

        return (ChessPiece) capturedPiece;
    }

    public ChessPiece replacePromotedPiece(String type) throws ChessException, BoardException {
        if (promoted == null) {
            throw new IllegalStateException("No piece is able to promote.");
        }
        if (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
            return promoted;
        }

        Position pos = promoted.getChessPosition().toPosition();
        Piece p = board.removePiece(pos);
        piecesOnBoard.remove(p);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePiece(newPiece, pos);
        piecesOnBoard.add(newPiece);
        return newPiece;
    }

    private ChessPiece newPiece(String type, Color color) {
        if (type.equals("B")) return new Bishop(board, color);
        if (type.equals("N")) return new Knight(board, color);
        if (type.equals("R")) return new Rook(board, color);
        return new Queen(board, color);
    }

    private Piece makeMove(Position source, Position target) throws BoardException {
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();

        Piece captured = board.removePiece(target);
        board.placePiece(p, target);

        if (captured != null) {
            piecesOnBoard.remove(captured);
            capturedPieces.add(captured);
        }

        // Castling

        // Kingside
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceRook = new Position(source.getRow(), source.getColumn() + 3);
            Position targetRook = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceRook);
            board.placePiece(rook, targetRook);
            rook.increaseMoveCount();
        }

        // Queenside
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceRook2 = new Position(source.getRow(), source.getColumn() - 4);
            Position targetRook2 = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook2 = (ChessPiece) board.removePiece(sourceRook2);
            board.placePiece(rook2, targetRook2);
            rook2.increaseMoveCount();
        }

        // En passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && captured == null) {
                Position pawnPos;
                if (p.getColor() == Color.WHITE) {
                    pawnPos = new Position(target.getRow() + 1, target.getColumn());
                } else {
                    pawnPos = new Position(target.getRow() - 1, target.getColumn());
                }
                captured = board.removePiece(pawnPos);
                capturedPieces.add(captured);
                piecesOnBoard.remove(captured);
            }
        }

        return captured;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) throws BoardException {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnBoard.add(capturedPiece);
        }

        // Kingside
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceRook = new Position(source.getRow(), source.getColumn() + 3);
            Position targetRook = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetRook);
            board.placePiece(rook, sourceRook);
            rook.decreaseMoveCount();
        }

        // Queenside
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceRook2 = new Position(source.getRow(), source.getColumn() - 4);
            Position targetRook2 = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook2 = (ChessPiece) board.removePiece(targetRook2);
            board.placePiece(rook2, sourceRook2);
            rook2.decreaseMoveCount();
        }

        // En passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerability) {
                ChessPiece pawn = (ChessPiece) board.removePiece(target);
                Position pawnPos;

                if (p.getColor() == Color.WHITE) {
                    pawnPos = new Position(3, target.getColumn());
                } else {
                    pawnPos = new Position(4, target.getColumn());
                }
                board.placePiece(pawn, pawnPos);
            }
        }
    }

    private void validateSourcePosition(Position position) throws BoardException, ChessException {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("There is no piece at source position.");
        }
        if (currentPlayer != ((ChessPiece) board.getPiece(position)).getColor()) {
            throw new ChessException("You cannot move a piece that is not yours.");
        }
        if (!board.getPiece(position).isThereAnyPossibleMove()) {
            throw new ChessException("There is no possible moves for chosen piece.");
        }
    }

    private void validateTargetPosition(Position source, Position target) throws BoardException, ChessException {
        if (!board.getPiece(source).possibleMove(target)) {
            throw new ChessException("Chosen piece cannot move to target position.");
        }
    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer.equals(Color.WHITE)) ? Color.BLACK : Color.WHITE;
    }

    private Color opponent(Color color) {
        return color.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color) {
        List<Piece> list = piecesOnBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).collect(Collectors.toList());
        for (Piece p : list) {
            if (p instanceof King) {
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("There is no " + color + " king on the board.");
    }

    private boolean testCheck(Color color) throws ChessException, BoardException {
        Position kingPos = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnBoard.stream().filter(x -> ((ChessPiece) x).getColor() == opponent(color)).collect(Collectors.toList());
        for (Piece p : opponentPieces) {
            boolean[][] mat = p.possibleMoves();
            if (mat[kingPos.getRow()][kingPos.getColumn()]) {
                return true;
            }
        }
        return false;
    }

    private boolean testCheckmate(Color color) throws ChessException, BoardException {
        if (!testCheck(color)) {
            return false;
        }
        List<Piece> list = piecesOnBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).collect(Collectors.toList());
        for (Piece p : list) {
            boolean[][] mat = p.possibleMoves();
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getColumns(); j++) {
                    if (mat[i][j]) {
                        Position source = ((ChessPiece) p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) throws ChessException, BoardException {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnBoard.add(piece);
    }

    private void initialSetup() throws BoardException, ChessException {

        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
    }

    public Board getBoard() {
        return this.board;
    }

}
