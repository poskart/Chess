package chess.model.pieces;

import java.util.ArrayList;
import java.util.List;
import chess.model.aux.Alliance;
import chess.model.board.Board;
import chess.model.game.Move;
import chess.model.game.Move.AttackMove;
import chess.model.game.Move.CommonMove;

public class King extends Piece 
{
	private static final int[] possibleMoveOffset = {-9, -8, -7, -1, 1, 7, 8, 9};
	
	public King(final int position, final Alliance alliance)
	{
		super(position, alliance);
		this.pieceType = PieceType.KING;
	}
	
	@Override
	public List<Move> findPossibleMoves(Board board)
	{
		List<Move> possibleMovesList = new ArrayList<>();
		int potentialAbsolutePosition;
		
		for(int offset : possibleMoveOffset)
		{
			potentialAbsolutePosition = position + offset;
			if(isPositionValidTargetForKing(board, position, potentialAbsolutePosition))
			{
				if(board.isBoardFieldOccupied(potentialAbsolutePosition))
				{
					possibleMovesList.add(new AttackMove(board, this, 
							board.getPieceOnField(potentialAbsolutePosition), position, potentialAbsolutePosition));
				}
				else
				{
					possibleMovesList.add(new CommonMove(board, this, position, potentialAbsolutePosition));
				}
			}
		}
		return possibleMovesList;
	}
	@Override	
	public List<Move> findPossibleAttackMoves(final Board board)
	{
		return findPossibleMoves(board);
	}
	
	private final boolean isPositionValidTargetForKing(Board board, int currentPosition, int targetPosition)
	{
		/*
		 * Check upper and bottom border
		 */
		if(isPositionOutOfTheBoardLinear(targetPosition))
			return false;
		/*
		 * Check right and left border
		 */
		if(isNewPositionCrossingTheBoard(currentPosition, targetPosition))
			return false;
		/*
		 * Check if is not occupied by the same alliance
		 */
		if(board.isBoardFieldOccupied(targetPosition) && 
				board.getPieceOnField(targetPosition).getAlliance() == alliance)
			return false;
		/*
		 * Check if there will be no check in target field
		 */
		if(board.getActiveAlliance() == getAlliance())
		{
			/* 
			 * Temporarily remove this king piece from its field to check
			 *  if each of the field surrounding it could be under attack 
			 */
			board.removePieceFromField(this, this.position);
			boolean tmpIsValidPositionForKing;
			boolean isTargetFieldOccupied = board.isBoardFieldOccupied(targetPosition);
			Piece targetPositionEnemyPiece = null;
			if(isTargetFieldOccupied)
				targetPositionEnemyPiece = board.removePieceFromField(
						board.getPieceOnField(targetPosition), targetPosition);
			if(board.isFieldUnderAttack(targetPosition, getAlliance()))
				tmpIsValidPositionForKing = false;
			else
				tmpIsValidPositionForKing = true;
			
			if(isTargetFieldOccupied)
				board.putPieceOnField(targetPositionEnemyPiece, targetPosition);
			board.putPieceOnField(this, this.position);
			return tmpIsValidPositionForKing;
		}
		return true;
	}
	
	public final boolean hasPossibleMoves(final Board board)
	{
		List<Move> possibleMovesList = findPossibleMoves(board);
		if (possibleMovesList.isEmpty())
			return false;
		return true;
	}
	
	public final King isInCheck(Board board)
	{
		if(board.isFieldUnderAttack(position, alliance))
			return this;
		return null;
	}
	
	@Override
	public String toString()
	{
		return PieceType.KING.toString();
	}
}