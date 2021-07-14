package fr.erias.edsabb.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriteOutput implements IWriteOutput {

	private FileOutputStream fos;
	
	public WriteOutput(File outputFile) throws IOException {
		outputFile.delete();
		outputFile.createNewFile();
		boolean append = true;
		fos = new FileOutputStream(outputFile, append);
	}
	
	public FileOutputStream getFileOutputStream() {
		return(fos);
	}
	
	@Override
	public void write (String line) throws IOException {
		fos.write(line.getBytes());
	}
	
	public void close() throws IOException {
		this.fos.close();
	}
}
