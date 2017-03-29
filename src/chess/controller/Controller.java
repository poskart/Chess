package chess.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import chess.model.Model;
import chess.model.pieces.Piece;
import chess.view.BoardTable.FieldPanel;
import chess.view.View;

/**
 * Main controller class which implements MouseListener interface
 * nedded my View section of the game. Controller initializes model
 * and view for the game and handles user actions on the game board. 
 * 
 * @author Piotr Poskart
 *
 */
public final class Controller implements MouseListener
{
	/** Main game model object */
	private final Model gameModel;
	/** Main game view object  */
	private final View gameView;
	/** Previous clicked panel object on the board */
	private Object previousPanelClicked;
	/** Id of the last clicked board panel */
	private int lastClickedPanelId;
	
	/**
	 * Controller constructor. Initializes game objects - model
	 * and view, as well as last clicked board panel attributes.
	 * 
	 * @param model - reference to main game model object
	 * @param view - reference to main game view object
	 */
	public Controller(final Model model, final View view)
	{
		this.gameModel = model;
		this.gameView = view;
		this.previousPanelClicked = null;
		this.lastClickedPanelId = -1;
	}
	/**
	 * Initializes view with the first game board setting.
	 */
	public void initialize()
	{
		gameView.setInitialGameBoard();
	}

	/**
	 * Performs mouse event handling on the board. This method
	 * sends information about pressed board panels to the game
	 * model object. It also decides if highlight should be shown
	 * based on pressed panel.
	 * 
	 * @param mouseEvent reference to performed mouse event 
	 */
	@Override
	public void mouseClicked(MouseEvent mouseEvent)
	{
		if(!gameModel.isGameOver())
		{
			FieldPanel panel = (FieldPanel)mouseEvent.getSource();
			if(panel == (FieldPanel)previousPanelClicked)
				return;
			final int panelId = panel.getPanelId();
			if(gameView.isHighlited())
				gameView.removeHighlight();
			if(gameModel.getGameBoard().isBoardFieldOccupied(panelId) && 
					gameView.isHighlightEnabled())
			{
				Piece piece = gameModel.getGameBoard().getPieceOnField(panelId);
				if(gameModel.getActivePlayer().getAlliance() == piece.getAlliance())
					gameView.highlightPossibleMoves(panelId);
			}
			gameModel.handleTwoTilesPressed(panelId, lastClickedPanelId);
			
			previousPanelClicked = (FieldPanel)panel;
			lastClickedPanelId = panel.getPanelId();
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e){
	}
	@Override
	public void mouseExited(MouseEvent e){
	}
	@Override
	public void mousePressed(MouseEvent e){
	}
	@Override
	public void mouseReleased(MouseEvent e){
	}
}
