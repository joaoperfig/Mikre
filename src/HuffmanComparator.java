import java.util.Comparator;

public class HuffmanComparator implements Comparator<HuffmanNode> { 
    public int compare(HuffmanNode x, HuffmanNode y) 
    { 
  
        return x.frequency - y.frequency; 
    } 
} 