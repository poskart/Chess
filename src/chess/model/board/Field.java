package chess.model.board;

import java.util.HashMap;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import chess.model.pieces.Piece;

/**
 * Class representing single board field. This class
 * defines abstract interface for EmptyField and OccupiedField
 * classes. Provides methods which return information about field
 * occupation, piece and coordinates.
 * @author Piotr Poskart
 *
 */
public abstract class Field 
{
	/** Coordinates of the field 0 - 63 */
	protected final int fieldCoordinates;
	/** Static map with all empty fields */
	private static final Map<Integer, EmptyField> EMPTY_FIELDS = createAllEmptyFields();


	private Field(int coordinates)
	{
		this.fieldCoordinates = coordinates;
	}
	
	/**
	 * This method creates map of all 64 empty fields to be predefined
	 * as static and used in the game for better performance.
	 * @return map of 64 EmptyField objects which are chess board empty fields.
	 */
	private static Map<Integer, EmptyField> createAllEmptyFields() 
	{
		final Map<Integer, EmptyField> emptyFieldMap = new HashMap<>();
		
		for(int i = 0; i < 64; i++)
		{
			emptyFieldMap.put(i, new EmptyField(i));
		}
		return ImmutableMap.copyOf(emptyFieldMap);
	}
	
	/**
	 * This method creates new field and returns reference to it.
	 * When piece is not null it creates OccupiedField object with this
	 * piece, otherwise it returns EmptyFIeld object from static empty fields
	 * map.
	 * @param coordinates coordinates of the field to be created.
	 * @param piece reference to piece which is to be placed on new field
	 * @return reference to new field
	 */
	public static Field createField(final int coordinates, final Piece piece)
	{
		if(piece != null)
			return new OcuppiedField(coordinates, piece);
		else
			return EMPTY_FIELDS.get(coordinates);
	}
	
	/**
	 * Abstract method which returns true is specific field is occupied by
	 * any piece, false otherwise.
	 * @return true if specific field is occupied by
	 * any piece, false otherwise.
	 */
	public abstract boolean isFieldOccupied();
	
	/**
	 * Abstract method which returns piece on specific field
	 * @return reference to piece on field
	 */
	public abstract Piece getPiece();
	 /**
	  * Class specification which represents empty field. Inherited from
	  * Field class.
	  * @author Piotr Poskart
	  *
	  */
	public static final class EmptyField extends Field
	{
		/**
		 * EmptyField object constructor
		 * @param coordinates coordinates of the field
		 */
		private EmptyField(final int coordinates)
		{
			super(coordinates);
		}
		/**
		 * Specified method which returns true is field is occupied by
		 * any piece, false otherwise.
		 * @return true if specific field is occupied by
		 * any piece, false otherwise.
		 */
		@Override
		public boolean isFieldOccupied()
		{
			return false;
		}
		/**
		 * Specified method which returns piece on specific field
		 * @return null because of the empty field.
		 */
		@Override
		public Piece getPiece()
		{
			return null;
		}
		/**
		 * This method returns string describing EmptyField object.
		 * @return String which describes EmptyField object which is "-"
		 */
		@Override
		public final String toString()
		{
			return new String("-");
		}
	}
	 /**
	  * Class specification which represents occupied field. Inherited from
	  * Field class.
	  * @author Piotr Poskart
	  *
	  */
	public static final class OcuppiedField extends Field
	{
		/** Reference to piece on this field */
		Piece piece;
		/**
		 * OcuppiedField object constructor
		 * @param coordinates coordinates of the field
		 * @param piece reference to piece which is on the field.
		 */
		private OcuppiedField(int coordinates, Piece piece)
		{
			super(coordinates);
			this.piece = piece;
		}
		/**
		 * Specified method which returns true is field is occupied by
		 * any piece, false otherwise.
		 * @return true if specific field is occupied by
		 * any piece, false otherwise.
		 */
		@Override
		public boolean isFieldOccupied()
		{
			return true;
		}
		/**
		 * Specified method which returns piece on specific field
		 * @return reference to piece on field
		 */
		@Override
		public Piece getPiece()
		{
			return piece;
		}
		/**
		 * This method returns string describing OccupiedField object.
		 * @return String which describes OccupiedField (String with piece sign).
		 */
		@Override
		public final String toString()
		{
			return piece.toString();
		}
	}
}
