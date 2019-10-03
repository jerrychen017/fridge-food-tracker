public class Fridge {
  private ItemList content;

  public Fridge() {
    content = new ItemList();
  }

  public boolean addItems(ItemList list) {
    return true;
  }

  public void addItem(Item item) {
    content.addItem(item);
  }

  public void remove(int id) {
    content.removeItem(id);
  }

  /*
  returns a list of expired items
   */
  public ItemList getExpired() {

    return new ItemList();
  }
}