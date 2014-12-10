package utils;

/**
 * int x,y dimension object, could be generic but with wrapper types
 */
public class DimensionInt
{
	private final int x;
	private final int y;

	public DimensionInt(final int x, final int y)
	{
		this.x = x;
		this.y = y;
	}

	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
	}

}
