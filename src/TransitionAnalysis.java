import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TransitionAnalysis {
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		System.out.println("Opening english file...");
		LangData ld = LangData.fromFile("data/english.mkrd");
		System.out.println("Done opening.");
		long durationseconds = (System.currentTimeMillis() - startTime)/100;
		System.out.print("Opening took ");
		System.out.print(durationseconds);
		System.out.println(" seconds.");
		String foldern = "data/test/";
		//String foldern = "data/experiment/";
		File folder = new File(foldern);
		File[] listOfFiles = folder.listFiles();
		int normalTransitions = 0;
		int generalTransitions = 0;
		int notFound = 0;
		int totalWords = 0;
		int capitalized = 0;
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
				totalWords += ct.totalwords;
				capitalized += ct.capitalized;
				
				for (ArrayList<String> sentence: ct.sentences) {
					sb = new StringBuilder();
					String lastWord = null;
					Integer val = 0;
					for (int j=0; j<sentence.size(); j++) {
						String word = sentence.get(j);
						if (j==0) {
							if (ld.hasStarter(word)) {
								val = ld.getStarterIndexOf(word);
								normalTransitions++;
							} else if (ld.hasWord(word)) {
								val = ld.getGlobalIndexOf(lastWord);
								generalTransitions++;
							} else {
								val = null;
								notFound++;
							}
						} else {
							if (ld.hasWord(lastWord) && ld.database.get(lastWord).hasWord(word)) {
								val = ld.database.get(lastWord).getSortedIndexOf(word);
								normalTransitions++;
							} else if (ld.hasWord(word)) {
								val = ld.getGlobalIndexOf(lastWord);
								generalTransitions++;
							} else {
								val = null;
								notFound++;
							}
						}
						lastWord = word;
						sb.append(val);
						sb.append(" ");
					}
					System.out.println(sb.toString());
				}
				
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Ignoring folder: " + listOfFiles[i].getName());
			}
		
		}
		System.out.print("Normal Transitions: ");
		System.out.println(normalTransitions);
		System.out.print("General Transitions: ");
		System.out.println(generalTransitions);
		System.out.print("New Words: ");
		System.out.println(notFound);
		System.out.print("Total Words: ");
		System.out.println(totalWords);
		System.out.print("Capitalized Words: ");
		System.out.println(capitalized);
		/*
		System.out.print("Opening took ");
		System.out.print(durationseconds);
		System.out.println(" seconds.");*/
	}
}
