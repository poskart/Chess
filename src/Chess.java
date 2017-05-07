import chess.controller.*;
import chess.view.View;
import chess.model.Model;

/**
 * This is the main chess game class which defines Model, View
 * and Controller objects, initializes and couples them together
 *  to cooperate. This class implements main() method to run the game.
 *  
 * @author Piotr Poskart
 *
 */
public class Chess
{
	/**
	 * This is main client method of the game. It creates MVC template
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
		view.addController(controller);
		
		controller.clientHandle();
	}
}
