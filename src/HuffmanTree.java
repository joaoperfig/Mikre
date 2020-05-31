import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import javafx.util.Pair;

public class HuffmanTree implements Serializable {
	
	public HuffmanNode root;
	public int maxLabelLen;
	public HashMap<String, ArrayList<Boolean>> codeMap;
	
	
	public HuffmanTree(ArrayList<Pair<String, Integer>> frequencytable) {
		int n = frequencytable.size();
		//System.out.println(n);
		PriorityQueue<HuffmanNode> q = new PriorityQueue<HuffmanNode>(n, new HuffmanComparator()); 
		
		for (Pair<String, Integer> pair : frequencytable) {
			HuffmanNode node = new HuffmanNode(pair.getKey(), pair.getValue());
			q.add(node);
		}
		if (q.size() == 1) {
			root = q.peek();
		}
		if (q.isEmpty()) {
			System.out.println("Cannot create Huffman Tree for zero nodes!!!");
		}
		while (q.size() > 1) { 
			  
            // first min extract. 
            HuffmanNode x = q.peek(); 
            q.poll(); 
  
            // second min extract. 
            HuffmanNode y = q.peek(); 
            q.poll(); 
  
            // new node f which is equal 
            // to the sum of the frequency of the two nodes 
            // assigning values to the f node. 
            // first extracted node as left child. 
            // second extracted node as the right child. 
            HuffmanNode f = HuffmanNode.makeParent(y, x);
  
            // marking the f node as the root node. 
            root = f; 
  
            // add this node to the priority-queue. 
            q.add(f); 
        } 
		
		root.setCodes(new ArrayList<Boolean>());
		
		maxLabelLen = root.depth()-1;
		
		codeMap = new  HashMap<String, ArrayList<Boolean>>();
		root.selfRegister(codeMap);
		
		
	}
	
	public ArrayList<Boolean> getBitsOf(String key) {
		return new ArrayList<Boolean>(codeMap.get(key));
	}
	
	public Pair<String, ArrayList<Boolean>> getKeyOf(ArrayList<Boolean> bits) {
		return root.traverseFind(bits);
	}
	
	public void debugPrint() {
		System.out.println("Printing Huffman Tree...");
		root.debugPrint();
	}
	
	/*
	public static void main (String[] args) {
		ArrayList<Pair<String, Integer>> fq = new ArrayList<Pair<String, Integer>>();

		fq.add(new Pair<String, Integer>("g", 30));
		fq.add(new Pair<String, Integer>("r", 20));
		fq.add(new Pair<String, Integer>("h", 15));
		fq.add(new Pair<String, Integer>("k", 13));
		fq.add(new Pair<String, Integer>("x", 7));
		fq.add(new Pair<String, Integer>("?", 4));
		fq.add(new Pair<String, Integer>("#", 2));
		fq.add(new Pair<String, Integer>("$", 1));
		fq.add(new Pair<String, Integer>("a", 300));
		fq.add(new Pair<String, Integer>("e", 210));
		fq.add(new Pair<String, Integer>("o", 100));
		fq.add(new Pair<String, Integer>("m", 60));
		fq.add(new Pair<String, Integer>("l", 55));
		
		HuffmanTree ht = new HuffmanTree(fq);
		
		ht.debugPrint();
		System.out.println(ht.maxLabelLen);
		
		System.out.println("Checking HashMap...");
		for(Entry<String, ArrayList<Boolean>> entry : ht.codeMap.entrySet()) {
		    String key = entry.getKey();
		    ArrayList<Boolean> value = entry.getValue();
		    System.out.print(key);
		    System.out.print("   ");
		    System.out.println(value);
		}
		
		System.out.println("Checking code getter");
		System.out.print("l  ");
		System.out.println(ht.getBitsOf("l"));
		System.out.print("a  ");
		System.out.println(ht.getBitsOf("a"));
		System.out.print("$  ");
		System.out.println(ht.getBitsOf("$"));
		
		ArrayList<Boolean> ab;
		System.out.println("Checking key getter");
		System.out.print("l  ");
		System.out.println(ht.getKeyOf(ht.getBitsOf("l")));
		System.out.print("a  ");
		System.out.println(ht.getKeyOf(ht.getBitsOf("a")));
		System.out.print("$  ");
		System.out.println(ht.getKeyOf(ht.getBitsOf("$")));
		System.out.print("l++ ");
		ab = ht.getBitsOf("l");
		ab.add(false);
		ab.add(false);
		System.out.println(ht.getKeyOf(ab));
		System.out.print("a++ ");
		ab = ht.getBitsOf("a");
		ab.add(false);
		ab.add(true);
		System.out.println(ht.getKeyOf(ab));
		System.out.print("$++ ");
		ab = ht.getBitsOf("$");
		ab.add(true);
		ab.add(true);
		System.out.println(ht.getKeyOf(ab));
		System.out.print("l-- ");
		ab = ht.getBitsOf("l");
		ab.remove(ab.size()-1);
		System.out.println(ht.getKeyOf(ab));
	}*/
	
}

