package me.rayzr522.flash.gui.properties;

import me.rayzr522.flash.factory.LogFactory;
import me.rayzr522.flash.gui.display.Node;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Watches child Nodes for changes, to update the parent pane.
 */
public class NodePropertyChangeWatcher {

    private static final Logger LOGGER = LogFactory.create(NodePropertyChangeWatcher.class);

    private Callback changeCallback;
    private Map<Node, Runnable> watched;

    /**
     * Reports to a given pane.
     *
     * @param changeCallback the callback to report to
     */
    public NodePropertyChangeWatcher(Callback changeCallback) {
        this.changeCallback = changeCallback;

        this.watched = new HashMap<>();
    }

    /**
     * Watches a node for changes.
     *
     * @param node the node to watch
     */
    public void watchNode(Node node) {
        Runnable changeListener = () -> changeCallback.nodeChanged(node);

        for (ObservableProperty<?> property : getPropertiesSafe(node)) {
            property.addListener(changeListener);
        }

        watched.put(node, changeListener);
    }

    /**
     * Returns all properties, handling an access error kinda gracefully.
     *
     * @param node the node to retrieve the properties for
     * @return all found properties
     */
    private Collection<ObservableProperty<?>> getPropertiesSafe(Node node) {
        try {
            return getProperties(node);
        } catch (IllegalAccessException e) {
            LOGGER.log(Level.WARNING, "Could not read property from Node!", e);
        }

        return Collections.emptyList();
    }

    /**
     * Returns all properties in the given class or a superclass of it
     *
     * @param node the node to get them for
     * @return all found properties
     * @throws IllegalAccessException if the access was denied
     */
    private Collection<ObservableProperty<?>> getProperties(Node node) throws IllegalAccessException {
        Set<ObservableProperty<?>> properties = new HashSet<>();

        Class<?> currentClass = node.getClass();
        while (currentClass.getSuperclass() != null) {
            properties.addAll(getProperties(node, currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }

        return properties;
    }

    private Set<ObservableProperty<?>> getProperties(Node handle, Field[] fields) throws IllegalAccessException {
        Set<ObservableProperty<?>> properties = new HashSet<>();

        for (Field field : fields) {
            ObservableProperty<?> property = processField(field, handle);

            if (property != null) {
                properties.add(property);
            }
        }

        return properties;
    }

    private ObservableProperty<?> processField(Field field, Node handle) throws IllegalAccessException {
        Class<?> type = field.getType();
        if (!ObservableProperty.class.isAssignableFrom(type)) {
            return null;
        }
        field.setAccessible(true);

        return (ObservableProperty<?>) field.get(handle);
    }

    /**
     * Stops watching a node for changes.
     *
     * @param node the node to stop watching for
     */
    public void unwatchChild(Node node) {
        Runnable runnable = watched.get(node);
        for (ObservableProperty<?> property : getPropertiesSafe(node)) {
            property.removeListener(runnable);
        }

        watched.remove(node);
    }

    @FunctionalInterface
    public interface Callback {

        /**
         * Called when a property of a node was changed.
         *
         * @param node the node that changed
         */
        void nodeChanged(Node node);
    }
}
