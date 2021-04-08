package Application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Main {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();
		List<ChessPiece> captured = new ArrayList<>();
		
		while(!chessMatch.getCheckMate()) {
		try {	
			UI.clearScreen();
			UI.printMatch(chessMatch, captured);
			System.out.println();
			System.out.println("Source: ");
			ChessPosition source = UI.readChessPosition(sc);
			
			boolean[][] possibleMoves = chessMatch.possibleMove(source);
			UI.clearScreen();
			UI.printBoard(chessMatch.getPieces(), possibleMoves);
			
			System.out.println();
			System.out.println("Target: ");
			ChessPosition target = UI.readChessPosition(sc);
			
			ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
			
			if(capturedPiece != null) { // sempre que executar um movimento e o mesmo resultar em uma peça capturada, add na lista de peça capturada
				captured.add(capturedPiece);
			}
			
			if(chessMatch.getPromoted() != null) {
				System.out.println("Enter piece for promotion (B/C/R/Q): ");
				String type = sc.nextLine().toUpperCase();
				while(!type.equals("B") && !type.equals("C") && !type.equals("R") && !type.equals("Q")) {
					System.out.println("Invalid value!! Enter piece for promotion (B/C/R/Q): ");
					type = sc.nextLine().toUpperCase();
				}
				chessMatch.replacePromotedPiece(type);
			}
			
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
		UI.clearScreen();
		UI.printMatch(chessMatch, captured);
	}

}
