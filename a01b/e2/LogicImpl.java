package a01b.e2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LogicImpl implements Logic {

	private final Map<Pair<Integer, Integer>, String> grid;
	private final int size;
	private final static String VOID = " ";
	private final static String STAR = "*";
	private final static String INNER = "1";
	private final static String EDGE1 = "2";
	private final static String EDGE2 = "3";

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
	public Map<Pair<Integer, Integer>, String> getGrid() {
		return Map.copyOf(grid);
	}

	private Optional<Pair<Integer, Integer>> getPositionString(final String s) {
		for (var elem : grid.entrySet()) {
			if (elem.getValue().equals(s)) {
				return Optional.of(elem.getKey());
			}
		}
		return Optional.empty();
	}

	@Override
	public void hit(final Pair<Integer, Integer> coords) {
		if (coords != null && coords.getX() >= 0 && coords.getX() < this.size && coords.getY() >= 0
				&& coords.getY() < this.size) {
			// Coordinata valida
			if (getPositionString(INNER).isEmpty()) {
				// Posiziono 1
				grid.put(coords, INNER);
				return;
			}
			if (getPositionString(EDGE1).isEmpty()) {
				// Posiziono 2, solo se è in linea con INNER
				var pos = getPositionString(INNER).get();
				if (pos.getX().equals(coords.getX()) || pos.getY().equals(coords.getY())) {
					// E' in linea
					grid.put(coords, EDGE1);
				}
				return;
			}
			if (getPositionString(EDGE2).isEmpty()) {
				var in = getPositionString(INNER).get();
				var out = getPositionString(EDGE1).get();
				if ((in.getX().equals(coords.getX()) || in.getY().equals(coords.getY())) && coords.getX() != out.getX()
						&& coords.getY() != out.getY()) {
					grid.put(coords, EDGE2);
					int max, min;
					if (in.getX().equals(coords.getX())) {
						// Lo spazio tra 1 e 3 è verticale
						min = in.getY() < coords.getY() ? in.getY() : coords.getY();
						max = in.getY() >= coords.getY() ? in.getY() : coords.getY();
					} else {
						// Lo spazio tra 1 e 2 è verticale
						min = in.getY() < out.getY() ? in.getY() : out.getY();
						max = in.getY() >= out.getY() ? in.getY() : out.getY();
					}
					// Riempito lo spazio verticale
					for (int i = min + 1; i < max; i++) {
						grid.replace(new Pair<>(in.getX(), i), STAR);
					}
					if (in.getY().equals(coords.getY())) {
						// Lo spazio tra 1 e 3 è verticale
						min = in.getX() < coords.getX() ? in.getX() : coords.getX();
						max = in.getX() >= coords.getX() ? in.getX() : coords.getX();
					} else {
						// Lo spazio tra 1 e 2 è verticale
						min = in.getX() < out.getX() ? in.getX() : out.getX();
						max = in.getX() >= out.getX() ? in.getX() : out.getX();
					}
					// Riempito lo spazio verticale
					for (int i = min + 1; i < max; i++) {
						grid.replace(new Pair<>(i, in.getY()), STAR);
					}
				}
			}
		}
	}

	@Override
	public boolean isOver() {
		if (getPositionString(INNER).isPresent() && getPositionString(EDGE1).isPresent()
				&& getPositionString(EDGE2).isPresent()) {
			return true;
		}
		return false;
	}

}
