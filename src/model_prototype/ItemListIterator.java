import java.util.Iterator;
import java.util.List;

public class ItemListIterator implements Iterator {
  private List<Item> items;
  private int position = 0;

  @Override
  public Object next() {
    Item it = items.get(position);
    position++;
    return it;
  }

  @Override
  public boolean hasNext() {
    return !(position >= this.items.size() || this.items.get(position) == null);
  }

  @Override
  public void remove() {

  }
}
