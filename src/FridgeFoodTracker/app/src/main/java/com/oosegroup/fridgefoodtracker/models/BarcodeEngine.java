import android.media.Image;

import java.util.ArrayList;
import java.util.List;

/*
BarcodeEngine class is an interface for barcode recognition
 */
public class BarcodeEngine {
  List<Item> itemList;
  public BarcodeEngine() {
    itemList = new ArrayList<>();
  }

  public Item scan(Image img) {
    Camera cam;
//    cam.takePicture();
    return null;
  }
}