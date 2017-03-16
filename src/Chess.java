import chess.controller.*;
import chess.view.View;
import chess.model.Model;

public class Chess 
{
	public static void main(String[] args)
	{
		Model model = new Model();
		View view = new View(model);
		model.addObserver(view);
		Controller controller = new Controller(model, view);
		
		view.addController(controller);
		try
		{
			Thread.sleep(2);
		}
		catch(InterruptedException ex)
		{
			Thread.currentThread().interrupt();
		}
		
		view.setInitialGameBoard();
		System.out.println(model.getGameBoard());
		
	}
}
