package data;

import java.awt.image.BufferedImage;

import utils.RangeFloatNorm;

public class Heightmap extends BlackWhiteMap
{

	private RangeFloatNorm niveau;

	public Heightmap(final BufferedImage img, final RangeFloatNorm niveau)
	{
		super(img);
		this.niveau = niveau;
	}

	public Heightmap(final BlackWhiteMap bwMap, final RangeFloatNorm rfn)
	{
		super(bwMap);
		this.niveau = rfn;
	}

	public RangeFloatNorm getNiveau()
	{
		return this.niveau;
	}

	public void setNiveau(final RangeFloatNorm rfn)
	{
		this.niveau = rfn;
	}

}
