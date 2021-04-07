package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch { // classe que serve para a s jogadas do xadrez
	
	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	
	private List<Piece> pieceOnTheBoard= new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	
	public ChessMatch() {
		board = new Board(8,8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}
	
	public int getTurn() {
		return turn;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
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
	
	public boolean[][] possibleMove(ChessPosition sourcePosition){ //metodo para imprimir os movimento possiveis a partir da posi��o de origem
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	// metodo para tirar uma pe�a em uma posi��o de origem e coloca-la em outro local, seja um movimento ou captura
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) { 
		Position source = sourcePosition.toPosition(); //coventendo a posi��o source e target para uma posi��o da matriz
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturePiece = makeMove(source, target); //realizando o movimento da pe�a
		
		if(testCheck(currentPlayer)) {
			undoMove(source, target, capturePiece);
			throw new ChessException("You can't put yourself in check");
		}
		
		check = (testCheck(opponet(currentPlayer))) ? true : false;
		
		nextTurn();
		return (ChessPiece)capturePiece; //com o downcasting, retornarei a pe�a capturada
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source); //retira a pe�a da posicao de origem
		Piece capturedPiece = board.removePiece(target); //remover uma possivel pe�a que esta na posi��o de destino
		board.placePiece(p, target);
		
		if(capturedPiece != null) {
			pieceOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		return capturedPiece;
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) { //desfazendo o movimento (l�gica xeque)
		Piece p = board.removePiece(target);
		board.placePiece(p, source);
		
		if(capturedPiece !=null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			pieceOnTheBoard.add(capturedPiece);
		}
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.thereIsAPiece(position)) { //se n�o existir uma pe�a nessa posi��o, tem a exce��o
			throw new ChessException("There is no piece on source position");
		}
		if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()) { //Se querer usar a pe�a errada
			throw new ChessException("The chosen Piece is not yours");
		}
		if(!board.piece(position).isThereAnyPossibleMove()) { // verificar se existe movimentos possiveis para a pe�a
			throw new ChessException("There is no possible moves for this the chosen piece");
		}
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE; //troca de turno
	}
	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) { // se para a pe�a de origem a posi��o de destino n � possivel, logo n pode mover pra l�
			throw new ChessException("the chosen piece can't move to target position");
		}
	}
	
	private Color opponet(Color color) {
		return(color == Color.WHITE) ? Color.BLACK : Color.WHITE; // se a cor passada no argumento for == white, ent vai retornar o color black, caso n�o, o retorna o white
	}
	
	private ChessPiece king(Color color) { // L�gica de check
		List<Piece> list = pieceOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece p : list) {  //para cada pe�a p na lista list, caso a pe�a p � uma instancia de KIng, ent encontrou o King
			if (p instanceof King) { 
				return(ChessPiece)p;
			}
		}
		throw new IllegalStateException("There is no "+ color +" King on the board");
	}
	
	private boolean testCheck(Color color) { //Testando o check
		Position kingPosition = king(color).getchessPosition().toPosition();
		List<Piece> opponetPieces = pieceOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponet(color)).collect(Collectors.toList());
		for(Piece p : opponetPieces) {
			boolean[][] mat = p.possibleMoves();
			if(mat[kingPosition.getRow()][kingPosition.getColumn()]){
				return true;
			}
		}
		return false;
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) { //instanciando a formula da posic�o
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		pieceOnTheBoard.add(piece);
	}
	
	private void initialSetup() { // posi��o inicial das pe�as
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
