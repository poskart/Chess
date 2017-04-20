import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChessServer 
{
	/**
     * Runs the application. Pairs up clients that connect.
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

class Player extends Thread 
{
    char mark;
    Player opponent;
    Socket socket;
    String receivedMessage;
    BufferedReader in;
    PrintWriter out;

    /**
     * Constructs a handler thread for a given socket and mark
     * initializes the stream fields, displays the first two
     * welcoming messages.
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
     * Accepts notification of who the opponent is.
     */
    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }
    
    public void printPrompt()
    {
    	if(mark == 'B')
    		out.println("Black, your move");
    	else if (mark == 'W')
    		out.println("White, your move");
    }
    
    /**
     * The run method of this thread.
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