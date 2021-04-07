package chess;

import boardgame.Board;
import boardgame.Piece;
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
	
	public boolean[][] possibleMove(ChessPosition sourcePosition){ //metodo para imprimir os movimento possiveis a partir da posição de origem
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	// metodo para tirar uma peça em uma posição de origem e coloca-la em outro local, seja um movimento ou captura
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) { 
		Position source = sourcePosition.toPosition(); //coventendo a posição source e target para uma posição da matriz
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturePiece = makeMove(source, target); //realizando o movimento da peça
		return (ChessPiece)capturePiece; //com o downcasting, retornarei a peça capturada
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source); //retira a peça da posicao de origem
		Piece capturedPiece = board.removePiece(target); //remover uma possivel peça que esta na posição de destino
		board.placePiece(p, target);
		return capturedPiece;
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.thereIsAPiece(position)) { //se não existir uma peça nessa posição, tem a exceção
			throw new ChessException("There is no piece on source position");
		}
		if(!board.piece(position).isThereAnyPossibleMove()) { // verificar se existe movimentos possiveis para a peça
			throw new ChessException("There is no possible moves for this the chosen piece");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) { // se para a peça de origem a posição de destino n é possivel, logo n pode mover pra lá
			throw new ChessException("the chosen piece can't move to target position");
		}
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) { //instanciando a formula da posicão
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}
	
	private void initialSetup() { // posição inicial das peças
		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK)); //AGORA
		//board.placePiece(new King(board, Color.WHITE), new Position(7, 4)); //antes
	}
	
}
