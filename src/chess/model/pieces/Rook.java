package chess.model.pieces;

import java.util.ArrayList;
import java.util.List;
import chess.model.aux.Alliance;
import chess.model.board.Board;
import chess.model.board.Move;
import chess.model.pieces.Piece.PieceType;

public class Rook extends Piece 
{

	public Rook(final int position, final Alliance alliance)
	{
		super(position, alliance);
		this.pieceType = PieceType.ROOK;
	}
	
	@Override
	public final List<Move> findPossibleMoves(final Board board)
	{
		return findPossibleRookPieceMoves(board, this);
	}
	public static final List<Move> findPossibleRookPieceMoves(final Board board, final Piece examinedPiece)
	{
		List<Move> possibleMovesList = new ArrayList<>();
		int potentialAbsolutePosition;
		/*
		 * Add possible fields on the right
		 */
		potentialAbsolutePosition = examinedPiece.getPosition() + 1;
		while(potentialAbsolutePosition % 8 == 0)
		{
			if(board.isBoardFieldOccupied(potentialAbsolutePosition))
			{
				if(board.getPieceOnField(potentialAbsolutePosition).alliance != examinedPiece.getAlliance())
					possibleMovesList.add(new Move(examinedPiece, potentialAbsolutePosition));
				break;
			}
			possibleMovesList.add(new Move(examinedPiece, potentialAbsolutePosition));
			potentialAbsolutePosition++;
		}
		/*
		 * Add possible fields on the left
		 */
		potentialAbsolutePosition = examinedPiece.getPosition() - 1;
		while(potentialAbsolutePosition % 8 != 7 && potentialAbsolutePosition >= 0)
		{
			if(board.isBoardFieldOccupied(potentialAbsolutePosition))
			{
				if(board.getPieceOnField(potentialAbsolutePosition).alliance != examinedPiece.getAlliance())
					possibleMovesList.add(new Move(examinedPiece, potentialAbsolutePosition));
				break;
			}
			possibleMovesList.add(new Move(examinedPiece, potentialAbsolutePosition));
			potentialAbsolutePosition--;
		}
		/*
		 * Add possible fields above
		 */
		potentialAbsolutePosition = examinedPiece.getPosition() + 8;
		while(potentialAbsolutePosition < 64)
		{
			if(board.isBoardFieldOccupied(potentialAbsolutePosition))
			{
				if(board.getPieceOnField(potentialAbsolutePosition).alliance != examinedPiece.getAlliance())
					possibleMovesList.add(new Move(examinedPiece, potentialAbsolutePosition));
				break;
			}
			possibleMovesList.add(new Move(examinedPiece, potentialAbsolutePosition));
			potentialAbsolutePosition += 8;
		}
		/*
		 * Add possible fields on the bottom
		 */
		potentialAbsolutePosition = examinedPiece.getPosition() - 8;
		while(potentialAbsolutePosition >= 0)
		{
			if(board.isBoardFieldOccupied(potentialAbsolutePosition))
			{
				if(board.getPieceOnField(potentialAbsolutePosition).alliance != examinedPiece.getAlliance())
					possibleMovesList.add(new Move(examinedPiece, potentialAbsolutePosition));
				break;
			}
			possibleMovesList.add(new Move(examinedPiece, potentialAbsolutePosition));
			potentialAbsolutePosition -= 8;
		}
		return possibleMovesList;
	}
	
	@Override
	public String toString()
	{
		return PieceType.ROOK.toString();
	}
}
