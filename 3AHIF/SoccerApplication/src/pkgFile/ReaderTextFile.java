package pkgFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReaderTextFile {
	private String filename;
	private FileReader fr;
	private BufferedReader br;
	
	public ReaderTextFile(String filename) {
		this.filename = filename;
	}
	public void open() throws IOException {
		if (fr != null) { fr.close(); }
		fr = new FileReader(filename);
		br = new BufferedReader(fr);
	}
	public void close() throws IOException {
		if (fr != null) fr.close();
	}
	public String readLine() throws IOException { 
		return fr != null ? br.readLine() : null;
	}
	
	public String getFilename() { return filename; }
}
