package chess.model;

import java.util.List;
import java.util.Observable;

import chess.model.game.Player;
import chess.model.game.WhitePlayer;
import chess.model.pieces.Piece;
import chess.model.aux.Alliance;
import chess.model.board.Board;
import chess.model.game.BlackPlayer;
import chess.model.game.Move;

public final class Model extends Observable
{
	Board gameBoard;
	WhitePlayer wPlayer;
	BlackPlayer bPlayer;
	Player activePlayer;
	
	public Model()
	{
		gameBoard = new Board();
		wPlayer = new WhitePlayer();
		bPlayer = new BlackPlayer();
		activePlayer = wPlayer;
		gameBoard.updateActiveAlliance(activePlayer.getAlliance());
	}
	
	public void executeMove(Move move)
	{
		move.execute();
		final Piece capturedPiece = move.getAttackedPiece();
		if(capturedPiece != null)
		{
			if(capturedPiece.getAlliance() == Alliance.BLACK)
				gameBoard.recomputePieces(Alliance.BLACK);
			else
				gameBoard.recomputePieces(Alliance.WHITE);
		}
		//if(move.czy to ruch zbijający)
			//gameBoard.recomputePieces();
		//gameBoard.calculateAllLegalMoves - sprawdź czy gość ma jeszcz jakies ruchy - remis itd
		changePlayer();
		setChanged();
		notifyObservers(move);
	}
	
	public Player getActivePlayer()
	{
		return activePlayer;
	}
	
	private void changePlayer()
	{
		if(activePlayer == wPlayer)
			activePlayer = bPlayer;
		else 
			activePlayer = wPlayer;
		gameBoard.updateActiveAlliance(activePlayer.getAlliance());
	}
	
	public void handleTwoTilesPressed(final int currentPosition, final int previousPosition)
	{
		Piece pieceToMove = gameBoard.getPieceOnField(previousPosition);
		if(pieceToMove != null)
		{
			if(pieceToMove.getAlliance() == activePlayer.getAlliance())
			{
				List<Move> movesList = pieceToMove.findPossibleMoves(gameBoard);
				for(Move possibleMove : movesList)
				{
					if(possibleMove.getTargetPosition() == currentPosition
							&& possibleMove.getSourcePosition() == previousPosition)
						executeMove(possibleMove);
				}
			}
		}
	}
	
	public Board getGameBoard()
	{
		return gameBoard;
	}
}
