package me.rayzr522.flash.gui.properties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents an expression that is updated when a part changes.
 */
public class StringExpression extends ObservableProperty<String> {

    private final ObservableProperty<?> before;
    private final ObservableProperty<?> thisValue;
    private final String format;
    private final String defaultValue;

    /**
     * Creates a new {@link ObservableProperty} with the given initial value.
     *
     * @param value the initial value
     */
    public StringExpression(String value) {
        this(null, new ObservableProperty<>(value));
    }

    /**
     * Creates a new {@link ObservableProperty} with the given initial value.
     *
     * @param before    the value before this expression
     * @param thisValue the initial value
     */
    public StringExpression(@Nullable ObservableProperty<?> before, @Nullable ObservableProperty<?> thisValue) {
        this(before, thisValue, "%s%s");
    }

    /**
     * Creates a new {@link ObservableProperty} with the given initial value.
     *
     * @param before    the value before this expression
     * @param thisValue the initial value
     * @param format    the format to apply to the previous and this value. Will be passed to
     *                  {@link String#format(String, Object...)}, has two {@code "%s"} parameters.
     */
    public StringExpression(@Nullable ObservableProperty<?> before, @Nullable ObservableProperty<?> thisValue,
                            @Nonnull String format) {
        this(before, thisValue, format, "");
    }

    /**
     * Creates a new {@link ObservableProperty} with the given initial value.
     *
     * @param before       the value before this expression
     * @param thisValue    the initial value
     * @param format       the format to apply to the previous and this value. Will be passed to
     *                     {@link String#format(String, Object...)}, has two {@code "%s"} parameters.
     * @param defaultValue the default value to use when this value is null. Does not affect the previous value at all.
     */
    public StringExpression(@Nullable ObservableProperty<?> before, @Nullable ObservableProperty<?> thisValue,
                            @Nonnull String format, @Nonnull String defaultValue) {
        super("");
        this.format = format;
        this.defaultValue = defaultValue;
        this.before = before;
        this.thisValue = thisValue;

        if (before != null) {
            before.addListener(this::recompute);
        }
        if (thisValue != null) {
            thisValue.addListener(this::recompute);
        }

        recompute();
    }

    private void recompute() {
        setValue(String.format(
                format,
                getSafeString(before, ""),
                getSafeString(thisValue, defaultValue)
        ));
    }

    private String getSafeString(ObservableProperty<?> property, String defaultValue) {
        if (property == null || property.getValue() == null) {
            return defaultValue;
        }
        return property.getValue().toString();
    }

    /**
     * Returns a combined binding, with the next {@link ObservableProperty} appended.
     *
     * @param next The next {@link ObservableProperty} to append
     * @return the newly created {@link StringExpression}
     */
    public StringExpression concat(ObservableProperty<?> next) {
        return new StringExpression(this, next);
    }

    /**
     * Returns a combined binding, with the next {@link ObservableProperty} appended.
     *
     * @param next   The next {@link ObservableProperty} to append
     * @param format the format to apply to the previous and this value
     * @return the newly created {@link StringExpression}
     */
    public StringExpression concat(ObservableProperty<?> next, String format) {
        return new StringExpression(this, next, format);
    }

    /**
     * Returns a combined binding, with the next {@link ObservableProperty} appended.
     *
     * @param next         The next {@link ObservableProperty} to append
     * @param format       the format to apply to the previous and this value
     * @param defaultValue the default value to use when this value is null. Does not affect the previous value at all.
     * @return the newly created {@link StringExpression}
     */
    public StringExpression concat(ObservableProperty<?> next, String format, String defaultValue) {
        return new StringExpression(this, next, format, defaultValue);
    }

    /**
     * Returns a combined binding, with the next {@link ObservableProperty} appended.
     *
     * @param next         The next {@link ObservableProperty} to append
     * @param defaultValue the default value to use when this value is null. Does not affect the previous value at all.
     * @return the newly created {@link StringExpression}
     */
    public StringExpression concatWithDefault(ObservableProperty<?> next, String defaultValue) {
        return new StringExpression(this, next, "%s%s", defaultValue);
    }

    /**
     * Returns a combined binding, with the next String appended.
     *
     * @param next The next String to append
     * @return the newly created {@link StringExpression}
     */
    public StringExpression concat(String next) {
        return concat(new ObservableProperty<>(next));
    }
}
