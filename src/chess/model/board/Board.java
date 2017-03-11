package chess.model.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import chess.model.aux.Alliance;
import chess.model.pieces.*;
import chess.view.BoardTable;

public class Board 
{
	private List<Field> fieldArray;
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;

	private final BoardTable boardGui;
	
	public final static int BOARD_FIELDS_NUMBER = 64;
	
	public Board(BoardTable boardView)
	{
		boardGui = boardView;
		initialize();
		whitePieces = findAllPiecesByColor(fieldArray, Alliance.WHITE);
		blackPieces = findAllPiecesByColor(fieldArray, Alliance.BLACK);
	}
	
	public void initialize()
	{
		fieldArray = new ArrayList<Field>();
		fieldArray.add(0, Field.createField(0, new Rook(0, Alliance.WHITE)));
		fieldArray.add(1, Field.createField(1, new Knight(1, Alliance.WHITE)));
		fieldArray.add(2, Field.createField(2, new Bishop(2, Alliance.WHITE)));
		fieldArray.add(3, Field.createField(3, new Queen(3, Alliance.WHITE)));
		fieldArray.add(4, Field.createField(4, new King(4, Alliance.WHITE)));
		fieldArray.add(5, Field.createField(5, new Bishop(5, Alliance.WHITE)));
		fieldArray.add(6, Field.createField(6, new Knight(6, Alliance.WHITE)));
		fieldArray.add(7, Field.createField(7, new Rook(7, Alliance.WHITE)));
		fieldArray.add(8, Field.createField(8, new Pawn(8, Alliance.WHITE)));
		fieldArray.add(9, Field.createField(9, new Pawn(9, Alliance.WHITE)));
		fieldArray.add(10, Field.createField(10, new Pawn(10, Alliance.WHITE)));
		fieldArray.add(11, Field.createField(11, new Pawn(11, Alliance.WHITE)));
		fieldArray.add(12, Field.createField(12, new Pawn(12, Alliance.WHITE)));
		fieldArray.add(13, Field.createField(13, new Pawn(13, Alliance.WHITE)));
		fieldArray.add(14, Field.createField(14, new Pawn(14, Alliance.WHITE)));
		fieldArray.add(15, Field.createField(15, new Pawn(15, Alliance.WHITE)));
		
		for(int i = 16; i < 48; i++)
			fieldArray.add(i, Field.createField(i, null));

		fieldArray.add(48, Field.createField(48, new Pawn(48, Alliance.BLACK)));
		fieldArray.add(49, Field.createField(49, new Pawn(49, Alliance.BLACK)));
		fieldArray.add(50, Field.createField(50, new Pawn(50, Alliance.BLACK)));
		fieldArray.add(51, Field.createField(51, new Pawn(51, Alliance.BLACK)));
		fieldArray.add(52, Field.createField(52, new Pawn(52, Alliance.BLACK)));
		fieldArray.add(53, Field.createField(53, new Pawn(53, Alliance.BLACK)));
		fieldArray.add(54, Field.createField(54, new Pawn(54, Alliance.BLACK)));
		fieldArray.add(55, Field.createField(55, new Pawn(55, Alliance.BLACK)));
		fieldArray.add(56, Field.createField(56, new Rook(56, Alliance.BLACK)));
		fieldArray.add(57, Field.createField(57, new Knight(57, Alliance.BLACK)));
		fieldArray.add(58, Field.createField(58, new Bishop(58, Alliance.BLACK)));
		fieldArray.add(59, Field.createField(59, new Queen(59, Alliance.BLACK)));
		fieldArray.add(60, Field.createField(60, new King(60, Alliance.BLACK)));
		fieldArray.add(61, Field.createField(61, new Bishop(61, Alliance.BLACK)));
		fieldArray.add(62, Field.createField(62, new Knight(62, Alliance.BLACK)));
		fieldArray.add(63, Field.createField(63, new Rook(63, Alliance.BLACK)));
	}
	
	private Collection<Piece> findAllPiecesByColor(final Collection<Field> fieldArray, final Alliance alliance)
	{
		final List<Piece> singleColorPieces = new ArrayList<>();
		for(final Field field : fieldArray)
		{
			if(field.isFieldOccupied() && field.getPiece().getAlliance() == alliance)
				singleColorPieces.add(field.getPiece());
		}
		return singleColorPieces;
	}
	
	private Collection<Move> calculateAllLegalMoves(final Collection<Piece> piecesToExamine)
	{
		List<Move> legalMoves = new ArrayList<>();
		
		for(Piece piece : piecesToExamine)
		{
			legalMoves.addAll(piece.findPossibleMoves(this));
		}
		return null;
	}
	
	public final boolean isBoardFieldOccupied(int position)
	{
		return fieldArray.get(position).isFieldOccupied();
	}
	
	public final boolean isFieldValid(int absoluteLinearPosition)
	{
		return (absoluteLinearPosition >= 0 && absoluteLinearPosition <=63)? true: false;
	}
	
	public final Piece getPieceOnField(int absolutePosition)
	{
		return fieldArray.get(absolutePosition).getPiece();
	}
	
	public final boolean isFieldUnderAttack(final int absolutePosition, Alliance defenderAlliance)
	{
		List<Move> possibleMovesList = new ArrayList<>();
		
		for(int i = 0; i < 64; i++)
		{
			if(fieldArray.get(i).isFieldOccupied() && fieldArray.get(i).getPiece().getAlliance() != defenderAlliance)
			{
				possibleMovesList = fieldArray.get(i).getPiece().findPossibleMoves(this);
				for(Move move : possibleMovesList)
				{
					if(move.getTargetPosition() == absolutePosition)
						return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		String boardFootprint = new String("");
		Piece tmpPiece;
		int linearIndex;
		for(int i = 56; i >= 0; i -= 8)				/* Iterate through rows */
		{
			for(int j = 0; j < 8; j++)				/* Iterate through columns */
			{
				linearIndex = i + j;
				if(fieldArray.get(linearIndex).isFieldOccupied())
				{
					tmpPiece = fieldArray.get(linearIndex).getPiece();
					if(tmpPiece.getAlliance() == Alliance.BLACK)
						boardFootprint += fieldArray.get(linearIndex).toString().toLowerCase() + " ";
					else
						boardFootprint += fieldArray.get(linearIndex).toString().toUpperCase() + " ";
				}
				else
					boardFootprint += fieldArray.get(linearIndex).toString()+ " ";
				
				if(j == 7)
					boardFootprint += "\n";
			}
		}
		return boardFootprint;
	}
}
