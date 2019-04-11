import javafx.util.Pair;

public class Main {
	public static void main(String[] args) {
        WordFollowers batata = new WordFollowers("batata");
        
        batata.addFollowInstance("doce");
        batata.addFollowInstance("com");
        batata.addFollowInstance("e");
        batata.addFollowInstance("mas");
        batata.addFollowInstance("que");
        batata.addFollowInstance("mais");
        batata.addFollowInstance("com");
        batata.addFollowInstance("e");
        batata.addFollowInstance("mas");
        batata.addFollowInstance("com");
        batata.addFollowInstance("e");
        batata.addFollowInstance("mas");
        batata.addFollowInstance("com");
        batata.addFollowInstance("e");
        batata.addFollowInstance("mas");
        batata.addFollowInstance("e");
        batata.addFollowInstance("com");
        batata.addFollowInstance("e");
        batata.addFollowInstance("com");
        batata.addFollowInstance("com");
        batata.addFollowInstance("e");
        batata.addFollowInstance("e");
        batata.addFollowInstance("e");
        batata.addFollowInstance("e");
        batata.addFollowInstance("e");
        batata.addFollowInstance("com");
        batata.addFollowInstance("e");
        batata.addFollowInstance("e");
        batata.addFollowInstance("e");

        
        for (Pair<String, Integer> p : batata.sortedFollowers) {
        	System.out.println(p.getKey());
        	System.out.println(p.getValue());
        }
        
        System.out.println(batata.getSortedIndexOf("com"));
        System.out.println(batata.getSortedIndexOf("ola"));
    }
}
