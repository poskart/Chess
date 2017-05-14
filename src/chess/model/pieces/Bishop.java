package chess.model.pieces;

import java.util.ArrayList;
import java.util.List;
import chess.model.common.Alliance;
import chess.model.board.Board;
import chess.model.game.Move;
import chess.model.game.Move.AttackMove;
import chess.model.game.Move.CommonMove;

/**
 * This class represents Bishop piece. It defines methods to 
 * calculate possible moves and other auxiliary functions.
 * @author Piotr Poskart
 *
 */
public class Bishop extends Piece 
{
	/** Table of offsets which defines possible Bishop moves on
	 * the linear positioned board fields */
	private static final int[] possibleMoveOffset = {-9, -7, 7, 9};
	/**
	 * Bishop constructor which takes position and alliance of new Bishop.
	 * @param position is position of new Bishop
	 * @param alliance is alliance of new Bishop
	 */
	public Bishop(final int position, final Alliance alliance)
	{
		super(position, alliance);
		this.pieceType = PieceType.BISHOP;
	}
	/**
	 * Bishop specific method which returns set of possible moves which Bishop
	 * can perform.
	 * @param board is reference to the board object
	 * @return container of Move object which this Bishop can perform.
	 */
	@Override
	public final List<Move> findPossibleMoves(final Board board)
	{
		return findPossibleBishopPieceMoves(board, this);
	}
	
	/**
	 * Static method which compute all possible Bishop moves for the given piece
	 * on specific position.
	 * @param board is reference to the board object
	 * @param examinedPiece is Bishop piece which moves are desired.
	 * @return list of Move object which are possible Bishop moves.
	 */
	public static final List<Move> findPossibleBishopPieceMoves(final Board board, final Piece examinedPiece)
	{
		List<Move> possibleMovesList = new ArrayList<>();
		int potentialAbsolutePosition, previousTestedPosition;
		
		for(int offset : possibleMoveOffset)
		{
			previousTestedPosition = examinedPiece.getPosition();
			potentialAbsolutePosition = examinedPiece.getPosition() + offset;
			while(potentialAbsolutePosition >= 0 && potentialAbsolutePosition < 64 &&
					!BishopAttackLineCrossedBoardBorder(previousTestedPosition, potentialAbsolutePosition))
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
	/**
	 * Bishop specific method which return set of possible attack moves which Bishop
	 * can perform. These are the same as common non attack) Bishop moves.
	 * @param board is reference to the board object
	 * @return container of Move object which Bishop can perform and that are 
	 * attack moves. 
	 */
	@Override	
	public List<Move> findPossibleAttackMoves(final Board board)
	{
		return findPossibleMoves(board);
	}
	/**
	 * This method checks if Bishop attack line crosses board border or not if searched 
	 * one after another.
	 * @param currentPosition is currently examined position within attack line
	 * @param previousPosition is previously examined position within attack line
	 * @return true if between current and previous position is board border, false otherwise.
	 */
	private static final boolean BishopAttackLineCrossedBoardBorder(final int currentPosition, final int previousPosition)
	{
		int roundedTo8DownDifference = (currentPosition - currentPosition % 8) - (previousPosition - previousPosition % 8);
		/*
		 * If subsequent positions in the same row or within 2 rows distance, then border crossed
		 */
		if(roundedTo8DownDifference == 0 || roundedTo8DownDifference == -16 || roundedTo8DownDifference == 16)
				return true;
		return false;
	}
	/**
	 * This is toString method specific for Bishop object. it returns String 
	 * with letter 'B' of the Bishop piece.
	 * @return String with letter of the Bishop.
	 */
	@Override
	public String toString()
	{
		return PieceType.BISHOP.toString();
	}
}