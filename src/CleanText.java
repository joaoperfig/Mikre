import java.util.ArrayList;

public class CleanText {
	public ArrayList<ArrayList<String>> sentences;
	private static String sentenceDividers = ".!?:;\n";
	private static String lowerCases = "abcdefghijklmnopqrstuvwxyz";
	private static String upperCases = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public CleanText() {
		sentences = new ArrayList<ArrayList<String>>();
	}
	
	public CleanText(String dirtyText) {
		sentences = new ArrayList<ArrayList<String>>();
		
		String allCases = lowerCases.concat(upperCases);
		
		ArrayList<String> thisSentence = new ArrayList<String>();
		StringBuilder wordBuilder = new StringBuilder();
		
		for(char c : dirtyText.toCharArray()) {
		    if (sentenceDividers.indexOf(c)>=0) { //c is a sentence divider
		    	if (wordBuilder.length() != 0) {
		    		thisSentence.add(wordBuilder.toString());
		    		wordBuilder = new StringBuilder();
		    	}
		    	if (thisSentence.size() != 0) {
		    		sentences.add(thisSentence);
		    		thisSentence = new ArrayList<String>();
		    	}
		    } else if (allCases.indexOf(c)>=0) { //c is a letter
		    	int ui = upperCases.indexOf(c);
		    	if (ui >= 0) {
		    		c = lowerCases.charAt(ui); //turn c lowercase
		    	}
		    	wordBuilder.append(c);
		    } else { //c is trash or blankspace
		    	if (wordBuilder.length() != 0) {
		    		thisSentence.add(wordBuilder.toString());
		    		wordBuilder = new StringBuilder();
		    	}
		    }
		}
		//process last strings on stacks
		if (wordBuilder.length() != 0) {
    		thisSentence.add(wordBuilder.toString());
    		wordBuilder = new StringBuilder();
    	}
    	if (thisSentence.size() != 0) {
    		sentences.add(thisSentence);
    		thisSentence = new ArrayList<String>();
    	}		
	}
	
	public void appendText(String dirtyText) {
		append(new CleanText(dirtyText));
	}
	
	public void append(CleanText cleanText) {
		sentences.addAll(cleanText.sentences);
	}
	
	public void showText() {
		for (ArrayList<String> sentence : sentences) {
			StringBuilder sb = new StringBuilder();
			for (String word: sentence) {
				sb.append(word);
				sb.append(", ");
			}
			System.out.println(sb.toString());
		}
	}
}
