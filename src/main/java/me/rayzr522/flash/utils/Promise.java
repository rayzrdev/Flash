package me.rayzr522.flash.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * JavaScript-inspired Promise class to handle asynchronous future values.
 *
 * @param <T> The return type of the Promise.
 */
public class Promise<T> {
    private List<Consumer<T>> listeners = new ArrayList<>();

    public Promise<T> then(Consumer<T> callback) {
        Objects.requireNonNull(callback, "callback cannot be null!");

        listeners.add(callback);
        return this;
    }

    public <K> Promise<K> then(Function<T, K> callback) {
        Objects.requireNonNull(callback, "callback cannot be null!");

        Promise<K> output = new Promise<>();
        listeners.add(value -> output.resolve(callback.apply(value)));
        return output;
    }

    public void resolve(T value) {
        listeners.forEach(listener -> listener.accept(value));
    }
}
