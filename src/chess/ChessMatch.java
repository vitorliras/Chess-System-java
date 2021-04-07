package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;
import chess.pieces.pawn;

public class ChessMatch { // classe que serve para a s jogadas do xadrez
	
	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	
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
	
	public boolean getCheckMate() {
		return checkMate;
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
		
		if(testCheck(currentPlayer)) {
			undoMove(source, target, capturePiece);
			throw new ChessException("You can't put yourself in check");
		}
		
		check = (testCheck(opponet(currentPlayer))) ? true : false;
		
		if(testCheckMate(opponet(currentPlayer))) {
			checkMate = true;
		}else {
			nextTurn();
		}
	
		return (ChessPiece)capturePiece; //com o downcasting, retornarei a peça capturada
	}
	
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source); //retira a peça da posicao de origem
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target); //remover uma possivel peça que esta na posição de destino
		board.placePiece(p, target);
		
		if(capturedPiece != null) {
			pieceOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		return capturedPiece;
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) { //desfazendo o movimento (lógica xeque)
		ChessPiece p = (ChessPiece)board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);
		
		if(capturedPiece !=null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			pieceOnTheBoard.add(capturedPiece);
		}
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.thereIsAPiece(position)) { //se não existir uma peça nessa posição, tem a exceção
			throw new ChessException("There is no piece on source position");
		}
		if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()) { //Se querer usar a peça errada
			throw new ChessException("The chosen Piece is not yours");
		}
		if(!board.piece(position).isThereAnyPossibleMove()) { // verificar se existe movimentos possiveis para a peça
			throw new ChessException("There is no possible moves for this the chosen piece");
		}
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE; //troca de turno
	}
	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) { // se para a peça de origem a posição de destino n é possivel, logo n pode mover pra lá
			throw new ChessException("the chosen piece can't move to target position");
		}
	}
	
	private Color opponet(Color color) {
		return(color == Color.WHITE) ? Color.BLACK : Color.WHITE; // se a cor passada no argumento for == white, ent vai retornar o color black, caso não, o retorna o white
	}
	
	private ChessPiece king(Color color) { // Lógica de check
		List<Piece> list = pieceOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece p : list) {  //para cada peça p na lista list, caso a peça p é uma instancia de KIng, ent encontrou o King
			if (p instanceof King) { 
				return(ChessPiece)p;
			}
		}
		throw new IllegalStateException("There is no "+ color +" King on the board");
	}
	
	private boolean testCheck(Color color) { //fazendo o check
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
	
	private boolean testCheckMate(Color color) {
		if(!testCheck(color)) {
			return false;
		}
		List<Piece> list =  pieceOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece p : list) { // se possuir algum movimento que tire do check ent retornara false
			boolean[][] mat = p.possibleMoves();
			for(int i=0; i <board.getRows(); i++) {
				for( int j = 0; j <board.getColumns(); j++) {
					if(mat[i][j]) {
						Position source = ((ChessPiece)p).getchessPosition().toPosition(); //por position ser protec.. e por estar numa classe em outro pacote e não é subclasse, então devo fazer um downcasting para chesspiece e parti dela chamar um getchespo.. e converter para position
						Position target = new Position(i, j); //movimento possivel para tirar o check
						Piece capturedPiece = makeMove(source, target);//movimento da peça p da origem para o destino
						boolean testCheck = testCheck(color); //testar se ainda estar em check
						undoMove(source, target, capturedPiece); //desfazendo o movimento
						if(!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) { //instanciando a formula da posicão
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		pieceOnTheBoard.add(piece);
	}
	
	private void initialSetup() { // posição inicial das peças
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new pawn(board, Color.WHITE));
        placeNewPiece('b', 2, new pawn(board, Color.WHITE));
        placeNewPiece('c', 2, new pawn(board, Color.WHITE));
        placeNewPiece('d', 2, new pawn(board, Color.WHITE));
        placeNewPiece('e', 2, new pawn(board, Color.WHITE));
        placeNewPiece('f', 2, new pawn(board, Color.WHITE));
        placeNewPiece('g', 2, new pawn(board, Color.WHITE));
        placeNewPiece('h', 2, new pawn(board, Color.WHITE));

        placeNewPiece('b', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new pawn(board, Color.BLACK));
        placeNewPiece('b', 7, new pawn(board, Color.BLACK));
        placeNewPiece('c', 7, new pawn(board, Color.BLACK));
        placeNewPiece('d', 7, new pawn(board, Color.BLACK));
        placeNewPiece('e', 7, new pawn(board, Color.BLACK));
        placeNewPiece('f', 7, new pawn(board, Color.BLACK));
        placeNewPiece('g', 7, new pawn(board, Color.BLACK));
        placeNewPiece('h', 7, new pawn(board, Color.BLACK));; //AGORA
		//board.placePiece(new King(board, Color.WHITE), new Position(7, 4)); //antes
	}
	
}
