package chess.model.pieces;

import java.util.List;
import chess.model.common.Alliance;
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
	/** Position of the piece */
	protected int position;
	/** Alliance of the piece */
	protected final Alliance alliance;
	/** Flag which indicates if piece has been moved yet */
	protected boolean afterFirstMove;
	/** Enum which describes type of the piece */
	protected PieceType pieceType;
	/**
	 * Piece constructor which takes position and alliance of new piece.
	 * @param position
	 * @param pieceAlliance
	 */
	Piece(final int position, final Alliance pieceAlliance)
	{
		this.position = position;
		this.alliance = pieceAlliance;
		this.afterFirstMove = false;
	}
	/**
	 * Abstract method which return set of possible moves which specific piece
	 * can perform.
	 * @param board is reference to the board object
	 * @return container of Move object which this piece can perform.
	 */
	public abstract List<Move> findPossibleMoves(final Board board);
	/**
	 * Abstract method which return set of possible attack moves which specific piece
	 * can perform.
	 * @param board is reference to the board object
	 * @return container of Move object which this piece can perform and that are 
	 * attack moves. 
	 */
	public abstract List<Move> findPossibleAttackMoves(final Board board);
	/**
	 * This method returns position of this piece.
	 * @return position of piece.
	 */
	public final int getPosition()
	{
		return position;
	}
	/**
	 * This method overwrites position of the piece with new value.
	 * @param newPosition
	 */
	public void updatePosition(final int newPosition)
	{
		this.position = newPosition;
	}
	/**
	 * This method returns alliance of this piece.
	 * @return alliance of this piece.
	 */
	public final Alliance getAlliance()
	{
		return alliance;
	}
	/**
	 * This method returns info whether piece has been already moved
	 * or not.
	 * @return true if this piece has been moved yet, false otherwise.
	 */
	public final boolean wasAlreadyMoved()
	{
		return afterFirstMove;
	}
	/**
	 * This mtehod overwrites flag which tells if piece has been moved yet.
	 * @param wasFirstMoveExecuted is boolean value which should be true if
	 * piece has been moved yet, false otherwise.
	 */
	public void setFirstMoveFlag(final boolean wasFirstMoveExecuted)
	{
		afterFirstMove = wasFirstMoveExecuted;
	}
	/**
	 * This method returns type of the piece.
	 * @return enum value with type of the piece.
	 */
	public final PieceType getPieceType()
	{
		return pieceType;
	}
	/**
	 * This is an enumerator which describes type of the piece
	 * @author Piotr Poskart
	 *
	 */
	public enum PieceType
	{
		PAWN("P"),
		BISHOP("B"),
		KNIGHT("N"),
		ROOK("R"),
		QUEEN("Q"),
		KING("K");
		
		private String pieceName;
		/**
		 * PieceType constructor which takes String with the letter which 
		 * describes specific piece type.
		 * @param pieceName
		 */
		PieceType(String pieceName)
		{
			this.pieceName = pieceName;
		}
		/**
		 * This method returns String with letter describing specific piece type.
		 */
		@Override
		public String toString()
		{
			return this.pieceName;
		}
	}
}
