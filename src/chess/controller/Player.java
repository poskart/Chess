package chess.controller;

import chess.model.aux.Alliance;

public abstract class Player 
{
	public Player(final Alliance alliance)
	{
		this.alliance = alliance;
	}
	
	public abstract void executeMove();
	
	protected final Alliance alliance;
}
