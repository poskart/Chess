package chess.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

import chess.model.Model;
import chess.model.aux.Alliance;
import chess.model.game.Move;
import chess.model.game.Move.AttackMove;
import chess.model.game.Move.CastlingMove;
import chess.model.game.Move.CommonMove;
import chess.model.game.Move.EmptyMove;
import chess.model.game.Move.PawnPromotionMove;
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
	
    private static int PORT = 8901;
    private Socket socket;
    private Alliance gameAlliance;
    private String readMessage;
    private BufferedReader in;
    private PrintWriter out;
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
		this.gameAlliance = Alliance.WHITE;
		this.readMessage = null;
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
		System.out.println("Controller mouse listener active");
		if(!gameModel.isGameOver())
		{
			if(gameModel.getActivePlayer().getAlliance() == gameAlliance)
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
				Move mv = gameModel.handleTwoTilesPressed(panelId, lastClickedPanelId);
				if(mv != null)
				{
					sendMove(mv);
				}
					
				previousPanelClicked = (FieldPanel)panel;
				lastClickedPanelId = panel.getPanelId();
			}
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

	public void connectToServer()
	{
		// Get the server address from a dialog box.
        String serverAddress = "localhost";
//        		JOptionPane.showInputDialog(
//        		gameView.getMainFrame(),
//            "Enter IP Address of the Server:",
//            "Welcome to the Chess game",
//            JOptionPane.QUESTION_MESSAGE);

        try
        {
	        // Make connection and initialize streams
	        socket = new Socket(serverAddress, PORT);
//	        input = new BufferedReader(
//                    new InputStreamReader(socket.getInputStream()));
//            output = new PrintWriter(socket.getOutputStream(), true);
	        in = new BufferedReader(new InputStreamReader(
	                socket.getInputStream()));
	        out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch(IOException e)
        {
        	System.out.println("Exception! - cannot connect to the server");
        	e.printStackTrace(System.out);
        	try {socket.close();} catch (IOException e1) {}
        }
	}
	
	public void sendMessage(String message)
	{
		try
		{
			out.println(message);
		}
		catch(Exception e)
		{
			System.out.println("Exception! - cannot write to the server");
        	e.printStackTrace(System.out);
		}
	}
	
	public void receiveMessage()
	{
		 try 
		 {
             readMessage = in.readLine();
             System.out.println("Client - new object received");
         } 
		 catch(Exception e1)
		 {
        	System.out.println("Exception! - cannot read from the input buffer");
        	e1.printStackTrace(System.out);
		 }
	}
	
	public void runClient()
	{
		Move moveToExecute = null;
		try
		{
	    	receiveMessage();
	    	if(readMessage.startsWith("ALLIANCE"))
	    	{
	    		  char allianceMark = readMessage.charAt(8);
	              if(allianceMark == 'B')
	              	gameAlliance = Alliance.BLACK;
	    	}
	        while (true) 
	        {
	        	receiveMessage();
	            if (readMessage.startsWith("MOVE")) 
	            {
	            	moveToExecute = transformToMove(readMessage);
	            	gameModel.executeMove(moveToExecute);
	            } 
	            else if (readMessage.startsWith("QUIT")) 
	            {
	                break;
	            }
	            else if (true) 
	            {
	                System.out.println("Otrzymano wiadomosc: " + readMessage);
	            }  
	        }
		}
        finally {
        	try {socket.close();} catch (IOException e1) {}
        }
	}
	
	public void sendMove(final Move move)
	{
		String moveInMessage = "MOVE ";
		if(move instanceof CommonMove)
		{
			moveInMessage += CommonMove.moveSignature + " "
					+ move.getSourcePosition() + " "
					+ move.getTargetPosition();
		}
		else if(move instanceof AttackMove)
		{
			moveInMessage += CommonMove.moveSignature + " "
					+ move.getSourcePosition() + " "
					+ move.getTargetPosition();
		}
		else if(move instanceof PawnPromotionMove)
		{
			moveInMessage += CommonMove.moveSignature + " "
					+ move.getSourcePosition() + " "
					+ move.getTargetPosition();
		}
		else if(move instanceof CastlingMove)
		{
			moveInMessage += CommonMove.moveSignature + " "
					+ move.getSourcePosition() + " "
					+ move.getTargetPosition() + " "
					+ ((CastlingMove)move).getRookPosition();
		}
		else if(move instanceof EmptyMove)
		{
			moveInMessage += CommonMove.moveSignature;
		}
		sendMessage(moveInMessage);
	}
	
	public final Move transformToMove(String moveDescription)
	{
		String[] splited = moveDescription.split("\\s+");
		if(splited[1] == "CM")
		{
			return new CommonMove(gameModel.getGameBoard(), 
					gameModel.getGameBoard().getPieceOnField(
							Integer.parseInt(splited[2])),
					Integer.parseInt(splited[2]),
					Integer.parseInt(splited[3])
					);
		}
		else if(splited[1] == "AM")
		{
			return new AttackMove(gameModel.getGameBoard(), 
					gameModel.getGameBoard().getPieceOnField(
							Integer.parseInt(splited[2])),
					gameModel.getGameBoard().getPieceOnField(
							Integer.parseInt(splited[3])),
					Integer.parseInt(splited[2]),
					Integer.parseInt(splited[3])
					);
		}
		else if(splited[1] == "PP")
		{
			return new PawnPromotionMove(gameModel.getGameBoard(), 
					gameModel.getGameBoard().getPieceOnField(
							Integer.parseInt(splited[2])),
					Integer.parseInt(splited[2]),
					Integer.parseInt(splited[3])
					);
		}
		else if(splited[1] == "CS")
		{
			return new CastlingMove(gameModel.getGameBoard(), 
					gameModel.getGameBoard().getPieceOnField(
							Integer.parseInt(splited[2])),
					Integer.parseInt(splited[2]),
					Integer.parseInt(splited[3]),
					Integer.parseInt(splited[4])
					);
		}
		else if(splited[1] == "EM")
		{
			return new EmptyMove();
		}
		return null;
	}
	
    private boolean wantsToPlayAgain() {
        int response = JOptionPane.showConfirmDialog(gameView.getMainFrame(),
            "Want to play again?",
            "Tic Tac Toe is Fun Fun Fun",
            JOptionPane.YES_NO_OPTION);
        gameView.getMainFrame().dispose();
        return response == JOptionPane.YES_OPTION;
    }
	
	public void clientHandle()
	{
		 while (true) 
		 {
			connectToServer();
          
            runClient();
            if (!wantsToPlayAgain())
            {
                break;
            }
            gameModel.resetModel();
            gameView.setInitialGameBoard();
        }
	}
}
