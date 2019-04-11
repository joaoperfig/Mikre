import java.util.ArrayList;
import java.util.HashMap;
import javafx.util.Pair;


// Class to save transitions from a word (sort of like a Markov Chain "Node")
// Has sorted list of most likely transitions and hashMap that maps that list (to ensure constant access time)
public class WordFollowers {
	public String word;
	public HashMap<String, Integer> followerPositions;   //points to sortedFollowers
	public ArrayList<Pair<String, Integer>> sortedFollowers;
	public int totalTransitions;
	
	public WordFollowers(String thisword) {
		word = thisword;
		followerPositions = new HashMap<String, Integer>();
		sortedFollowers = new ArrayList<Pair<String, Integer>>();
		totalTransitions = 0;
	}
	
	public void createFollower(String follower) {
		int lastPos = sortedFollowers.size();
		sortedFollowers.add(new Pair<String, Integer>(follower, 1));
		followerPositions.put(follower, lastPos);
	}
	
	public void addFollowInstance(String follower) {
		totalTransitions++;
		Integer position = followerPositions.get(follower);

		if (position == null) {
			createFollower(follower);
			return;
		}
		
		int instances = sortedFollowers.get(position).getValue()+1;
		int swap = position;
		while (!((swap==0) || (sortedFollowers.get(swap-1).getValue()>=instances))) {
			swap = swap - 1;
		}
		Pair<String, Integer> carry = sortedFollowers.get(swap);
		Pair<String, Integer> thisone = new Pair<String, Integer>(follower, instances);
		sortedFollowers.set(position, carry);
		sortedFollowers.set(swap, thisone);
		
		followerPositions.put(carry.getKey(), position);
		followerPositions.put(follower, swap);
			
					
	}
	
	public int getTransitionsTo(String follower) {  //Number of transition instances
		Integer position = followerPositions.get(follower);
		if(position == null) {
			return 0;
		} else {	
			return sortedFollowers.get(position).getValue();
		}	
	}
	
	public int getTransitionProbabilityTo(String follower) {
		Integer position = followerPositions.get(follower);
		if(position == null) {
			return 0;
		} else {	
			return sortedFollowers.get(position).getValue()/totalTransitions;
		}	
	}
	
	public int getSortedIndexOf(String follower) {
		return followerPositions.get(follower);
	}
	
	public String getAtSortedIndex(int index) {
		return sortedFollowers.get(index).getKey();
	}
}
