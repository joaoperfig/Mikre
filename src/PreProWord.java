import java.util.ArrayList;

public class PreProWord extends PreProcessed {
	private static String lowerCases = "abcdefghijklmnopqrstuvwxyz'-";
	private static String upperCases = "ABCDEFGHIJKLMNOPQRSTUVWXYZ'-";
	public String lower;

	public PreProWord(String c) {
		super(c);
		pptype = "word";
		lower = c.toLowerCase();
	}
	
	public boolean isValid(LangData ld) {
		char[] chars = content.toCharArray();
		for(int i=1; i<chars.length; i++) {
			if (lowerCases.indexOf(chars[i])<0) {
				// if a letter after the first one is not lowercase, then this is not valid
				return false;
			}
		}
		return ld.hasWord(lower);
	}
	
	public boolean isUpperCase() { // assuming is valid
		return (upperCases.indexOf(content.charAt(0))>=0);
	}
	
	public ArrayList<Boolean> toBits(LangData ld){
		System.out.println("ERROR: REQUESTED BITS FROM WORD");
		return null;
	}
	
	public ArrayList<Boolean> wordBits(LangData ld, PreProWord last){
		ArrayList<Boolean> res = new ArrayList<Boolean>();
		WordFollowers followLast;
		if (last == null) {
			followLast = ld.starters;
		} else {
			followLast = ld.database.get(last.lower);
		}
		
		boolean caseAbnormal = (isUpperCase() != ld.caseData.usuallyUpperCase(lower)); 
		boolean needsGeneral = !followLast.hasWord(lower);
		
		if (needsGeneral) {
			followLast = ld.general;
		}
		
		if (caseAbnormal || needsGeneral) {
			res.add(true);  // a 1 at the start of a word means it needs special parameters
			res.add(caseAbnormal);// add parameters
			res.add(needsGeneral);// add parameters
			//System.out.println("Abormal word -> 1"+ (caseAbnormal ? "1" : "0") + (needsGeneral ? "1" : "0"));
		} else {
			res.add(false);  // a 0 at the start of a word means it does not need special parameters
			//System.out.println("Normal word -> 0");
		}
		
		res.addAll(followLast.compressed.getBitsOf(lower)); // add Huffman bits of this word 
		//System.out.println("Huffman Bits -> "+BitOperations.bitsToBinaryString(followLast.compressed.getBitsOf(lower)));
		
		return res;
	}
	
	public String getGunk() {
		System.out.println("ERROR: REQUESTED GUNK FROM WORD");
		return "";
	}

}
