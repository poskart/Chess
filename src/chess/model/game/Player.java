package chess.model.game;

import chess.model.aux.Alliance;

public abstract class Player 
{
	public Player(final Alliance alliance)
	{
		this.alliance = alliance;
	}
	
	public Alliance getAlliance()
	{
		return alliance;
	}
	
	public abstract void executeMove();
	
	protected final Alliance alliance;
}