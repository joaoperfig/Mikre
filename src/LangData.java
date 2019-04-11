import java.util.HashMap;

public class LangData {
	private WordFollowers general;                   //General index of all known words
	private WordFollowers starters;                  //Index of words at start of sentences
	public HashMap<String, WordFollowers> database;  //Markov chain of words
	
	public LangData() {
		general = new WordFollowers("");
		starters = new WordFollowers("");
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
}
