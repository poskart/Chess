package chess.model.pieces;

import java.util.ArrayList;
import java.util.List;

import chess.model.aux.Alliance;
import chess.model.board.Board;
import chess.model.board.Move;
import chess.model.pieces.Piece.PieceType;

public class Knight extends Piece
{
	private static final int[] possibleMoveOffset = {-17, -15, -10, -6, 6, 10, 15, 17};
	
	public Knight(int position, Alliance alliance)
	{
		super(position, alliance);
	}
	
	@Override
	public final List<Move> findPossibleMoves(final Board board)
	{
		List<Move> possibleMovesList = new ArrayList<>();
		int potentialAbsolutePosition;
		for(int offset : possibleMoveOffset)
		{
			potentialAbsolutePosition = this.position + offset;
			if(board.isFieldValid(potentialAbsolutePosition))
			{
				if(board.isBoardFieldOccupied(potentialAbsolutePosition) && board.getPieceOnField(potentialAbsolutePosition).alliance == alliance)
					continue;
				possibleMovesList.add(new Move(this, potentialAbsolutePosition));
			}
		}
		return possibleMovesList;
	}
	
	@Override
	public String toString()
	{
		return PieceType.KNIGHT.toString();
	}
}
