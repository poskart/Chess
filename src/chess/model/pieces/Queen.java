package chess.model.pieces;

import java.util.ArrayList;
import java.util.List;
import chess.model.common.Alliance;
import chess.model.board.Board;
import chess.model.game.Move;
/**
 * This class represents Queen piece. It defines methods to 
 * calculate possible moves and other auxiliary functions of the Queen.
 * @author Piotr Poskart
 *
 */
public class Queen extends Piece 
{
	/**
	 * Queen constructor which takes position and alliance of new Queen.
	 * @param position is position of new Queen
	 * @param alliance is alliance of new Queen
	 */
	public Queen(final int position, final Alliance alliance)
	{
		super(position, alliance);
		this.pieceType = PieceType.QUEEN;
	}
	/**
	 * Queen specific method which returns set of possible moves which Queen
	 * can perform. It consists of Bishop and Rook moves.
	 * @param board is reference to the board object
	 * @return container of Move object which this Queen can perform.
	 */
	@Override
	public List<Move> findPossibleMoves(Board board) 
	{
		List<Move> possibleMovesList = new ArrayList<>();
		possibleMovesList = Rook.findPossibleRookPieceMoves(board, this);
		possibleMovesList.addAll(Bishop.findPossibleBishopPieceMoves(board, this));
		return possibleMovesList;
	}
	/**
	 * Queen specific method which return set of possible attack moves which Queen
	 * can perform. These are the same as common non attack) Queen moves.
	 * @param board is reference to the board object
	 * @return container of Move object which Queen can perform and that are 
	 * attack moves. 
	 */
	@Override	
	public List<Move> findPossibleAttackMoves(final Board board)
	{
		return findPossibleMoves(board);
	}
	/**
	 * This method returns String with 'Q' letter describing Queen object.
	 * @return String 'Q'
	 */
	@Override
	public String toString()
	{
		return PieceType.QUEEN.toString();
	}
}