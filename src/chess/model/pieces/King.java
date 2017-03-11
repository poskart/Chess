package chess.model.pieces;

import java.util.ArrayList;
import java.util.List;
import chess.model.aux.Alliance;
import chess.model.board.Board;
import chess.model.board.Move;
import chess.model.pieces.Piece.PieceType;

public class King extends Piece 
{
	private static final int[] possibleMoveOffset = {-9, -8, -7, -1, 1, 7, 8, 9};
	
	public King(final int position, final Alliance alliance)
	{
		super(position, alliance);
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
				possibleMovesList.add(new Move(this, potentialAbsolutePosition));
		}
		return possibleMovesList;
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
		if(board.isFieldUnderAttack(targetPosition, getAlliance()))
			return false;
		
		return true;
	}
	
	public final boolean isCheck(final Board board)
	{
		List<Move> possibleMovesList = findPossibleMoves(board);
		if (possibleMovesList.isEmpty())
			return true;
		return false;
	}
	
	@Override
	public String toString()
	{
		return PieceType.KING.toString();
	}
}