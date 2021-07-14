package fr.erias.edsabb.writer;

import java.io.IOException;

public class WriteOutputTest implements IWriteOutput {

	public String lines = "" ;
	
	@Override
	public void write(String line) throws IOException {
		System.out.print(line);
		this.lines = this.lines + line;
	}
}
