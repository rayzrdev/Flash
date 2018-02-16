package me.rayzr522.flash.gui;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.inventory.ItemStack;

/**
 * An element that is in a slot in the GUI.
 */
public class PrimitiveRenderedElement {

  private String drawIntention;
  private ItemStack item;
  private GuiEventReceiver eventReceiver;

  /**
   * Creates a new {@link PrimitiveRenderedElement}.
   *
   * @param drawIntention the intention you had with drawing this. Can be used by the {@link
   * RenderTarget} to change the layout
   * @param item the item to display, if possible
   * @param eventReceiver the receiver of the event
   */
  public PrimitiveRenderedElement(@Nullable String drawIntention, @Nonnull ItemStack item,
      @Nonnull GuiEventReceiver eventReceiver) {
    this.drawIntention = drawIntention;
    this.item = item;
    this.eventReceiver = eventReceiver;
  }

  /**
   * @return the intention you had with drawing this. Can be used by the {@link RenderTarget} to
   * change the layout
   */
  @Nullable
  public String getDrawIntention() {
    return drawIntention;
  }

  @Nonnull
  public ItemStack getItem() {
    return item.clone();
  }

  @Nonnull
  public GuiEventReceiver getEventReceiver() {
    return eventReceiver;
  }
}
