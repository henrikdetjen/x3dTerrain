package data;

import utils.Range;
import utils.RangeFloatNorm;

public class Entity
{

	// /////////////
	// VARIABLEN //
	// /////////////
	/**
	 * ID
	 */
	private String id;
	/**
	 * Das x3d-object das platziert wird (um nachher den Output zu generieren,
	 * brauchen wir den Inhalt des Objekts
	 */
	private String url;

	/**
	 * Die Dichte der Objektverteilung / Grauwert um Objekt auf der Karte zu
	 * platzieren
	 */
	private Range<Float> height;

	/**
	 * Max: Ausbreitung des Objekts im aktuellen Layer z.B. Baumkronen-Umfang<br/>
	 * Min: un�berschreibbare Ausbreitung des Objekts, wenn layer.static=true
	 * z.B. Baumstamm-Umfang (dann k�nnen auf anderen Layern keine Objekte "in"
	 * den Bereich platziert werden
	 */
	private Range<Float> radius;

	/**
	 * Drehwinkelbeschr�nkung
	 */
	private RangeFloatNorm degree;

	/**
	 * Skalierungsbeschr�nkung
	 */
	private RangeFloatNorm scale;

	/**
	 * Wahrscheinlichkeit zu platzieren
	 */
	private float propability;
	/**
	 * Gewichtung zu anderen Objekten auf dem Layer Standardm��ig = 1, wenn nur
	 * ein Objekt Sinnvoll, wenn 2 Objekte in einem Layer verteilt werden sollen
	 * (z.B. 2 Baumarten Esche, Buche) und dabei ein bestimmtes Verh�ltnis
	 * gelten soll z.B. 3x Esche zu 1x Buche (0.75 / 0.25)
	 */
	private float weight = 1;

	/**
	 * Set to true if Entity was placed at least once for a Level. Helper flag
	 */
	private boolean placed;

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
			return true;

		if (o != null && o instanceof Entity)
			return this.id.equals(((Entity) o).id);

		return false;
	}

	@Override
	public int hashCode()
	{
		return 17 + this.id.hashCode();
	}

	// /////////////////////////
	// //GETTERS AND SETTERS////
	// /////////////////////////
	public String getId()
	{
		return this.id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public String getUrl()
	{
		return this.url;
	}

	public void setUrl(final String url)
	{
		this.url = url;
	}

	public float getPropability()
	{
		return this.propability;
	}

	public void setPropability(final float propability)
	{
		this.propability = propability;
	}

	public float getWeight()
	{
		return this.weight;
	}

	public void setWeight(final float weight)
	{
		this.weight = weight;
	}

	public Range<Float> getHeight()
	{
		return this.height;
	}

	public void setHeight(final Range<Float> height)
	{
		this.height = height;
	}

	public Range<Float> getRadius()
	{
		return this.radius;
	}

	public void setRadius(final RangeFloatNorm rangeFloatNorm)
	{
		this.radius = rangeFloatNorm;
	}

	public RangeFloatNorm getDegree()
	{
		return this.degree;
	}

	public void setDegree(final RangeFloatNorm degree)
	{
		this.degree = degree;
	}

	public RangeFloatNorm getScale()
	{
		return this.scale;
	}

	public void setScale(final RangeFloatNorm scale)
	{
		this.scale = scale;
	}

	public boolean isPlaced()
	{
		return this.placed;
	}

	public void setPlaced(final boolean placed)
	{
		this.placed = placed;
	}

}