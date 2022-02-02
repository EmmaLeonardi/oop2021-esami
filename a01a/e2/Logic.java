package a01a.e2;

import java.util.Map;

public interface Logic {
	
	public void pressed(Pair<Integer, Integer> coords);
	
	public Map<Pair<Integer,Integer>,String> getStars();
	
	public boolean isOver();

}
