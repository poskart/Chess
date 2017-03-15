package chess.controller;

import chess.model.aux.Alliance;
import chess.model.board.*;
import chess.view.BoardTable;

public class Game implements Runnable
{
	private WhitePlayer wPlayer;
	private BlackPlayer bPlayer;
	
	private final Board gameBoard;
	private final BoardTable boardView;
	
	public Game(final Board gameBoard, final BoardTable boardView)
	{
		this.gameBoard = gameBoard;
		this.boardView = boardView;
	}
	
	@Override
	public void run()
	{
		initialize();
		while(true)
		{
			wPlayer.executeMove();
			bPlayer.executeMove();
		}
	}
	
	public void initialize()
	{
		wPlayer = new WhitePlayer(Alliance.WHITE);
		bPlayer = new BlackPlayer(Alliance.BLACK);
		boardView.setInitialGameBoard();
	}

}
