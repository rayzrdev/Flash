package me.rayzr522.flash.gui.properties;

import javax.annotation.Nonnull;
import java.util.Objects;

public class Disposable {

    private ObservableProperty<?> property;
    private Runnable runnable;

    public Disposable(@Nonnull ObservableProperty<?> property, @Nonnull Runnable runnable) {
        this.property = Objects.requireNonNull(property, "property can not be null!");
        this.runnable = Objects.requireNonNull(runnable, "runnable can not be null!");
    }

    public void dispose() {
        property.removeListener(runnable);
    }
}
