package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece {

	public Rook(Board board, Color color) {
		super(board, color);
		
	}

	@Override
	public String toString() {
		return "R";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];//vai ficar preso(provis�rio)
		
		Position p = new Position(0,0);
		
		//above
		p.setValues(position.getRow() - 1, position.getColumn()); //movimento acima da pe�a, caso for true
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) { //enuqanto a posi��o de movimento tiver vaga, vai ser true
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() - 1);
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//left
		p.setValues(position.getRow(), position.getColumn() -1); //movimento acima da pe�a, caso for true
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) { //enuqanto a posi��o de movimento tiver vaga, vai ser true
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn()-1);
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//Rigth
		p.setValues(position.getRow(), position.getColumn() + 1); //movimento acima da pe�a, caso for true
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) { //enuqanto a posi��o de movimento tiver vaga, vai ser true
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() + 1);
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
				
		//below
		p.setValues(position.getRow() + 1, position.getColumn()); //movimento acima da pe�a, caso for true
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) { //enuqanto a posi��o de movimento tiver vaga, vai ser true
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() + 1);
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		return mat;
	}
	
	
	


}
