package chess.model.game;

import chess.model.board.Board;
import chess.model.pieces.Piece;
import chess.model.pieces.Queen;

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
	
	public Move getSpecialMove()
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
	
	public static class PawnPromotionMove extends Move
	{		
		public PawnPromotionMove(final Board board, 
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
			tmpPiece = new Queen(targetPosition, movedPiece.getAlliance());
			board.putPieceOnField(tmpPiece, targetPosition);
		}
		@Override
		public void undo()
		{
			board.removePieceFromField(board.getPieceOnField(targetPosition), targetPosition);
			board.putPieceOnField(movedPiece, sourcePosition);
		}
	}
	
	public static class CastlingMove extends Move
	{	
		final int rookPosition;
		public CastlingMove(final Board board, 
				final Piece movedPiece,
				final int sourcePosition, 
				final int targetPosition, 
				final int rookPosition)
		{
			super(board, movedPiece, sourcePosition, targetPosition);
			this.rookPosition = rookPosition;
		}
		
		@Override
		public void execute()
		{
			super.execute();
			board.removePieceFromField(movedPiece, sourcePosition);
			board.putPieceOnField(movedPiece, targetPosition);
			final Piece tmpRook = board.getPieceOnField(rookPosition);
			board.removePieceFromField(tmpRook, rookPosition);
			board.putPieceOnField(tmpRook, (sourcePosition + targetPosition)/2);
		}
		@Override
		public void undo()
		{
			board.removePieceFromField(movedPiece, targetPosition);
			board.putPieceOnField(movedPiece, sourcePosition);
			final int rookNewPosition = (sourcePosition + targetPosition)/2;
			final Piece tmpRook = board.getPieceOnField(rookNewPosition);
			board.removePieceFromField(tmpRook, rookNewPosition);
			board.putPieceOnField(tmpRook, rookPosition);
		}
		@Override
		public final Move getSpecialMove()
		{
			return new CommonMove(board, board.getPieceOnField(rookPosition),
					rookPosition, (sourcePosition + targetPosition)/2);
		}
		@Override
		public final boolean isCastlingMove()
		{
			return true;
		}
	}
}
