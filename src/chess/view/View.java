package chess.view;

import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import chess.model.Model;
import chess.model.game.Move;

/**
 * This is the main View class in MVC approach. It extends main
 * GUI class BoardTable with implemented GUI and implements Observer
 * in order to perform updates immediately after the chess board model
 * is updated.
 * @author Piotr Poskart
 *
 */
public final class View extends BoardTable implements Observer 
{
	/** Reference to the main Model object in MVC approach */
	private Model gameModel;
	
	/**
	 * View constructor. Initializes model reference with the 
	 * given model Object and passes it to inner GUI objects..
	 * @param model reference to the main game Model object
	 */
	public View(final Model model)
	{
		super(model.getGameBoard());
		this.gameModel = model; 
	}
	
	/**
	 * Updates view after each Model notification. This method
	 * redraws board and print result view when game is ended.
	 * @param obs reference to the observable object which is the
	 * model object
	 * @param obj move which has just been executed in model..
	 */
	@Override
	public void update(Observable obs, Object obj)
	{
		Move move = (Move)obj;
		redrawBoard(move);
		if(move.isCastlingMove())
		{
			redrawBoard(move.getSpecialMove());
		}
		if(gameModel.isGameOver())
			printResult(gameModel.getWinningAlliance());
	}

	/**
	 * Passes controller object to the parent GUI object.
	 * @param controller controller object to be passed to parent
	 * view objects.
	 */
	public void addController(MouseListener controller)
	{
		super.addController(controller);
	}
	/**
	 * Calls setInitialGameBoard of the super class.
	 */
	public void setInitialGameBoard()
	{
		super.setInitialGameBoard();
	}
	/**
	 * Redraws field panels of the BoardTable object
	 * corresponding to previously executed move
	 * @param move move which has just been executed
	 */
	private void redrawBoard(Move move)
	{
		redrawFieldPanel(move.getSourcePosition());
		redrawFieldPanel(move.getTargetPosition());
	}
	/**
	 * Checks if legal moves are currently highlighted.
	 * @return true if legal moves are highlighted, false otherwise.
	 */
	public final boolean isHighlited()
	{
		if(super.highlightedMoves == null || super.highlightedMoves.isEmpty())
			return false;
		return true;
	}
	/**
	 * Highlights possible moves by calling super method.
	 * @param fieldId is position of the piece whose legal moves
	 * has to be highlighted.
	 */
	public void highlightPossibleMoves(final int fieldId )
	{
		super.highlightPossibleMoves(fieldId);
	}
	/**
	 * Removes highlighted fields from the view by calling super method.
	 */
	public void removeHighlight()
	{
		super.removeHighlight();
	}
	/**
	 * Check if legal moves highlighting is enabled
	 * @return true if highlight enabled, false otherwise.
	 */
	public final boolean isHighlightEnabled()
	{
		return isHighlightEnabled;
	}
	/**
	 * This method returns main view frame object (JFrame)
	 * @return main frame object (JFrame)
	 */
	public final JFrame getMainFrame()
	{
		return super.getMainFrame();
	}
}
