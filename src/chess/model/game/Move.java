package chess.model.game;

import chess.model.board.Board;
import chess.model.pieces.Piece;

public abstract class Move 
{
	protected final Piece movedPiece;
	protected final int targetPosition;
	protected final int sourcePosition;
	protected final Board board;
	
	public Move(final Board board, final Piece movedPiece, final int sourcePosition, final int targetPosition)
	{
		this.movedPiece = movedPiece;
		this.targetPosition = targetPosition;
		this.sourcePosition = sourcePosition;
		this.board = board;
	}
	
	public final int getTargetPosition()
	{
		return targetPosition;
	}
	
	public final int getSourcePosition()
	{
		return sourcePosition;
	}
	
	public final Piece getMovedPiece()
	{
		return movedPiece;
	}
	
	public Piece getAttackedPiece()
	{
		return null;
	}
	
	public void execute()
	{
		if(!movedPiece.wasAlreadyMoved())
			movedPiece.setFirstMoveFlag(true);
	}
	
	abstract public void undo();
	
	public boolean isAttackMove()
	{
		return false;
	}
	
	public boolean isCastlingMove()
	{
		return false;
	}
	
	public boolean isCheckMateMove()
	{
		return false;
	}
	
	public static class AttackMove extends Move
	{
		private Piece attackedPiece;
		
		public AttackMove(final Board board, 
				final Piece movedPiece,
				final Piece attackedPiece,
				final int sourcePosition, 
				final int targetPosition)
		{
			super(board, movedPiece, sourcePosition, targetPosition);
			this.attackedPiece = attackedPiece;
		}
		
		@Override
		public Piece getAttackedPiece()
		{
			return attackedPiece;
		}
		@Override
		public void execute()
		{
			super.execute();
			board.removePieceFromField(attackedPiece, targetPosition);
			board.removePieceFromField(movedPiece, sourcePosition);
			board.putPieceOnField(movedPiece, targetPosition);
		}
		@Override
		public void undo()
		{
			board.removePieceFromField(movedPiece, targetPosition);
			board.putPieceOnField(movedPiece, sourcePosition);
			board.putPieceOnField(attackedPiece, targetPosition);
		}
		@Override
		public boolean isAttackMove()
		{
			return true;
		}
	}
	
	public static class CommonMove extends Move
	{		
		public CommonMove(final Board board, 
				final Piece movedPiece,
				final int sourcePosition, 
				final int targetPosition)
		{
			super(board, movedPiece, sourcePosition, targetPosition);
		}
		
		@Override
		public void execute()
		{
			super.execute();
			Piece tmpPiece = board.removePieceFromField(movedPiece, sourcePosition);
			board.putPieceOnField(tmpPiece, targetPosition);
		}
		@Override
		public void undo()
		{
			board.removePieceFromField(movedPiece, targetPosition);
			board.putPieceOnField(movedPiece, sourcePosition);
		}
	}
}
