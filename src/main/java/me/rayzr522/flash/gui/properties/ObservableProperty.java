package me.rayzr522.flash.gui.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A property that will notify listeners when it was changed.
 *
 * @param <T> the type of the data it wraps
 */
public class ObservableProperty<T> {

    private List<Runnable> listeners;
    private T value;

    /**
     * Creates a new {@link ObservableProperty} with the given initial value.
     *
     * @param value the initial value
     */
    public ObservableProperty(T value) {
        this.value = value;

        this.listeners = new ArrayList<>();
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
     */
    public void addListener(Runnable listener) {
        listeners.add(listener);
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
