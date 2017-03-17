package chess.view;

import java.awt.Color;

/**
 * This class contains chess game GUI Settings like board dimensions, 
 * images paths and board fields colors
 * 
 * @author Piotr Poskart
 */

public final class GUISettings 
{
	/** Board size (square edge length) */
	public static final int BOARD_SIZE = 640;
	/** Board field size (square edge length) */
	public static final int FIELD_SIZE = 10;
	
	/** Color of the light pieces of the board */
	public static final Color WHITE_COLOR = new Color(220, 200, 120);
	/** Color of the dark pieces of the board */
	public static final Color DARK_COLOR = new Color(30, 30, 30);
	
	/** Relative path to main directory with images of pieces */
	public static final String PIECE_IMAGES_PATH = "img/pieces/";
	/** Relative path to image with allowed move image */
	public static final String HIGHLIGHT_IMAGE_PATH = "img/aux/allowed.gif";
}
