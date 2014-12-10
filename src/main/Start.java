package main;

import input.InputParser;

import java.util.Date;

import output.OutputType;
import output.OutputUtils;
import output.OutputWriter;
import placing.Placer;
import data.Config;
import data.Level;

//kann nat�rlich sp�ter alles �ber config Datei und Reflection laufen
public class Start
{
	public static void main(final String[] args)
	{
		// 1.Einlesen der Config
		InputParser r = null;

		if (args.length > 0)
		{
			final String cfg_path = args[0];
			r = new InputParser(cfg_path);
		}
		else
			r = new InputParser();
		final Config c = r.read();

		// OUTPUT
		final OutputWriter ow = new OutputWriter(c.getOutputFolder());
		// für alle Files / Levels
		for (final Level l : c.getLevels())
		{

			System.out.println("\n\n!!!START Level Entity WRITING!!!");
			System.out.println("==========================");
			for (int i = 0; i < l.getRuns(); i++)
			{
				String date = new Date().toGMTString(); // TODO: Timestamp
														// anders holen
				date = date.replaceAll(" ", "_");
				date = date.replaceAll(":", "_");
				final String filename = date + "_" + l.getId() + "_" + i
						+ ".x3d";
				final String result = new Placer(l).placeObjects();
				// String result = "test";
				ow.write(result, l.getMeta(), filename);
			}
			System.out.println("==========================");
			System.out.println("!!!WRITING Level Entity DONE!!!");

			if (l.isTerrainMesh())
			{
				System.out.println("\n\n!!!START Level Terrain Output!!!");

				final String terrain = OutputUtils.getTerrainGeometry(
						l.getScale(), l.getHeightmap(), l.getMeshScale(),
						OutputType.WRL);
				OutputWriter
						.write(terrain, c.getOutputFolder() + "terrain.txt");
				System.out.println(terrain);
			}
		}

	}
}
