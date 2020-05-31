import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

public class CaseData implements Serializable {
	public HashMap<String, Integer> upperCount;
	
	public CaseData() {
		upperCount = new HashMap<String, Integer>();
	}
	
	public boolean containsWord(String word) { // pass as lowercase!
		return upperCount.containsKey(word);
	}
	
	public void deltaWord(String word, int delta) { // pass as lowercase!
		if (containsWord(word)) {
			upperCount.put(word, upperCount.get(word)+delta);
		} else {
			upperCount.put(word, delta);
		}
	}
	
	public void addUpperWord(String word) {
		String lower = word.toLowerCase();
		deltaWord(lower, 1); // add one to count
	}
	
	public void addLowerWord(String word) {
		String lower = word.toLowerCase();
		deltaWord(lower, -1); // subtract one to count
	}
	
	public int getCount(String word) {
		String lower = word.toLowerCase();
		if (containsWord(lower)) {
			return  upperCount.get(lower);
		} else {
			return 0;
		}
	}
	
	public boolean usuallyUpperCase(String word) {
		int c = getCount(word);
		return (c > 0); //if tied, assume lowercase
	}
	
	public void add(CaseData other) { // add the data of other casedata
		for (Entry<String, Integer> entry : other.upperCount.entrySet()) {
			deltaWord(entry.getKey(), entry.getValue());
		}
	}

}
