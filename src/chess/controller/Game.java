package chess.controller;

import chess.model.aux.Alliance;
import chess.model.board.*;

public class Game implements Runnable
{
	public Game(Board gameBoard)
	{
		this.gameBoard = gameBoard;
	}
	
	@Override
	public void run()
	{
		initialize();
		wPlayer.executeMove();
		bPlayer.executeMove();
	}
	
	public void initialize()
	{
		wPlayer = new WhitePlayer(Alliance.WHITE);
		bPlayer = new BlackPlayer(Alliance.BLACK);
		gameBoard.initialize();
	}
	
	private WhitePlayer wPlayer;
	private BlackPlayer bPlayer;
	
	private final Board gameBoard;
}
