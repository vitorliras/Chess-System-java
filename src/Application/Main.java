package Application;

import java.util.InputMismatchException;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Main {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();
		
		while(true) {
		try {	
			UI.clearScreen();
			UI.printMatch(chessMatch);
			System.out.println();
			System.out.println("Source: ");
			ChessPosition source = UI.readChessPosition(sc);
			
			boolean[][] possibleMoves = chessMatch.possibleMove(source);
			UI.clearScreen();
			UI.printBoard(chessMatch.getPieces(), possibleMoves);
			
			System.out.println();
			System.out.println("Target: ");
			ChessPosition target = UI.readChessPosition(sc);
			
			ChessPiece captuPiece = chessMatch.performChessMove(source, target);
		}catch(ChessException e) {
			System.out.println();
			System.out.println("PRESS ENTER");
			System.out.println(e.getMessage());
			sc.hasNextLine();
		}
		catch(InputMismatchException e) {
			System.out.println();
			System.out.println("PRESS ENTER");
			System.out.println(e.getMessage());
			sc.hasNextLine();
		}
		}
	}

}
