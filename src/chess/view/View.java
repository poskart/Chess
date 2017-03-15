package chess.view;

import java.awt.event.ActionListener;
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
	
	public void addController(ActionListener controller)
	{
		
	}
	
	public void setInitialGameBoard()
	{
		super.setInitialGameBoard();
	}
	
	private void redrawBoard(Move move)
	{
		//if(SimpleMove)
		//
		//else if(Castling)
		//
		//else if (Other)
		//
	}
}
