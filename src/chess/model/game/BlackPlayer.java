package chess.model.game;

import chess.model.common.Alliance;

/**
 * This is class which represents white player
 * @author Piotr Poskart
 *
 */
public final class BlackPlayer extends Player
{
	/**
	 * Blackplayer constructor which calls super to 
	 * assign black alliance to this player.
	 */
	public BlackPlayer()
	{
		super(Alliance.BLACK);
	}
}