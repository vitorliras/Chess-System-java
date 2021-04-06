package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch { // classe que serve para a s jogadas do xadrez
	
	private Board board;
	
	public ChessMatch() {
		board = new Board(8,8);
		initialSetup();
	}
	
	public ChessPiece[][] getPieces(){
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for(int i = 0; i< board.getRows(); i++) {
			for(int j = 0; j< board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
		
	}
	private void placeNewPiece(char column, int row, ChessPiece piece) { //instanciando a formula da posic�o
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}
	
	private void initialSetup() { // posi��o inicial das pe�as
		placeNewPiece('a', 8,new Rook(board, Color.WHITE)); //agora
		placeNewPiece('e', 8,new King(board, Color.WHITE));
		placeNewPiece('e', 1,new King(board, Color.WHITE));
		//board.placePiece(new King(board, Color.WHITE), new Position(7, 4)); //antes
	}
	
}
