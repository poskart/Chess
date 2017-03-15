import chess.controller.*;
import chess.view.BoardTable;
import chess.model.board.Board;

public class Chess 
{
	public static void main(String[] args)
	{
		BoardTable boardView = new BoardTable();
		Board gameBoard = new Board(boardView);
		Game chessGame = new Game(gameBoard, boardView);
		chessGame.run();
		
		System.out.println(gameBoard);
		
	}
}
