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

/**
 * Basic model class in MVC approach. This class is main chess model
 * which put together main game data types container and logic (Board 
 * object) with Player objects and some variables and flags to handle 
 * the game.
 * 
 * @author Piotr Poskart
 *
 */
public final class Model extends Observable
{
	/** Main model class which defines data types containers and 
	 * game logic */
	private Board gameBoard;
	/** White player object */
	private WhitePlayer wPlayer;
	/** Black player object */
	private BlackPlayer bPlayer;
	/** Active player reference */
	private Player activePlayer;
	/** Current king in check reference */
	private King kingInCheck;
	/** Is game over flag */
	private boolean gameOver;
	/** If the game results in stalemate */
	private boolean stalemate;
	/** If the game results in checkmate */
	private boolean checkmate;
	/** Alliance of the winning side */
	private Alliance winningAlliance;
	
	/**
	 * Model object constructor. Initializes all model attributes.
	 * Creates new Board object and player objects. Sets the first
	 *  active player.
	 */
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
	/**
	 * This method resets model to its default state and sets all
	 * the parameters to its initial values as at the beginning of the game.
	 */
	public void resetModel()
	{
		gameBoard.resetBoard();
		activePlayer = wPlayer;
		gameBoard.updateActiveAlliance(activePlayer.getAlliance());
		kingInCheck = null;
		gameOver = false;
		stalemate = false;
		checkmate = true;
		winningAlliance = null;
	}
	/**
	 * Executes move given in method's parameter, updates board pieces
	 * count, checks game end conditions, change active player and 
	 * notifies model Observer (View object).
	 * @param move is next legal move to be executed. 
	 */
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
	
	/** 
	 * Returns currently active player.
	 * @return reference to Player object with active turn. 
	 */
	public Player getActivePlayer()
	{
		return activePlayer;
	}
	/**
	 * Changes active player (turn) to the next player.
	 */
	private void changePlayer()
	{
		if(activePlayer == wPlayer)
			activePlayer = bPlayer;
		else 
			activePlayer = wPlayer;
		gameBoard.updateActiveAlliance(activePlayer.getAlliance());
	}
	/**
	 * Checks end game conditions after player activePlayer given in the method's
	 * parameter has executed his move. If it detect end game condition then 
	 * appropriate flags are set (stalemate, checkmate).
	 * @param kingInCheck - reference to King which is currently in check
	 * @param activePlayer - reference to active player who has just executed 
	 * his move.
	 */
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
	/**
	 * This method handles two positions received from controller object. Based on
	 * this positions this method determine if there is move chosen by the player 
	 * and executed move if there was correct pieces on the board pressed.
	 * @param currentPosition - the last clicked board field position
	 * @param previousPosition - previous clicked board field position
	 * @return Move object if executed, null otherwise.
	 */
	public final Move handleTwoTilesPressed(final int currentPosition, final int previousPosition)
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
						return possibleMove;
					}
				}
			}
		}
		return null;
	}
	/**
	 * Return reference to game board object
	 * @return reference to game board object
	 */
	public Board getGameBoard()
	{
		return gameBoard;
	}
	/**
	 * Check is the game over or not.
	 * @return true if game is over, false otherwise.
	 */
	public final boolean isGameOver()
	{
		return gameOver;
	}
	/**
	 * Check the winning player alliance
	 * @return alliance of the winning player
	 */
	public Alliance getWinningAlliance()
	{
		return winningAlliance;
	}
	/**
	 * Checks if there was a stalemate
	 * @return true if stalemate, false otherwise
	 */
	public boolean wasStalemate()
	{
		return stalemate;
	}
	/**
	 * Checks whether game finished with checkmate
	 * @return true if checkmate, false otherwise
	 */
	public boolean wasCheckMate()
	{
		return checkmate;
	}
}
