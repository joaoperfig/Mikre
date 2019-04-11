import javafx.util.Pair;

public class TestMain {
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
        
        System.out.println();
        
        String dt = "Ola, eu sou o Joao! Eu tenho 21 anos e sou muito fixe\nHeheheheh, bom meme.Ai... Tudo BEM";
        System.out.println(dt);
        CleanText ct = new CleanText (dt);
        ct.showText();
        
        LangData ld = new LangData();
        ld.processInfo(ct);
        
        System.out.println();
        System.out.println(ld.getWordAtGlobalIndex(0));
        System.out.println(ld.database.get("eu").getAtSortedIndex(0));
    }
}
