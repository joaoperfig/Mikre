import java.util.ArrayList;

public class PreProGunk extends PreProcessed {
	
	public PreProGunk(String c) {
		super(c);
		pptype = "gunk";
	}
	
	public void addGunk(PreProGunk gunk) {
		content = content + gunk.content;
	}
	
	public ArrayList<Boolean> toBits(LangData ld){
		ArrayList<Boolean> res = new ArrayList<Boolean>();
		res.add(true); // first bit is 1 to show its gunk
		for (int i=0; i<countMarkerChars(); i++) {
			res.add(true);  // add number of markers
		}
		return res;
	}
	
	public int countMarkerChars() {
		char marker = ',';
		int count = 0;
		  
		for (int i = 0; i < content.length(); i++) {
		    if (content.charAt(i) == marker) {
		        count++;
		    }
		}
		return count;
	}
	
	public String getGunk() {
		return content;
	}
}
