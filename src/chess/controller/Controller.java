package chess.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import chess.model.Model;
import chess.model.pieces.Piece;
import chess.view.BoardTable.FieldPanel;
import chess.view.View;

public final class Controller implements MouseListener
{
	private final Model gameModel;
	private final View gameView;
	private Object previousSource;
	private int lastClickedPanelId;
	
	public Controller(final Model model, final View view)
	{
		this.gameModel = model;
		this.gameView = view;
		this.previousSource = null;
		this.lastClickedPanelId = -1;
	}
	
	public void initialize()
	{
		gameView.setInitialGameBoard();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if(!gameModel.isGameOver())
		{
			FieldPanel panel = (FieldPanel)e.getSource();
			if(panel == (FieldPanel)previousSource)
				return;
			final int panelId = panel.getPanelId();
			if(gameView.isHighlited())
				gameView.removeHighlight();
			if(gameModel.getGameBoard().isBoardFieldOccupied(panelId))
			{
				Piece piece = gameModel.getGameBoard().getPieceOnField(panelId);
				if(gameModel.getActivePlayer().getAlliance() == piece.getAlliance())
					gameView.highlightPossibleMoves(panelId);
			}
			gameModel.handleTwoTilesPressed(panelId, lastClickedPanelId);
			
			System.out.println(panel.getPanelId());
			previousSource = (FieldPanel)panel;
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
