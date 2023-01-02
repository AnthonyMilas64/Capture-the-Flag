package sample;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Item {

    private String id; //the id of the item
    private String name; //the name of the item
    private String owner; //the owner of the item
    private int price; //the price to buy the item
    private String definition; //the definition of what the item is
    private ImageView img; //the image of the item
    private String imgPath; //the imagePath of the item
    private Button btn; //the button representing the item
    private String color; //the color of the button

    private boolean isStructure; //is the item is a structure or not
    private int strength; //the strength of the item
    private int layer; //the layer the item is placed in

    private int[][] gemRooms = new int[2][2]; //holds the coordinates of the rooms the gems are in
    private int[][] gemCoords = new int[2][2]; //holds the coordinates of the gems inside a room

    //id is the id of the new item, owner is who owns the item, loaded is if the imageView representing the item is visible
    public Item(String id, String owner, boolean loaded){
        this.id = id;
        name = id;
        this.owner = owner;
        price = 10000;
        img = new ImageView();
        img.setFitWidth(35);
        img.setFitHeight(35);
        img.setImage(null);
        isStructure = false;
        strength = 10;
        layer = -1;

        initializeProperties();
        if(loaded){
            setImg();
        }
        if(isStructure){
            btn = new Button();
            btn.setMinSize(5,5);
            btn.setPrefSize(7, 7);
            btn.setStyle("-fx-background-radius: 0; -fx-background-color: " + color + ";");
        }
    }

    public int[][] getGemRooms() {
        return gemRooms;
    }

    public int[][] getGemCoords() {
        return gemCoords;
    }

    //all the accessors below return the global variable called
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public int getPrice() {
        return price;
    }

    public Button getBtn() {
        return btn;
    }

    public String getDefinition() {
        return definition;
    }

    public boolean isStructure() {
        return isStructure;
    }

    public void setStructure(boolean structure) {
        isStructure = structure;
    }

    public int getLayer() {
        return layer;
    }

    public int getStrength() {
        return strength;
    }

    public void setImg() {
        if(!id.equals("blank") && img.getImage() == null) {
            img.setImage(new Image(imgPath));
        }
    }

    public void hideImg(){
        img.setImage(null);
    }

    public ImageView getImg() {
        return img;
    }

    public String getImgPath() {
        return imgPath;
    }

    //checks to see which item is being initialized, then initializes it
    private void initializeProperties() {
        if(id.equals("p1")){ //initializes the first player
            layer = 3;
            color = "#e60045";
            isStructure = true;
            imgPath = "resources/itemImgs/player1.png";
        } else if (id.equals("p2")) { //initializes the second player
            layer = 3;
            color = "#1b659e";
            isStructure = true;
            imgPath = "resources/itemImgs/player2.png";
        } else if (id.equals("void")) { //initializes the void
            layer = 0;
            color = "#1c003d";
            isStructure = true;
            imgPath = "resources/itemImgs/void.png";
        } else if (id.equals("lava")) { //initializes the lava
            layer = 1;
            color = "#cf6e29";
            isStructure = true;
            imgPath = "resources/itemImgs/lava.png";
        } else if (id.equals("border")) { //initializes the border
            layer = 3;
            color = "#4a4747";
            isStructure = true;
            imgPath = "resources/itemImgs/border.png";
        } else if (id.equals("floor1")) { //initializes the floor1
            layer = 1;
            color = "#c4bebe";
            isStructure = true;
            imgPath = "resources/itemImgs/floor1.png";
        } else if (id.equals("floor2")) { //initializes the floor2
            name = "Floor";
            price = 2;
            definition = "This item is a floor that can be placed on top of void. Other items can be placed on top of it. This floor is also weak and will break if any explosion hits it.";
            layer = 1;
            strength = 1;
            color = "#bfa677";
            isStructure = true;
            imgPath = "resources/itemImgs/floor2.png";
        } else if (id.equals("floor3")) { //initializes the floor3
            name = "Strong Floor";
            definition = "This item is a floor that can be placed on top of void. Other items can be placed on top of it. This floor is also Stronger than a normal floor and won't break if a weak explosion hits it.";
            price = 4;
            layer = 1;
            strength = 2;
            color = "#52422f";
            isStructure = true;
            imgPath = "resources/itemImgs/floor3.png";
        } else if (id.equals("platform1")) { //initializes the platform1
            name = "Platform";
            definition = "This item is allows you to quickly place down floors by clicking on an area with no floor. It places 3 x 3 square of floors surrounding the area you clicked";
            price = 5;
            imgPath = "resources/itemImgs/platform1.png";
        } else if (id.equals("platform2")) { //initializes the platform2
            name = "Strong Platform";
            definition = "This item is allows you to quickly place down strong floors by clicking on an area with no floor. It places 3 x 3 square of strong floors surrounding the area you clicked";
            price = 10;
            imgPath = "resources/itemImgs/platform2.png";
        } else if (id.equals("wall1")) { //initializes the wall1
            name = "Wall";
            definition = "This item is a wall that can be placed on top of a floor. The only way to get past the wall is to break it. This wall is weak and will break if any explosion hits it.";
            price = 3;
            layer = 3;
            strength = 1;
            color = "#402307";
            isStructure = true;
            imgPath = "resources/itemImgs/wall1.png";
        } else if (id.equals("wall2")) { //initializes the wall2
            name = "Strong Wall";
            definition = "This item is a wall that can be placed on top of a floor. The only way to get past the wall is to break it. This wall is also Stronger than a normal wall and won't break if a weak explosion hits it.";
            price = 5;
            layer = 3;
            strength = 2;
            color = "#6e2b0e";
            isStructure = true;
            imgPath = "resources/itemImgs/wall2.png";
        } else if (id.equals("bomb")) { //initializes the bomb
            name = "Bomb";
            definition = "This item can be placed on top of a floor. Right click the bomb to detonate it. The explosion is relatively weak at first, but can be upgraded to increase the blast radius and the strength of the explosion.";
            price = 5;
            layer = 2;
            strength = 1;
            color = "#0f0602";
            isStructure = true;
            imgPath = "resources/itemImgs/bomb.png";
        } else if (id.equals("trap1")) { //initializes the trap1
            price = 8;
            definition = "This when your opponent stands on this trap, their health will decrease by 2 hearts. Be careful not to confuse it with the inventory trap, it looks very similar.";
            layer = 2;
            strength = 1;
            color = "#9e6159";
            isStructure = true;
            name = "Health Trap";
            imgPath = "resources/itemImgs/trap1.png";
        } else if (id.equals("trap2")) { //initializes the trap2
            price = 20;
            definition = "This when your opponent stands on this trap, they will lose all the items they're holding. Be careful not to confuse it with the health trap, it looks very similar.";
            layer = 2;
            strength = 1;
            color = "#9e6159";
            isStructure = true;
            name = "Inventory Trap";
            imgPath = "resources/itemImgs/trap2.png";
        } else if (id.equals("grenade")) { //initializes the grenade
            name = "Grenade";
            definition = "A grenade is very similar to a bomb, however it detonates as soon as it placed. Like the bomb the strength of the explosion can be upgraded, but the blast radius will always stay the same.";
            price = 3;
            imgPath = "resources/itemImgs/grenade.png";
        } else if (id.equals("lavaBoots")) { //initializes the lavaBoots
            price = 20;
            definition = "Lava Boots are mysterious types of boots that allow you to walk on lava if they are in your inventory. ";
            name = "Lava Boots";
            imgPath = "resources/itemImgs/lavaboots.png";
        } else if (id.equals("map")) { //initializes the map
            price = 60;
            definition = "When selected in your inventory, the map shows a view of all the rooms, even hidden traps. But, when you are viewing your map, you can't move.";
            name = "Map";
            imgPath = "resources/itemImgs/map.png";
        } else if (id.equals("healingFlask")) { //initializes the healing flask
            name = "Healing Flask";
            definition = "Healing Flasks heal you upon drinking; to use, click on your character while holding the healing flask. This item can be upgraded to heal more health.";
            price = 7;
            imgPath = "resources/itemImgs/healingFlask.png";
        }else if (id.equals("gold")) { //initializes the gold
            name = "Gold";
            imgPath = "resources/itemImgs/gold.png";
        }else if (id.equals("emerald")) { //initializes the emerald
            name = "Emerald";
            imgPath = "resources/itemImgs/emerald.png";
        }else if (id.equals("ruby")) { //initializes the ruby
            name = "Ruby";
            imgPath = "resources/rubyImages/ruby.png";
        }else if (id.equals("sapphire")) { //initializes the sapphire
            name = "Sapphire";
            imgPath = "resources/sapphireImages/sapphire.png";
        }else if (id.equals("shop")) { //initializes the shop structure
            price = 30;
            definition = "This is a shop that can be taken anywhere. Although it's more expensive than most items, it very useful when you need to quickly make a purchase";
            isStructure = true;
            layer = 2;
            strength = 1;
            color = "#998712";
            name = "Shop";
            imgPath = "resources/imgShop/shop.png";
        }else if (id.equals("chest")) { //initializes the chest structure
            price = 65;
            definition = "This is a chest that can be taken anywhere. It may be the most expensive item, but you'll never have to worry about forgetting any items or running out of inventory space.";
            isStructure = true;
            layer = 2;
            strength = 1;
            color = "#70480b";
            name = "Chest";
            imgPath = "resources/chestImages/chest.png";
        } else if (id.length() >= 5 && id.substring(0,4).equals("shop")) {  //initializes the shop
            layer = 2;
            color = "#998712";
            isStructure = true;
            imgPath = "resources/imgShop/shop"+id.substring(4)+".png";
        } else if (id.length() >= 6 && id.substring(0,5).equals("chest")) {  //initializes the chest
            layer = 2;
            color = "#70480b";
            isStructure = true;
            imgPath = "resources/chestImages/chest"+id.substring(5)+".png";
        } else if (id.length() >= 5 && id.substring(0,4).equals("ruby")) { //initializes the ruby object
            gemRooms[0][0] = 0;
            gemRooms[0][1] = 4;
            gemRooms[1][0] = 2;
            gemRooms[1][1] = 4;
            gemCoords[0][0] = 1;
            gemCoords[0][1] = 9;
            gemCoords[1][0] = 5;
            gemCoords[1][1] = 5;
            layer = 2;
            color = "#e30022";
            isStructure = true;
            imgPath = "resources/rubyImages/ruby"+id.substring(4)+".png";
        } else if (id.length() >= 9 && id.substring(0,8).equals("sapphire")) { //initializes the sapphire object
            gemRooms[0][0] = 0;
            gemRooms[0][1] = 0;
            gemRooms[1][0] = 2;
            gemRooms[1][1] = 0;
            gemCoords[0][0] = 5;
            gemCoords[0][1] = 5;
            gemCoords[1][0] = 9;
            gemCoords[1][1] = 1;
            layer = 2;
            color = "#000fe3";
            isStructure = true;
            imgPath = "resources/sapphireImages/sapphire"+id.substring(8)+".png";
        } else if (id.length() >= 8 && id.substring(0,7).equals("goldGen")) { //initializes the gold generator
            layer = 2;
            color = "#f6fa00";
            isStructure = true;
            imgPath = "resources/goldGenImages/goldGen"+id.substring(7)+".png";
        } else if (id.length() >= 11 && id.substring(0,10).equals("emeraldGen")) { //initializes the emerald generator
            layer = 2;
            color = "#2de004";
            isStructure = true;
            imgPath = "resources/emeraldGenImages/emeraldGen"+id.substring(10)+".png";
        } else if (id.length() >= 8 && id.substring(0,7).equals("rHolder")) { //initializes the ruby holder
            layer = 2;
            color = "#506c7a";
            isStructure = true;
            imgPath = "resources/rHolderImages/rHolder"+id.substring(7)+".png";
        } else if (id.length() >= 8 && id.substring(0,7).equals("sHolder")) { //initializes the sapphire holder
            layer = 2;
            color = "#506c7a";
            isStructure = true;
            imgPath = "resources/sHolderImages/sHolder"+id.substring(7)+".png";
        }


    }
}

