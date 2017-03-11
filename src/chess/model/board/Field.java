package chess.model.board;

import java.util.HashMap;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import chess.model.pieces.Piece;

public abstract class Field 
{
	protected final int fieldCoordinates;
	
	private static final Map<Integer, EmptyField> EMPTY_FIELDS = createAllEmptyFields();
	
	private static Map<Integer, EmptyField> createAllEmptyFields() 
	{
		final Map<Integer, EmptyField> emptyFieldMap = new HashMap<>();
		
		for(int i = 0; i < 64; i++)
		{
			emptyFieldMap.put(i, new EmptyField(i));
		}
		return ImmutableMap.copyOf(emptyFieldMap);
	}

	public static Field createField(final int coordinates, final Piece piece)
	{
		if(piece != null)
			return new OcuppiedField(coordinates, piece);
		else
			return EMPTY_FIELDS.get(coordinates);
	}
	
	private Field(int coordinates)
	{
		this.fieldCoordinates = coordinates;
	}
	
	
	public abstract boolean isFieldOccupied();
	
	public abstract Piece getPiece();
	
	public static final class EmptyField extends Field
	{
		private EmptyField(final int coordinates)
		{
			super(coordinates);
		}
		
		@Override
		public boolean isFieldOccupied()
		{
			return false;
		}
		@Override
		public Piece getPiece()
		{
			return null;
		}
		@Override
		public final String toString()
		{
			return new String("-");
		}
	}
	
	public static final class OcuppiedField extends Field
	{
		Piece piece;
		
		private OcuppiedField(int coordinates, Piece piece)
		{
			super(coordinates);
			this.piece = piece;
		}
		
		@Override
		public boolean isFieldOccupied()
		{
			return true;
		}
		@Override
		public Piece getPiece()
		{
			return piece;
		}
		@Override
		public final String toString()
		{
			return piece.toString();
		}
	}
}
