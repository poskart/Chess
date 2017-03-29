package chess.model.pieces;

import java.util.ArrayList;
import java.util.List;
import chess.model.aux.Alliance;
import chess.model.board.Board;
import chess.model.game.Move;
import chess.model.game.Move.AttackMove;
import chess.model.game.Move.CommonMove;

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
		int potentialAbsolutePosition, previouslyCheckedPosition;
		/*
		 * Add possible fields on the right
		 */
		previouslyCheckedPosition = examinedPiece.getPosition();
		potentialAbsolutePosition = previouslyCheckedPosition + 1;
		while(!Board.isNewPositionCrossingTheBoard(previouslyCheckedPosition, potentialAbsolutePosition)
				&& board.isFieldValid(potentialAbsolutePosition))
		{
			if(board.isBoardFieldOccupied(potentialAbsolutePosition))
			{
				if(board.getPieceOnField(potentialAbsolutePosition).alliance != examinedPiece.getAlliance())
					possibleMovesList.add(new AttackMove(board, examinedPiece, board.getPieceOnField(potentialAbsolutePosition), 
							examinedPiece.getPosition(), potentialAbsolutePosition));
				break;
			}
			possibleMovesList.add(new CommonMove(board, examinedPiece, examinedPiece.getPosition(), potentialAbsolutePosition));
			previouslyCheckedPosition = potentialAbsolutePosition;
			potentialAbsolutePosition++;
		}
		/*
		 * Add possible fields on the left
		 */
		previouslyCheckedPosition = examinedPiece.getPosition();
		potentialAbsolutePosition = previouslyCheckedPosition - 1;
		while(!Board.isNewPositionCrossingTheBoard(previouslyCheckedPosition, potentialAbsolutePosition)
				&& board.isFieldValid(potentialAbsolutePosition))
		{
			if(board.isBoardFieldOccupied(potentialAbsolutePosition))
			{
				if(board.getPieceOnField(potentialAbsolutePosition).alliance != examinedPiece.getAlliance())
					possibleMovesList.add(new AttackMove(board, examinedPiece, board.getPieceOnField(potentialAbsolutePosition), 
							examinedPiece.getPosition(), potentialAbsolutePosition));
				break;
			}
			possibleMovesList.add(new CommonMove(board, examinedPiece, examinedPiece.getPosition(), potentialAbsolutePosition));
			previouslyCheckedPosition = potentialAbsolutePosition;
			potentialAbsolutePosition--;
		}
		/*
		 * Add possible fields above
		 */
		previouslyCheckedPosition = examinedPiece.getPosition();
		potentialAbsolutePosition = previouslyCheckedPosition + 8;
		while(board.isFieldValid(potentialAbsolutePosition))
		{
			if(board.isBoardFieldOccupied(potentialAbsolutePosition))
			{
				if(board.getPieceOnField(potentialAbsolutePosition).alliance != examinedPiece.getAlliance())
					possibleMovesList.add(new AttackMove(board, examinedPiece, board.getPieceOnField(potentialAbsolutePosition), 
							examinedPiece.getPosition(), potentialAbsolutePosition));
				break;
			}
			possibleMovesList.add(new CommonMove(board, examinedPiece, examinedPiece.getPosition(), potentialAbsolutePosition));
			previouslyCheckedPosition = potentialAbsolutePosition;
			potentialAbsolutePosition += 8;
		}
		/*
		 * Add possible fields on the bottom
		 */
		previouslyCheckedPosition = examinedPiece.getPosition();
		potentialAbsolutePosition = previouslyCheckedPosition - 8;
		while(board.isFieldValid(potentialAbsolutePosition))
		{
			if(board.isBoardFieldOccupied(potentialAbsolutePosition))
			{
				if(board.getPieceOnField(potentialAbsolutePosition).alliance != examinedPiece.getAlliance())
					possibleMovesList.add(new AttackMove(board, examinedPiece, board.getPieceOnField(potentialAbsolutePosition), 
							examinedPiece.getPosition(), potentialAbsolutePosition));
				break;
			}
			possibleMovesList.add(new CommonMove(board, examinedPiece, examinedPiece.getPosition(), potentialAbsolutePosition));
			previouslyCheckedPosition = potentialAbsolutePosition;
			potentialAbsolutePosition -= 8;
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
		return PieceType.ROOK.toString();
	}
}
