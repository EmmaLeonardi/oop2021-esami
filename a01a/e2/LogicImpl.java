package a01a.e2;

import java.util.HashMap;
import java.util.Map;

public class LogicImpl implements Logic {

	private final int size;
	private final Map<Pair<Integer, Integer>, String> grid;
	private final static String VOID = " ";
	private final static String STAR = "*";
	private final static String FIRST = "1";

	public LogicImpl(final int size) {
		this.size = size;
		this.grid = new HashMap<>();
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				grid.put(new Pair<>(i, j), VOID);
			}
		}

	}

	@Override
	public void pressed(final Pair<Integer, Integer> coords) {
		if (coords != null && coords.getX() >= 0 && coords.getX() < this.size && coords.getY() >= 0
				&& coords.getY() < this.size) {
			// Coordinata valida
			if (getFirstCoords() == null) {
				// Non c'è un 1, lo setto
				grid.put(new Pair<>(coords.getX(), coords.getY()), FIRST);
			} else {
				Pair<Integer, Integer> coord1 = getFirstCoords();
				// devo riempire lo spazio tra coords e 1 di *
				Pair<Integer, Integer> coordMin = new Pair<>(
						coord1.getX() < coords.getX() ? coord1.getX() : coords.getX(),
						coord1.getY() < coords.getY() ? coord1.getY() : coords.getY());
				Pair<Integer, Integer> coordMax = new Pair<>(
						coord1.getX() >= coords.getX() ? coord1.getX() : coords.getX(),
						coord1.getY() >= coords.getY() ? coord1.getY() : coords.getY());

				for (int i = coordMin.getX(); i <= coordMax.getX(); i++) {
					for (int j = coordMin.getY(); j <= coordMax.getY(); j++) {
						grid.put(new Pair<>(i, j), STAR);
					}
				}
			}
		}
	}

	@Override
	public Map<Pair<Integer, Integer>, String> getStars() {
		return Map.copyOf(grid);
	}

	@Override
	public boolean isOver() {
		// Se non c'è nessuno spazio
		for (var elem : grid.entrySet()) {
			if (elem.getValue() == VOID) {
				return false;
			}
		}
		return true;
	}

	private Pair<Integer, Integer> getFirstCoords() {
		for (var elem : grid.entrySet()) {
			if (elem.getValue().equals(FIRST)) {
				return elem.getKey();
			}
		}
		return null;
	}
}
