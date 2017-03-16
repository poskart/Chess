package chess.view;

import chess.model.aux.Alliance;
import chess.model.board.Board;
import chess.model.game.Move;
import chess.model.pieces.Piece;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class BoardTable 
{
	private final JFrame gameFrame;
	private final BoardPanel gameBoardPanel;
	
	private static final Dimension BOARD_DIMENSION = new Dimension(GUISettings.BOARD_SIZE, GUISettings.BOARD_SIZE);
	private static final Dimension FIELD_DIMENSION = new Dimension(GUISettings.FIELD_SIZE, GUISettings.FIELD_SIZE);

	private static final Color DARK_FIELD_COLOR = new Color(30, 30, 30);
	private static final Color WHITE_FIELD_COLOR = new Color(220, 220, 220);
	
	protected List<Move> highlightedMoves;
	
	private Board gameBoard;
	
	public BoardTable(Board board)
	{
		this.gameFrame = new JFrame("Chess");
		this.gameFrame.setSize(BOARD_DIMENSION);
		final JMenuBar gameMenuBar = new JMenuBar();
		gameMenuBar.add(createMainMenu());
		this.gameFrame.setJMenuBar(gameMenuBar);
		gameBoardPanel = new BoardPanel();
		this.gameFrame.add(this.gameBoardPanel, BorderLayout.CENTER);
		this.gameFrame.setVisible(true);
		gameBoard = board;
		highlightedMoves = null;
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
	
	public void setInitialGameBoard()
	{
		gameBoardPanel.drawPieces(gameBoard.getBlackPieces(), gameBoard.getWhitePieces());
	}
	
	void addController(MouseListener controller)
	{
		gameBoardPanel.addController(controller);
	}
	
	public void highlightPossibleMoves(final int fieldId)
	{
		highlightedMoves = gameBoard.getPieceOnField(fieldId).findPossibleMoves(gameBoard);
		for(Move move : highlightedMoves)
		{
			gameBoardPanel.highlight(move.getTargetPosition());
		}
	}
	
	public void removeHighlight()
	{
		if(highlightedMoves != null)
		{
			for(Move move : highlightedMoves)
			{
				gameBoardPanel.removeHighlight(move.getTargetPosition());
			}
		}
		highlightedMoves.clear();
	}
	
	public void redrawFieldPanel(final int position)
	{
		gameBoardPanel.fieldArray.get(position).redrawField(gameBoard);
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
		
		public void drawPieces(final Collection<Piece> firstPiecesSet, final Collection<Piece> secondPiecesSet)
		{
			for(final Piece piece : firstPiecesSet)
				fieldArray.get(piece.getPosition()).drawPiece(piece);
			for(final Piece piece : secondPiecesSet)
				fieldArray.get(piece.getPosition()).drawPiece(piece);
		}
		
		public void addController(MouseListener controller)
		{
			for(final FieldPanel panel : fieldArray)
			{
				panel.addMouseListener((MouseListener) controller);
			}
		}
		
		public void highlight(final int panelId)
		{
			fieldArray.get(panelId).highlight();
		}
		
		public void removeHighlight(final int panelId)
		{
			fieldArray.get(panelId).removeHighlight();
		}
	}
	
	public class FieldPanel extends JPanel
	{
		private final int fieldId;
		private JLabel pieceLabel;
		private JLabel otherLabel;
		
		FieldPanel(final int fieldId)
		{
			super(new BorderLayout());
			this.fieldId = fieldId;
			setPreferredSize(FIELD_DIMENSION);
			setBackground(DARK_FIELD_COLOR);
			assignColorToField();
			validate();
			otherLabel = null;
			pieceLabel = null;
		}
		
		public final int getPanelId()
		{
			return fieldId;
		}
		
		public void redrawField(final Board board)
		{
			removeAll();
			assignColorToField();
			if(board.isBoardFieldOccupied(fieldId))
				drawPiece(board.getPieceOnField(fieldId));
			validate();
			repaint();
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
		
		public void drawPiece(Piece piece)
		{
			removeAll();
			String fileName = new String(GUISettings.PIECE_IMAGES_PATH);
			if(piece.getAlliance() == Alliance.BLACK)
				fileName += "B";
			else
				fileName += "W";
			fileName += piece.getPieceType().toString() + ".gif";
			try
			{
				ImageIcon image = new ImageIcon(fileName);
				this.pieceLabel = new JLabel("", image, JLabel.CENTER);
				add(pieceLabel, BorderLayout.CENTER);
//				validate();
//				repaint();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		public void highlight()
		{
			try
			{
				ImageIcon image = new ImageIcon(GUISettings.HIGHLIGHT_IMAGE_PATH);
				otherLabel = new JLabel("", image, JLabel.CENTER);
				add(otherLabel, BorderLayout.CENTER);
				validate();
				repaint();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		public void removeHighlight()
		{
			remove(otherLabel);
			otherLabel = null;
			validate();
			repaint();
		}
	}
}
