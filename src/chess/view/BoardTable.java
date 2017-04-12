package chess.view;

import chess.model.aux.Alliance;
import chess.model.board.Board;
import chess.model.game.Move;
import chess.model.pieces.Piece;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This class is main View class for the chess game.
 * It represents board with separate board fields within it
 * and provides data structures and methods that allow to view
 * current state of the board.
 * 
 * @author piotr
 *
 */
public class BoardTable 
{
	/** Main frame of the GUI */
	private final JFrame gameFrame;
	/** Main board panel (@JPanel extension) with separate 8x8 (@JPanel) fields */
	private final BoardPanel gameBoardPanel;
	/** Constant Dimension of the chess board */
	private static final Dimension BOARD_DIMENSION = 
			new Dimension(GUISettings.BOARD_SIZE, GUISettings.BOARD_SIZE);
	/** Constant dimension of the board field */
	private static final Dimension FIELD_DIMENSION =
			new Dimension(GUISettings.FIELD_SIZE, GUISettings.FIELD_SIZE);
	/** Container for currently highlighted moves */
	protected List<Move> highlightedMoves;
	/** Variable to store board model to which this @BoardTable class refers to*/
	private Board gameBoard;
	/** Is possible moves highlight enabled flag */
	protected boolean isHighlightEnabled;
	/**
	 * Initializes new BoardTable (view) object
	 * @param board is the board model
	 */
	public BoardTable(Board board)
	{
		gameBoard = board;
		gameFrame = new JFrame("Chess");
		gameFrame.setTitle("Chess");
		gameFrame.setSize(BOARD_DIMENSION);
		final JMenuBar gameMenuBar = new JMenuBar();
		gameMenuBar.add(createMainMenu());
		gameFrame.setJMenuBar(gameMenuBar);
		
		gameBoardPanel = new BoardPanel();
		gameFrame.add(gameBoardPanel, BorderLayout.CENTER);
		gameFrame.setVisible(true);
		highlightedMoves = null;
		isHighlightEnabled = false;
	}
	/**
	 * This method creates @JMenu main menu for the game.
	 * @return new @JMenu object with specified JmenuItems
	 */
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
		final JCheckBoxMenuItem highlight = new JCheckBoxMenuItem("Highlight possible moves");
		highlight.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					setHighlight(highlight.getState());
				}
			});
		mainMenu.add(highlight);
		mainMenu.add(exit);
		return mainMenu;
	}
	
	/**
	 * Prints match result on the screen when game is finished
	 * 
	 * @param winningAlliance is an alliance (color) of the winning side.
	 */
	public void printResult(final Alliance winningAlliance)
	{
		JLabel winLabel = null;
		if(winningAlliance == null)
			winLabel = new JLabel("Game over: Stalemate");
		else if(winningAlliance == Alliance.BLACK)
			winLabel = new JLabel("Check mate, black wins!");
		else
			winLabel = new JLabel("Check mate, white wins!");
		winLabel.setFont(new Font("Arial", 1, 32));
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(GUISettings.GAME_OVER_PANEL_LENGTH, 
				GUISettings.GAME_OVER_PANEL_HIGHT));
		panel.setBackground(new Color(20, 80, 7, 160));
		panel.add(winLabel, BorderLayout.CENTER);
		panel.setLocation(20, 300);
		gameFrame.remove(gameBoardPanel);
		gameFrame.add(panel, BorderLayout.CENTER);
		panel.validate();
		panel.repaint();
		this.gameFrame.setVisible(true);
	}
	
	/**
	 * Prints initial state of the game board with complete sets
	 * of white and black pieces.
	 */
	public void setInitialGameBoard()
	{
		gameBoardPanel.drawPieces(gameBoard.getBlackPieces(), gameBoard.getWhitePieces());
	}
	/**
	 * Adds controller (@MouseListener) to the bottom level @BoardPanel object
	 * to be passed to board JPanel fields.
	 * @param controller
	 */
	void addController(MouseListener controller)
	{
		gameBoardPanel.addController(controller);
	}
	/**
	 * Finds possible moves for the piece pointed by fieldId and calls
	 * @BoardPanel object to highlight specific fields on the board.
	 * @param fieldId - absolute position for piece on which possible moves are highlighted.
	 */
	public void highlightPossibleMoves(final int fieldId)
	{
		highlightedMoves = gameBoard.getPieceOnField(fieldId).findPossibleMoves(gameBoard);
		gameBoard.removeAllCheckMakingMoves(highlightedMoves);
		for(Move move : highlightedMoves)
		{
			gameBoardPanel.highlight(move.getTargetPosition());
		}
	}
	/**
	 * Removes currently highlighted fields.
	 */
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
	/**
	 * Redraw board field panel after its state has changed (i.e. piece was removed).
	 * @param position - position of the field to be redrawn.
	 */
	public void redrawFieldPanel(final int position)
	{
		gameBoardPanel.fieldArray.get(position).redrawField(gameBoard);
	}
	/**
	 *	Set value of isHighlightEnabled variable to turn on or turn off
	 *	possible moves highlight
	 *@param 
	 */
	public void setHighlight(final boolean highlightEnabled)
	{
		isHighlightEnabled = highlightEnabled;
	}
	/**
	 * This method returns main JFrame object
	 * @return main frame object (JFrame)
	 */
	public JFrame getMainFrame()
	{
		return gameFrame;
	}
	
	/**
	 * Class which represents game board panel with separate field panels.
	 * This class object contains all interactive 64 field panels which are
	 * clicked on by the player. 
	 * @author piotr
	 *
	 */
	private class BoardPanel extends JPanel
	{
		/** Container for all 64 field panels on the chess board */ 
		final List<FieldPanel> fieldArray;
		
		/**
		 * BoardPanel constructor initializes new BoardPanel object
		 * and add 64 new @FieldPanel objects to itself.
		 */
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
		
		/**
		 * This method draws pieces passed as collections on the board.
		 * @param firstPiecesSet - first set of pieces to be drawn on the board
		 * @param secondPiecesSet - second set of pieces to be drawn on the board
		 */
		public void drawPieces(final Collection<Piece> firstPiecesSet, 
				final Collection<Piece> secondPiecesSet)
		{
			for(final Piece piece : firstPiecesSet)
				fieldArray.get(piece.getPosition()).drawPiece(piece);
			for(final Piece piece : secondPiecesSet)
				fieldArray.get(piece.getPosition()).drawPiece(piece);
		}
		/**
		 * This method add @MouseListener object to all field panels within 
		 * this BoardPanel object which can control corresponding field panels.
		 * @param controller - Controller object which listen @FieldPanel objects while
		 * clicked with mouse
		 */
		public void addController(MouseListener controller)
		{
			for(final FieldPanel panel : fieldArray)
			{
				panel.addMouseListener((MouseListener) controller);
			}
		}
		/**
		 * Highlights correcponding field panel
		 * @param panelId - position of field panel to be highlighted.
		 */
		public void highlight(final int panelId)
		{
			fieldArray.get(panelId).highlight();
		}
		/**
		 * Removes highlight from the given field panel.
		 * @param panelId - position of field panel to remove highlight.
		 */
		public void removeHighlight(final int panelId)
		{
			fieldArray.get(panelId).removeHighlight();
		}
	}
	
	/**
	 * This class represents field of the chess game board.
	 * Object of this class is drawn on the GUI board view
	 * and interacts with user by mouse actions.
	 * @author piotr
	 */
	public class FieldPanel extends JPanel
	{
		/** Position of the field panel in the board*/
		private final int fieldId;
		/** Variable which keeps label with piece image*/
		private JLabel pieceLabel;
		/** Variable which keeps label with other (highlight) image*/
		private JLabel otherLabel;
		
		/**
		 * @FieldPanel object constructor. Initializes new FielPanel object
		 * with the color corresponding to field position on the board
		 * and with the corresponding piece if it exists.
		 * @param fieldId - position of the field on the board
		 */
		FieldPanel(final int fieldId)
		{
			super(new BorderLayout());
			this.fieldId = fieldId;
			setPreferredSize(FIELD_DIMENSION);
			setBackground(GUISettings.DARK_COLOR);
			assignColorToField();
			if(gameBoard.getPieceOnField(fieldId) != null)
				drawPiece(gameBoard.getPieceOnField(fieldId));
			validate();
			otherLabel = null;
			pieceLabel = null;
		}
		/**
		 * Returns field position.
		 * @return field position on the board.
		 */
		public final int getPanelId()
		{
			return fieldId;
		}
		/**
		 * Redraws panel on the GUI.
		 * @param board - board model which handles the game
		 */
		public void redrawField(final Board board)
		{
			removeAll();
			assignColorToField();
			if(board.isBoardFieldOccupied(fieldId))
				drawPiece(board.getPieceOnField(fieldId));
			validate();
			repaint();
		}
		/**
		 * Assigns color to field panel based on its position.
		 */
		private void assignColorToField()
		{
			int rowParity = fieldId/8;
			rowParity %= 2;
			if(fieldId % 2 == rowParity)
				setBackground(GUISettings.DARK_COLOR);
			else
				setBackground(GUISettings.WHITE_COLOR);
		}
		/**
		 * Draws piece image on the field panel.
		 * @param piece - piece to be drawn on the panel.
		 */
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
		/**
		 * Draws green dot on the field panel which means 
		 * this is allowed field for specific piece to move to.
		 */
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
		/**
		 * Removes highlight image from the field panel.
		 */
		public void removeHighlight()
		{
			remove(otherLabel);
			otherLabel = null;
			validate();
			repaint();
		}
	}
}
