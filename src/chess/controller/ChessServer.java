package chess.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import chess.model.aux.Alliance;

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
    Alliance activeAlliance;

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
            sendMessage(new String("Welcome to the chess game!"));
        } 
        catch (IOException e) 
        {
            System.out.println("Player died: " + e);
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
			 receivedMessage = in.readLine();
             if(receivedMessage != null)
            	 System.out.println(receivedMessage);
         } 
		 catch(IOException e1)
		 {
        	System.out.println("Exception! - cannot read from the input buffer");
        	e1.printStackTrace(System.out);
		 }
	}
	
    /**
     * Accepts notification of who the opponent is.
     */
    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }
    
    /**
     * The run method of this thread.
     */
    public void run() {
        try {
            // The thread is only started after everyone connects.
            sendMessage(new String("ALLIANCE " + mark));

            // Tell the first player that it is her turn.
            if (mark == 'W') {
            	sendMessage(new String("White! your move"));
            }

            // Repeatedly get commands from the client and process them.
            while (true) 
            {
            	receiveMessage();
            	if(receivedMessage != null)
            	{
	            	if (receivedMessage.startsWith("MOVE")) 
	                {
	            		sendMessage(new String("New move registered in server..."));
	            		opponent.sendMessage(receivedMessage);
	                } 
	            	else
	            	{
	            		System.out.println(receivedMessage);
		            	if (receivedMessage.startsWith("QUIT")) 
		                {
		                    return;
		                }
	            	}
            	}
            }
        } 
        catch (Exception e) 
        {
        	e.printStackTrace(System.out);
        }
        finally
        {
            try {socket.close();} catch (IOException e) {}
        }
    }
}