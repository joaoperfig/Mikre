import java.util.ArrayList;

public class PreProSentence extends PreProcessed {
	public ArrayList<PreProcessed> ppcontent;
	public ArrayList<PreProWord> words;
	public boolean hasStartSpace = false;
	public boolean hasEndChar = false;
	public char endchar;
	
	
	
	public PreProSentence() {
		super("");
		ppcontent = new ArrayList<PreProcessed>();
		words = new ArrayList<PreProWord>();
		pptype = "sentence";
	}
	
	public PreProSentence(PreProWord w) {
		super(w.content);
		ppcontent = new ArrayList<PreProcessed>();
		words = new ArrayList<PreProWord>();
		ppcontent.add(w);
		words.add(w);
		pptype = "sentence";
	}
	
	public PreProSentence (ArrayList<PreProcessed> list) {
		super("");
		pptype = "sentence";
		ppcontent = list;
		words = new ArrayList<PreProWord>();
		StringBuilder sb = new StringBuilder();
		for (PreProcessed pp : ppcontent) {
			sb.append(pp.content);
			if(pp instanceof PreProWord) {
				words.add((PreProWord) pp);
			}
		}
		content = sb.toString();
	}
	
	public boolean canAdd(PreProcessed pp) {
		if (hasEndChar) return false;
		
		if (ppcontent.get(ppcontent.size()-1) instanceof PreProWord) {
			return pp instanceof PreProSpace;
		} else if (ppcontent.get(ppcontent.size()-1) instanceof PreProSpace) {
			return pp instanceof PreProWord;
		} else {
			return false;
		}
	}
	
	public void add(PreProcessed pp) {
		ppcontent.add(pp);
		content = content+pp.content;
		if(pp instanceof PreProWord) {
			words.add((PreProWord) pp);
		}
	}
	
	public void addStartSpace() {
		if (!hasStartSpace) {
			hasStartSpace = true;
			content = " "+content;
		}
	}
	
	public boolean attemptSelfFinalize() {
		if (ppcontent.get(ppcontent.size()-1) instanceof PreProSpace) {
			ppcontent.remove(ppcontent.size()-1);
			hasEndChar = true;
			endchar = ' ';
			return true;
		} else {
			return false;
		}
	}
	
	public void addEndChar(char ec) {
		if (!hasEndChar) {
			hasEndChar = true;
			endchar = ec;
			content = content + ec;
		}
	}
	
	public int numWords() {
		return words.size();
	}
	
	public String getGunk() {
		if(!hasEndChar || Compressor.sentEnds.indexOf(endchar)>=0) { // endchar does not need to go into gunk
			return "";
		} else {
			return ""+endchar;
		}
	}
	
	public ArrayList<Boolean> toBits(LangData ld){
		ArrayList<Boolean> res = new ArrayList<Boolean>();
		res.add(false); // first bit is 0 to show its a sentence
		//System.out.println(">"+content+"<");
		//System.out.println("Sentence start -> 0");
		
		
		if(hasStartSpace) {
			res.add(true); // shows if has start space
			//System.out.println("First space -> 1");
		} else {
			res.add(false);
			//System.out.println("No First space -> 0");
		}
		
		PreProWord last = null;
		for(PreProWord word : words) {
			res.addAll(word.wordBits(ld, last));
			last = word;
		}
		
		if (!hasEndChar) {
			res.addAll(BitOperations.binaryStringToBits("0000")); // no endchar
			//System.out.println("No endchar -> 0000");
		} else if (Compressor.sentEnds.indexOf(endchar)>=0) {
			int charid = Compressor.sentEnds.indexOf(endchar);
			charid++; // add 1 because 0000 is taken
			res.addAll(BitOperations.intToBits(4, charid));
			//System.out.println("Other endchar -> "+BitOperations.bitsToBinaryString(BitOperations.intToBits(4, charid)));
		} else {
			res.addAll(BitOperations.binaryStringToBits("1111")); // gunk endchar
			//System.out.println("Gunk endchar -> 1111");
		}
		//System.out.println(BitOperations.bitsToBinaryString(res));
		
		res.addAll(BitOperations.binaryStringToBits("100")); // end marker
		
		return res;
	}

}
