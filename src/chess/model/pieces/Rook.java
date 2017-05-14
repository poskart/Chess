package chess.model.pieces;

import java.util.ArrayList;
import java.util.List;
import chess.model.common.Alliance;
import chess.model.board.Board;
import chess.model.game.Move;
import chess.model.game.Move.AttackMove;
import chess.model.game.Move.CommonMove;

/**
* This class represents Rook piece. It defines methods to 
* calculate possible moves and other auxiliary funcions.
* @author Piotr Poskart
*
*/
public class Rook extends Piece 
{
	/**
	 * Rook constructor which takes position and alliance of new Rook.
	 * @param position is position of new rook
	 * @param alliance is alliance of new rook
	 */
	public Rook(final int position, final Alliance alliance)
	{
		super(position, alliance);
		this.pieceType = PieceType.ROOK;
	}
	/**
	 * Rook specific method which returns set of possible moves which Rook
	 * can perform.
	 * @param board is reference to the board object
	 * @return container of Move object which this Rook can perform.
	 */
	@Override
	public final List<Move> findPossibleMoves(final Board board)
	{
		return findPossibleRookPieceMoves(board, this);
	}
	/**
	 * Static method which compute all possible rook moves for the given piece
	 * on specific position.
	 * @param board is reference to the board object
	 * @param examinedPiece is rook piece which moves are desired.
	 * @return list of Move object which are possible rook moves.
	 */
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
	/**
	 * Rook specific method which return set of possible attack moves which Rook
	 * can perform. These are the same as common non attack) Rook moves.
	 * @param board is reference to the board object
	 * @return container of Move object which Rook can perform and that are 
	 * attack moves. 
	 */
	@Override	
	public List<Move> findPossibleAttackMoves(final Board board)
	{
		return findPossibleMoves(board);
	}
	/**
	 * This is toString method specific for Bishop object. it returns String 
	 * with letter of the Bishop piece.
	 * @return String with letter of the Rook.
	 */
	@Override
	public String toString()
	{
		return PieceType.ROOK.toString();
	}
}
