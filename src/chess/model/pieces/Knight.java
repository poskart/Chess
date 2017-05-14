package chess.model.pieces;

import java.util.ArrayList;
import java.util.List;

import chess.model.common.Alliance;
import chess.model.board.Board;
import chess.model.game.Move;
import chess.model.game.Move.AttackMove;
import chess.model.game.Move.CommonMove;

/**
 * This class represents Knight piece. It defines methods to 
 * calculate possible moves and other auxiliary functions.
 * @author Piotr Poskart
 *
 */
public class Knight extends Piece
{
	/** Table of offsets which defines possible Knight moves on
	 * the linear positioned board fields */
	private static final int[] possibleMoveOffset = {-17, -15, -10, -6, 6, 10, 15, 17};
	/**
	 * Knight constructor which takes position and alliance of new Knight.
	 * @param position is position of new Knight
	 * @param alliance is alliance of new Knight
	 */
	public Knight(int position, Alliance alliance)
	{
		super(position, alliance);
		this.pieceType = PieceType.KNIGHT;
	}
	/**
	 * Knight specific method which returns set of possible moves which Knight
	 * can perform.
	 * @param board is reference to the board object
	 * @return container of Move object which this Knight can perform.
	 */
	@Override
	public final List<Move> findPossibleMoves(final Board board)
	{
		List<Move> possibleMovesList = new ArrayList<>();
		int potentialAbsolutePosition;
		for(int offset : possibleMoveOffset)
		{
			potentialAbsolutePosition = this.position + offset;
			if(board.isFieldValid(potentialAbsolutePosition) && 
					!isKnightPotentialPositionCrossingBorder(position, potentialAbsolutePosition))
			{
				if(board.isBoardFieldOccupied(potentialAbsolutePosition))
				{
					if(board.getPieceOnField(potentialAbsolutePosition).alliance != alliance)
						possibleMovesList.add(new AttackMove(board, this, board.getPieceOnField(potentialAbsolutePosition),
								position, potentialAbsolutePosition));
				}
				else
					possibleMovesList.add(new CommonMove(board, this, position, potentialAbsolutePosition));
			}
		}
		return possibleMovesList;
	}
	/**
	 * Knight specific method which return set of possible attack moves which Knight
	 * can perform. These are the same as common non attack) Knight moves.
	 * @param board is reference to the board object
	 * @return container of Move object which Knight can perform and that are 
	 * attack moves. 
	 */
	@Override	
	public List<Move> findPossibleAttackMoves(final Board board)
	{
		return findPossibleMoves(board);
	}
	/**
	 * This is toString method specific for Knight object. it returns String 
	 * with letter 'N' of the Knight piece.
	 * @return String with letter of the Knight.
	 */
	@Override
	public String toString()
	{
		return PieceType.KNIGHT.toString();
	}
	/**
	 * Auxiliary method which checks whether or not this king's new potential position
	 * is crossing the border according to current position.
	 * @param currentPosition is current position of the Knight
	 * @param targetPosition is target position of the Knight
	 * @return true if move from current to target position crosses the board border,
	 * false otherwise.
	 */
	private final boolean isKnightPotentialPositionCrossingBorder(
			final int currentPosition,
			final int targetPosition)
	{
		if(currentPosition % 8 == 6 && targetPosition % 8 == 0)
			return true;
		if(currentPosition % 8 == 7 && (targetPosition % 8 == 0 || targetPosition % 8 == 1))
			return true;
		if(currentPosition % 8 == 1 && targetPosition % 8 == 7)
			return true;
		if(currentPosition % 8 == 0 && (targetPosition % 8 == 7 || targetPosition % 8 == 6))
			return true;

		return false;
	}
}
