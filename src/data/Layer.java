package data;

import java.util.HashMap;
import java.util.Set;

import utils.RangeFloatNorm;

public class Layer implements Comparable<Layer>
{

	// /////////////
	// VARIABLEN //
	// /////////////
	/**
	 * z.B. Baum, Kraut, ..
	 */
	private String id;
	/**
	 * Layer-Order (niedrigste = zuerst)
	 */
	private int number;
	/**
	 * Koennen die Layerpositionen ueberschrieben werden? True = kein
	 * ueberschreiben der Objektpositionen moeglich False = der Layer kann
	 * ueberschrieben werden (z.B. kann Gras auf Position eines Baums sein)
	 */
	private boolean isStatic;
	/**
	 * Einheit für die Platzierungsdichte im Originalraster
	 */
	private float units;
	/**
	 * Objekte die platziert werden sollen
	 */
	private Set<Entity> entities;

	/**
	 * Die Verteilungskarte (DensityMap), wo sollen die Objekte in der Ebene
	 * platziert werden? X-Z Dimension (Flaeche)
	 */
	private BlackWhiteMap densityMap;

	/**
	 * Helper: saves random Ranges for Entities
	 */
	private HashMap<RangeFloatNorm, Entity> randomEntity;

	/**
	 * Initializes the Entity's the Layer is responsible of
	 * 
	 * @param entities
	 */
	public void initEntities(final Set<Entity> entities)
	{
		if (entities == null || entities.isEmpty())
			throw new IllegalArgumentException(
					"Entities must not be null nor empty.");
		this.entities = entities;

		// 1. create range [0 - sumWeight]
		float sumWeight = 0.0f;
		for (final Entity entity : entities)
			sumWeight += entity.getWeight();

		// 2. divide each weight with sumWeight all
		// ranges are mapped to -> [0 - 1] for later random retrieval
		float tmpWeight = 0.0f;
		float entityWeight = 0.0f;
		this.randomEntity = new HashMap<RangeFloatNorm, Entity>();

		for (final Entity e : entities)
		{
			entityWeight = e.getWeight() / sumWeight;
			final RangeFloatNorm from_To = new RangeFloatNorm(tmpWeight,
					tmpWeight + entityWeight);
			this.randomEntity.put(from_To, e);
			tmpWeight += entityWeight;
		}
	}

	/**
	 * Retrieves a random entity
	 * 
	 * @return
	 */
	public Entity getRandomEntity()
	{
		// Zufallszahl von 0-1
		final float random = (float) Math.random();
		// ...in der Zuordnung range(0-1) -> entity finden
		for (final RangeFloatNorm toHandle : this.randomEntity.keySet())
			if (toHandle.getMin() <= random && toHandle.getMax() >= random)
				return this.randomEntity.get(toHandle);
		return null;
	}

	// ///////////////////////
	// GETTERS ANS SETTERS //
	// ///////////////////////
	public String getId()
	{
		return this.id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public int getNumber()
	{
		return this.number;
	}

	public void setNumber(final int number)
	{
		this.number = number;
	}

	public boolean isStatic()
	{
		return this.isStatic;
	}

	public void setStatic(final boolean isStatic)
	{
		this.isStatic = isStatic;
	}

	public BlackWhiteMap getDensityMap()
	{
		return this.densityMap;
	}

	public void setDensityMap(final BlackWhiteMap densityMap)
	{
		this.densityMap = densityMap;
	}

	public Set<Entity> getEntities()
	{
		return this.entities;
	}

	public float getUnits()
	{
		return this.units;
	}

	public void setUnits(final float units)
	{
		this.units = units;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
			return true;

		if (o != null && o instanceof Layer)
			return this.id.equals(((Layer) o).id);

		return false;
	}

	@Override
	public int hashCode()
	{
		return 17 + this.id.hashCode();
	}

	@Override
	public int compareTo(final Layer layer)
	{
		if (this.number < layer.number)
			return -1;
		else if (this.number > layer.number)
			return 1;
		else
			return 0;
	}

}
