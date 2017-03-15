package chess.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import chess.model.Model;
import chess.view.View;

public final class Controller implements ActionListener
{
	private final Model gameModel;
	private final View gameView;
	
	public Controller(final Model model, final View view)
	{
		this.gameModel = model;
		this.gameView = view;
	}
	
	public void initialize()
	{
		gameView.setInitialGameBoard();
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		
	}
}
