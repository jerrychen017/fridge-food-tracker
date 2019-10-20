import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemList {
  private List<Item> items;
  private ItemHistory history;

  public ItemList(int size) {
    this.items = new ArrayList<Item>(size);
    this.history = new ItemHistory();
  }

  public ItemList() {
    this.items = new ArrayList<Item>();
    this.history = new ItemHistory();
  }

  public void addItem(Item it) {
    this.items.add(it);
  }

  /*
  removes the item with given id.
   */
  public void removeItem(int id) {
    for (Item i : items) {
      if (i.getId() == id) {
        items.remove(i);
        break;
      }
    }
  }


  public Iterator createIterator() {
    return items.iterator();
  }

}