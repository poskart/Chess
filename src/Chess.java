import chess.controller.*;
import chess.view.View;
import chess.model.Model;

/**
 * This is the main chess game class which defines Model, View
 * and Controller objects, initializes and couples them together
 *  to cooperate. This class implements main() method to run the game.
 *  
 * @author PIotr Poskart
 *
 */
public class Chess 
{
	/**
	 * This is main method of the game. It creates MVC template
	 * objects, initializes and couples them together running the
	 * game.
	 * @param args
	 */
	public static void main(String[] args)
	{
		Model model = new Model();
		View view = new View(model);
		model.addObserver(view);
		Controller controller = new Controller(model, view);
		controller.clientHandle();
		
		view.addController(controller);
		view.setInitialGameBoard();

		//System.out.println(model.getGameBoard());
	}
}
