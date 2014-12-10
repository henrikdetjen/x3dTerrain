package output;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class OutputWriter
{

	private final String OUTPUT_FILEPATH;

	public OutputWriter(final String resultFolder)
	{
		this.OUTPUT_FILEPATH = resultFolder;
	}

	public void write(final String content, final HashMap<String, String> meta,
			final String filename)
	{
		final String fullfilename = this.OUTPUT_FILEPATH + filename;
		try (FileReader fr = new FileReader(new File("src/output/output.x3d"));
				BufferedReader br = new BufferedReader(fr);
				FileWriter fw = new FileWriter(fullfilename))
		{
			final StringBuffer sb = new StringBuffer();
			String line = "";
			while (line != null)
			{
				line = br.readLine();
				// System.out.println(line);
				if (line != null)
					if (line.contains("[META]"))
						sb.append(this.buildMeta(meta));
					else if (line.contains("[CONTENT]"))
						sb.append(content);
					else
						sb.append(line);
			}

			// Ergebnis schreiben
			fw.write(sb.toString());
			System.out.println("written:" + fullfilename);
		}
		catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("failed to write file:" + fullfilename);
		}
	}

	public static void write(final String content, final String filename)
	{
		try (FileWriter fw = new FileWriter(filename))
		{
			// Ergebnis schreiben
			fw.write(content);
		}
		catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String buildMeta(final HashMap<String, String> meta)
	{
		final StringBuilder tmp = new StringBuilder();
		for (final String key : meta.keySet())
			tmp.append("<meta content='" + meta.get(key) + "' name='" + key
					+ "' />");
		return tmp.toString();
	}

}
