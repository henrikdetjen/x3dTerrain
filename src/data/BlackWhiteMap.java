package data;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BlackWhiteMap
{
	// svn.test

	/**
	 * H�he des Bildes
	 */
	private final int width;
	/**
	 * Breite des Bildes
	 */
	private final int height;

	/**
	 * Normalisierten Schwarz-Wei� Werte
	 */
	private final float[][] values;

	public BlackWhiteMap(final BufferedImage image)
	{
		this.width = image.getWidth();
		this.height = image.getHeight();
		// image = BlackWhiteMap.convertToGrayscale(image);
		this.values = this.getBlackWhiteValues(image);
	}

	public BlackWhiteMap(final BlackWhiteMap bwMap)
	{
		this.height = bwMap.getHeight();
		this.width = bwMap.getWidth();
		this.values = bwMap.getValues();
	}

	/**
	 * Retrieves the fragment value from the given texture-coordinates.
	 * 
	 * @param s
	 *            [0-1]
	 * @param t
	 *            [0-1]
	 */
	public float lookupValue(final float s, final float t)
	{
		// width = 512 -> [0 - 511]
		final int x = (int) Math.max(0, this.height * s - 1);
		final int y = (int) Math.max(0, this.width * t - 1);
		return this.values[y][x];
	}

	/**
	 * INITIALES LESEN UND PARSEN DER WERTE
	 * 
	 * @param image
	 * @return
	 */
	private float[][] getBlackWhiteValues(final BufferedImage image)
	{
		final int width = image.getWidth();
		final int height = image.getHeight();
		final float[][] result = new float[height][width];

		for (int row = 0; row < height; row++)
			for (int col = 0; col < width; col++)
			{
				final Color c = new Color(image.getRGB(col, row));

				// only get first channel since we have black and white map
				result[row][col] = (float) c.getRed() / 255;
			}
		return result;
	}

	public static BufferedImage convertToGrayscale(final BufferedImage image)
	{
		final BufferedImage imageGs = new BufferedImage(image.getWidth(),
				image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		final Graphics g = image.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return imageGs;
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	public float[][] getValues()
	{
		return this.values;
	}

}
