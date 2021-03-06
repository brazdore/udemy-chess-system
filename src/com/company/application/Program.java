package com.company.application;

import com.company.boardgame.BoardException;
import com.company.chess.rules.ChessException;
import com.company.chess.rules.ChessMatch;
import com.company.chess.rules.ChessPiece;
import com.company.chess.rules.ChessPosition;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) throws BoardException, ChessException {

		Scanner sc = new Scanner(System.in);
		ChessMatch match = new ChessMatch();

		while (true) {
			try {
				UI.clearScreen();
				UI.printBoard(match.getPieces());
				System.out.println();
				System.out.print("Source: ");
				ChessPosition source = UI.readChessPosition(sc);
				System.out.print("Target: ");
				ChessPosition target = UI.readChessPosition(sc);
				System.out.println();

				ChessPiece captured = match.performChessMove(source, target);
			} catch (ChessException | InputMismatchException e) {
				System.out.println(e.getMessage() + " Press ENTER to continue.");
				sc.nextLine();

			}
		}
	}
}

