package a01b.e2;

import java.util.Map;

public interface Logic {
	
	public Map<Pair<Integer,Integer>,String> getGrid();
	
	public void hit(Pair<Integer, Integer> coords);
	
	public boolean isOver();

}
