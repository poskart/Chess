import chess.controller.*;
import chess.view.View;
import chess.model.Model;

public class Chess 
{
	public static void main(String[] args)
	{
		Model model = new Model();
		View view = new View(model);
		Controller controller = new Controller(model, view);
	
		try
		{
			Thread.sleep(1);
		}
		catch(InterruptedException ex)
		{
			Thread.currentThread().interrupt();
		}
		
		view.setInitialGameBoard();
		System.out.println(model.getGameBoard());
		
	}
}
