package chess.model.pieces;

import java.util.ArrayList;
import java.util.List;
import chess.model.aux.Alliance;
import chess.model.board.Board;
import chess.model.game.Move;
import chess.model.game.Move.AttackMove;
import chess.model.game.Move.CommonMove;

public class Bishop extends Piece 
{
	private static final int[] possibleMoveOffset = {-9, -7, 7, 9};
	
	public Bishop(final int position, final Alliance alliance)
	{
		super(position, alliance);
		this.pieceType = PieceType.BISHOP;
	}
	
	@Override
	public final List<Move> findPossibleMoves(final Board board)
	{
		return findPossibleBishopPieceMoves(board, this);
	}
	
	public static final List<Move> findPossibleBishopPieceMoves(final Board board, final Piece examinedPiece)
	{
		List<Move> possibleMovesList = new ArrayList<>();
		int potentialAbsolutePosition, previousTestedPosition;
		
		for(int offset : possibleMoveOffset)
		{
			previousTestedPosition = examinedPiece.getPosition();
			potentialAbsolutePosition = examinedPiece.getPosition() + offset;
			while(potentialAbsolutePosition >= 0 && potentialAbsolutePosition < 64 &&
					!bishopAttackLineCrossedBoardBorder(previousTestedPosition, potentialAbsolutePosition))
			{
				if(board.isBoardFieldOccupied(potentialAbsolutePosition))
				{
					if(board.getPieceOnField(potentialAbsolutePosition).alliance != examinedPiece.getAlliance())
						possibleMovesList.add(new AttackMove(board, examinedPiece, board.getPieceOnField(potentialAbsolutePosition), 
								examinedPiece.getPosition(), potentialAbsolutePosition));
					break;
				}
				possibleMovesList.add(new CommonMove(board, examinedPiece, examinedPiece.getPosition(), potentialAbsolutePosition));

				/*
				 * If it is the border of the board then break
				 */
				if(potentialAbsolutePosition < 8 || potentialAbsolutePosition >= 56 ||
						potentialAbsolutePosition % 8 == 0 || potentialAbsolutePosition % 8 == 7 )
					break;
				previousTestedPosition = potentialAbsolutePosition;
				potentialAbsolutePosition += offset;
			}
		}
		return possibleMovesList;
	}
	
	private static final boolean bishopAttackLineCrossedBoardBorder(final int currentPosition, final int previousPosition)
	{
		int roundedTo8DownDifference = (currentPosition - currentPosition % 8) - (previousPosition - previousPosition % 8);
		/*
		 * If subsequent positions in the same row or within 2 rows distance, then border crossed
		 */
		if(roundedTo8DownDifference == 0 || roundedTo8DownDifference == -16 || roundedTo8DownDifference == 16)
				return true;
		return false;
	}
	
	@Override
	public String toString()
	{
		return PieceType.BISHOP.toString();
	}
}