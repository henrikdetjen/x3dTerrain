package output;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import utils.DimensionInt;
import data.Entity;
import data.Heightmap;

/**
 * Class creating the x3d String representation of a given Entity
 * 
 */
public class OutputUtils
{
	/**
	 * Hide Constructor
	 */
	private OutputUtils()
	{

	}

	/**
	 * Retrieves the X3D representation of the given instance using the given
	 * size, rotation and height value.
	 * 
	 * the given id is used
	 * 
	 * @param x
	 *            X-coordinate
	 * @param y
	 *            Y-coordinate
	 * @param z
	 *            Z-coordinate / height
	 * 
	 * @param entity
	 *            Entity which id converts to x3d attribute 'USE' or 'DEF'
	 *            regarding its placed state.
	 * @param scale
	 * @param degree
	 *            in Radiant SI-Units
	 * 
	 * @return
	 */
	public static String getX3DInstance(final Entity entity, final float x,
			final float y, final float z, final float scale, final float degree)
	{
		if (entity == null)
			throw new IllegalArgumentException("Entity must not be null.");
		String instance;

		if (entity.isPlaced())
			instance = "<Inline USE=\"" + entity.getId() + "\"/>";
		else
		{
			instance = "<Inline DEF=\"" + entity.getId() + "\" url=\""
					+ entity.getUrl() + "\"/>";
			entity.setPlaced(true);
		}

		final StringBuilder sb = new StringBuilder();

		// compare float's using eta like 0.0000001 ? depends on input data
		sb.append("<Transform translation=\"" + x + " " + z + " " + y + "\"");
		if (scale != 0f)
			sb.append(" scale=\"" + scale + " " + scale + " " + scale + "\"");
		if (degree != 0f)
			sb.append(" rotation=\"0 1 0 " + degree + "\"");
		sb.append(">" + instance + "</Transform>");

		return sb.toString();
	}

	/**
	 * 
	 * @param dimension
	 *            Output dimension of the grid. If component-wise divided
	 *            through <code>segments</code> result should have no rest.
	 * @param map
	 *            HeightMap
	 * @param segments
	 *            Output segment dimension.
	 * @param outputType
	 *            Type of output
	 * @return
	 */
	public static String getTerrainGeometry(final DimensionInt dimension,
			final Heightmap map, final DimensionInt segments,
			final OutputType outputType)
	{
		if (outputType == null)
			throw new IllegalArgumentException("OutputType must not be null.");

		// currently only WRL is supported
		if (outputType != OutputType.WRL)
			throw new IllegalArgumentException("Unsupported outputType.");

		// height values
		final StringBuilder sb = new StringBuilder(
				"geometry ElevationGrid { height [");

		final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		final DecimalFormat df = (DecimalFormat) nf;
		df.setMaximumFractionDigits(2);

		// naive approach no blurring
		for (int x = 0; x < segments.getX(); x++)
		{
			for (int y = 0; y < segments.getY(); y++)
				sb.append(df.format(map.getNiveau().calculate(
						map.lookupValue((float) x / segments.getY(), (float) y
								/ segments.getX())))
						+ " ");
			sb.append("\n");
		}
		sb.append("] ");
		// dimension
		sb.append("xDimension " + segments.getX() + " ");
		sb.append("yDimension " + segments.getY() + " ");

		final int xSpacing = dimension.getX() / (segments.getX() + 1);
		final int ySpacing = dimension.getY() / (segments.getX() + 1);

		// spacing
		sb.append("xSpacing " + xSpacing + " ");
		sb.append("ySpacing " + ySpacing + " ");
		sb.append("creaseAngle 2" + "}");

		return sb.toString();
	}
}
