package me.rayzr522.flash.struct;

import java.util.Objects;

/**
 * A simple pair, that holds two values.
 *
 * @param <R> the type of the first element
 * @param <T> the type of the second element
 */
public class Pair<R, T> {

    private R first;
    private T second;

    public Pair(R first, T second) {
        this.first = first;
        this.second = second;
    }

    public R getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    public static <R, T> Pair<R, T> of(R first, T second) {
        return new Pair<>(first, second);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
