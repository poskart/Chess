package chess.model.common;

/**
 * This is enumeration type for chess Alliance representation.
 * @author Piotr Poskart
 *
 */
public enum Alliance 
{
	/**
	 * White enumeration type
	 */
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
	
	/**
	 * Black enumeration type
	 */
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
	
	/**
	 * This method returns contrary alliance of the current alliance
	 * @return
	 */
	public abstract Alliance getContraryAlliance();
	
	/**
	 * This method returns direction of the attack of the current alliance.
	 * @return
	 */
	public abstract int getDirection();
}