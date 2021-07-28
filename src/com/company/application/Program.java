package com.company.application;

import com.company.boardgame.BoardException;
import com.company.chess.rules.ChessException;
import com.company.chess.rules.ChessMatch;
import com.company.chess.rules.ChessPiece;
import com.company.chess.rules.ChessPosition;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) throws BoardException, ChessException {

        Scanner sc = new Scanner(System.in);
        ChessMatch match = new ChessMatch();
        List<ChessPiece> capturedPieces = new ArrayList<>();

        while (!match.getCheckmate()) {
            try {
                UI.clearScreen();
                UI.printMatch(match, capturedPieces);
                System.out.println();
                System.out.print("Source: ");
                ChessPosition source = UI.readChessPosition(sc);
                System.out.print("Target: ");
                ChessPosition target = UI.readChessPosition(sc);
                System.out.println();

                ChessPiece captured = match.performChessMove(source, target);
                if (captured != null) {
                    capturedPieces.add(captured);
                }

                if (match.getPromoted() != null) {
                    System.out.println("Choose a promotion (B, N, R, Q): ");
                    String type = sc.nextLine().toUpperCase();

                    while (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
                        System.out.println("Invalid input. Choose a promotion (B, N, R, Q): ");
                        type = sc.nextLine();
                    }
                    match.replacePromotedPiece(type);
                }

            } catch (ChessException | InputMismatchException e) {
                System.out.println(e.getMessage() + " Press ENTER to continue.");
                sc.nextLine();

            }
        }
        UI.clearScreen();
        UI.printMatch(match, capturedPieces);
    }
}

