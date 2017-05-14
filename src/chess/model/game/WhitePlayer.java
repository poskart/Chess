package chess.model.game;

import chess.model.common.Alliance;

/**
 * This is class which represents white player
 * @author Piotr Poskart
 *
 */
public final class WhitePlayer extends Player
{
	/**
	 * White player constructor which calls super to 
	 * assign white alliance to this player.
	 */
	public WhitePlayer()
	{
		super(Alliance.WHITE);
	}
}