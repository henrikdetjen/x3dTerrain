package data;

import java.util.HashMap;
import java.util.Set;
import java.util.SortedSet;

import utils.DimensionInt;

/**
 * Die Klasse beinhaltet alle zur Platzierung notwendigen Parameter
 * 
 */
public class Level implements Comparable<Level>
{

	// ////////////
	// VARIABLEN //
	// ////////////
	/**
	 * alle Metainformationen Key-Value
	 */
	private HashMap<String, String> meta;
	/**
	 * Anzahl der Durchlaeufe für das Level
	 */
	private int runs;
	/**
	 * die id des Files/Levels
	 */
	private String id;
	/**
	 * die einzelnen Layer
	 */
	private SortedSet<Layer> layers;
	/**
	 * Die Hoehenkarte (Heightmap), wie hoch sollen die Objekte in der Hoehe
	 * platziert werden? Y Dimension (Hoehe)
	 */
	private Heightmap heightmap;
	/**
	 * Größe des levels
	 */
	private DimensionInt scale;
	/**
	 * Output units per scale unit.
	 */
	private int units;
	/**
	 * Definiert, ob der Koordinatenursprung an der Ecke oder in der Mitte des
	 * Levels sein soll
	 */
	private boolean center;
	/**
	 * Definiert, ob ein "Grundlayer" zur übergebenen Höhenkarte erzeugt werden
	 * soll
	 */
	private boolean terrainMesh;
	/**
	 * die Auflösung eines "Meshes" in X- und Y-Richtung
	 */
	private DimensionInt meshScale;

	// ///////////////////////
	// GETTERS ANS SETTERS //
	// ///////////////////////

	public int getRuns()
	{
		return this.runs;
	}

	public boolean isCenter()
	{
		return this.center;
	}

	public void setCenter(final boolean center)
	{
		this.center = center;
	}

	public DimensionInt getMeshScale()
	{
		return this.meshScale;
	}

	public void setMeshScale(final DimensionInt meshScale)
	{
		this.meshScale = meshScale;
	}

	public boolean isTerrainMesh()
	{
		return this.terrainMesh;
	}

	public void setTerrainMesh(final boolean terrainMesh)
	{
		this.terrainMesh = terrainMesh;
	}

	public void setRuns(final int runs)
	{
		this.runs = runs;
	}

	public String getId()
	{
		return this.id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public DimensionInt getScale()
	{
		return this.scale;
	}

	public void setScale(final DimensionInt scale)
	{
		this.scale = scale;
	}

	public Heightmap getHeightmap()
	{
		return this.heightmap;
	}

	public void setHeightmap(final Heightmap heightmap)
	{
		this.heightmap = heightmap;
	}

	public Set<Layer> getLayers()
	{
		return this.layers;
	}

	public void setLayers(final SortedSet<Layer> layers)
	{
		this.layers = layers;
	}

	public int getUnits()
	{
		return this.units;
	}

	public void setUnits(final int units)
	{
		this.units = units;
	}

	public HashMap<String, String> getMeta()
	{
		return this.meta;
	}

	public void setMeta(final HashMap<String, String> meta)
	{
		this.meta = meta;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
			return true;

		if (o != null && o instanceof Level)
			return this.id.equals(((Level) o).id);

		return false;
	}

	@Override
	public int hashCode()
	{
		return 17 + this.id.hashCode();
	}

	@Override
	public int compareTo(final Level level)
	{
		return this.id.compareTo(level.id);
	}

}
