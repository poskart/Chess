package chess.model.pieces;

import java.util.ArrayList;
import java.util.List;
import chess.model.aux.Alliance;
import chess.model.board.Board;
import chess.model.game.Move;

public class Queen extends Piece 
{

	public Queen(final int position, final Alliance alliance)
	{
		super(position, alliance);
		this.pieceType = PieceType.QUEEN;
	}
	
	@Override
	public List<Move> findPossibleMoves(Board board) 
	{
		List<Move> possibleMovesList = new ArrayList<>();
		possibleMovesList = Rook.findPossibleRookPieceMoves(board, this);
		possibleMovesList.addAll(Bishop.findPossibleBishopPieceMoves(board, this));
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
		return PieceType.QUEEN.toString();
	}
}