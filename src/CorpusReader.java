import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CorpusReader {
	
	public static void main(String[] args) {
		
		LangData ld = new LangData();
		String foldern = "data/corpus/";
		File folder = new File(foldern);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("Opening file: " + listOfFiles[i].getName());
				InputStream is = null;
				try {
					is = new FileInputStream(foldern+listOfFiles[i].getName());
				} catch (FileNotFoundException e) {	}
				
				BufferedReader buf = new BufferedReader(new InputStreamReader(is));
				        
				String line=null;
				try {
					line = buf.readLine();
				} catch (IOException e) { }
				StringBuilder sb = new StringBuilder();
				        
				while(line != null){
				   sb.append(line).append("\n");
				   try {
					line = buf.readLine();
				   } catch (IOException e) {}
				}
				String fileAsString = sb.toString();
				CleanText ct = new CleanText(fileAsString);
				ld.processInfo(ct);
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Ignoring folder: " + listOfFiles[i].getName());
			}
		}
		
		ld.showBasicInfo();
	}
}
