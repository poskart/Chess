package chess.model;

import java.util.List;
import java.util.Observable;

import chess.model.game.Player;
import chess.model.game.WhitePlayer;
import chess.model.pieces.King;
import chess.model.pieces.Piece;
import chess.model.aux.Alliance;
import chess.model.board.Board;
import chess.model.game.BlackPlayer;
import chess.model.game.Move;

public final class Model extends Observable
{
	private Board gameBoard;
	private WhitePlayer wPlayer;
	private BlackPlayer bPlayer;
	private Player activePlayer;
	private King kingInCheck;
	private boolean gameOver;
	private boolean stalemate;
	private boolean checkmate;
	private Alliance winningAlliance;
	
	public Model()
	{
		gameBoard = new Board();
		wPlayer = new WhitePlayer();
		bPlayer = new BlackPlayer();
		activePlayer = wPlayer;
		gameBoard.updateActiveAlliance(activePlayer.getAlliance());
		kingInCheck = null;
		gameOver = false;
		stalemate = false;
		checkmate = true;
		winningAlliance = null;
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
		kingInCheck = (King) gameBoard.isKingInCheck(
				activePlayer.getAlliance().getContraryAlliance());
		checkGameOverConditions(kingInCheck, activePlayer);
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
	
	private void checkGameOverConditions(final King kingInCheck, final Player activePlayer)
	{
		List<Move> legalMoves = (List<Move>)gameBoard.getAllLegalMovesOfAlliance(
				activePlayer.getAlliance().getContraryAlliance());
		legalMoves = (List<Move>)gameBoard.removeAllCheckMakingMoves(legalMoves);
		if(legalMoves.isEmpty())
		{
			gameOver = true;
			if(kingInCheck != null)
			{
				checkmate = true;
				winningAlliance = kingInCheck.getAlliance().getContraryAlliance();
			}
			else
				stalemate = true;
		}
	}
	
	public void handleTwoTilesPressed(final int currentPosition, final int previousPosition)
	{
		Piece pieceToMove = gameBoard.getPieceOnField(previousPosition);
		if(pieceToMove != null)
		{
			if(pieceToMove.getAlliance() == activePlayer.getAlliance())
			{
				List<Move> movesList = pieceToMove.findPossibleMoves(gameBoard);
				gameBoard.removeAllCheckMakingMoves(movesList);
				for(Move possibleMove : movesList)
				{
					if(possibleMove.getTargetPosition() == currentPosition
							&& possibleMove.getSourcePosition() == previousPosition)
					{
						executeMove(possibleMove);
						break;
					}
				}
			}
		}
	}
	
	public Board getGameBoard()
	{
		return gameBoard;
	}
	
	public final boolean isGameOver()
	{
		return gameOver;
	}
	
	public Alliance getWinningAlliance()
	{
		return winningAlliance;
	}
	
	public boolean wasStalemate()
	{
		return stalemate;
	}
	
	public boolean wasCheckMate()
	{
		return checkmate;
	}
}
