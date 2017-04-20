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
	
	public void exitChessClient()
	{
		sendMessage("QUIT");
		System.out.println("Contoller notified server for close...");
		System.exit(0);
	}
	
	/**
	 * This method connects client to the server.
	 */
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
	
	/**
	 * This method sends message to the connected socket and
	 * handles exceptions related to network communication.
	 * @param message is message to be sent to the server.
	 */
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
	
	/**
	 * This method receives message from the connected socket and
	 * handles exceptions related to network communication.
	 */
	public void receiveMessage()
	{
		 try 
		 {
             readMessage = in.readLine();
         } 
		 catch(Exception e1)
		 {
        	System.out.println("Exception! - cannot read from the input buffer");
        	e1.printStackTrace(System.out);
		 }
	}
	
	/**
	 * This method is the main client loop to perform communication 
	 * during the chess match. It receives messages and processes it
	 * sending moves to the model to be executed or performing another
	 * actions depending on  received content. It breaks if game is 
	 * over or received DISCON message from the server.
	 */
	public void runClient()
	{
		Move moveToExecute = null;
		try
		{
	        while (true) 
	        {
	        	receiveMessage();
	        	if(readMessage != null)
	        	{
		        	if(readMessage.startsWith("ALLIANCE"))
			    	{
		        		if(readMessage.charAt(9)== 'B')
			              	gameAlliance = Alliance.BLACK;
		        		else if (readMessage.charAt(9)== 'W')
			              	gameAlliance = Alliance.WHITE;
			    	}
		        	else if(readMessage.startsWith("START"))
		        	{
		        		gameModel.setNetworkGameStartFlag();
		        	}
		        	else if (readMessage.startsWith("MOVE")) 
		            {
		            	moveToExecute = transformToMove(readMessage);
		            	gameModel.executeMove(moveToExecute);
		            	if(gameModel.isGameOver())
		            		break;
		            } 
		        	else if(readMessage.startsWith("DISCON"))
		        	{
		        		break;
		        	}
		        	else
		        	{
		        		gameView.setMessageText(readMessage);
		        	}
	        	}
	        }
	        sendMessage("QUIT");
		}
        finally {
        	try {socket.close();} catch (IOException e1) {}
        }
	}
	
	/**
	 * This method transforms given move to text description of 
	 * this move and send it through socket to the server.
	 * @param move Move object which is a move to be sent to the server.
	 */
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
	
	/**
	 * This method transform move description (String) to proper 
	 * Move object.
	 * @param moveDescription is input String with description of the move
	 * @return Move object corresponding to move description.
	 */
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
	
	/**
	 * This method prints prompt for user to ask if he wants to play and
	 * returns true if yes, false otherwise.
	 * @return true if user wants to play, false otherwise.
	 */
    private boolean wantsToPlayAgain() 
    {
        int response = JOptionPane.showConfirmDialog(gameView.getMainFrame(),
            "Want to play again?",
            "Network Chess",
            JOptionPane.YES_NO_OPTION);
        return response == JOptionPane.YES_OPTION;
    }
	
    /**
     * This method contains main client loop in which it 
     * performs cyclic connection to the server, running the game 
     * and provides user decision to play or not to play again. 
     * It also clears the game objects and prepares new board 
     * before new game.
     */
	public void clientHandle()
	{
		 while (true) 
		 {
			connectToServer();
          
            runClient();
            if (!wantsToPlayAgain())
            {
    	        sendMessage("QUIT");
                System.exit(0);
            }
            gameModel.resetModel();
            gameView.prepareNextMatch();
        }
	}
}
