package com.company.application;

import com.company.boardgame.Board;
import com.company.boardgame.BoardException;
import com.company.boardgame.Position;
import com.company.chess.rules.ChessException;
import com.company.chess.rules.ChessMatch;

public class Program {

    public static void main(String[] args) {

        try {
            ChessMatch match = new ChessMatch();
            UI.printBoard(match.getPieces());
        } catch (BoardException | ChessException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}

