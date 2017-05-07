import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This is main chess server class. This class defines objects
 * and methods to establish network connection and manage incoming
 * connections from client applications. Main thread of this server
 * waits for new clients, initializes new match and pair those clients
 * to play the game. When new client is connected, server gives another
 * dedicated thread to handle connection with this client. New client 
 * service threads are created always when 2 players are ready to play.
 * Client service threads ends its work when QUIT message received 
 * from the client. When one client interrupted the game, server receives
 * QUIT message and notifies second player that game is over. Next server
 * waits for the next player and tries to start next match.
 * 
 * @author Piotr Poskart
 *
 */
public class ChessServer 
{
	/**
     * This main server method runs the application. It pairs up 
     * clients that connect to this server.
     */
    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(8901);
        System.out.println("Chess Server is Running");
        try {
            while (true) {
                Player player1 = new Player(listener.accept(), 'W');
                System.out.println("Server accepted 1 player");
                Player player2 = new Player(listener.accept(), 'B');
                System.out.println("Server accepted 2 player");
                player1.setOpponent(player2);
                player2.setOpponent(player1);
                player1.start();
                player2.start();
            }
        } finally {
            listener.close();
        }
    }
}

/**
 * This is Player class which implements server thread dedicated 
 * to perform communication with the single client during the match.
 * 
 * @author Piotr Poskart
 *
 */
class Player extends Thread 
{
	/** Mark for player alliance description 'W' or 'B' */
    char mark;
    /** Reference to another thread which serves second's client connection */
    Player opponent;
    /** Reference to socket used in network communication */
    Socket socket;
    /** String object to store messages received from the client */
    String receivedMessage;
    /** BufferedReader object for buffered messages reading from client */
    BufferedReader in;
    /** PrintWriter object for writing messages to client */
    PrintWriter out;

    /**
     * Constructs a handler thread for a given socket and mark
     * initializes the stream fields, displays the first
     * welcoming message.
     */
    public Player(Socket socket, char mark)
    {
        this.socket = socket;
        this.mark = mark;
        try 
        {
        	in = new BufferedReader(new InputStreamReader(
	                socket.getInputStream()));
        	out = new PrintWriter(socket.getOutputStream(), true);
        	out.println("Welcome to the chess game! Waiting for opponent...");
        } 
        catch (IOException e) 
        {
            System.out.println("Player died: " + e);
        }
    }
	
    /**
     * This method sets reference to the opponent 
     * (another server thread which handles enemy communication)
     * @param opponent is reference to the thread handling opponent
     *  communication.
     */
    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }
    /**
     * This method sends prompt for the client with the
     * current move, informing that now it is its turn.
     */
    public void printPrompt()
    {
    	if(mark == 'B')
    		out.println("Black, your move");
    	else if (mark == 'W')
    		out.println("White, your move");
    }
    
    /**
     * The run method of this thread. It handles communication within 
     * entire match. First it sends configuration messages to the client
     * (e.g. alliance setting and START message). The it processes messages
     * read from the client and communicate with opponent server thread 
     * managing the state of the game. It ends when QUIT message received 
     * or when error occured.
     */
    public void run() 
    {
        try 
        {
            // The thread is only started after everyone connects.
        	out.println(new String("ALLIANCE " + mark));
        	out.println(new String("START "));
        	
            // Tell the first player that it is her turn.
            if (mark == 'W')
            	printPrompt();
        	else
        		out.println("Opponent's move...");
            // Repeatedly get commands from the client and process them.
            while (true) 
            {
            	receivedMessage = in.readLine();
            	if(receivedMessage != null)
            	{
	            	if (receivedMessage.startsWith("MOVE")) 
	                {
	            		opponent.out.println(receivedMessage);
	            		opponent.printPrompt();
	            		out.println("Opponent's move...");
	                } 
	            	else
	            	{
	            		System.out.println(receivedMessage);
		            	if (receivedMessage.startsWith("QUIT")) 
		                {
		            		// notify second player that another has disconnected
		            		opponent.out.println("Opponent disconnected...");
		            		opponent.out.println("DISCON");
		            		return;
		                }
	            	}
            	}
            	else
            	{
            		System.out.println("Otrzymalem null");
            		return;
            	}
            }
        } 
        catch (IOException e)
        {
         	System.out.println("Exception! - cannot read from the input buffer");
        	e.printStackTrace(System.out);        
    	}
        catch(Exception e1)
        {
        	System.out.println("Error, Undefined server exception");
        }
        finally
        {
            try {socket.close();} catch (IOException e) {}
        }
    }
}