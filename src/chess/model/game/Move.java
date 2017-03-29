package chess.model.game;

import chess.model.board.Board;
import chess.model.pieces.Piece;
import chess.model.pieces.Queen;

/**
 * This is the main move class which defines all moves 
 * for the chess game.
 * @author Piotr Poskart
 *
 */
public abstract class Move 
{
	/** Reference to piece which is moved */
	protected final Piece movedPiece;
	/** Position which moved piece is to be moved on */
	protected final int targetPosition;
	/** Position of the moved piece */
	protected final int sourcePosition;
	/** Reference to the board object */
	protected final Board board;
	
	/**
	 * Move constructor. Initializes move with pieces to be moved
	 * and their positions.
	 * @param board reference to the current board object
	 * @param movedPiece reference to piece which is to be moved
	 * @param sourcePosition position of the moved piece
	 * @param targetPosition position which moved piece is to be moved on
	 */
	public Move(final Board board, final Piece movedPiece, final int sourcePosition, final int targetPosition)
	{
		this.movedPiece = movedPiece;
		this.targetPosition = targetPosition;
		this.sourcePosition = sourcePosition;
		this.board = board;
	}
	/**
	 * Returns target position.
	 * @return position which the piece is to be moved on.
	 */
	public final int getTargetPosition()
	{
		return targetPosition;
	}
	/**
	 * Returns source position of the moved piece.
	 * @return source position of the moved piece.
	 */
	public final int getSourcePosition()
	{
		return sourcePosition;
	}
	/**
	 * Returns reference to the moved piece.
	 * @return reference to the moved piece object.
	 */
	public final Piece getMovedPiece()
	{
		return movedPiece;
	}
	/**
	 * Returns reference to the attacked piece if it is attack move.
	 * If it is not attack move it returns null.
	 * @return reference to the attacked piece, null if there is not
	 * any attacked piece.
	 */
	public Piece getAttackedPiece()
	{
		return null;
	}
	/**
	 * Return additional move of the current move if it exists.
	 * @return move object of the additional move within current move.
	 */
	public Move getSpecialMove()
	{
		return null;
	}
	/**
	 * Executes move. This super method sets afterFirstMove flag
	 * of the moved piece if not set already.
	 */
	public void execute()
	{
		if(!movedPiece.wasAlreadyMoved())
			movedPiece.setFirstMoveFlag(true);
	}
	/**
	 * This method is an abstract method which return the board to previous
	 * state, when move has not been executed yet.
	 */
	abstract public void undo();
	/**
	 * Checks is this an attack move.
	 * @return true if attack move, false otherwise.
	 */
	public boolean isAttackMove()
	{
		return false;
	}
	/**
	 * Checks is this castling move.
	 * @return true if castling move, false otherwise.
	 */
	public boolean isCastlingMove()
	{
		return false;
	}
	/**
	 * Checks is this an checkmate move.
	 * @return true if checkmate move, false otherwise.
	 */
	public boolean isCheckMateMove()
	{
		return false;
	}
	
	/**
	 * This is a class of the attack move which extends Move class
	 * and contains info about attacked piece.
	 * @author Piotr Poskart
	 *
	 */
	public static class AttackMove extends Move
	{
		/** Reference to piece which is attacked */
		private Piece attackedPiece;
		/**
		 * AttackMove constructor. Initializes information about 
		 * pieces and positions to be changed.
		 * @param board reference to the board object.
		 * @param movedPiece reference to the moved piece.
		 * @param attackedPiece reference to the attacked piece.
		 * @param sourcePosition source position of the moved piece.
		 * @param targetPosition target position of the moved piece.
		 */
		public AttackMove(final Board board, 
				final Piece movedPiece,
				final Piece attackedPiece,
				final int sourcePosition, 
				final int targetPosition)
		{
			super(board, movedPiece, sourcePosition, targetPosition);
			this.attackedPiece = attackedPiece;
		}
		/**
		 * Returns reference to the attacked piece
		 * @return reference to the attacked piece.
		 */
		@Override
		public Piece getAttackedPiece()
		{
			return attackedPiece;
		}
		/**
		 * Executes move. Moves pieces on the board to the target position
		 * corresponding to their target position in the move.
		 */
		@Override
		public void execute()
		{
			super.execute();
			board.removePieceFromField(attackedPiece, targetPosition);
			board.removePieceFromField(movedPiece, sourcePosition);
			board.putPieceOnField(movedPiece, targetPosition);
		}
		/**
		 * Undoes move which has been already executed by execute() method.
		 */
		@Override
		public void undo()
		{
			board.removePieceFromField(movedPiece, targetPosition);
			board.putPieceOnField(movedPiece, sourcePosition);
			board.putPieceOnField(attackedPiece, targetPosition);
		}
		/**
		 * Check if it is an attack move
		 * @return true
		 */
		@Override
		public boolean isAttackMove()
		{
			return true;
		}
	}
	
	/**
	 * This is a class of the common move which extends Move class
	 * and hos only one piece to be moved to empty target position.
	 * @author Piotr Poskart
	 *
	 */
	public static class CommonMove extends Move
	{		
		/**
		 * CommonMove constructor. Initializes information about 
		 * pieces and positions to be changed.
		 * @param board reference to the board object.
		 * @param movedPiece reference to the moved piece.
		 * @param sourcePosition source position of the moved piece.
		 * @param targetPosition target position of the moved piece.
		 */
		public CommonMove(final Board board, 
				final Piece movedPiece,
				final int sourcePosition, 
				final int targetPosition)
		{
			super(board, movedPiece, sourcePosition, targetPosition);
		}
		/**
		 * Executes move. Moves pieces on the board to the target position
		 * corresponding to their target position in the move.
		 */
		@Override
		public void execute()
		{
			super.execute();
			Piece tmpPiece = board.removePieceFromField(movedPiece, sourcePosition);
			board.putPieceOnField(tmpPiece, targetPosition);
		}
		/**
		 * Undoes move which has been already executed by execute() method.
		 */
		@Override
		public void undo()
		{
			board.removePieceFromField(movedPiece, targetPosition);
			board.putPieceOnField(movedPiece, sourcePosition);
		}
	}
	
	public static class PawnPromotionMove extends Move
	{		
		/**
		 * PawnPromotionMove constructor. Initializes information about 
		 * pieces and positions to be changed.
		 * @param board reference to the board object.
		 * @param movedPiece reference to the moved piece.
		 * @param sourcePosition source position of the moved piece.
		 * @param targetPosition target position of the moved piece.
		 */
		public PawnPromotionMove(final Board board, 
				final Piece movedPiece,
				final int sourcePosition, 
				final int targetPosition)
		{
			super(board, movedPiece, sourcePosition, targetPosition);
		}
		/**
		 * Executes move. Moves pieces on the board to the target position
		 * corresponding to their target position in the move. This move also
		 * deletes pawn after promotion and places new Queen piece instead of
		 * the pawn.
		 */
		@Override
		public void execute()
		{
			super.execute();
			Piece tmpPiece = board.removePieceFromField(movedPiece, sourcePosition);
			tmpPiece = new Queen(targetPosition, movedPiece.getAlliance());
			board.putPieceOnField(tmpPiece, targetPosition);
		}
		/**
		 * Undoes move which has been already executed by execute() method.
		 */
		@Override
		public void undo()
		{
			board.removePieceFromField(board.getPieceOnField(targetPosition), targetPosition);
			board.putPieceOnField(movedPiece, sourcePosition);
		}
	}
	
	public static class CastlingMove extends Move
	{	
		/** Position of the rook which takes part in castling */
		final int rookPosition;
		/**
		 * CastlingMove constructor. Initializes information about 
		 * pieces and positions to be changed.
		 * @param board reference to the board object.
		 * @param movedPiece reference to the moved piece.
		 * @param sourcePosition source position of the moved piece.
		 * @param targetPosition target position of the moved piece.
		 * @param rookPosition is the position of the rook which takes 
		 * part in castling.
		 */
		public CastlingMove(final Board board, 
				final Piece movedPiece,
				final int sourcePosition, 
				final int targetPosition, 
				final int rookPosition)
		{
			super(board, movedPiece, sourcePosition, targetPosition);
			this.rookPosition = rookPosition;
		}
		/**
		 * Executes move. Moves pieces on the board to the target position
		 * corresponding to their target position in the move. It moves king 
		 * to its valid castling position and moves rook next to the king accordingly.
		 */
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
		/**
		 * Undoes move which has been already executed by execute() method.
		 */
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
		/**
		 * Returns special move related to this move. It is the rook move 
		 * next to the king move in castling.
		 * @return
		 */
		@Override
		public final Move getSpecialMove()
		{
			return new CommonMove(board, board.getPieceOnField(rookPosition),
					rookPosition, (sourcePosition + targetPosition)/2);
		}
		/**
		 * Returns true, because it is castling move.
		 * @return true.
		 */
		@Override
		public final boolean isCastlingMove()
		{
			return true;
		}
	}
}
