package me.rayzr522.flash.gui.properties;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A property that will notify listeners when it was changed.
 *
 * @param <T> the type of the data it wraps
 */
public class ObservableProperty<T> {

    private Set<Runnable> listeners;
    private T value;

    /**
     * Creates a new {@link ObservableProperty} with the given initial value.
     *
     * @param value the initial value
     */
    public ObservableProperty(T value) {
        this.value = value;

        this.listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        listeners.forEach(Runnable::run);
    }

    /**
     * Adds a listener, that is called after changes were applied.
     *
     * @param listener the listener to run
     * @return a {@link Disposable} to undo the subscription
     */
    public Disposable addListener(Runnable listener) {
        listeners.add(listener);
        return new Disposable(this, listener);
    }

    /**
     * Removes a listener.
     *
     * @param listener the listener to remove
     */
    public void removeListener(Runnable listener) {
        listeners.remove(listener);
    }

    @Override
    public String toString() {
        return "ObservableProperty{" +
                "value=" + value +
                '}';
    }
}
