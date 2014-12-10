package height_adj;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import utils.RangeFloatNorm;
import data.Heightmap;

public class HeightAdjuster
{

	private static final String HEIGHTMAP = "files/maps/heightmap.bmp";
	private static final float HEIGHTMAP_MIN = 0f;
	private static final float HEIGHTMAP_MAX = 10f;
	private static final float ENV_W = 100f;
	private static final float ENV_H = 100f;
	private static final float URSPRUNG_X = +0.5f; // s
	private static final float URSPRUNG_Y = +0.5f; // t
	private static final String FILE_TO_ADJUST = "src/height_adj/tombstones_flat.x3d";
	private static final String RESULTFILE = "files/results/tombstones.x3d";

	public static void main(final String[] args)
	{
		// Heightmap holen
		final BufferedImage image = HeightAdjuster
				.readImg(HeightAdjuster.HEIGHTMAP);
		final Heightmap heightmap = new Heightmap(image, new RangeFloatNorm(
				HeightAdjuster.HEIGHTMAP_MIN, HeightAdjuster.HEIGHTMAP_MAX));

		// Transform Werte aus Datei einlesen
		final ArrayList<String> buffer = HeightAdjuster.readFile();
		// int c = 0;
		// Werte anpassen
		for (int l = 0; l < buffer.size(); l++)
		{
			String s = buffer.get(l);
			// TRANSFORM KNOTEN gefunden
			if (s.contains("<Transform") || s.contains("<transform"))
			{
				final String[] tmp = s.split("\"");
				for (int i = 0; i < tmp.length; i++)
					// TRANSLATION ATTR gefunden
					if (tmp[i].contains("translation"))
					{
						// KOORDINATEN einlesen
						final String pos[] = tmp[i + 1].split(" ");
						final float x = Float.parseFloat(pos[0]);
						final float y = Float.parseFloat(pos[1]);
						final float z = Float.parseFloat(pos[2]);
						// HAUPTROUTINE
						final int h = 100;
						final int w = 100;
						float height = heightmap.lookupValue(x
								/ HeightAdjuster.ENV_W
								+ HeightAdjuster.URSPRUNG_X, z
								/ HeightAdjuster.ENV_H
								+ HeightAdjuster.URSPRUNG_Y);
						height = heightmap.getNiveau().calculate(height);
						System.out.println("( " + x + "["
								+ (x / w + HeightAdjuster.URSPRUNG_X) + "] / "
								+ z + "[" + (z / h + HeightAdjuster.URSPRUNG_Y)
								+ "]" + "): " + y + " -> " + height);
						tmp[i + 1] = new String(x + " " + (y + height) + " "
								+ z);
						// viewpoints ausgeben?
						// final String view = tmp[i + 3].split(" ")[3];
						// System.out.println("<Viewpoint DEF='" + c
						// + "' description='vp_" + c + "' position='" + x
						// + " " + (y + height) + " " + (z - 2)
						// + "' orientation='0 1 0 " + view
						// + "' jump='true' />");
						// c++;

						break;
					}
				// reset old String
				s = "";
				// append changed String / rebuild split
				for (int i = 0; i < tmp.length; i++)
				{

					s += tmp[i];
					if (i < tmp.length - 1)
						s += "\"";
				}
				buffer.set(l, s);
				// System.out.println(s);
			}
		}

		// Buffer wieder in Datei schreiben
		HeightAdjuster.writeFile(buffer);

	}

	private static void writeFile(final ArrayList<String> buffer)
	{
		try (FileWriter fw = new FileWriter(HeightAdjuster.RESULTFILE);
				BufferedWriter bw = new BufferedWriter(fw))
		{
			for (final String s : buffer)
			{
				bw.write(s + "\n");
				bw.flush();
			}
		}
		catch (final Exception e)
		{
		}

	}

	private static ArrayList<String> readFile()
	{
		final ArrayList<String> buffer = new ArrayList<String>();
		try (final FileReader fr = new FileReader(HeightAdjuster.FILE_TO_ADJUST);
				final BufferedReader br = new BufferedReader(fr);)
		{
			String line = "";
			while (line != null)
			{
				buffer.add(line);
				line = br.readLine();
			}
			buffer.remove(0);
		}
		catch (final Exception e)
		{
		}
		return buffer;
	}

	private static BufferedImage readImg(final String path)
	{
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(new File(path));
		}
		catch (final IOException e)
		{
			System.out.println("Bildfehler / Falsche Pfadangabe: " + e);
		}
		return img;
	}
}
