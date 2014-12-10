package utils;

/**
 * Generic range utility helper
 * 
 * @author Marco Janc (c) 2013
 */
public class RangeFloatNorm extends Range<Float>
{
	/**
	 * linear max
	 */
	private final float normalizedMax;

	public RangeFloatNorm(final Float min, final Float max)
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
	public final float calculate(final float value)
	{
		return this.min + value * this.normalizedMax;
	}

	public float getNormalizedMax()
	{
		return this.normalizedMax;
	}
}
