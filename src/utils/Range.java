package utils;

/**
 * Generic range utility helper
 * 
 * @author Marco Janc (c) 2013
 */
public class Range<T>
{
	/**
	 * Minimum boundary
	 */
	protected final T min;
	/**
	 * Maximum boundary
	 */
	protected final T max;

	public Range(T min, T max)
	{
		this.min = min;
		this.max = max;
	}

	@Override
	public String toString()
	{
		return String.valueOf(this.min) + "-" + String.valueOf(this.max);
	}

	/*
	 * Getter and Setter
	 */

	public T getMin()
	{
		return this.min;
	}

	public T getMax()
	{
		return this.max;
	}
}