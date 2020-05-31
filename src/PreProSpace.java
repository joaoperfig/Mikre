import java.util.ArrayList;

public class PreProSpace extends PreProcessed {

	public PreProSpace(String c) {
		super(c);
		if(!c.equals(" ")) {
			System.out.println("WARNING: PREPROSPACE INITIATED WITH NON SPACE");
		}
		pptype = "space";
	}
	
	public String getGunk() {
		System.out.println("ERROR: REQUESTED GUNK FROM SPACE");
		return "";
	}
	
	public ArrayList<Boolean> toBits(LangData ld){
		System.out.println("ERROR: REQUESTED BITS FROM SPACE");
		return null;
	}


}
