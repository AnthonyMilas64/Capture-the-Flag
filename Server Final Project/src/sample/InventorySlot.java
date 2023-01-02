package sample;
import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class InventorySlot {
    private ArrayList<Item> slot;
    private Item item;
    private ImageView img;
    private Label lbl;
    private ImageView imgBorder;
    private boolean selected;

    //initializes all variables
    public InventorySlot(){
        slot = new ArrayList<>();
        item = null;
        selected = false;

        img = new ImageView();
        img.setFitWidth(30);
        img.setFitHeight(30);

        lbl = new Label();
        lbl.setStyle(" -fx-font-size: 17.0; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-family: Candara;");
        lbl.setPrefSize(40,40);
        lbl.setText("");

        imgBorder = new ImageView();
        imgBorder.setFitWidth(45);
        imgBorder.setFitHeight(45);
        imgBorder.setImage(new Image("resources/invBorder.png"));
    }

    //returns whether this slot is selected
    public boolean getSelected(){
        return selected;
    }

    //sets this slot to selected or unselected
    public void setSelected(boolean selected){
        if(selected) {
            imgBorder.setImage(new Image("resources/invSelected.png"));
        } else {
            imgBorder.setImage(new Image("resources/invBorder.png"));
        }
        this.selected = selected;
    }

    //returns the imageView representing the border of the slot
    public ImageView getImgBorder() {
        return imgBorder;
    }

    //checks to see when an item can be added
    public void addItem(String id, int amount){
        if(amount>0) {
            if (slot.size() > 0) {
                if (slot.get(0).getId().equals(id)) {
                    addItem2(id, amount);
                }
            } else {
                addItem2(id, amount);
            }
        }
    }

    //is called by addItem, adds items to this slot
    private void addItem2(String id, int amount){
        for (int i = 0; i < amount; i++) {
            slot.add(new Item(id,"p1", false));
        }
        lbl.setText(Integer.toString(slot.size()));
        if(slot.size() == 0){
            lbl.setText("");
        }
        item = new Item(id,"p1",false);
        img.setImage(new Image(item.getImgPath()));
    }

    //removes item from the slot
    public void removeItems(int amount){
        for (int x = 0; x < amount; x++) {
            if(slot.size()>0){
                slot.remove(0);
                lbl.setText(Integer.toString(slot.size()));
            }
            if(slot.size() == 0){
                item = null;
                img.setImage(null);
                lbl.setText("");
            }
        }
    }

    //returns the Item in the slot
    public Item getItem() {
        return item;
    }

    //returns the image of the item in the slot
    public ImageView getImg() {
        return img;
    }

    //returns the label of the slot
    public Label getLbl() {
        return lbl;
    }

    //returns the list of the items being held
    public ArrayList<Item> getSlot() {
        return slot;
    }
}
