package me.rayzr522.flash.gui.listener;

import me.rayzr522.flash.factory.LogFactory;
import me.rayzr522.flash.gui.Gui;
import me.rayzr522.flash.gui.events.ClickEvent;
import me.rayzr522.flash.gui.render.InventoryRenderTarget;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.logging.Logger;

public class InventoryGuiListener implements Listener {

    private static final Logger LOGGER = LogFactory.create(InventoryGuiListener.class);

    @EventHandler
    public void onClickEvent(InventoryClickEvent e) {
        // only listen to clicks in the gui
        if (e.getRawSlot() >= e.getView().getTopInventory().getSize()) {
            return;
        }

        InventoryHolder holder = e.getInventory().getHolder();

        if (!(holder instanceof InventoryRenderTarget)) {
            return;
        }

        InventoryRenderTarget renderTarget = (InventoryRenderTarget) holder;

        Gui gui = renderTarget.getGui();
        if (gui == null) {
            LOGGER.warning("Encountered an InventoryRenderTarget without a gui for " + e.getWhoClicked().getName());
            return;
        }

        ClickEvent clickEvent = InventoryGuiEventFactory.getClickEvent(e, gui);
        if (clickEvent != null) {
            gui.onClick(clickEvent);
        }
    }
}
