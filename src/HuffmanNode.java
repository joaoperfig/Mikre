import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.util.Pair;

public class HuffmanNode implements Serializable {
	
	public String data;
	public int frequency;
	public HuffmanNode left;
	public HuffmanNode right;
	public boolean leaf;
	public ArrayList<Boolean> code;
	
	public HuffmanNode(String word, int freq) {
		data = word;
		frequency = freq;
		left = null;
		right = null;
		leaf = true;
	}
	
	public HuffmanNode() {
		
	}
	
	public static HuffmanNode makeParent(HuffmanNode l, HuffmanNode r) {
		HuffmanNode res = new HuffmanNode();
		res.leaf = false;
		res.left = l;
		res.right = r;
		res.data = null;
		res.frequency = l.frequency + r.frequency;
		return res;
	}
	
	public void setCodes(ArrayList<Boolean> current) {
		code = current;
		if(left != null) {
			ArrayList<Boolean> lal = new ArrayList<Boolean>(current);
			lal.add(false);
			left.setCodes(lal);
		}
		if (right != null) {
			ArrayList<Boolean> ral = new ArrayList<Boolean>(current);
			ral.add(true);
			right.setCodes(ral);
		}
	}
	
	public int depth() {
		int leftd = 0;
		if (left != null) {
			leftd = left.depth();
		}
		int rightd = 0;
		if (right != null) {
			rightd = right.depth();
		}
		return Math.max(leftd, rightd)+1;
	}

	
	public void debugPrint() {
		if (leaf) {
			System.out.println("   "+data+"  "+Integer.toString(frequency)+"   "+BitOperations.codestr(code));
		}
		if(left != null) {
			left.debugPrint();
		}
		if (right != null) {
			right.debugPrint();
		}
	}
	
	public void selfRegister(HashMap<String, ArrayList<Boolean>> map) {
		if (leaf) {
			map.put(data, code);
		}
		if(left != null) {
			left.selfRegister(map);
		}
		if (right != null) {
			right.selfRegister(map);
		}
	}
	
	public Pair<String, ArrayList<Boolean>> traverseFind(ArrayList<Boolean> findcode) {
		if(leaf) {
			return new Pair<String, ArrayList<Boolean>>(data, new ArrayList<Boolean>(code));
		}
		if (findcode.isEmpty()) {
			System.out.println("ERROR: Ran out of bits while descending huffmanTree");
			return null;
		}
		boolean bit = findcode.remove(0);
		if (bit) { // True, go right
			return right.traverseFind(findcode);
		} else {
			return left.traverseFind(findcode);
		} // false, go left
	}
}
