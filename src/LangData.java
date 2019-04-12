import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.util.Pair;

public class LangData implements Serializable {
	private WordFollowers general;                   //General index of all known words
	private WordFollowers starters;                  //Index of words at start of sentences
	public HashMap<String, WordFollowers> database;  //Markov chain of words
	
	public LangData() {
		general = new WordFollowers("");
		starters = new WordFollowers("");
		database = new HashMap<String, WordFollowers>();
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
			System.out.println(word);
			transitions += database.get(p).totalTransitions;
		}
		System.out.println("Total transitions: "+transitions);
		System.out.println("Most common words and most likely sentences starting from them:");
		for (int i =0; i<30; i++) {
			StringBuilder sb = new StringBuilder();
			String word = getWordAtGlobalIndex(i);
			sb.append("   ");
			sb.append(word);
			for (int j=0; j<10; j++) {
				word = database.get(word).getAtSortedIndex(0);
				sb.append(" ");
				sb.append(word);
			}
			System.out.println(sb.toString());
		}
	}
}
