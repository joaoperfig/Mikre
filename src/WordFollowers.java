import java.util.HashMap;

// Class to save transitions from a word (sort of like a Markov Chain "Node"
public class WordFollowers {
	private String word;
	private HashMap<String, Integer> followers;
	private int totalTransitions;
	
	public WordFollowers(String thisword) {
		word = thisword;
		followers = new HashMap<String, Integer>();
		totalTransitions = 0;
	}
	
	public String getWord() {
		return word;
	}
	
	public void addFollowInstance(String follower) {
		totalTransitions++;
		Integer current = followers.get(follower);
		if(current == null) {
			followers.put(follower, 1);
		} else {
			followers.put(follower, current+1);
		}				
	}
	
	public int getTransitionsTo(String follower) {  //Number of transition instances
		Integer current = followers.get(follower);
		if(current == null) {
			return 0;
		} else {
			return current;
		}			
	}
	
	public int getTransitionProbabilityTo(String follower) {
		Integer current = followers.get(follower);
		if(current == null) {
			return 0;
		} else {
			return current/totalTransitions;
		}	
	}
}
