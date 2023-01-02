package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;

public class Upgrades {

    private static String[] ids; //the ids of the upgrades
    private static String[] names; //the names of the upgrades
    private static ImageView[] imageViews = new ImageView[8]; //the imageViews of the upgrades
    private static String[] imgPaths; //the imagePaths of the upgrades
    private static int[] prices; //the prices of the upgrades
    private static int[] counters; //the number of times the upgrades has been purchased
    private static boolean[] canUpgrades; //if the upgrade can be purchased or not
    private static String[] definitions; //the definitions of the upgrades

    //initializes all the variables
    public static void setUpgrades(){
        bombStrength = 1;
        bombRadius = 1;
        goldTime = 3;
        goldProduce = 1;
        emeraldTime = 7;
        emeraldProduce = 1;
        grenadeStrength = 1;
        grenadeRadius = 1;
        healthBoost = 2;
        imagesInitialized = false;
        ids = new String[]{"upHealth","upInv","upBomb","upTrap","upGoldGen", "upEmeraldGen", "upGrenade", "upHealingFlask"};
        names = new String[]{"Upgrade Health","Upgrade Inventory","Upgrade Bomb","Upgrade Traps","Upgrade Gold Gen", "Upgrade Emerald Gen","Upgrade Grenade", "Upgrade Healing Flask"};
        imageViews = new ImageView[8];
        imgPaths = new String[]{"resources/heart1.png", "resources/invSelected.png", "resources/itemImgs/bomb.png", "resources/itemImgs/trap1.png", "resources/goldGenImages/goldgenerator.png","resources/emeraldGenImages/emeraldgenerator.png", "resources/itemImgs/grenade.png","resources/itemImgs/healingFlask.png"};
        prices = new int[]{5,5,5,20,7,10,7,7};
        counters = new int[]{0,0,0,0,0,0,0,0};
        canUpgrades = new boolean[]{true,true,true,true,true,true,true,true};
        definitions = new String[]{"When purchased, this upgrade will increase your health by a full heart. The price will increase by 3 emeralds after purchasing.",
                "When purchased, this upgrade will increase the size of your inventory by 1 slot. The price will increase by 2 emeralds after purchasing.",
                "When purchased, this upgrade will increase the strength of the bomb, doing an extra half a heart of damage to any player in the blast radius.",
                "When purchased, this upgrade will make all of your traps invisible to the opponent.",
                "When purchased, this upgrade will decrease the time for gold to generate by 1 second. The price will increase by 2 emeralds after purchasing.",
                "When purchased, this upgrade will decrease the time for emeralds to generate by 1 second. The price will increase by 2 emeralds after purchasing.",
                "When purchased, this upgrade will increase the strength of the grenade, doing an extra half a heart of damage to any player in the blast radius.The price will increase by 1 emerald after purchasing.",
                "When purchased, this upgrade will increase the power of the healing flask by a half a heart.The price will increase by 1 emerald after purchasing."};
    }

    //id is the upgrade the player is purchasing, finds the correct upgrade to do based on id
    public static void upgrades(String id, Character player){
        if(id.equals("upHealth")){
            upgradeHealth(player);
        } else if(id.equals("upInv")){
            upgradeInventory(player);
        } else if(id.equals("upBomb")){
            upgradeBomb();
        } else if(id.equals("upTrap")){
            upgradeTrap(player);
        } else if(id.equals("upGoldGen")){
            upgradeGoldGen();
        } else if(id.equals("upEmeraldGen")){
            upgradeEmeraldGen();
        } else if(id.equals("upGrenade")){
            upgradeGrenade();
        } else if(id.equals("upHealingFlask")){
            upgradeHealingFlask();
        }

    }

    //upgrades the player's health. When purchased, this upgrade will increase your health by a full heart. The price will increase by 3 emeralds after purchasing.
    private static void upgradeHealth(Character player){
        player.setTotalHealth(player.getTotalHealth()+2);
        player.setHealth(player.getHealth()+2);
        prices[0] += 3;
        counters[0]++;
        if(counters[0]>=5){
            canUpgrades[0] = false;
            definitions[0] = "This upgrade has reached the maximum number of purchases.";
        }
    }

    //upgrades the player's inventory. When purchased, this upgrade will increase the size of your inventory by 1 slot. The price will increase by 2 emeralds after purchasing.
    private static void upgradeInventory(Character player){
        player.upgradeInventory();
        prices[1] += 2;
        counters[1]++;
        if(counters[1]>=5){
            canUpgrades[1] = false;
            definitions[1] = "This upgrade has reached the maximum number of purchases.";
        }
    }

    //upgrades the bomb
    private static int bombStrength = 1; //strength of bomb
    private static int bombRadius = 1; //blast radius of bomb
    public static int getBombStrength() {
        return bombStrength;
    }
    public static int getBombRadius() {
        return bombRadius;
    }
    private static void upgradeBomb(){
        if(counters[2] == 0 || counters[2] >= 2){ //When purchased, this upgrade will increase the blast radius of the bomb from a 3 by 3 area to a 5 by 5 area.
            bombStrength++;
            if(counters[2]<1) {
                definitions[2] = "When purchased, this upgrade will increase the blast radius of the bomb from a 3 by 3 area to a 5 by 5 area.";
            }
        } else if(counters[2] == 1){ //When purchased, this upgrade will increase the strength of the bomb, doing an extra half a heart of damage to any player in the blast radius.
            bombRadius++;
            definitions[2] = "When purchased, this upgrade will increase the strength of the bomb, doing an extra half a heart of damage to any player in the blast radius.";
        }
        counters[2]++;
        if(counters[2]>=8){
            canUpgrades[2] = false;
            definitions[2] = "This upgrade has reached the maximum number of purchases.";
        }
    }

    //When purchased, this upgrade will make all of your traps invisible to the opponent.
    private static void upgradeTrap(Character player){
        player.setTrapsVisible(false);
        prices[3] += 2;
        counters[3]++;
        if(counters[3]>=1){
            canUpgrades[3] = false;
            definitions[3] = "This upgrade has reached the maximum number of purchases.";
        }
    }

    //upgrades gold generator.
    private static int goldTime = 3; //amount of time it takes for gold to generate
    private static int goldProduce = 1; //amount of gold produced per time
    public static int getGoldTime() {
        return goldTime;
    }
    public static int getGoldProduce() {
        return goldProduce;
    }
    private static void upgradeGoldGen(){
        if(counters[4] == 0 || counters[4] == 2){ //When purchased, this upgrade will increase the number of gold generated by 1. The price will increase by 2 emeralds after purchasing.
            goldTime--;
            definitions[4] = "When purchased, this upgrade will increase the number of gold generated by 1. The price will increase by 2 emeralds after purchasing.";
        } else if(counters[4] == 1 || counters[4] == 3){ //When purchased, this upgrade will decrease the time for gold to generate by 1 second. The price will increase by 2 emeralds after purchasing.
            goldProduce++;
            definitions[4] = "When purchased, this upgrade will decrease the time for gold to generate by 1 second. The price will increase by 2 emeralds after purchasing.";
        }
        prices[4] += 2;
        counters[4]++;
        if(counters[4]>=4){
            canUpgrades[4] = false;
            definitions[4] = "This upgrade has reached the maximum number of purchases.";
        }
    }


    private static int emeraldTime = 7; //amount of time it takes for emeralds to generate
    private static int emeraldProduce = 1; //amount of emeralds produced per time
    //upgrades emerald generator
    public static int getEmeraldTime() {
        return emeraldTime;
    }
    public static int getEmeraldProduce() {
        return emeraldProduce;
    }
    private static void upgradeEmeraldGen(){
        if(counters[5] == 0 || counters[5] == 2){//When purchased, this upgrade will increase the number of emeralds generated by 1. The price will increase by 2 emeralds after purchasing.
            emeraldTime--;
            definitions[5] = "When purchased, this upgrade will increase the number of emeralds generated by 1. The price will increase by 2 emeralds after purchasing.";
        } else if(counters[5] == 1 || counters[5] == 3){//When purchased, this upgrade will increase the number of emeralds generated by 1. The price will increase by 2 emeralds after purchasing.
            emeraldProduce++;
            definitions[5] = "When purchased, this upgrade will increase the number of emeralds generated by 1. The price will increase by 2 emeralds after purchasing.";
        }
        prices[5] += 2;
        counters[5]++;
        if(counters[5]>=4){
            canUpgrades[5] = false;
            definitions[5] = "This upgrade has reached the maximum number of purchases.";
        }
    }


    private static int grenadeStrength = 1; //strength of grenade
    private static int grenadeRadius = 1; //blast radius of grenade
    public static int getGrenadeStrength() {
        return grenadeStrength;
    }
    public static int getGrenadeRadius() {
        return grenadeRadius;
    }
    //upgrades the grenade. When purchased, this upgrade will increase the strength of the grenade, doing an extra half a heart of damage to any player in the blast radius.The price will increase by 1 emerald after purchasing.
    private static void upgradeGrenade(){
        grenadeStrength++;
        counters[6]++;
        prices[6]++;
        if(counters[6]>=6){
            canUpgrades[6] = false;
            definitions[6] = "This upgrade has reached the maximum number of purchases.";
        }
    }

    //
    private static int healthBoost = 2; //the amount of health healed each time healing flask is used
    public static int getHealthBoost() {
        return healthBoost;
    }
    //When purchased, this upgrade will increase the power of the healing flask by a half a heart.The price will increase by 1 emerald after purchasing.
    private static void upgradeHealingFlask(){
        healthBoost++;
        counters[7]++;
        prices[7]++;
        if(counters[7]>=5){
            canUpgrades[7] = false;
            definitions[7] = "This upgrade has reached the maximum number of purchases.";
        }
    }


    //returns name based on id
    public static String getName(String id){
        for (int x = 0; x < ids.length; x++) {
            if(ids[x].equals(id)){
                return names[x];
            }
        }
        return null;
    }

    //returns imageView based on id
    private static boolean imagesInitialized = false; //if the imageViews have been initialized or not
    public static ImageView getImageView(String id){
        if(!imagesInitialized){
            for (int x = 0; x < imageViews.length; x++) {
                ImageView img = new ImageView();
                img.setFitWidth(35);
                img.setFitHeight(35);
                img.setImage(new Image(imgPaths[x]));
                imageViews[x] = img;
                imagesInitialized = true;
            }
        }
        for (int x = 0; x < ids.length; x++) {
            if(ids[x].equals(id)){
                return imageViews[x];
            }
        }
        return new ImageView();
    }

    //returns imgPath based on id
    public static String getImgPath(String id){
        for (int x = 0; x < ids.length; x++) {
            if(ids[x].equals(id)){
                return imgPaths[x];
            }
        }
        return "null";
    }

    //returns price based on id
    public static int getPrice(String id){
        for (int x = 0; x < ids.length; x++) {
            if(ids[x].equals(id)){
                return prices[x];
            }
        }
        return -1;
    }

    //returns counter based on id
    public static int getCounter(String id){
        for (int x = 0; x < ids.length; x++) {
            if(ids[x].equals(id)){
                return counters[x];
            }
        }
        return -1;
    }

    //returns canUpgrade based on id
    public static boolean getCanUpgrade(String id){
        for (int x = 0; x < ids.length; x++) {
            if(ids[x].equals(id)){
                return canUpgrades[x];
            }
        }
        return false;
    }

    //returns definition based on id
    public static String getDefinition(String id){
        for (int x = 0; x < ids.length; x++) {
            if(ids[x].equals(id)){
                return definitions[x];
            }
        }
        return "no definition :(";
    }






}

