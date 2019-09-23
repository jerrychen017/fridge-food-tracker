import java.util.Iterator;

public class ItemListIterator implements Iterator {
  private List<Items> items;
  private int position = 0;

  @Override
  public Object next() {
    Item it = items.get(position);
    position++;
    return it;
  }

  @Override
  public boolean hasNext() {
    return !(position >= items.length || items.get[position] == null);
  }

  @Override
  public void remove() {

  }
}
