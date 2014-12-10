package height_adj;


public class HeightAdjuster_DOM
{
	/*
	 * static final String HEIGHTMAP = "files/maps/heightmap.bmp"; static final
	 * float HEIGHTMAP_MIN = 0f; static final float HEIGHTMAP_MAX = 10f; static
	 * final String FILE_TO_ADJUST =
	 * "src/height_adj/vegetation_flat_manual.x3d"; static final float FILE_X =
	 * 100f; static final float FILE_Y = 100f; static final String RESULTFILE =
	 * "files/results/vegetation.x3d";
	 * 
	 * static DocumentBuilderFactory dbFactory; static DocumentBuilder dBuilder;
	 * static Document doc;
	 * 
	 * public static void main(final String[] args) { try {
	 * HeightAdjuster.readDomDoc(); } catch (SAXException | IOException |
	 * ParserConfigurationException e) { e.printStackTrace(); } final
	 * BufferedImage image = HeightAdjuster .readImg(HeightAdjuster.HEIGHTMAP);
	 * final Heightmap heightmap = new Heightmap(image, new RangeFloatNorm(
	 * HeightAdjuster.HEIGHTMAP_MIN, HeightAdjuster.HEIGHTMAP_MAX));
	 * 
	 * final NodeList nodes = HeightAdjuster.doc
	 * .getElementsByTagName("Transform"); for (int i = 0; i <
	 * nodes.getLength(); i++) { System.out.println(i + "..."); final
	 * NamedNodeMap nnm = nodes.item(i).getAttributes(); final Node attr =
	 * nnm.getNamedItem("translation");
	 * System.out.println(attr.getTextContent()); final String POS[] =
	 * attr.getTextContent().split(" "); final float X =
	 * Float.parseFloat(POS[0]); final float Y = Float.parseFloat(POS[1]); final
	 * float Z = Float.parseFloat(POS[2]); final float height =
	 * heightmap.getNiveau().calculate( heightmap.lookupValue(X /
	 * HeightAdjuster.FILE_X, Y / HeightAdjuster.FILE_Y));
	 * 
	 * System.out.println("(" + X + "/" + Z + "): " + Y + " -> " + height);
	 * attr.setTextContent(X + " " + height + " " + Z);
	 * System.out.println(attr.getTextContent()); }
	 * 
	 * try { final TransformerFactory transformerFactory = TransformerFactory
	 * .newInstance(); final Transformer transformer =
	 * transformerFactory.newTransformer(); final DOMSource source = new
	 * DOMSource(HeightAdjuster.doc); final StreamResult result = new
	 * StreamResult(new File( HeightAdjuster.RESULTFILE));
	 * transformer.transform(source, result); } catch (final Exception e) { }
	 * 
	 * /* // TransformerFactory instance is used to create Transformer objects.
	 * final TransformerFactory factory = TransformerFactory.newInstance();
	 * Transformer transformer = null; try { transformer =
	 * factory.newTransformer(); } catch (final
	 * TransformerConfigurationException e3) { // TODO Auto-generated catch
	 * block e3.printStackTrace(); }
	 * 
	 * transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	 * transformer.setOutputProperty(OutputKeys.METHOD, "xml"); // transformer
	 * .setOutputProperty("{http://xml.apache.org/xslt}indent-amount", // "3");
	 * 
	 * // create string from xml tree final StringWriter sw = new
	 * StringWriter(); final StreamResult result = new StreamResult(sw); final
	 * DOMSource source = new DOMSource(HeightAdjuster.doc); try {
	 * transformer.transform(source, result);
	 * 
	 * final String xmlString = sw.toString(); System.out.println(xmlString);
	 * final File file = new File(HeightAdjuster.RESULTFILE);
	 * 
	 * final FileWriter fw = new FileWriter(file); fw.write(xmlString);
	 * fw.flush(); fw.close(); } catch (final IOException e2) { // TODO
	 * Auto-generated catch block e2.printStackTrace(); }
	 */
	/*
	 * }
	 * 
	 * static void readDomDoc() throws SAXException, IOException,
	 * ParserConfigurationException { HeightAdjuster.dbFactory =
	 * DocumentBuilderFactory.newInstance(); HeightAdjuster.dBuilder =
	 * HeightAdjuster.dbFactory.newDocumentBuilder(); HeightAdjuster.doc =
	 * HeightAdjuster.dBuilder.parse(new File( HeightAdjuster.FILE_TO_ADJUST));
	 * HeightAdjuster.doc.getDocumentElement().normalize(); }
	 * 
	 * static BufferedImage readImg(final String path) { BufferedImage img =
	 * null; try { img = ImageIO.read(new File(path)); } catch (final
	 * IOException e) { System.out.println("Bildfehler / Falsche Pfadangabe: " +
	 * e); } return img; }
	 */
}
