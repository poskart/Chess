package chess.model.pieces;

import java.util.ArrayList;
import java.util.List;

import chess.model.aux.Alliance;
import chess.model.board.Board;
import chess.model.game.Move;
import chess.model.game.Move.AttackMove;
import chess.model.game.Move.CommonMove;

public class Knight extends Piece
{
	private static final int[] possibleMoveOffset = {-17, -15, -10, -6, 6, 10, 15, 17};
	
	public Knight(int position, Alliance alliance)
	{
		super(position, alliance);
		this.pieceType = PieceType.KNIGHT;
	}
	
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
	@Override	
	public List<Move> findPossibleAttackMoves(final Board board)
	{
		return findPossibleMoves(board);
	}
	@Override
	public String toString()
	{
		return PieceType.KNIGHT.toString();
	}
	
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
