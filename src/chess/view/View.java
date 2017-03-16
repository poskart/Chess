package chess.view;

import java.awt.event.MouseListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import chess.model.Model;
import chess.model.game.Move;

public final class View extends BoardTable implements Observer 
{
	private Model gameModel;
	
	public View(final Model model)
	{
		super(model.getGameBoard());
		this.gameModel = model; 
	}
	
	@Override
	public void update(Observable obs, Object obj)
	{
		Move move = (Move)obj;
		redrawBoard(move);
	}

	public void addController(MouseListener controller)
	{
		super.addController(controller);
	}
	
	
	public void setInitialGameBoard()
	{
		super.setInitialGameBoard();
	}
	
	private void redrawBoard(Move move)
	{
		redrawFieldPanel(move.getSourcePosition());
		redrawFieldPanel(move.getTargetPosition());
	}
	
	public final boolean isHighlited()
	{
		if(super.highlightedMoves == null || super.highlightedMoves.isEmpty())
			return false;
		return true;
	}
	
	public void highlightPossibleMoves(final int fieldId )
	{
		super.highlightPossibleMoves(fieldId);
	}
	
	public void removeHighlight()
	{
		super.removeHighlight();
	}
}
