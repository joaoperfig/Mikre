import java.util.ArrayList;

public class BitOperations {
	
	public static String codestr(ArrayList<Boolean> code) {
		String res = "";
		for (boolean b : code) {
			res = res + (b ? "1" : "0" );
		}
		return res;
	}
	
	public static int numberOfBitsToIndex(int cases) {
		int res = 0;
		int possibs = 1;
		while(possibs < cases) {
			res++;
			possibs = possibs*2;
		}
		return res;
	}
	
	public static ArrayList<Boolean> binaryStringToBits(String s){
		ArrayList<Boolean> res = new ArrayList<Boolean>();
		for(char c : s.toCharArray()) {
			res.add(c ==  '1'); // true if 1, false if 0
		}
		return res;
	}
	
	public static String bitsToBinaryString(ArrayList<Boolean> bits) {
		StringBuilder sb = new StringBuilder();
		for (boolean bit : bits) {
			if (bit) sb.append("1");
			else sb.append("0");
		}
		return sb.toString();
	}
	
	public static ArrayList<Boolean> intToBits(int numbits, int value){
		String binstr = Integer.toBinaryString(value); // kinda spaghetti
		if (binstr.length() > numbits) {
			System.out.println("Not enouhgt bits to convert this int!");
		}
		ArrayList<Boolean> res = new ArrayList<Boolean>();
		for(int i=0; i<(numbits-binstr.length()); i++) {
			res.add(false); // add zeroes to fill space
		}
		for(char c : binstr.toCharArray()) {
			res.add(c ==  '1'); // true if 1, false if 0
		}
		return res;
	}
	
	public static ArrayList<Byte> bitsToBytes(ArrayList<Boolean> bits){ // adds zeroes to the end if not multiple of 8
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		int bytebuilder = 0;
		int bitsdone = 0;
		for (boolean bit : bits) {
			bytebuilder = (bytebuilder*2) + (bit? 1 : 0);
			bitsdone++;
			if (bitsdone == 8) {
				bytes.add((byte)bytebuilder);
				bytebuilder = 0;
				bitsdone = 0;
			}
		}
		boolean added = false;
		while (bitsdone%8 != 0) {
			bytebuilder = (bytebuilder*2);
			bitsdone++;
			added = true;
		}
		if (added) {
			bytes.add((byte)bytebuilder);
		}
		return bytes;
	}
	
}
