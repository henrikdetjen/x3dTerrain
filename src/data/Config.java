package data;
import java.util.SortedSet;

public class Config {
	/////////////////////////////////////////////
	/**
	 * Die einzelnen Level (=Files)
	 */
	private SortedSet<Level> levels;
	/**
	 * hierhin werden die Ergebnisse geschrieben
	 */
	private String outputFolder;
	
	//////////////////////////////////////////////
	public SortedSet<Level> getLevels() {
		return levels;
	}
	public void setLevels(SortedSet<Level> levels) {
		this.levels = levels;
	}
	public String getOutputFolder() {
		return outputFolder;
	}
	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}
	
}