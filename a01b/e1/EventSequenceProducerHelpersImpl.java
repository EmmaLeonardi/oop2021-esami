package a01b.e1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

public class EventSequenceProducerHelpersImpl implements EventSequenceProducerHelpers {

	@Override
	public <E> EventSequenceProducer<E> fromIterator(Iterator<Pair<Double, E>> iterator) {
		return new EventSequenceProducer<E>() {

			@Override
			public Pair<Double, E> getNext() throws NoSuchElementException {
				if (iterator.hasNext()) {
					return iterator.next();
				}
				throw new NoSuchElementException("Iterator has finished elements");
			}
		};
	}

	@Override
	public <E> List<E> window(EventSequenceProducer<E> sequence, double fromTime, double toTime) {
		List<E> result = new ArrayList<>();
		var event = sequence.getNext();
		while (event.get1() <= toTime) {
			if (event.get1() >= fromTime) {
				result.add(event.get2());
			}
			event = sequence.getNext();
		}

		return result;
	}

	@Override
	public <E> Iterable<E> asEventContentIterable(EventSequenceProducer<E> sequence) {
		return new Iterable<E>() {

			@Override
			public Iterator<E> iterator() {
				return new Iterator<E>() {

					private Optional<E> elem = Optional.empty();

					@Override
					public boolean hasNext() {
						try {
							if (elem.isEmpty()) {
								elem = Optional.of(sequence.getNext().get2());
							}
							return true;
						} catch (NoSuchElementException e) {
							return false;
						}
					}

					@Override
					public E next() {
						if (elem.isPresent()) {
							var result = elem.get();
							elem = Optional.empty();
							return result;
						}
						return sequence.getNext().get2();
					}
				};
			}
		};
	}

	@Override
	public <E> Optional<Pair<Double, E>> nextAt(EventSequenceProducer<E> sequence, double time) {
		var event = sequence.getNext();
		while (event.get1() <= time) {
			try {
				event = sequence.getNext();
			} catch (NoSuchElementException e) {
				return Optional.empty();
			}
		}
		return Optional.of(event);
	}

	@Override
	public <E> EventSequenceProducer<E> filter(EventSequenceProducer<E> sequence, Predicate<E> predicate) {
		return new EventSequenceProducer<E>() {

			@Override
			public Pair<Double, E> getNext() throws NoSuchElementException {
				var elem = sequence.getNext();
				if (predicate.test(elem.get2())) {
					return elem;
				} else {
					return getNext();
				}
			}
		};
	}

}
