package chess.model;

import java.util.Observable;

import chess.model.game.Player;
import chess.model.board.Board;
import chess.model.game.Move;

public final class Model extends Observable
{
	Board gameBoard;
	Player player1;
	Player player2;
	Player activePlayer;
	
	public Model()
	{
		gameBoard = new Board();
	}
	
	public void executeMove(Move move)
	{
		gameBoard.executeMove(move);
		changePlayer();
		setChanged();
		notifyObservers(move);
		
	}
	
	public Player getActivePlayer()
	{
		return activePlayer;
	}
	
	private void changePlayer()
	{
		if(activePlayer == player1)
			activePlayer = player2;
		else 
			activePlayer = player1;
	}
	
	public Board getGameBoard()
	{
		return gameBoard;
	}
}
