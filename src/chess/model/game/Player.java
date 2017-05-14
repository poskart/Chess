package chess.model.game;

import chess.model.common.Alliance;

/**
 * This is an abstract class representing player in the game.
 * It contains alliance of the player and method to get this alliance.
 * @author Piotr Poskart
 *
 */
public abstract class Player 
{
	/** Alliance of the player */
	protected final Alliance alliance;

	/**
	 * Player constructor. It assigns given alliance to player.
	 * @param alliance is an alliance of this player.
	 */
	public Player(final Alliance alliance)
	{
		this.alliance = alliance;
	}
	/**
	 * This method returns alliance of the player
	 * @return alliance of thus player
	 */
	public Alliance getAlliance()
	{
		return alliance;
	}
}
