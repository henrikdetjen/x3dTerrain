package input;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.DimensionInt;
import utils.Range;
import utils.RangeFloatNorm;
import data.BlackWhiteMap;
import data.Config;
import data.Entity;
import data.Heightmap;
import data.Layer;
import data.Level;

public class InputParser implements InputReader
{

	static String CFG_FILE_PATH = "files/config.xml";
	static final String MAP_PATH = "files/maps/";
	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Document doc;

	public InputParser()
	{

	}

	public InputParser(final String cfgFilePath)
	{
		InputParser.CFG_FILE_PATH = cfgFilePath;
	}

	@Override
	public Config read()
	{
		try
		{
			// Init File
			final File cfg_File = new File(InputParser.CFG_FILE_PATH);
			this.dbFactory = DocumentBuilderFactory.newInstance();
			this.dBuilder = this.dbFactory.newDocumentBuilder();
			this.doc = this.dBuilder.parse(cfg_File);
			this.doc.getDocumentElement().normalize();

			return this.readCfg(this.doc);
		}
		catch (final Exception e)
		{
		}
		return null;
	}

	private Config readCfg(final Document doc)
	{
		final Config cfg = new Config();
		System.out.println("!!!START READING!!!");
		System.out.println("root of xml file = "
				+ doc.getDocumentElement().getNodeName());
		final Node cfg_node = doc.getElementsByTagName("config").item(0);
		// Outputfolder
		cfg.setOutputFolder(InputParser.getAttributeValue("folder", cfg_node));
		// Levels
		final SortedSet<Level> levels = new TreeSet<Level>();
		System.out.println("Levels...");
		final NodeList nodes = cfg_node.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++)
		{
			final Node n = nodes.item(i);
			if (n.getNodeName() == "level")
				levels.add(this.readLevel(n));
		}
		cfg.setLevels(levels);
		return cfg;
	}

	private Level readLevel(final Node levelNode)
	{
		final Level lvl = new Level();
		// ID
		final String id = InputParser.getAttributeValue("id", levelNode);
		System.out.println(".....:::::Level - " + id + ":::::.....");
		lvl.setId(id);
		// runs
		final int runs = Integer.parseInt(InputParser.getAttributeValue("runs",
				levelNode));
		lvl.setRuns(runs);
		System.out.println("runs = " + runs);
		System.out.println("==========================");
		// ///////////////
		// / Metadaten ///
		// ///////////////
		final HashMap<String, String> meta = new HashMap<String, String>();
		System.out.println("Metadaten...");
		final NodeList nodes1 = InputParser.getNode("meta", levelNode)
				.getChildNodes();
		for (int i = 0; i < nodes1.getLength(); i++)
		{
			final Node n = nodes1.item(i);
			if (n.getNodeName() != "#text")
			{
				this.printNode(n);
				meta.put(n.getNodeName(), n.getTextContent());
			}
		}
		lvl.setMeta(meta);
		System.out.println("==========================");

		// ///////////////
		// /// Layer /////
		// ///////////////
		final SortedSet<Layer> layers = new TreeSet<Layer>();
		System.out.println("Layers...");
		final NodeList nodeLayers = InputParser.getNode("layers", levelNode)
				.getChildNodes();
		for (int i = 0; i < nodeLayers.getLength(); i++)
		{
			final Node n = nodeLayers.item(i);
			if (n.getNodeName() == "layer")
				layers.add(this.readLayer(n));
		}
		lvl.setLayers(layers);
		System.out.println("==========================");

		// Environment
		final Node nodeEnvironment = InputParser.getNode("environment",
				levelNode);

		// ///////////////
		// Heightmap ////
		// ///////////////
		final Node nodeHeightmap = InputParser.getNode("heightmap",
				nodeEnvironment);
		final String mapname2 = InputParser.getAttributeValue("src",
				nodeHeightmap);
		final String pathname2 = InputParser.MAP_PATH + mapname2;
		System.out.println("heightmap: " + pathname2);
		// min max niveau
		final Heightmap hm = new Heightmap(this.readImage(pathname2),
				InputParser.getXmlRangeFloatNorm(nodeHeightmap));
		lvl.setHeightmap(hm);

		// ///////////////
		// env-settings //
		// ///////////////
		final boolean mesh = Boolean.parseBoolean(InputParser
				.getAttributeValue("terrainMesh", nodeEnvironment));
		System.out.println("create mesh?: " + mesh);
		lvl.setTerrainMesh(mesh);
		if (mesh)
		{
			final int segmentsX = Integer.parseInt(InputParser
					.getAttributeValue("segmentsX", nodeEnvironment));
			System.out.println("x_segments: " + segmentsX);
			final int segmentsY = Integer.parseInt(InputParser
					.getAttributeValue("segmentsY", nodeEnvironment));
			System.out.println("y_segments: " + segmentsY);
			lvl.setMeshScale(new DimensionInt(segmentsX, segmentsY));
		}
		System.out.println("Original scale...");
		final int x = Integer.parseInt(InputParser.getAttributeValue("dimX",
				nodeEnvironment));
		System.out.println("x_axis: " + x);
		final int y = Integer.parseInt(InputParser.getAttributeValue("dimY",
				nodeEnvironment));
		System.out.println("y_axis: " + y);
		lvl.setScale(new DimensionInt(x, y));
		final boolean center = Boolean.parseBoolean(InputParser
				.getAttributeValue("center", nodeEnvironment));
		System.out.println("center coords?: " + center);
		lvl.setCenter(center);

		System.out.println("==========================");
		System.out.println("!!!READING DONE!!!");

		return lvl;
	}

	private Layer readLayer(final Node layerNode)
	{
		final Layer l = new Layer();
		// ID
		l.setId(InputParser.getAttributeValue("id", layerNode));
		System.out.println("\t**************************");
		System.out.println("\tLayer \"" + l.getId() + "\"");
		System.out.println("\t**************************");
		// nummer
		final int no = Integer.parseInt(InputParser.getAttributeValue("no",
				layerNode));
		l.setNumber(no);
		System.out.println("\tnr: " + no);
		// static?
		final boolean isStatic = Boolean.parseBoolean(InputParser
				.getAttributeValue("static", layerNode));
		l.setStatic(isStatic);
		System.out.println("\tstatic: " + isStatic);
		// units
		l.setUnits(Float.parseFloat(InputParser.getAttributeValue("units",
				layerNode)));

		// Densitymap
		final String mapname1 = InputParser.getAttributeValue("src",
				InputParser.getNode("densitymap", layerNode));
		final String pathname1 = InputParser.MAP_PATH + mapname1;
		System.out.println("\tdensitymap: " + pathname1);
		final BlackWhiteMap dm = this.readImage(pathname1);
		l.setDensityMap(dm);

		// ///////////////
		// // Objects ////
		// ///////////////
		System.out.println("\tEntities...");
		final Set<Entity> entities = new HashSet<Entity>();

		final NodeList nodeEntities = InputParser.getNode("objects", layerNode)
				.getChildNodes();

		for (int i = 0; i < nodeEntities.getLength(); i++)
		{
			final Node nodeEntity = nodeEntities.item(i);
			if (nodeEntity.getNodeName() == "object")
				entities.add(this.readObject(nodeEntity));
		}
		l.initEntities(entities);

		System.out.println("\t**************************\n");
		return l;
	}

	private Entity readObject(final Node objectNode)
	{
		final Entity e = new Entity();
		// ID
		final String id = InputParser.getAttributeValue("id", objectNode);
		e.setId(id);
		System.out.println("\t\t--------------------------");
		System.out.println("\t\tEntity \"" + e.getId() + "\"");
		System.out.println("\t\t--------------------------");
		// weight
		final Float weight = Float.parseFloat(InputParser.getAttributeValue(
				"weight", objectNode));
		System.out.println("\t\tweight: " + weight);
		e.setWeight(weight);
		// propability
		final Float propability = Float.parseFloat(InputParser
				.getAttributeValue("propability", objectNode));
		System.out.println("\t\tpropability: " + propability);
		e.setPropability(propability);
		// SOURCE
		final String source = InputParser.getAttributeValue("url", objectNode);
		System.out.println("\t\turl: " + source);
		e.setUrl(source);
		// height
		e.setHeight(InputParser.getXmlRangeFloat(InputParser.getNode("height",
				objectNode)));
		// radius
		e.setRadius(InputParser.getXmlRangeFloatNorm(InputParser.getNode(
				"radius", objectNode)));
		// Scale
		e.setScale(InputParser.getXmlRangeFloatNorm(InputParser.getNode(
				"scale", objectNode)));
		// degree
		e.setDegree(InputParser.getXmlRangeFloatNorm(InputParser.getNode(
				"degree", objectNode)));
		System.out.println("\t\t--------------------------\n");
		return e;
	}

	public static Range<Float> getXmlRangeFloat(final Node node)
	{
		final NamedNodeMap attrs = node.getAttributes();
		final Float min = Float.parseFloat(attrs.getNamedItem("min")
				.getTextContent());
		System.out.println("\t\t" + node.getNodeName() + ": min: " + min);
		final Float max = Float.parseFloat(attrs.getNamedItem("max")
				.getTextContent());
		System.out.println("\t\t" + node.getNodeName() + ": max: " + max);
		return new Range<Float>(min, max);
	}

	public static RangeFloatNorm getXmlRangeFloatNorm(final Node node)
	{
		final NamedNodeMap attrs = node.getAttributes();
		final Float min = Float.parseFloat(attrs.getNamedItem("min")
				.getTextContent());
		System.out.println("\t\t" + node.getNodeName() + ": min: " + min);
		final Float max = Float.parseFloat(attrs.getNamedItem("max")
				.getTextContent());
		System.out.println("\t\t" + node.getNodeName() + ": max: " + max);
		return new RangeFloatNorm(min, max);
	}

	/**
	 * Speicher für die Maps, die mit der readImage-Methode gelesen werden, um
	 * doppeltes Auslesen zu vermeiden
	 */
	private Map<String, BlackWhiteMap> bwMaps = null;

	private BlackWhiteMap readImage(final String path)
	{
		if (this.bwMaps == null)
			this.bwMaps = new HashMap<String, BlackWhiteMap>();
		if (this.bwMaps.containsKey(path))
			return this.bwMaps.get(path);

		BufferedImage img = null;
		try
		{
			img = ImageIO.read(new File(path));
			System.out.println("\t[sucessesfully read]");
			final BlackWhiteMap bwMap = new BlackWhiteMap(img);
			this.bwMaps.put(path, bwMap);
			return bwMap;
		}
		catch (final IOException e)
		{
			System.out.println("\t[reading error: " + e.toString() + "]");
		}
		return null;
	}

	private void printNode(final Node inNode)
	{
		if (inNode.getNodeName() != "#text")
			System.out.println(inNode.getNodeName() + ": "
					+ inNode.getTextContent());
	}

	private void printNode(final Node inNode, final int tabCount)
	{
		if (inNode.getNodeName() != "#text")
		{
			String tabs = "";
			for (int i = 0; i < tabCount; i++)
				tabs = tabs + "\t";
			System.out.println(tabs + inNode.getNodeName() + ": "
					+ inNode.getTextContent());
		}
	}

	private static Node getNode(final String tag, final Node parent)
	{
		Node node = null;

		for (int i = 0; i < parent.getChildNodes().getLength(); i++)
		{
			final Node child = parent.getChildNodes().item(i);
			if (child.getNodeName().equals(tag))
			{
				node = child;
				break;
			}
		}

		if (node == null)
			System.out.println("!!! Element " + tag + " not found !!!");
		return node;
	}

	private static String getAttributeValue(final String attribute,
			final Node node)
	{
		final NamedNodeMap atbs = node.getAttributes();
		return atbs.getNamedItem(attribute).getTextContent();
	}

	private static String getNodeValue(final String tag, final Node parent)
	{
		Node node = null;
		for (int i = 0; i < parent.getChildNodes().getLength(); i++)
			if (parent.getChildNodes().item(i).getNodeName() == tag)
				node = parent.getChildNodes().item(i);
		if (node == null)
			System.out.println("!!! Element " + tag + " not found !!!");
		return node.getTextContent();
	}

}
