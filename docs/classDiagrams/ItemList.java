import java.util.ArrayList;
import java.util.Iterator;

public class ItemList {
  private List<Item> items;
  private ItemHistory history;

  public ItemList(int size) {
    items = new ArrayList<Item>(size)
  }

  public Iterator createIterator() {
    return items.iterator();
  }
}