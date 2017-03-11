package chess.view;

import chess.model.board.Board;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class BoardTable 
{
	private final JFrame gameFrame;
	private final BoardPanel gameBoardPanel;
	
	private static final Dimension BOARD_DIMENSION = new Dimension(640, 640);
	private static final Dimension FIELD_DIMENSION = new Dimension(10, 10);

	private static final Color DARK_FIELD_COLOR = new Color(30, 30, 30);
	private static final Color WHITE_FIELD_COLOR = new Color(220, 220, 220);
	
	public BoardTable()
	{
		this.gameFrame = new JFrame("Chess");
		this.gameFrame.setSize(BOARD_DIMENSION);
		final JMenuBar gameMenuBar = new JMenuBar();
		gameMenuBar.add(createMainMenu());
		this.gameFrame.setJMenuBar(gameMenuBar);
		gameBoardPanel = new BoardPanel();
		this.gameFrame.add(this.gameBoardPanel, BorderLayout.CENTER);
		this.gameFrame.setVisible(true);
	}
	
	private JMenu createMainMenu()
	{
		final JMenu mainMenu = new JMenu("Menu");
		final JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener()
			{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
			});
		mainMenu.add(exit);
		return mainMenu;
	}
	
	private class BoardPanel extends JPanel
	{
		final List<FieldPanel> fieldArray;
		
		BoardPanel()
		{
			super(new GridLayout(8, 8));
			this.fieldArray = new ArrayList<>();
			for(int i = 0; i < Board.BOARD_FIELDS_NUMBER; i++)
			{
				final FieldPanel newField = new FieldPanel(i);
				fieldArray.add(i, newField);
				add(newField);
			}
		}
	}
	
	private class FieldPanel extends JPanel
	{
		private final int fieldId;
		
		FieldPanel(final int fieldId)
		{
			super(new GridBagLayout());
			this.fieldId = fieldId;
			setPreferredSize(FIELD_DIMENSION);
			setBackground(DARK_FIELD_COLOR);
			assignColorToField();
			validate();
		}
		
		private void assignColorToField()
		{
			int rowParity = fieldId/8;
			rowParity %= 2;
			if(fieldId % 2 == rowParity)
				setBackground(DARK_FIELD_COLOR);
			else
				setBackground(WHITE_FIELD_COLOR);
		}
	}
}
