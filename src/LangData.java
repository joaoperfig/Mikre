import java.util.ArrayList;
import java.util.HashMap;

public class LangData {
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
}
