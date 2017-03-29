package chess.model.pieces;

import java.util.List;
import chess.model.aux.Alliance;
import chess.model.board.Board;
import chess.model.game.Move;

/**
 * This is abstract class which is the base for all
 * pieces in the game. It implements such piece features
 * as position, alliance, piece type and provides get and 
 * set methods for this attributes as well as some additional
 * auxiliary functions.
 * 
 * @author piotr
 *
 */
public abstract class Piece 
{
	
	protected int position;
	protected final Alliance alliance;
	protected boolean afterFirstMove;
	protected PieceType pieceType;
	
	Piece(final int position, final Alliance pieceAlliance)
	{
		this.position = position;
		this.alliance = pieceAlliance;
		this.afterFirstMove = false;
	}
	
	public abstract List<Move> findPossibleMoves(final Board board);
	
	public abstract List<Move> findPossibleAttackMoves(final Board board);
	
	public final int getPosition()
	{
		return position;
	}
	
	public void updatePosition(final int newPosition)
	{
		this.position = newPosition;
	}
	
	public final Alliance getAlliance()
	{
		return alliance;
	}
	
	public final boolean wasAlreadyMoved()
	{
		return afterFirstMove;
	}
	
	public void setFirstMoveFlag(final boolean wasFirstMoveExecuted)
	{
		afterFirstMove = wasFirstMoveExecuted;
	}

	public final PieceType getPieceType()
	{
		return pieceType;
	}
	
	public enum PieceType
	{
		PAWN("P"),
		BISHOP("B"),
		KNIGHT("N"),
		ROOK("R"),
		QUEEN("Q"),
		KING("K");
		
		private String pieceName;
		
		PieceType(String pieceName)
		{
			this.pieceName = pieceName;
		}
		
		@Override
		public String toString()
		{
			return this.pieceName;
		}
	}
}
