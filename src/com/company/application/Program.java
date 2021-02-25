package com.company.application;

import com.company.boardgame.Board;
import com.company.boardgame.Position;
import com.company.chess.rules.ChessMatch;

public class Program {

    public static void main(String[] args) {

        ChessMatch match = new ChessMatch();
        UI.printBoard(match.getPieces());


    }

}
