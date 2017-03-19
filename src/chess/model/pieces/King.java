package chess.model.pieces;

import java.util.ArrayList;
import java.util.List;
import chess.model.aux.Alliance;
import chess.model.board.Board;
import chess.model.game.Move;
import chess.model.game.Move.AttackMove;
import chess.model.game.Move.CommonMove;
import chess.model.game.Move.CastlingMove;

public class King extends Piece 
{
	private static final int[] possibleMoveOffset = {-9, -8, -7, -1, 1, 7, 8, 9};
	/**
	 * King constructor, initializes king piece with alliance and given position
	 * 
	 * @param position new king's position on the board
	 * @param alliance new king's alliance
	 */
	public King(final int position, final Alliance alliance)
	{
		super(position, alliance);
		this.pieceType = PieceType.KING;
	}
	/**
	 * Finds available legal king moves including castling
	 * 
	 * @param board board object used in the game
	 * @return list of king possible moves
	 */
	@Override
	public List<Move> findPossibleMoves(Board board)
	{
		List<Move> possibleMovesList = findPossibleAttackMoves(board);
		possibleMovesList = addCastlingMoves(possibleMovesList, board);
		return possibleMovesList;
	}
	/**
	 * Finds available legal attack moves for king (without castling)
	 * 
	 * @param board board object used in the game
	 * @return list of king possible attack moves
	 */
	@Override	
	public List<Move> findPossibleAttackMoves(final Board board)
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
	
	/**
	 * Adds all possible castling moves for this king
	 * 
	 * @param possibleMoves is the list of all possible moves to which castling moves
	 * will be added
	 * @param board board object which game is using
	 * @return list of possible moves including castling
	 */
	public List<Move> addCastlingMoves(List<Move> possibleMoves, final Board board)
	{
		if(!wasAlreadyMoved() && isInCheck(board) == null)
		{
			int queenSideRookPosition = position - position % 8;
			int noQueenSideRookPosition = queenSideRookPosition + 7;
			/** Change rook positions */
			if(position % 8 == 3)
			{
				queenSideRookPosition += 7;
				noQueenSideRookPosition -= 7;
			}
			/** Check Queen side castling */
			if(board.isBoardFieldOccupied(queenSideRookPosition) && 
					!board.getPieceOnField(queenSideRookPosition).wasAlreadyMoved())
			{
				final int isRookPositionHigher = queenSideRookPosition > position? 1 : -1;
				int betweenAttackedFieldsCounter = 0;
				for(int betweenPosition = position + isRookPositionHigher;
						betweenPosition != queenSideRookPosition;
						betweenPosition += isRookPositionHigher)
				{
					if(board.isBoardFieldOccupied(betweenPosition))
						break;
					if(betweenAttackedFieldsCounter < 2)
					{
						if(board.isFieldUnderAttack(betweenPosition, this.alliance))
							break;
					}
					betweenAttackedFieldsCounter++;
				}
				if(betweenAttackedFieldsCounter >= 3)
					possibleMoves.add(new CastlingMove(
							board, this, position, position + isRookPositionHigher*2, queenSideRookPosition));
			}
			/** Check no Queen side castling */
			if(board.isBoardFieldOccupied(noQueenSideRookPosition) && 
					!board.getPieceOnField(noQueenSideRookPosition).wasAlreadyMoved())
			{
				final int isRookPositionHigher = noQueenSideRookPosition > position? 1 : -1;
				int betweenPosition;
				for(betweenPosition = position + isRookPositionHigher;
						betweenPosition != noQueenSideRookPosition;
						betweenPosition += isRookPositionHigher)
				{
					if(board.isBoardFieldOccupied(betweenPosition))
						break;
					if(board.isFieldUnderAttack(betweenPosition, this.alliance))
						break;
				}
				if(betweenPosition == noQueenSideRookPosition)
					possibleMoves.add(new CastlingMove(
							board, this, position, position + isRookPositionHigher*2, noQueenSideRookPosition));
			}
		}
		return possibleMoves;
	}
	/**
	 * Check if king has possible moves available
	 * @param board board object used in the game
	 * @return true if king has legal moves, false otherwise
	 */
	public final boolean hasPossibleMoves(final Board board)
	{
		List<Move> possibleMovesList = findPossibleMoves(board);
		if (possibleMovesList.isEmpty())
			return false;
		return true;
	}
	/**
	 * Determine whether king is in check or not
	 * @param board board object used in the game
	 * @return true if king is in check, false otherwise
	 */
	public final King isInCheck(Board board)
	{
		if(board.isFieldUnderAttack(position, alliance))
			return this;
		return null;
	}
	/**
	 * Prints king sign "K"
	 */
	@Override
	public String toString()
	{
		return PieceType.KING.toString();
	}
}