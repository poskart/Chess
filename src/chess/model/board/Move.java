package chess.model.board;

import chess.model.pieces.Piece;

public class Move 
{
	private final Piece movedPiece;
	private final int targetPosition;
	
	public Move(final Piece movedPiece, final int targetPosition)
	{
		this.movedPiece = movedPiece;
		this.targetPosition = targetPosition;
	}
	
	public final int getTargetPosition()
	{
		return targetPosition;
	}
	
	public final void execute()
	{
		if(!movedPiece.wasAlreadyMoved())
			movedPiece.setFirstMoveFlag(true);
	}
}
