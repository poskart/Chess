package chess.controller;

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
import chess.view.View;

/**
 * Main controller class which implements MouseListener interface
 * nedded my View section of the game. Controller initializes model
 * and view for the game and handles user actions on the game board. 
 * 
 * @author Piotr Poskart
 *
 */
public final class Controller
{
	/** Main game model object */
	private final Model gameModel;
	/** Main game view object  */
	private final View gameView;
	
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
		this.readMessage = null;
		this.gameAlliance = Alliance.WHITE;
	}
	/**
	 * Initializes view with the first game board setting.
	 */
	public void initialize()
	{
		gameView.setInitialGameBoard();
	}
	/**
	 * Get alliance of the player
	 * @return alliance of the player which plays this chess game
	 */
	public final Alliance getAlliance()
	{
		return gameAlliance;
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
             System.out.println("Client - new object received: ");
             if(readMessage != null)
            	 System.out.println("\t" + readMessage);
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
	        while (true) 
	        {
	        	receiveMessage();
	        	if(readMessage.startsWith("ALLIANCE"))
		    	{
	        		if(readMessage.charAt(9)== 'B')
	        		{
		              	gameAlliance = Alliance.BLACK;
		              	System.out.println("Zmieniono kolor na B");
	        		}
		    	}
	        	else if (readMessage.startsWith("MOVE")) 
	            {
	            	moveToExecute = transformToMove(readMessage);
	            	gameModel.executeMove(moveToExecute);
	            	if(gameModel.isGameOver())
	            		break;
	            } 
	        }
	        sendMessage("QUIT");
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
			moveInMessage += AttackMove.moveSignature + " "
					+ move.getSourcePosition() + " "
					+ move.getTargetPosition();
		}
		else if(move instanceof PawnPromotionMove)
		{
			moveInMessage += PawnPromotionMove.moveSignature + " "
					+ move.getSourcePosition() + " "
					+ move.getTargetPosition();
		}
		else if(move instanceof CastlingMove)
		{
			moveInMessage += CastlingMove.moveSignature + " "
					+ move.getSourcePosition() + " "
					+ move.getTargetPosition() + " "
					+ ((CastlingMove)move).getRookPosition();
		}
		else if(move instanceof EmptyMove)
		{
			moveInMessage += EmptyMove.moveSignature;
		}
		sendMessage(moveInMessage);
	}
	
	public final Move transformToMove(String moveDescription)
	{
		String[] splited = moveDescription.split("\\s+");
		if(splited[1].equals("CM"))
		{
			return new CommonMove(gameModel.getGameBoard(), 
					gameModel.getGameBoard().getPieceOnField(
							Integer.parseInt(splited[2])),
					Integer.parseInt(splited[2]),
					Integer.parseInt(splited[3])
					);
		}
		else if(splited[1].equals("AM"))
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
		else if(splited[1].equals("PP"))
		{
			return new PawnPromotionMove(gameModel.getGameBoard(), 
					gameModel.getGameBoard().getPieceOnField(
							Integer.parseInt(splited[2])),
					Integer.parseInt(splited[2]),
					Integer.parseInt(splited[3])
					);
		}
		else if(splited[1].equals("CS"))
		{
			return new CastlingMove(gameModel.getGameBoard(), 
					gameModel.getGameBoard().getPieceOnField(
							Integer.parseInt(splited[2])),
					Integer.parseInt(splited[2]),
					Integer.parseInt(splited[3]),
					Integer.parseInt(splited[4])
					);
		}
		else if(splited[1].equals("EM"))
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
        //gameView.getMainFrame().dispose();
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
