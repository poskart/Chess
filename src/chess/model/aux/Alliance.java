package chess.model.aux;

public enum Alliance 
{
	WHITE
	{
		@Override
		public int getDirection()
		{
			return 1;
		}
		@Override
		public Alliance getContraryAlliance()
		{
			return BLACK;
		}
	},
	
	BLACK
	{
		@Override
		public int getDirection()
		{
			return -1;
		}
		@Override
		public Alliance getContraryAlliance()
		{
			return WHITE;
		}
	};
	
	public abstract Alliance getContraryAlliance();
	
	public abstract int getDirection();
}