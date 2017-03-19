package chess.model.pieces;

import java.util.List;
import chess.model.aux.Alliance;
import chess.model.board.Board;
import chess.model.game.Move;

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
	
	public static final boolean isPositionOutOfTheBoardLinear(final int targetPosition)
	{
		return targetPosition > 63 || targetPosition < 0;
	}
	
	public static final boolean isNewPositionCrossingTheBoard(
			final int currentPosition, final int targetPosition)
	{
		return (currentPosition % 8 == 0 && targetPosition % 8 == 7) || 
				(currentPosition % 8 == 7 && targetPosition % 8 == 0);
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
