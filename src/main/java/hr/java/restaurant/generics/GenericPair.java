package hr.java.restaurant.generics;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic pairing between two types of objects.
 *
 * @param <S> The first type in the pair.
 * @param <T> The second type in the pair.
 */
public class GenericPair<S, T> {

    private final List<Pair<S, T>> pairs;

    /**
     * Constructor to initialize the list of pairs.
     */
    public GenericPair() {
        this.pairs = new ArrayList<>();
    }

    /**
     * Adds a pair of objects to the list.
     *
     * @param first  The first object in the pair.
     * @param second The second object in the pair.
     */
    public void addPair(S first, T second) {
        pairs.add(new Pair<>(first, second));
    }

    /**
     * Returns the list of pairs.
     *
     * @return List of pairs.
     */
    public List<Pair<S, T>> getPairs() {
        return new ArrayList<>(pairs);
    }

    /**
     * Inner class to represent a pair of objects.
     *
     * @param <S> The first type in the pair.
     * @param <T> The second type in the pair.
     */
    public static class Pair<S, T> {
        private final S first;
        private final T second;

        public Pair(S first, T second) {
            this.first = first;
            this.second = second;
        }

        public S getFirst() {
            return first;
        }

        public T getSecond() {
            return second;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "first=" + first +
                    ", second=" + second +
                    '}';
        }
    }
}
