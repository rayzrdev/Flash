package me.rayzr522.flash.gui.display;

import me.rayzr522.flash.factory.LogFactory;
import me.rayzr522.flash.gui.properties.ObservableProperty;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Watches child Nodes for changes, to update the parent pane.
 */
class ChildChangeWatcher {

    private static final Logger LOGGER = LogFactory.create(ChildChangeWatcher.class);

    private Pane owner;
    private Map<Node, Runnable> watched;

    ChildChangeWatcher(Pane owner) {
        this.owner = owner;

        this.watched = new HashMap<>();
    }

    /**
     * Watches a node for changes.
     *
     * @param node the node to watch
     */
    public void watchNode(Node node) {
        Runnable changeListener = () -> owner.renderChildImpl(node);

        for (ObservableProperty<?> property : getPropertiesSafe(node)) {
            property.addListener(changeListener);
        }

        watched.put(node, changeListener);
    }

    private Collection<ObservableProperty<?>> getPropertiesSafe(Object object) {
        try {
            return getProperties(object);
        } catch (IllegalAccessException e) {
            LOGGER.log(Level.WARNING, "Could not read property from Node!", e);
        }

        return Collections.emptyList();
    }

    private Collection<ObservableProperty<?>> getProperties(Object object) throws IllegalAccessException {
        Set<ObservableProperty<?>> properties = new HashSet<>();

        Class<?> currentClass = object.getClass();
        while (currentClass.getSuperclass() != null) {
            properties.addAll(getProperties(object, currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }

        return properties;
    }

    private Set<ObservableProperty<?>> getProperties(Object handle, Field[] fields) throws IllegalAccessException {
        Set<ObservableProperty<?>> properties = new HashSet<>();

        for (Field field : fields) {
            ObservableProperty<?> property = processField(field, handle);

            if (property != null) {
                properties.add(property);
            }
        }

        return properties;
    }

    private ObservableProperty<?> processField(Field field, Object handle) throws IllegalAccessException {
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
}
