package sample;


public class Layer {

    private Item[] items; //hold all items in the layer

    public Layer(){
        items = new Item[4]; //there are 4 layers
        initializeItems();
    }

    public void initializeItems(){ //initializes each item in items
        for (int x = 0; x < items.length ; x++) {
            items[x] = new Item("blank","null", false);
        }
    }

    public void loadImages(Character player){ //loads the images of each item in items
        for (Item item : items) {
            if(item.getId().length() >= 4 && item.getId().substring(0,4).equals("trap") && !player.isTrapsVisible() && item.getOwner().equals("p2")){
                item.hideImg();
            } else{
                item.setImg();
            }
        }
    }

    public void setStructure(String id, String owner){ //sets the structure based on the id. Owner is who placed the structure
        Item item = new Item(id, owner, false);
        items[item.getLayer()] = new Item(id, owner, false);
    }

    //sets the structure by passing through an object
    public void setStructure(Item structure){
        items[structure.getLayer()] = structure;
    }

    //removes a structure from the index "layer" in items
    public void removeStructure(int layer){
        items[layer] = new Item("blank","null", false);
    }

    //removes the structure based on id
    public void removeStructure(String id){
        if(!id.equals("blank")) {
            Item item = new Item(id, "null", false);
            items[item.getLayer()] = new Item("blank", "null", false);
        }
    }

    //clears all items in items
    public void clear(){
        for (int x = 0; x < items.length; x++) {
            items[x] = new Item("blank","null", false);
        }
    }

    //makes an explosion on this specific layer. Strength is the strength of the explosion
    public void explode(Character player, int strength){
        for (int x = items.length-1; x > 0; x--) {
            if(!items[x].getId().equals("blank")){
                if(items[x].getId().equals(player.getId())){
                    player.setHealth(player.getHealth()-strength-3);
                }
                if(items[x].getStrength() <= strength){
                    removeStructure(x);
                }
                break;
            }
        }
    }

    //returns if the player can move to this layer
    public boolean canMove(){
        return (items[3].getId().equals("blank") && items[1].getId().length() >= 5 && items[1].getId().substring(0,5).equals("floor")) || (items[0].getId().equals("void") && items[1].getId().substring(0,5).equals("blank")) || items[1].getId().equals("lava");
    }

    //returns if the item id can be placed in this layer
    public boolean canPlace(String id){
        Item item = new Item(id, "null", false);
        if(item.getLayer() == 0){
            return true;
        } else if(item.getLayer() == 1){
            if(items[0].getId().equals("void") && items[1].getId().equals("blank")){
                return true;
            }
        } else if(item.getLayer() == 2){
            if(items[1].getId().length() >= 5 && items[1].getId().substring(0,5).equals("floor") && items[2].getId().equals("blank")){
                return true;
            }
        } else {
            if(items[1].getId().length() >= 5 && items[1].getId().substring(0,5).equals("floor") && items[2].getId().equals("blank") && items[3].getId().equals("blank")){
                return true;
            }
        }

        return id.equals("floor1") || id.equals("lava") || id.equals("border") || (id.length() >= 9 && id.substring(0,8).equals("sapphire")) || (id.length() >= 5 && id.substring(0,4).equals("ruby"));
    }

    //checks to see if an event happened in this layer
    public String checkEvent(Character player){
        if (items[2].getId().length()>=4 && items[2].getId().substring(0, 4).equals("shop")) {
            return "shop";
        } else if (items[2].getId().length()>=5 && items[2].getId().substring(0, 5).equals("chest")) {
            return "onChest";
        } else if (items[2].getId().length()>=player.getGemTarget().length() && items[2].getId().substring(0, player.getGemTarget().length()).equals(player.getGemTarget())) {
            return "onGem";
        } else if (items[2].getId().length()>=8 && items[2].getId().substring(0,7).equals(player.getGemTarget().substring(0,1)+"Holder") && player.isHolding(player.getGemTarget())) {
            return "onHolder";
        } else if ((items[0].getId().equals("void") && items[1].getId().substring(0,5).equals("blank")) || (items[1].getId().equals("lava") && !player.isHolding("lavaBoots"))) {
            return "death";
        } else if (items[2].getId().equals("trap1") && !(items[2].getOwner().equals(player.getId()))) {
            return "healthTrap";
        } else if (items[2].getId().equals("trap2") && !(items[2].getOwner().equals(player.getId()))) {
            return "invTrap";
        }
        return "null";
    }

    //returns items
    public Item[] getItems() {
        return items;
    }

    //returns the top structure in the layer
    public Item getTopStructure(){
        for (int x = items.length-1; x >= 0; x--) {
            if(!items[x].getId().equals("blank")){
                return items[x];
            }
        }
        return null;
    }

}
