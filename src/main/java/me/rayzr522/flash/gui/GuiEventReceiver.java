package me.rayzr522.flash.gui;

import me.rayzr522.flash.gui.events.ClickEvent;

/**
 * Receives events related to the GUI.
 */
public interface GuiEventReceiver {

    /**
     * Called when the component is clicked.
     *
     * @param clickEvent the clickEvent that describes what happened
     */
    void onClick(ClickEvent clickEvent);
}
