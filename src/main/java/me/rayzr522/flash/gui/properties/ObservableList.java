package me.rayzr522.flash.gui.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A List implementation that notifies its observers, when its contents change.
 *
 * @param <T> the type of the elements in this list
 */
public class ObservableList<T> extends ArrayList<T> {

  private List<ListChangeListener<T>> listeners = new ArrayList<>();

  public ObservableList(int initialCapacity) {
    super(initialCapacity);
  }

  public ObservableList() {
  }

  public ObservableList(Collection<? extends T> c) {
    super(c);
  }

  /**
   * Adds a listener to this list.
   *
   * @param listener the listener to add
   */
  public void addListener(ListChangeListener<T> listener) {
    listeners.add(listener);
  }

  @Override
  public void add(int index, T element) {
    super.add(index, element);
    notifyListeners();
  }

  @Override
  public boolean add(T t) {
    boolean add = super.add(t);
    if (add) {
      notifyListeners();
    }
    return add;
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    boolean b = super.addAll(c);
    if (b) {
      notifyListeners();
    }
    return b;
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    boolean b = super.addAll(index, c);
    if (b) {
      notifyListeners();
    }
    return b;
  }

  @Override
  public T set(int index, T element) {
    T set = super.set(index, element);
    notifyListeners();
    return set;
  }

  @Override
  public T remove(int index) {
    T remove = super.remove(index);
    if (remove != null) {
      notifyListeners();
    }
    return remove;
  }

  @Override
  public boolean remove(Object o) {
    boolean remove = super.remove(o);
    if (remove) {
      notifyListeners();
    }
    return remove;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    boolean b = super.removeAll(c);
    if (b) {
      notifyListeners();
    }
    return b;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    boolean b = super.retainAll(c);
    if (b) {
      notifyListeners();
    }
    return b;
  }

  @Override
  public void clear() {
    super.clear();
    notifyListeners();
  }

  private void notifyListeners() {
    listeners.forEach(listener -> listener.listChanged(this));
  }


  @FunctionalInterface
  public interface ListChangeListener<T> {

    /**
     * Called when the list has changed.
     *
     * @param list the list that changed.
     */
    void listChanged(ObservableList<T> list);
  }
}
