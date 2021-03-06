package utils;

/**
 * Generic range utility helper
 * 
 * @author Marco Janc (c) 2013
 */
public class RangeIntNorm extends Range<Integer>
{
	/**
	 * linear max
	 */
	private final Integer normalizedMax;

	public RangeIntNorm(final Integer min, final Integer max)
	{
		super(min, max);
		// what happens with negative values?
		this.normalizedMax = this.max - this.min;
	}

	/**
	 * Calculates the value within the range boundaries from the given value
	 * within the [0-1] range<br>
	 * <br>
	 * <i>{@link #min} + value * {@link #normalizedMax}</i>
	 * 
	 * 
	 * @param value
	 *            [0-1]
	 * 
	 * @return [{@link #min} - {@link #max}]
	 */
	public final Integer calculate(final Integer value)
	{
		return this.min + value * this.normalizedMax;
	}

	public Integer getNormalizedMax()
	{
		return this.normalizedMax;
	}
}
