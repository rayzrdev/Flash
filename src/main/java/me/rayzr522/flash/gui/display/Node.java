package me.rayzr522.flash.gui.display;

import me.rayzr522.flash.gui.GuiEventReceiver;
import me.rayzr522.flash.gui.RenderTarget;
import me.rayzr522.flash.gui.events.ClickEvent;
import me.rayzr522.flash.gui.properties.ObservableProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The base for everything that can be displayed in a gui.
 */
public abstract class Node implements GuiEventReceiver {

    private Map<Object, Object> attachedData;
    private WeakReference<RenderTarget> lastRenderTarget;

    private ObservableProperty<Integer> width;
    private ObservableProperty<Integer> height;

    public Node(int width, int height) {
        this.width = new ObservableProperty<>(width);
        this.height = new ObservableProperty<>(height);

        this.attachedData = new HashMap<>();
        this.lastRenderTarget = new WeakReference<>(null);
    }

    public int getWidth() {
        return width.getValue();
    }

    public int getHeight() {
        return height.getValue();
    }

    public void setWidth(int width) {
        this.width.setValue(width);
    }

    public void setHeight(int height) {
        this.height.setValue(height);
    }

    /**
     * Returns the currently set {@link RenderTarget}.
     *
     * @return the current {@link RenderTarget}, if present
     */
    protected Optional<RenderTarget> getRenderTarget() {
        return Optional.ofNullable(lastRenderTarget.get());
    }

    /**
     * The internal method used for rendering this component.
     * <p>
     * <p><em>Do <strong>not</strong> call this yourself. In fact, you shouldn't call {@link #render(RenderTarget)}
     * either...</em>
     *
     * @param target the target to render with
     */
    public final void implRender(RenderTarget target) {
        lastRenderTarget = new WeakReference<>(target);
        render(target);
    }

    /**
     * Renders this component onto the given {@link RenderTarget}.
     *
     * @param target the target to render with
     */
    public abstract void render(RenderTarget target);

    @Override
    public void onClick(ClickEvent clickEvent) {
    }

    /**
     * Retrieves some attached data.
     *
     * @param key the key
     * @param <K> the type of the key, will be blindly cast
     * @param <V> the type of the value, will be blindly cast
     * @return the data, if any
     */
    @Nullable
    public <K, V> V getAttachedData(@Nonnull K key) {
        @SuppressWarnings("unchecked")
        V v = (V) attachedData.get(key);
        return v;
    }

    /**
     * Removes some attached data.
     *
     * @param key the key
     * @param <K> the type of the key, will be blindly cast
     * @param <V> the type of the value, will be blindly cast
     * @return the data that was previously associated with this key, if any
     */
    public <K, V> V removeAttachedData(@Nonnull K key) {
        @SuppressWarnings("unchecked")
        V v = (V) attachedData.remove(key);
        return v;
    }

    /**
     * Stores some data on this node.
     *
     * @param key   the key
     * @param value the value to store
     * @param <K>   the type of the key, will be blindly cast
     * @param <V>   the type of the value, will be blindly cast
     */
    public <K, V> void setAttachedData(K key, V value) {
        attachedData.put(key, value);
    }

}
