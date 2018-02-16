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

    private List<ObservableProperty<?>> getPropertiesSafe(Object object) {
        try {
            return getProperties(object);
        } catch (IllegalAccessException e) {
            LOGGER.log(Level.WARNING, "Could not read property from Node!", e);
        }

        return Collections.emptyList();
    }

    private List<ObservableProperty<?>> getProperties(Object object) throws IllegalAccessException {
        List<ObservableProperty<?>> properties = new ArrayList<>();

        for (Field field : object.getClass().getDeclaredFields()) {
            Class<?> type = field.getType();
            if (!ObservableProperty.class.isAssignableFrom(type)) {
                continue;
            }
            field.setAccessible(true);

            properties.add((ObservableProperty<?>) field.get(object));
        }

        return properties;
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
