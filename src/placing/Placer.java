package placing;

import java.util.HashSet;
import java.util.Set;

import output.OutputUtils;
import data.BlackWhiteMap;
import data.Entity;
import data.Heightmap;
import data.Layer;
import data.Level;

/**
 * Places objects of a given Level on a map.
 */
public class Placer
{
	/**
	 * Level to use
	 */
	private final Level level;

	/**
	 * Heights of the map, to reduce call to length of maps.
	 */
	private final int mapHeight;
	/**
	 * Width of the map, to reduce call to length of maps.
	 */
	private final int mapWidth;
	/**
	 * Map where a true value indicates no other object can be placed anymore at
	 * this index.
	 * 
	 * @see #mapHeight Height of array
	 * @see #mapWidth Width of array
	 */
	private final boolean map[][];
	/**
	 * Map where a true value indicates no other object from the same layer can
	 * be placed anymore at this index anymore.
	 * 
	 * @see #mapHeight Height of array
	 * @see #mapWidth Width of array
	 */
	private final boolean mapLayer[][];

	/**
	 * List of Entities that have been placed at least once, for x3d 'DEF' and
	 * 'USE'
	 */
	private final Set<Entity> placedEntities;

	/**
	 * Creates a new Place from the given level
	 * 
	 * @param level
	 */
	public Placer(final Level level)
	{
		if (level == null)
			throw new IllegalArgumentException("Level must not be null.");

		this.level = level;
		this.mapHeight = this.level.getScale().getY();
		this.mapWidth = this.level.getScale().getX();
		this.map = new boolean[this.mapHeight][this.mapWidth];
		this.mapLayer = new boolean[this.mapHeight][this.mapWidth];
		this.placedEntities = new HashSet<Entity>();
	}

	/**
	 * Creates a x3d markup string of random generated entities of the level
	 * 
	 * @return
	 */
	public String placeObjects()
	{
		final StringBuilder sb = new StringBuilder();

		float xOffset = 0f;
		float yOffset = 0f;

		if (this.level.isCenter())
		{
			xOffset = this.level.getScale().getX() * -0.5f;
			yOffset = this.level.getScale().getY() * -0.5f;
		}

		for (final Layer layer : this.level.getLayers())
		{
			sb.append("<Group DEF=\"" + layer.getId() + "\">");

			final BlackWhiteMap densityMap = layer.getDensityMap();

			// traverse height - coordinate
			for (int xGrid = 0; xGrid < this.mapHeight; xGrid++)
				// traverse width - coordinate
				for (int yGrid = 0; yGrid < this.mapWidth; yGrid++)
				{
					final float unitOffset = 1.0f / layer.getUnits();

					// separate each grid-cell into equal subunits
					for (int xSub = 0; xSub < layer.getUnits(); xSub++)
						for (int ySub = 0; ySub < layer.getUnits(); ySub++)
						{
							// choose random object
							final Entity entity = layer.getRandomEntity();

							final String x3d = this.createX3D(xGrid, yGrid,
									xSub, ySub, unitOffset, xOffset, yOffset,
									densityMap, layer.isStatic(), entity);

							if (x3d != null)
								sb.append(x3d);
						}
				}

			sb.append("</Group>");

			// reset placed flags
			for (final Entity entity : layer.getEntities())
				entity.setPlaced(false);
		}

		return sb.toString();
	}

	/**
	 * Creates the X3D representation of the Entities of the given Layer at the
	 * given map coordinates.
	 * 
	 * @param sb
	 *            StringBuilder to append the x3d output
	 * @param xGrid
	 *            Current X coordinate of grid
	 * @param yGrid
	 *            Current Y coordinate of grid
	 * @param xSub
	 *            Current X sub coordinate of grid-cell
	 * @param xSub
	 *            Current Y sub coordinate of grid-cell
	 * @param subUnitOffset
	 *            Offset in grid coordinates per sub-unit
	 * @param xOffset
	 *            output X coordinate offset
	 * @param yOffset
	 *            output Y coordinate offset
	 * @param densityMap
	 *            Additional propability map
	 * @param isStatic
	 *            If current layer is static
	 * @param entity
	 *            Entity to create
	 */
	private String createX3D(final int xGrid, final int yGrid, final int xSub,
			final int ySub, final float subUnitOffset, final float xOffset,
			final float yOffset, final BlackWhiteMap densityMap,
			final boolean isStatic, final Entity entity)
	{
		final Heightmap heightmap = this.level.getHeightmap();

		// no placement possible either global or in layer if layer static flag
		// is true
		if (this.map[xGrid][yGrid] || this.mapLayer[xGrid][yGrid])
			return null;

		/*
		 * calculate coordinates in respect to subunit and offset by half a
		 * subUnit in random direction
		 * 
		 * Math.random() * 0.5 - 1.0 -> [0-1] * 0.5 - 1.0 = [0 - 0.5] - 1.0 =
		 * [-0.5-0.5]
		 */
		final float x = xGrid + xSub * subUnitOffset
				+ ((float) Math.random() * 0.5f - 1.0f);
		final float y = yGrid + ySub * subUnitOffset
				+ ((float) Math.random() * 0.5f - 1.0f);

		// normalized x and y values for texture lookups
		final float xNorm = x / this.mapWidth;
		final float yNorm = y / this.mapHeight;

		// check height-range
		final float heightmapValue = heightmap.lookupValue(xNorm, yNorm);
		if (heightmapValue < entity.getHeight().getMin()
				|| heightmapValue > entity.getHeight().getMax())
			return null;

		final float p = (float) Math.random();

		// check propability
		if (p > entity.getPropability())
			return null;

		// check density map
		if (densityMap.lookupValue(xNorm, yNorm) >= p)
		{
			// use units to place more then one per heightmap unit
			// in random directions!

			final float height = heightmap.getNiveau()
					.calculate(heightmapValue);
			// use different random values for scale and degree
			final float scale = entity.getScale().calculate(p);
			final float degree = entity.getDegree().calculate(p);

			// mark global maps with radius
			if (isStatic)
			{
				Placer.mark(this.map, xGrid, yGrid, entity.getRadius().getMin()
						.intValue());
				Placer.mark(this.mapLayer, xGrid, yGrid, entity.getRadius()
						.getMax().intValue());
			}

			// check if coordinates should be centered

			return OutputUtils.getX3DInstance(entity, x + xOffset, y + yOffset,
					height, scale, degree);
		}

		return null;
	}

	/**
	 * Marks all cells around the given coordinates with true
	 * 
	 * @param map
	 * @param x
	 * @param y
	 * 
	 * @param radius
	 */
	private static void mark(final boolean[][] map, final int x, final int y,
			final int radius)
	{
		for (int i = Math.max(0, x - radius); i < Math.min(map[0].length, x
				+ radius); i++)
			for (int j = Math.max(0, y - radius); j < Math.min(map[i].length, y
					+ radius); j++)
				map[x][y] = true;
	}
}
