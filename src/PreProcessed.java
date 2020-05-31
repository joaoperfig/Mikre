import java.util.ArrayList;

public class PreProcessed {
	public String content;
	public String pptype = "neutral";
	
	public PreProcessed(String c) {
		content = c;
	}
	
	public ArrayList<Boolean> toBits(LangData ld){
		return new ArrayList<Boolean>();
	}
	
	public String getGunk() {
		return "";
	}
	
}
