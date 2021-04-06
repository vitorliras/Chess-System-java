package chess;

import boardgame.Position;

public class ChessPosition { //classe para converter a posi��o do xaderz
	
	private char column;
	private int row;
	
	public ChessPosition(char column, int row) { 
		if(column < 'a' || column > 'h' || row< 1 || row > 8) {
			throw new ChessException("Error instantiating ChessPosition. Valid values are from a1 to h8.");
		}
		this.column = column;
		this.row = row;
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}
	
	protected Position toPosition() { //formula para calcular uma nova posicao
		return new Position(8 - row, column - 'a');
	}
	
	protected static ChessPosition fromPosition(Position position) { //conver��o da posi��o matriz para posicao desejada do xadrez
		return new ChessPosition((char)('a' - position.getColumn()), 8 - position.getRow());
	}
	
	@Override
	public String toString() {
		return "" + column + row;
	}
	
}
