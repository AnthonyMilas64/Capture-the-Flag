package sample;

import javafx.scene.control.Button;

//This class represents the player. There will be 2 instances of it, one for the user, and another for the opponent
public class Character extends Item{
    private int[] coords; //the coordinates of the player in the room. It's an array because arrays are pass by reference, so if I need to change the coordinates from another class, it's easier.
    private InventorySlot[] inventory;
    private int totalHealth = 6; //represents the total health the player can have, has to be > 0 and even.
    private int health = 6; //represents the current health of the player, has to be > 0 and <= totalHealth.
    private String gemTarget;
    private boolean trapsVisible = true;
    private Button btn;

    //initializes all the necessary variables.
    //type is meant to differentiate between the player and opponents. 1 is player, 2 is opponent
    public Character(int type){
        super("p"+type,"null",true);
        coords = new int[]{6,6};
        inventory = new InventorySlot[4];
        initializeInventory();
        inventory[0].setSelected(true);
        btn = new Button();
        btn.setMinSize(5,5);
        btn.setPrefSize(7, 7);
    }

    //sets trapVisible
    public void setTrapsVisible(boolean trapsVisible) {
        this.trapsVisible = trapsVisible;
    }

    //gets value for trap visible
    public boolean isTrapsVisible() {
        return trapsVisible;
    }

    //gets the button for the character
    @Override
    public Button getBtn() {
        if(gemTarget.equals("sapphire")){
            btn.setStyle("-fx-background-radius: 0; -fx-background-color: #e60045;");
        } else if(gemTarget.equals("ruby")){
            btn.setStyle("-fx-background-radius: 0; -fx-background-color: #1b659e;");
        }
        return btn;
    }

    //sets target gem
    public void setGemTarget(String gemTarget) {
        this.gemTarget = gemTarget;
    }

    //returns target gem
    public String getGemTarget() {
        return gemTarget;
    }

    //returns amount of an item the player is holding
    public int getAmount(String type){
        if(isHolding(type)){
            for (InventorySlot inventorySlot : inventory) {
                if (inventorySlot.getItem() != null && inventorySlot.getItem().getId().equals(type)) {
                    System.out.println(inventorySlot.getSlot().size());
                    return inventorySlot.getSlot().size();
                }
            }
        }
        return 0;
    }

    //returns whether a player is holding an item or not
    public boolean isHolding(String id){
        for (InventorySlot slot: inventory) {
            if(slot.getItem() != null && slot.getItem().getId().equals(id)){
                return true;
            }
        }
        return false;
    }

    //adds an item to the player's inventory
    public void addItem(String id, int amount){
        int slotIndex = -1;
        for (int x = 0; x < inventory.length; x++) { //check for an item that matches what you have so it can add it
            if(inventory[x].getItem() != null && id.equals(inventory[x].getItem().getId())){
                slotIndex = x;
                break;
            }
        }
        if(slotIndex == -1){ //checks for an open space to add the item
            for (int x = 0; x < inventory.length; x++) {
                if (inventory[x].getItem() == null){
                    slotIndex = x;
                    break;
                }
            }
        }
        if(slotIndex != -1){ //the there's no space, the inventory is upgraded
            inventory[slotIndex].addItem(id,amount);
        } else{
            upgradeInventory();
            inventory[inventory.length-1].addItem(id,amount);
        }
    }

    //checks to see if an item can be added to the player's inventory
    public boolean canAddItem(String id){
        for (InventorySlot inventorySlot : inventory) { //check for an item that matches what you have so it can add it
            if (inventorySlot.getItem() != null && id.equals(inventorySlot.getItem().getId())) {
                return true;
            }
        }
        //checks for an open space to add the item
        for (InventorySlot inventorySlot : inventory) {
            if (inventorySlot.getItem() == null) {
                return true;
            }
        }
        return false;
    }

    //removes items from the player's inventory
    public void removeItem(String id, int amount){
        int slotIndex = -1;
        for (int x = 0; x < inventory.length; x++) { //check for an item that matches what you have so it can remove it
            if(inventory[x].getItem() != null && id.equals(inventory[x].getItem().getId())){
                slotIndex = x;
                break;
            }
        }
        if(slotIndex != -1){
            inventory[slotIndex].removeItems(amount);
        }
    }

    //gets the index of the item the player has selected
    public int getItemSelected(){
        for (int x = 0; x < inventory.length; x++) {
            if(inventory[x].getSelected()){
                return x;
            }
        }
        return -1;
    }

    //sets the index of the item the player has selected
    public void setItemSelected(int slotSelected) {
        if(getItemSelected() != -1){
            inventory[getItemSelected()].setSelected(false);
        }
        inventory[slotSelected].setSelected(true);
    }

    //the following methods return a global variable
    public int[] getCoords() {
        return coords;
    }

    public InventorySlot[] getInventory() {
        return inventory;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        if(this.health>totalHealth){
            this.health = totalHealth;
        }
    }

    public int getTotalHealth() {
        return totalHealth;
    }

    public void setTotalHealth(int totalHealth) {
        this.totalHealth = totalHealth;
    }

    //initializes the inventory
    public void initializeInventory(){
        for (int x = 0; x < inventory.length; x++) {
            inventory[x] = new InventorySlot();
        }
        setItemSelected(0);
    }

    //clears the inventory
    public void clearInventory(){
        inventory = new InventorySlot[inventory.length];
        initializeInventory();
    }

    //upgrades the inventory
    public void upgradeInventory(){
        InventorySlot[] inventory2 = new InventorySlot[inventory.length+1];
        System.arraycopy(inventory, 0, inventory2, 0, inventory.length);
        inventory = new InventorySlot[inventory2.length];
        System.arraycopy(inventory2, 0, inventory, 0, inventory.length);
        inventory[inventory.length-1] = new InventorySlot();
    }

}
