package boardgame;

public class Board { // classe para a construção do tabuleito
	
	private int rows;
	private int columns;
	private Piece[][] pieces;
	
	public Board(int rows, int columns) {
		if(rows < 1 || columns < 1) { //uma condicao de execao no construtor, para caso a linha ae coluna for menor que 1
			throw new BoardException("Error creating board: the must be at least 1 row and 1 column");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns]; //instanciando pecas nesse construtor
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
	
	public Piece piece(int row, int column) { //metodo que vai retorna a peça que ta na matriz na posicao em [][]
		if(!positionExists(row, column)) {
			throw new BoardException("Position not on the board");
		}
		return pieces[row][column];
	}
	
	public Piece piece(Position position) { //peca recebendo uma posicao
		if(!positionExists(position)) {
			throw new BoardException("Position not on the board");
		}
		return pieces[position.getRow()][position.getColumn()];
	}
	
	public void placePiece(Piece piece, Position position) { //colocar uma peça em uma posicao
		if(thereIsAPiece(position)) {
			throw new BoardException("There is already a piece on position" + position);
		}
		pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position;
	}
	
	private boolean positionExists(int row, int column) { //posicao existe qdo ta dentro do tabuleiro
		return row >= 0 && row < rows && column >= 0 && column < columns; //condição para ver se a posicao existe
	}
	
	public boolean positionExists(Position position) {
		return positionExists(position.getRow(), position.getColumn()); //reaproveitando o metodo de cima
	}
	
	public boolean thereIsAPiece(Position position) { //se for diferente de nulo, tem uma peça na posicao
		if(!positionExists(position)) {
			throw new BoardException("Position not on the board");
		}
		return piece(position) != null;
	}
}
