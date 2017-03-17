package chess.model.pieces;

import java.util.ArrayList;
import java.util.List;
import chess.model.aux.Alliance;
import chess.model.board.Board;
import chess.model.game.Move;
import chess.model.game.Move.AttackMove;
import chess.model.game.Move.CommonMove;

public class Pawn extends Piece 
{
	public Pawn(final int position, final Alliance alliance)
	{
		super(position, alliance);
		this.pieceType = PieceType.PAWN;
	}
	
	@Override
	public List<Move> findPossibleMoves(Board board)
	{
		List<Move> possibleMovesList = new ArrayList<>();
		int potentialAbsolutePosition;
		
		/*
		 * One field forward
		 */
		potentialAbsolutePosition = position + 8 * alliance.getDirection();
		if(!isPositionOutOfTheBoardLinear(potentialAbsolutePosition))
		{
			if(!board.isBoardFieldOccupied(potentialAbsolutePosition))
				possibleMovesList.add(new CommonMove(board, this, position, potentialAbsolutePosition));
		}
		/*
		 * Two fields forward
		 */
		if(!wasAlreadyMoved())
		{
			int betweenPosition = potentialAbsolutePosition;
			potentialAbsolutePosition = position + 16 * alliance.getDirection();
			if(!isPositionOutOfTheBoardLinear(potentialAbsolutePosition))
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
	
	private final boolean isFieldValidToCapture(
			Board board, final int currentPosition, final int targetPosition)
	{
		if(isPositionOutOfTheBoardLinear(targetPosition) ||
				isNewPositionCrossingTheBoard(currentPosition, targetPosition))
			return false;

		if(board.isBoardFieldOccupied(targetPosition) && 
				board.getPieceOnField(targetPosition).getAlliance() != getAlliance())
			return true;	
		
		return false;
	}
	
	@Override
	public String toString()
	{
		return PieceType.PAWN.toString();
	}
}