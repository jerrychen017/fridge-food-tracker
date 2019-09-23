public class Fridge {
  private ItemList content;


  public boolean addItems(ItemList list) {
    return true;
  }

  public boolean remove(Item it) {
    return true;
  }

  public ItemList getExpired() {
    return new ItemList();
  }
}