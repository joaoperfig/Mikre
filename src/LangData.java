import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.util.Pair;

public class LangData implements Serializable {
	public WordFollowers general;                   //General index of all known words
	public WordFollowers starters;                  //Index of words at start of sentences
	public HashMap<String, WordFollowers> database;  //Markov chain of words
	public boolean currentUpdatedHuffman;
	public CaseData caseData;
	
	public LangData() {
		general = new WordFollowers("");
		starters = new WordFollowers("");
		database = new HashMap<String, WordFollowers>();
		currentUpdatedHuffman = false;
		caseData = new CaseData();
	}
	
	public static LangData fromFile(String filePath) {
		LangData ld = null;
		try {
		     FileInputStream fileIn = new FileInputStream(filePath);
		     ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(fileIn));
		     ld = (LangData) in.readObject();
		     in.close();
		     fileIn.close();
		} catch (IOException i) {
		     i.printStackTrace();
		     return null;
		} catch (ClassNotFoundException c) {
		     c.printStackTrace();
		     return null;
		}
		return ld;
	      
	}
	
	public boolean hasStarter(String word) {
		return starters.hasWord(word);
	}
	
	public boolean hasWord(String word) {
		return general.hasWord(word);
	}
	
	public Integer getGlobalIndexOf(String word) {
		return general.getSortedIndexOf(word);
	}
	
	public String getWordAtGlobalIndex(int index) {
		return general.getAtSortedIndex(index);
	}
	
	public Integer getStarterIndexOf(String word) {
		return starters.getSortedIndexOf(word);
	}
	
	public String getWordAtStarterIndex(int index) {
		return starters.getAtSortedIndex(index);
	}
	
	public void processInfo(CleanText ct) {
		caseData.add(ct.caseData);
		currentUpdatedHuffman = false;
		String lastWord = null;
		String word;
		for (ArrayList<String> sentence: ct.sentences) {
			for(int i=0; i<sentence.size(); i++) {
				word = sentence.get(i);
				if (i==0) {
					starters.addFollowInstance(word);
				} else {
					database.get(lastWord).addFollowInstance(word);
				}
				general.addFollowInstance(word);
				if(!database.containsKey(word)) {
					database.put(word, new WordFollowers(word));
				}
				lastWord = word;
			}
		}
	}
	
	public void showBasicInfo() {
		System.out.println("Database info:");
		System.out.println("Total words: "+database.size());
		int transitions = starters.totalTransitions;
		for (Pair<String, Integer> p: general.sortedFollowers) {
			String word = p.getKey();
			transitions += database.get(word).totalTransitions;
		}
		if (currentUpdatedHuffman) {
			System.out.println("Huffman trees are up to date.");
		} else {
			System.out.println("Huffman trees need to be computed!");
		}
		System.out.println("Total transitions: "+transitions);
		System.out.println("Most common words and most likely sentences starting from them:");
		for (int i =0; i<30; i++) {
			StringBuilder sb = new StringBuilder();
			String word = getWordAtGlobalIndex(i);
			sb.append("   ");
			sb.append(word);
			StringBuilder bitcodesb = new StringBuilder();
			bitcodesb.append(" 111");
			bitcodesb.append(BitOperations.codestr(general.compressed.getBitsOf(word)));
			for (int j=0; j<10; j++) {
				bitcodesb.append(" 0");
				bitcodesb.append(BitOperations.codestr(database.get(word).compressed.getBitsOf(database.get(word).getAtSortedIndex(0)))); 
				word = database.get(word).getAtSortedIndex(0);
				sb.append(" ");
				sb.append(word);
			}
			System.out.print(sb.toString());
			System.out.print(" -> ");
			System.out.println(bitcodesb.toString());
		}
		System.out.println("Most common words and most likely case:");
		for (int i =0; i<30; i++) {
			StringBuilder sb = new StringBuilder();
			String word = getWordAtGlobalIndex(i);
			sb.append("   ");
			sb.append(word);
			if(caseData.usuallyUpperCase(word)) {
				sb.append(" -> UpperCase");
			}else {
				sb.append(" -> LowerCase");
			}
			System.out.println(sb.toString());
		}
		System.out.println("Some uppercase words:");
		int last = 0;
		for (int i = 0; i<30; i++) {
			StringBuilder sb = new StringBuilder();
			String word = getWordAtGlobalIndex(last);
			while (!caseData.usuallyUpperCase(word)) {
				last++;
				word = getWordAtGlobalIndex(last);
			}
			last++;
			sb.append("   ");
			sb.append(word);
			System.out.println(sb.toString());
		}
	}
	
	public void UpdateHuffman() {
		general.UpdateHuffman();
		starters.UpdateHuffman();
		for (WordFollowers wf : database.values()) {
			wf.UpdateHuffman();
		}
		currentUpdatedHuffman = true;
	}
	
	public void saveTofile(String filePath) {
		 try {
	         FileOutputStream fileOut =  new FileOutputStream(filePath);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(this);
	         out.close();
	         fileOut.close();
	      } catch (IOException i) {
	         i.printStackTrace();
	      }
	}
}
