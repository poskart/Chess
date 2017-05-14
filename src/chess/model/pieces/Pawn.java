package chess.model.pieces;

import java.util.ArrayList;
import java.util.List;
import chess.model.common.Alliance;
import chess.model.board.Board;
import chess.model.game.Move;
import chess.model.game.Move.AttackMove;
import chess.model.game.Move.CommonMove;
import chess.model.game.Move.PawnPromotionMove;
/**
 * This class represents Pawn piece. It defines methods to 
 * calculate possible moves and other auxiliary functions.
 * @author Piotr Poskart
 *
 */
public class Pawn extends Piece 
{
	/**
	 * Pawn constructor which takes position and alliance of new Pawn.
	 * @param position is position of new Pawn
	 * @param alliance is alliance of new Pawn
	 */
	public Pawn(final int position, final Alliance alliance)
	{
		super(position, alliance);
		this.pieceType = PieceType.PAWN;
	}
	/**
	 * Pawn specific method which returns set of possible moves which Pawn
	 * can perform.
	 * @param board is reference to the board object
	 * @return container of Move object which this Pawn can perform.
	 */
	@Override
	public List<Move> findPossibleMoves(Board board)
	{
		List<Move> possibleMovesList = new ArrayList<>();
		int potentialAbsolutePosition;
		
		/*
		 * One field forward
		 */
		potentialAbsolutePosition = position + 8 * alliance.getDirection();
		if(!Board.isPositionOutOfTheBoardLinear(potentialAbsolutePosition))
		{
			if(!board.isBoardFieldOccupied(potentialAbsolutePosition))
			{
				if(!board.isFieldValid(potentialAbsolutePosition + 8 * alliance.getDirection()))
					possibleMovesList.add(new PawnPromotionMove(board, this, position, potentialAbsolutePosition));
				else
					possibleMovesList.add(new CommonMove(board, this, position, potentialAbsolutePosition));
			}
		}
		/*
		 * Two fields forward
		 */
		if(!wasAlreadyMoved())
		{
			int betweenPosition = potentialAbsolutePosition;
			potentialAbsolutePosition = position + 16 * alliance.getDirection();
			if(!Board.isPositionOutOfTheBoardLinear(potentialAbsolutePosition))
			{
				if(!board.isBoardFieldOccupied(potentialAbsolutePosition) && !board.isBoardFieldOccupied(betweenPosition))
					possibleMovesList.add(new CommonMove(board, this, position, potentialAbsolutePosition));
			}
		}
		/*
		 * Capturing fields (diagonal)
		 */
		potentialAbsolutePosition = position + alliance.getDirection()*7;
		if(isFieldValidToCapture(board, position, potentialAbsolutePosition))
			possibleMovesList.add(new AttackMove(board, this, board.getPieceOnField(potentialAbsolutePosition),
					position, potentialAbsolutePosition));
		potentialAbsolutePosition = position + alliance.getDirection()*9;
		if(isFieldValidToCapture(board, position, potentialAbsolutePosition))
			possibleMovesList.add(new AttackMove(board, this, board.getPieceOnField(potentialAbsolutePosition),
					position, potentialAbsolutePosition));
		
		return possibleMovesList;
	}
	/**
	 * Pawn specific method which return set of possible attack moves which Pawn
	 * can perform. These are other than common (non attack) Pawn moves.
	 * @param board is reference to the board object
	 * @return container of Move object which Pawn can perform and that are 
	 * attack moves. 
	 */
	@Override	
	public List<Move> findPossibleAttackMoves(final Board board)
	{
		List<Move> possibleMovesList = new ArrayList<>();
		int potentialAbsolutePosition;
		/*
		 * Capturing fields (diagonal)
		 */
		potentialAbsolutePosition = position + alliance.getDirection()*7;
		if(!Board.isPositionOutOfTheBoardLinear(potentialAbsolutePosition) &&
				!Board.isNewPositionCrossingTheBoard(position, potentialAbsolutePosition))
			possibleMovesList.add(new AttackMove(board, this, board.getPieceOnField(potentialAbsolutePosition),
					position, potentialAbsolutePosition));
		potentialAbsolutePosition = position + alliance.getDirection()*9;
		if(!Board.isPositionOutOfTheBoardLinear(potentialAbsolutePosition) &&
				!Board.isNewPositionCrossingTheBoard(position, potentialAbsolutePosition))
			possibleMovesList.add(new AttackMove(board, this, board.getPieceOnField(potentialAbsolutePosition),
					position, potentialAbsolutePosition));
		
		return possibleMovesList;
	}
	/**
	 * This method checks if given target position is valid to move on.
	 * @param board reference to the board object
	 * @param currentPosition current position of this pawn
	 * @param targetPosition target position of this pawn
	 * @return true if target position is valid to move on, false otherwise.
	 */
	private final boolean isFieldValidToCapture(
			Board board, final int currentPosition, final int targetPosition)
	{
		if(Board.isPositionOutOfTheBoardLinear(targetPosition) ||
				Board.isNewPositionCrossingTheBoard(currentPosition, targetPosition))
			return false;

		if(board.isBoardFieldOccupied(targetPosition) && 
				board.getPieceOnField(targetPosition).getAlliance() != getAlliance())
			return true;	
		
		return false;
	}
	/**
	 * This is toString method specific for Pawn object. it returns String 
	 * with letter 'P' of the Pawn piece.
	 * @return String with letter of the Pawn.
	 */
	@Override
	public String toString()
	{
		return PieceType.PAWN.toString();
	}
}