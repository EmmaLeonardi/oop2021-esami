package a01a.e1;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AcceptorFactoryAdvancedImpl implements AcceptorFactory {

	@Override
	public Acceptor<String, Integer> countEmptyStringsOnAnySequence() {
		return generalised(0, (e, s) -> e.equals("") ? Optional.of(s + 1) : Optional.of(s), s -> Optional.of(s));
	}

	@Override
	public Acceptor<Integer, String> showAsStringOnlyOnIncreasingSequences() {
		return generalised(new ArrayList<Integer>(), (e, l) -> {
			if (l.size() == 0 || l.get(l.size() - 1) < e) {
				l.add(e);
				return Optional.of(l);
			}
			return Optional.empty();
		}, l -> l.stream().map(a -> a.toString()).reduce((a, b) -> a.concat(":" + b)));
	}

	@Override
	public Acceptor<Integer, Integer> sumElementsOnlyInTriples() {
		return generalised(new ArrayList<Integer>(), (e, s) -> {
			s.add(e);
			if (s.size() > 3) {
				return Optional.empty();
			}
			return Optional.of(s);
		}, s -> s.size() == 3 ? Optional.of(s.stream().mapToInt(a -> a).sum()) : Optional.empty());
	}

	@Override
	public <E, O1, O2> Acceptor<E, Pair<O1, O2>> acceptBoth(Acceptor<E, O1> a1, Acceptor<E, O2> a2) {
		return generalised(new Pair<>(a1, a2), (e, s) -> {
			if (s.get1().accept(e) && s.get2().accept(e)) {
				return Optional.of(new Pair<>(s.get1(), s.get2()));
			}
			return Optional.empty();
		}, s -> {
			Optional<O1> o1 = s.get1().end();
			Optional<O2> o2 = s.get2().end();
			if (o1.isPresent() && o2.isPresent()) {
				return Optional.of(new Pair<>(o1.get(), o2.get()));
			}
			return Optional.empty();
		});
	}

	@Override
	public <E, O, S> Acceptor<E, O> generalised(S initial, BiFunction<E, S, Optional<S>> stateFun,
			Function<S, Optional<O>> outputFun) {
		return new Acceptor<E, O>() {

			private Optional<S> state = Optional.ofNullable(initial);

			@Override
			public boolean accept(E e) {
				if (state.isPresent()) {
					state = stateFun.apply(e, state.get());
				}
				return state.isPresent();
			}

			@Override
			public Optional<O> end() {
				if (state.isPresent()) {
					return outputFun.apply(state.get());
				}
				return Optional.empty();

			}
		};
	}

}
