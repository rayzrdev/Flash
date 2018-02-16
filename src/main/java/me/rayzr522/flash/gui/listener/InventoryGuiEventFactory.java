package me.rayzr522.flash.gui.listener;

import me.rayzr522.flash.gui.Gui;
import me.rayzr522.flash.gui.PrimitiveRenderedElement;
import me.rayzr522.flash.gui.display.Node;
import me.rayzr522.flash.gui.events.ClickEvent;
import me.rayzr522.flash.struct.Pair;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Optional;
import java.util.function.Consumer;

public class InventoryGuiEventFactory {

    /**
     * Creates a Gui {@link ClickEvent} from an {@link InventoryClickEvent} and the associated {@link Gui}.
     *
     * @param event the event
     * @param gui   the gui
     * @return the click event or null, if it could not be determined <em>what</em> was clicked.
     */
    public static ClickEvent getClickEvent(InventoryClickEvent event, Gui gui) {
        int slot = event.getSlot();
        Pair<Integer, Integer> coordinates = slotToCoordinates(slot);

        Optional<Node> node = gui.findNode(coordinates.getFirst(), coordinates.getSecond());
        if (!node.isPresent()) {
            return null;
        }

        PrimitiveRenderedElement element = gui.findElement(coordinates.getFirst(), coordinates.getSecond());
        //noinspection OptionalIsPresent
        if (element == null) {
            return null;
        }

        return new ClickEvent(gui, node.get(), element, event.getClick(), getCancelWriteThrough(event));
    }

    private static Pair<Integer, Integer> slotToCoordinates(int slot) {
        return new Pair<>(slot % 9, slot / 9);
    }

    private static Consumer<Boolean> getCancelWriteThrough(Cancellable cancellable) {
        return cancellable::setCancelled;
    }
}
