/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.awt.*;
import java.awt.event.KeyListener;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import socketfx.Constants;
import socketfx.FxSocketClient;
import socketfx.SocketListener;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author jtconnor
 */
public class ClientGUIController implements Initializable {

    @FXML
    public TextField  portTextField, sendTextField, hostTextField , txtShop;
    @FXML
    private Button connectButton, btnRdy, btnSendMsg;
    @FXML
    private GridPane gPaneBase, gPaneInvButton, gPaneInv, gPaneLblInv, gPaneInvBorder, gPaneShopItems,gPaneShopUpgrades, gPaneHealth,
            gPaneChest, gPaneChestLbl, gPaneChestBtn, gPaneChestBorder, gPaneBtnShopItems, gPaneBtnShopUpgrades, gPaneMap;
    @FXML
    private static AnchorPane aPane = new AnchorPane();
    @FXML
    private ImageView imgWholeScreen, imgInv, imgShopItem, imgMoving;
    @FXML
    private Pane paneGame,paneStart, paneShop, paneShop2, paneItemShop, paneChest;
    @FXML
    public Label  lblMessages, lblInv, lblShop2, lblPrice, lblMoving, lblDefinitions;
    @FXML
    private StackPane sPaneScreen;


    private final static Logger LOGGER =
            Logger.getLogger(MethodHandles.lookup().lookupClass().getName());



    public enum ConnectionDisplayState {

        DISCONNECTED, WAITING, CONNECTED, AUTOCONNECTED, AUTOWAITING
    }

    private FxSocketClient socket;
    private void connect() {
        socket = new FxSocketClient(new FxSocketListener(),
                hostTextField.getText(),
                Integer.parseInt(portTextField.getText()),
                Constants.instance().DEBUG_NONE);
        socket.connect();
    }

    private void displayState(ConnectionDisplayState state) {
//        switch (state) {
//            case DISCONNECTED:
//                connectButton.setDisable(false);
//                sendButton.setDisable(true);
//                sendTextField.setDisable(true);
//                break;
//            case WAITING:
//            case AUTOWAITING:
//                connectButton.setDisable(true);
//                sendButton.setDisable(true);
//                sendTextField.setDisable(true);
//                break;
//            case CONNECTED:
//                connectButton.setDisable(true);
//                sendButton.setDisable(false);
//                sendTextField.setDisable(false);
//                break;
//            case AUTOCONNECTED:
//                connectButton.setDisable(true);
//                sendButton.setDisable(false);
//                sendTextField.setDisable(false);
//                break;
//        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boolean isConnected = false;
        displayState(ConnectionDisplayState.DISCONNECTED);


        

        Runtime.getRuntime().addShutdownHook(new ShutDownThread());

        /*
         * Uncomment to have autoConnect enabled at startup
         */
//        autoConnectCheckBox.setSelected(true);
//        displayState(ConnectionDisplayState.WAITING);
//        connect();
    }

    class ShutDownThread extends Thread {

        @Override
        public void run() {
            if (socket != null) {
                if (socket.debugFlagIsSet(Constants.instance().DEBUG_STATUS)) {
                    LOGGER.info("ShutdownHook: Shutting down Server Socket");    
                }
                socket.shutdown();
            }
        }
    }

    class FxSocketListener implements SocketListener {

        //receives a message from the opponent, checks if information has been sent responds accordingly if it has.
        @Override
        public void onMessage(String line) {
            if(line.substring(0,3).equals("msg")){ //message has been sent
                lblMessages.setText(line.substring(3));
            }
            else if(line.substring(0,5).equals("start")){ //message has been sent
                startGame();
            } else if(line.length() >= 4 && line.substring(0,4).equals("/upd")){ //an update to the screen has been made by the opponent
                String itemID = line.substring(line.indexOf(':') + 1, line.indexOf('X'));
                int xRoom = Integer.parseInt(line.substring(line.indexOf('x') + 1, (line.length()-2)));
                int yRoom = Integer.parseInt(line.substring(line.length()-1));
                int x = Integer.parseInt(line.substring(line.indexOf('X') + 1, line.indexOf('Y')));
                int y = Integer.parseInt(line.substring(line.indexOf('Y') + 1, line.indexOf('x')));
                if(line.length() >= 10 && line.substring(0,10).equals("/updRemove")){ //a structure was removed by the opponent
                    rooms[xRoom][yRoom].getFloor()[x][y].removeStructure(itemID);
                    updateMapRoom(xRoom,yRoom);
                    updateScreen();
                } else if(line.length() >= 8 && line.substring(0,8).equals("/updBoom")){ //the opponent created an explosion
                    rooms[xRoom][yRoom].getFloor()[x][y].explode(player, Integer.parseInt(itemID));
                    updateMapRoom(xRoom,yRoom);
                    updateScreen();
                } else if(line.length() >= 6 && line.substring(0,6).equals("/updP2")){ //the opponent moved to a new location
                    rooms[xRoom][yRoom].getFloor()[x][y].setStructure(player2);
                    updateMapRoom(xRoom,yRoom);
                    updateScreen();
                } else if(line.length() >= 9 && line.substring(0,9).equals("/updPlace")){ //a structure was placed by the opponent
                    rooms[xRoom][yRoom].getFloor()[x][y].setStructure(itemID,player2.getId());
                    updateMapRoom(xRoom,yRoom);
                    inRoom(xRoom,yRoom,x,y,itemID);
                }
            } else if(line.length() >= 5 && line.substring(0,5).equals("/trap")){ //the opponent upgraded their traps to be in visisble to the player
                player2.setTrapsVisible(false);
                hideTraps();
            } else if(line.equals("/captured")){ //the opponent captured on of the player's gems
                numGemsLost++;
            } else if(line.equals("/reset")){ //the game needs to reset
                reset();
            } else if(line.equals("/gameLost")){ //the opponent has lost the game
                death();
                gameLost();
            }
        }

        //searches through each block in each room to see if it's a trap that the opponent placed down. If it is, then it makes it invisible.
        private void hideTraps(){
            for (Room[] value : rooms) {
                for (int yRoom = 0; yRoom < rooms[0].length; yRoom++) {
                    for (int x = 0; x < value[yRoom].getFloor().length; x++) {
                        for (int y = 0; y < value[yRoom].getFloor()[0].length; y++) {
                            String id = value[yRoom].getFloor()[x][y].getItems()[2].getId();
                            if (id.length() > 4 && id.substring(0, 4).equals("trap") && value[yRoom].getFloor()[x][y].getItems()[2].getOwner().equals("p2")) {
                                value[yRoom].getFloor()[x][y].getItems()[2].hideImg();
                            }
                        }
                    }
                }
            }
        }

        //this checks to see if the update the opponent made was in the same room as the player, if it was it loads the structures that were placed.
        //xR and yR are used to see if the room is the same. x and y are the coordinates in thr room of the structure that was placed. id is the id of the structure that was placed.
        private void inRoom(int xR, int yR, int x, int y, String id){
            if(xRoom == xR && yRoom == yR){
                System.out.println("in room...");
                Item item = new Item(id, "null",false);
                if(!(id.length() > 4 && id.substring(0,4).equals("trap") && !player2.isTrapsVisible())){ //if the structure placed was a trap and the opponent's traps are supposed to be invisible, it doesn't load the img.
                    room.getFloor()[x][y].getItems()[item.getLayer()].setImg();
                }
            }
            updateScreen();
        }

        @Override
        public void onClosedStatus(boolean isClosed) {

        }
    }

    //sends a message when clicked
    @FXML
    private void handleBtnSend() {
        if(btnSendMsg.getText().equals("Type Message")){
            sendTextField.setVisible(true);
            btnSendMsg.setText("Send Message");
            lblMessages.setLayoutY(557);
        } else {
            if (!sendTextField.getText().equals("")) {
                sendTextField.setVisible(false);
                btnSendMsg.setText("Type Message");
                lblMessages.setLayoutY(532);
                String str = sendTextField.getText();
                if (str.length() >= 4 && str.substring(0, 4).equals("/add")) { //the player is using a cheat code and wants to add an item
                    String info = str.substring(5);
                    player.addItem(info.substring(0, info.indexOf(' ')), Integer.parseInt(info.substring(info.indexOf(' ') + 1)));
                    updateInventory();
                } else if (str.length() >= 9 && str.substring(0, 9).equals("/creative")){ //the player is using a cheat code. It add all the items to the player's chest, upgrades the player's inventory, and gives the player gold and emeralds
                    loadShop();
                    for (int x = 0; x < shopItems.length; x++) {
                        for (int y = 0; y < shopItems[0].length; y++) {
                            loadChest();
                            if(shopItems[x][y] != null) {
                                chestItems[x][y].addItem(shopItems[x][y].getId(), 100);
                            }
                        }
                        player.upgradeInventory();
                    }
                    player.addItem("gold", 5000);
                    player.addItem("emerald", 5000);
                    player.addItem("chest", 1);
                    updateInventory();
                } else {
                    socket.sendMessage("msg" + str);
                    System.out.println("sent message client");
                }
                sendTextField.setText("");
            }
        }
    }

    public void sendByEnter(KeyEvent pressed){
        if(pressed.getCode().equals(KeyCode.ENTER)){
            handleBtnSend();
        }
    }

    //connects when clicked
    @FXML
    private void handleConnectButton(ActionEvent event) {
        connectButton.setVisible(false);
        btnRdy.setVisible(true);
        displayState(ConnectionDisplayState.WAITING);
        connect();
    }

    //the game has been lost
    private void gameLost(){
        paneGame.setVisible(false);
    }

    //means the player is ready to start
    @FXML
    private void handleBtnRdy(){
        btnRdy.setDisable(true);
        socket.sendMessage("rdy.......");
    }

    private String gemTarget = "ruby"; //the type of gem the opponent needs to capture
    private String opponentTarget = "sapphire"; //the type of gem the player needs to capture
    private int xStartR = 1; //the x coordinate of the room the player starts in
    private int yStartR = 0; //the y coordinate of the room the player starts in


    private int xRoom = xStartR; //the x location of the room the player is in
    private int yRoom = yStartR; //the y location of the room the player is in
    private boolean start = false; //the boolean representing whether the game has started or not
    private Room[][] rooms; //the array of rooms
    private Character player; //the player
    private Character player2; //the opponent
    private Room room; //the room the player is in

    //starts the game by calling all the necessary methods that initialize important components of the game.
    private void startGame(){
        player = new Character(1);
        player2 = new Character(2);
        player.setGemTarget(gemTarget);
        player2.setGemTarget(opponentTarget);
        initializeRooms();
        loadScreen();
        room.getFloor()[player.getCoords()[0]][player.getCoords()[1]].setStructure(player);
        updateOpponent("/updP2X"+player.getCoords()[0]+"Y"+player.getCoords()[1]+"x"+xRoom+"y"+yRoom);
        updateScreen();
        if(!start) {
            animationTimer();
        }
        start = true;
        System.out.println("Game is Starting");
    }

    //sets the screen to what it should look like when the game starts
    private void loadScreen(){
        paneStart.setVisible(false);
        paneGame.setVisible(true);
        Main.setHeight(750);
        Main.setWidth(1000);
        paneGame.setPrefHeight(Main.getHeight());
        paneGame.setPrefWidth(Main.getWidth());
        imgWholeScreen.setFitWidth(paneGame.getPrefWidth());
        imgWholeScreen.setFitHeight(paneGame.getPrefHeight());
        updateInventory();
        lblInv.setText("Inventory");
    }


    //initializes all the rooms in rooms
    private Button[][] btnBase = new Button[13][13]; //this is the base layer of buttons that goes behind the screen so when the player clicks the screen, these buttons are going to be clicked.
    private void initializeRooms(){
        rooms = new Room[3][5];
        gPaneBase.getChildren().clear();
        for (int x = 0; x < btnBase.length; x++) { //initializes btnBase with Buttons
            for (int y = 0; y < btnBase[x].length; y++) {
                btnBase[x][y] = new Button();
                btnBase [x][y]. setPrefSize(35,35);
                btnBase[x][y].setStyle("-fx-background-radius: 0; -fx-background-color: #494545;");
                btnBase[x][y].addEventHandler(MouseEvent.MOUSE_CLICKED, this::structureClicked);
                gPaneBase.add(btnBase[x][y],x,y);
            }
        }
        for (int x = 0; x < rooms.length; x++) { //initializes every room in rooms, and it also initializes all the gridPanes in griPanesMap
            for (int y = 0; y < rooms[x].length; y++) {
                rooms[x][y] = new Room(x*rooms[x].length+y+1);
                gridPanesMap[x][y] = new GridPane();
                for (int floorX = 0; floorX < rooms[x][y].getFloor().length; floorX++) { //initializes all the gridPanes in griPanesMap
                    for (int floorY = 0; floorY < rooms[x][y].getFloor()[0].length; floorY++) {
                        gridPanesMap[x][y].add(rooms[x][y].getFloor()[floorX][floorY].getTopStructure().getBtn(),floorX,floorY);
                    }
                }
            }
        }
        room = rooms[xRoom][yRoom];
        rooms[xRoom][yRoom].loadImages(player2);
    }

    private GridPane gPaneScreen = new GridPane(); //the gridPane that shows all the structures on the first layer of the screen
    private GridPane gPaneScreen2 = new GridPane(); //the gridPane that shows all the structures on the second layer of the screen
    private GridPane gPaneScreen3 = new GridPane(); //the gridPane that shows all the structures on the third layer of the screen
    private GridPane gPaneScreen4 = new GridPane(); //the gridPane that shows all the structures on the fourth layer of the screen
    private GridPane[][] gridPanesMap = new GridPane[3][5]; //holds all the gridPanes that show each room in map.
    //updates the screen and the map.
    private void updateScreen(){
        if(sPaneScreen.isVisible()) {
            sPaneScreen.getChildren().clear();
            gPaneScreen.getChildren().clear();
            gPaneScreen2.getChildren().clear();
            gPaneScreen3.getChildren().clear();
            gPaneScreen4.getChildren().clear();
            for (int x = 0; x < rooms[xRoom][yRoom].getFloor().length; x++) { //goes through each spot in the room and assigns the correct structure to it
                for (int y = 0; y < rooms[xRoom][yRoom].getFloor()[x].length; y++) {
                    gPaneScreen.add(rooms[xRoom][yRoom].getFloor()[x][y].getItems()[0].getImg(), x, y);
                    gPaneScreen2.add(rooms[xRoom][yRoom].getFloor()[x][y].getItems()[1].getImg(), x, y);
                    gPaneScreen3.add(rooms[xRoom][yRoom].getFloor()[x][y].getItems()[2].getImg(), x, y);
                    gPaneScreen4.add(rooms[xRoom][yRoom].getFloor()[x][y].getItems()[3].getImg(), x, y);
                }
            }
            updateMapRoom(xRoom,yRoom);
            sPaneScreen.getChildren().addAll(gPaneScreen, gPaneScreen2, gPaneScreen3, gPaneScreen4);
        } else if(gPaneMap.isVisible()){ //goes through each gridPane in maps and updates them.
            updateMapRoom(xRoom,yRoom);
            gPaneMap.getChildren().clear();
            for (int x = 0; x < gridPanesMap.length; x++) {
                for (int y = 0; y < gridPanesMap[0].length; y++) {
                    gPaneMap.add(gridPanesMap[x][y],x,y);
                }
            }
        }

        updateHealth();
    }

    //goes through each gridPane in maps, updates the gridPane which represent the room that experienced an update. xR and yR are the coordinates of the room that was updated
    private void updateMapRoom(int xR, int yR){
        gPaneMap.getChildren().clear();
        gridPanesMap[xR][yR].getChildren().clear();
        for (int xFloor = 0; xFloor < rooms[xR][yR].getFloor().length; xFloor++) {
            for (int yFloor = 0; yFloor < rooms[xR][yR].getFloor()[0].length; yFloor++) {
                gridPanesMap[xR][yR].add(rooms[xR][yR].getFloor()[xFloor][yFloor].getTopStructure().getBtn(),xFloor,yFloor);
            }
        }
    }

    //updates the players health to what it should be
    private void updateHealth(){
        ImageView[] hearts;
        if(player.getTotalHealth()%2 == 0){
            hearts = new ImageView[player.getTotalHealth()/2];
        }
        else{
            hearts = new ImageView[player.getTotalHealth()/2+1];
        }

        int health = player.getHealth();
        if(health <= 0){
            death();
            return;
        }
        int numX = (player.getTotalHealth()-player.getHealth())/2;
        gPaneHealth.getChildren().clear();
        gPaneHealth.setHgap(6);
        for (int x = 0; x < hearts.length; x++) {
            hearts[x] = new ImageView();
            hearts[x].setFitWidth(25);
            hearts[x].setFitHeight(25);
            if(numX>0){
                hearts[x].setImage(new Image("resources/heart3.png"));
                numX-=1;
            } else if(health%2 == 1){
                hearts[x].setImage(new Image("resources/heart2.png"));
                health -=1;
            } else {
                hearts[x].setImage(new Image("resources/heart1.png"));
            }
            gPaneHealth.add(hearts[x],x,0);
        }
    }

    //updates the opponent when a change was made to the screen
    private void updateOpponent(String update){
        socket.sendMessage(update);
    }

    //updates the player'sinventory
    private Button[] invButtons;
    private void updateInventory(){
        gPaneInvBorder.getChildren().clear();
        gPaneInvBorder.setHgap(5);
        gPaneInvButton.getChildren().clear();
        gPaneInvButton.setHgap(5);
        gPaneInv.getChildren().clear();
        gPaneInv.setHgap(20);
        gPaneLblInv.getChildren().clear();
        gPaneLblInv.setHgap(10);
        invButtons = new Button[player.getInventory().length];
        for (int x = 0; x < player.getInventory().length; x++) { //updates each inventory slot
            //initializes the buttons behind the inventory
            invButtons[x] = new Button();
            invButtons [x].setPrefSize(45,45);
            invButtons[x].setStyle("-fx-background-radius: 0; -fx-background-color: #494545;");
            invButtons[x].addEventHandler(MouseEvent.MOUSE_CLICKED, this::inventoryClicked);

            //adds the Buttons, Borders, labels, and ImageViews to the GridPane
            gPaneInvButton.add(invButtons[x],x,0);
            gPaneInvBorder.add(player.getInventory()[x].getImgBorder(), x, 0);
            gPaneInv.add(player.getInventory()[x].getImg(), x, 0);
            gPaneLblInv.add(player.getInventory()[x].getLbl(), x, 0);
        }
        imgInv.setFitWidth(player.getInventory().length*50+35);

        gPaneBase.setVisible(!(!paneChest.isVisible() && player.getInventory()[player.getItemSelected()].getItem() != null && player.getInventory()[player.getItemSelected()].getItem().getId().equals("map")));
        sPaneScreen.setVisible(!(!paneChest.isVisible() && player.getInventory()[player.getItemSelected()].getItem() != null && player.getInventory()[player.getItemSelected()].getItem().getId().equals("map")));
        gPaneMap.setVisible(!paneChest.isVisible() && player.getInventory()[player.getItemSelected()].getItem() != null && player.getInventory()[player.getItemSelected()].getItem().getId().equals("map"));
        updateScreen();

    }

    //the inventory was clicked
    private void inventoryClicked(MouseEvent event){
        int x = -1;
        for (int xTemp = 0; xTemp < player.getInventory().length; xTemp++) {
            if (event.getSource().equals(invButtons[xTemp])) {
                x = xTemp;
            }
        }

        if(paneChest.isVisible()){ // the chest is open, then items can be moved around
            if(movingItem.getItem() != null){
                if(player.getInventory()[x].getItem() == null || player.getInventory()[x].getItem().getId().equals(movingItem.getItem().getId())){
                    if(event.getButton().name().equals("PRIMARY") || event.getButton().name().equals("MIDDLE")){ //adds the whole stack
                        player.getInventory()[x].addItem(movingItem.getItem().getId(),movingItem.getSlot().size());
                        movingItem.removeItems(movingItem.getSlot().size());
                    } else if(event.getButton().name().equals("SECONDARY")){ //adds 1
                        player.getInventory()[x].addItem(movingItem.getItem().getId(),1);
                        movingItem.removeItems(1);
                    }
                } else{ //switches the item being moved
                    InventorySlot tempItem = new InventorySlot();
                    tempItem.addItem(player.getInventory()[x].getItem().getId(),player.getInventory()[x].getSlot().size()); //makes player inventory the temp
                    player.getInventory()[x].removeItems(player.getInventory()[x].getSlot().size()); //removes items from player inventory
                    player.getInventory()[x].addItem(movingItem.getItem().getId(),movingItem.getSlot().size()); //add the moving item to player inventory
                    movingItem.removeItems(movingItem.getSlot().size());
                    movingItem.addItem(tempItem.getItem().getId(),tempItem.getSlot().size());
                }
                imgMoving.setImage(movingItem.getImg().getImage());
                lblMoving.setText(movingItem.getLbl().getText());
            } else if(player.getInventory()[x].getItem() != null) {
                movingItemX = x;
                movingItemY = -1;
                if(event.getButton().name().equals("PRIMARY") || event.getButton().name().equals("MIDDLE")){ //starts moving an item
                    movingItem.addItem(player.getInventory()[x].getItem().getId(),player.getInventory()[x].getSlot().size());
                    player.getInventory()[x].removeItems(player.getInventory()[x].getSlot().size());
                } else if(event.getButton().name().equals("SECONDARY")){ //picks up half and starts moving that
                    int amount;
                    if(player.getInventory()[x].getSlot().size() %2 == 0){
                        amount = player.getInventory()[x].getSlot().size()/2;
                    } else{
                        amount = player.getInventory()[x].getSlot().size()/2+1;
                    }
                    movingItem.addItem(player.getInventory()[x].getItem().getId(),amount);
                    player.getInventory()[x].removeItems(amount);
                }
                imgMoving.setImage(movingItem.getImg().getImage());
                lblMoving.setText(movingItem.getLbl().getText());
            }
        }
        player.setItemSelected(x);
        updateInventory();

    }

    //a structure on the screen was clicked
    private void structureClicked(MouseEvent event){
        int x = -1;
        int y = -1;
        for (int tempX = 0; tempX < btnBase.length; tempX++) { //finds the coordinates of the structure that was clicked
            for (int tempY = 0; tempY < btnBase[tempX].length; tempY++) {
                if(event.getSource().equals(btnBase[tempX][tempY])){
                    x = tempX;
                    y = tempY;
                }
            }
        }

        int layer = -1;
        for (int l = 0; l < 4; l++) { //finds the layer of th structure clicked
            if(!room.getFloor()[x][y].getItems()[l].getId().equals("blank")){
                layer = l;
            }
        }

        if(event.getButton().name().equals("PRIMARY")){ //the player left clicked, if the player placed down the structure, the player can break it
            if(room.getFloor()[x][y].getItems()[layer].getOwner().equals(player.getId())){
                if(player.canAddItem(room.getFloor()[x][y].getItems()[layer].getId())){ //if the player is able to pick up the item it's breaking, it will
                    player.addItem(room.getFloor()[x][y].getItems()[layer].getId(), 1);
                }
                updateOpponent("/updRemove:"+room.getFloor()[x][y].getItems()[layer].getId()+"X"+x+"Y"+y+"x"+xRoom+"y"+yRoom);
                room.getFloor()[x][y].removeStructure(layer);
                updateScreen();
            }

        } else if(event.getButton().name().equals("SECONDARY")){ //the player right clicked
            if(player.getInventory()[player.getItemSelected()].getItem() != null && player.getInventory()[player.getItemSelected()].getItem().isStructure()){ //checks to see if the player is trying to place down a structure
                if(room.getFloor()[x][y].canPlace(player.getInventory()[player.getItemSelected()].getItem().getId())){
                    room.getFloor()[x][y].setStructure(player.getInventory()[player.getItemSelected()].getItem().getId(), player.getId());
                    updateOpponent("/updPlace:"+player.getInventory()[player.getItemSelected()].getItem().getId()+"X"+x+"Y"+y+"x"+xRoom+"y"+yRoom);
                    Item item = new Item(player.getInventory()[player.getItemSelected()].getItem().getId(), "null",false);
                    room.getFloor()[x][y].getItems()[item.getLayer()].setImg();
                    player.removeItem(player.getInventory()[player.getItemSelected()].getItem().getId(),1);
                    updateInventory();
                    updateScreen();
                }
            }
            //checks to see if the player is trying to place down a platform
            if(player.getInventory()[player.getItemSelected()].getItem() != null && player.getInventory()[player.getItemSelected()].getItem().getId().length()>8 && player.getInventory()[player.getItemSelected()].getItem().getId().substring(0,8).equals("platform")){
                if(room.getFloor()[x][y].canPlace("floor"+(Integer.parseInt(player.getInventory()[player.getItemSelected()].getItem().getId().substring(8))+1))){
                    addObject(x-1,y-1,"floor"+(Integer.parseInt(player.getInventory()[player.getItemSelected()].getItem().getId().substring(8))+1));
                    player.removeItem(player.getInventory()[player.getItemSelected()].getItem().getId(),1);
                    updateInventory();
                    updateScreen();
                }
                //checks to see if the player is trying to use a healing flask
            } else if(player.getInventory()[player.getItemSelected()].getItem() != null && player.getInventory()[player.getItemSelected()].getItem().getId().equals("healingFlask") && room.getFloor()[x][y].getItems()[layer].getId().equals(player.getId())){
                player.setHealth(player.getHealth()+Upgrades.getHealthBoost());
                player.removeItem(player.getInventory()[player.getItemSelected()].getItem().getId(),1);
                updateInventory();
                updateHealth();
                //checks to see if the player is trying to use a grenade
            } else if(player.getInventory()[player.getItemSelected()].getItem() != null && player.getInventory()[player.getItemSelected()].getItem().getId().equals("grenade")){
                explodeBomb(x,y, "grenade");
                player.removeItem(player.getInventory()[player.getItemSelected()].getItem().getId(),1);
                updateInventory();
                updateScreen();
            }
            //checks to see if the player is trying to activate a bomb
            if(room.getFloor()[x][y].getItems()[layer].getId().equals("bomb") && room.getFloor()[x][y].getItems()[layer].getOwner().equals(player.getId())){
                explodeBomb(x,y, "bomb");
                updateScreen();
            }
        }

        System.out.println(room.getFloor()[x][y].getItems()[layer].getId()+" was clicked");
    }

    //this method makes an explosion. Explosions can occur from a bomb or grenade. StartX and startY are the starting coordinates of the explosion. Type can either be bomb or grenade
    private void explodeBomb(int startX, int startY, String type){
        if (type.equals("bomb")) { //makes the explosion based the the blast radius and strength of the bomb, and then updates the opponent
            for (int x = 0; x < Upgrades.getBombRadius() * 2 + 1; x++) {
                for (int y = 0; y < Upgrades.getBombRadius() * 2 + 1; y++) {
                    if (startX - Upgrades.getBombRadius() + x >= 0 && startX - Upgrades.getBombRadius() + x < room.getFloor().length && startY - Upgrades.getBombRadius() + y >= 0 && startY - Upgrades.getBombRadius() + y < room.getFloor()[0].length) {
                        updateOpponent("/updBoom:" + Upgrades.getBombStrength() + "X" + (startX - Upgrades.getBombRadius() + x) + "Y" + (startY - Upgrades.getBombRadius() + y) + "x" + xRoom + "y" + yRoom);
                        room.getFloor()[startX - Upgrades.getBombRadius() + x][startY - Upgrades.getBombRadius() + y].explode(player, Upgrades.getBombStrength());
                    }
                }
            }
        }
        if(type.equals("grenade")) { //makes the explosion based the the blast radius and strength of the grenade, and then updates the opponent
            for (int x = 0; x < Upgrades.getGrenadeRadius() * 2 + 1; x++) {
                for (int y = 0; y < Upgrades.getGrenadeRadius() * 2 + 1; y++) {
                    if (startX - Upgrades.getGrenadeRadius() + x >= 0 && startX - Upgrades.getGrenadeRadius() + x < room.getFloor().length && startY - Upgrades.getGrenadeRadius() + y >= 0 && startY - Upgrades.getGrenadeRadius() + y < room.getFloor()[0].length) {
                        updateOpponent("/updBoom:" + Upgrades.getGrenadeStrength() + "X" + (startX - Upgrades.getGrenadeRadius() + x) + "Y" + (startY - Upgrades.getGrenadeRadius() + y) + "x" + xRoom + "y" + yRoom);
                        room.getFloor()[startX - Upgrades.getGrenadeRadius() + x][startY - Upgrades.getGrenadeRadius() + y].explode(player, Upgrades.getGrenadeStrength());
                    }
                }
            }
        }

    }

    //detects when the player moved
    public void keyPressed(javafx.scene.input.KeyEvent pressed){
        if(start) {
            if((pressed.getCode().equals(KeyCode.A) || pressed.getCode().equals(KeyCode.D) || pressed.getCode().equals(KeyCode.W) || pressed.getCode().equals(KeyCode.S)) && !gPaneMap.isVisible()) {
                int[] movement = new int[2];
                if (pressed.getCode().equals(KeyCode.A)) { //moves left
                    move1(movement, 0, -1);
                } else if (pressed.getCode().equals(KeyCode.D)) { //moves right
                    move1(movement, 0, 1);
                } else if (pressed.getCode().equals(KeyCode.W)) { //moves up
                    move1(movement, 1, -1);
                } else if (pressed.getCode().equals(KeyCode.S)) { //moves down
                    move1(movement, 1, 1);
                }
                room = rooms[xRoom][yRoom];
                move2(movement);
                updateScreen();
            }
        }
    }

    //this method checks to see if rhe player is trying to exit the room. coord can be 0 or 1, 0 means the x coordinate has changed, 1 means the y coordinate has changed.
    //direction can be -1,0, or 1. -1 means the player's x or y coordinate has decreased, 1 means the player's x or y coordinate has increased.
    //movement stores information about how the player moved, coord and direction are used to update movement.
    private void move1(int[] movement, int coord, int direction){
        int[] coords = player.getCoords();
        int[] starts = new int[]{12,12,0,0}; //stores the values of possible x and y values for entering a room.
        updateOpponent("/updRemove:"+player2.getId()+"X"+coords[0]+"Y"+coords[1]+"x"+xRoom+"y"+yRoom);
        movement[coord] = direction; //updates movement based on coord and direction
        if(notInBounds(coords[0] + movement[0], coords[1] + movement[1])) { //if the player is trying to exit the room

            //the player's coords are updated, it checks to see if player 2 is in the same spot, if so the player's coords are changed back to what they where
            int temptCoord = coords[coord];
            coords[coord] = starts[coord + direction + 1];
            if(!rooms[xRoom + movement[0]][yRoom + movement[1]].getFloor()[coords[0]][coords[1]].getItems()[3].getId().equals(player2.getId())) { //if the place the player wants to move isn't occupied by player2
                coords[coord] = temptCoord; //changes the player's coords back to what they were so the player can be removed from that spot on the screen
                room.getFloor()[player.getCoords()[0]][player.getCoords()[1]].removeStructure(player.getId());
                updateMapRoom(xRoom,yRoom);
                xRoom += movement[0];
                yRoom += movement[1];
                rooms[xRoom][yRoom].loadImages(player2);
                coords[coord] = starts[coord + direction + 1];
            } else{ //the space is occupied by player2, so the player doesn't move
                coords[coord] = temptCoord;
            }
            movement[coord] = 0; //sets all movement values to 0 so that when move2 is called the player's coordinates won't be updated again.
        }
    }

    //returns whether the x and y coordinates are not in the bounds of the room.
    private boolean notInBounds(int x, int y){
        return x >= room.getFloor().length || x < 0 || y >= room.getFloor()[x].length || y < 0;
    }

    //this method updates the players coordinates based on movement and adds the player to the correct spot on the screen.
    //movement stores information about how the player moved, index 0 is for movement in the x coordinate, index 1 is for movement in the y coordinate.
    //the values in movement can either be -1,0, or 1, -1 means the player's x or y coordinate has decreased, 1 means the player's x or y coordinate has increased.
    //if the player was moved during move1, then move2 won't change the player's coordinates since movement will only hold 0s.
    private void move2(int[] movement){
        int[] coords = player.getCoords();
        if(room.getFloor()[coords[0] + movement[0]][coords[1] + movement[1]].canMove()){ //makes sure the player can moved in the spot it's trying to go
            room.getFloor()[coords[0]][coords[1]].removeStructure(player.getId());
            coords[0] = coords[0] + movement[0];
            coords[1] = coords[1] + movement[1];
        }
        updateOpponent("/updP2X"+coords[0]+"Y"+coords[1]+"x"+xRoom+"y"+yRoom);
        room.getFloor()[coords[0]][coords[1]].setStructure(player); //places the player on the screen using the player's coords
        doEvent(); //checks to see if an event was triggered when the player moved
    }

    private boolean shopInitialized = false; //whether the shop has been initialized or not
    private Button[][] shopItemsButtons = new Button[5][3]; //the buttons that go behind the items in the shop. When an item is clicked, one of these buttons will be pressed
    private Button[][] shopUpgradesButtons = new Button[5][2]; //the buttons that go behind the upgrades in the shop. When an upgrades is clicked, one of these buttons will be pressed
    private Item[][] shopItems; //the 2d array that holds the items in shopItems
    private String[][] upgradesId = {{"upHealth","upTrap"},
            {"upHealingFlask","upGoldGen"},
            {"upInv","upEmeraldGen"},
            {"upBomb",null},
            {"upGrenade",null}};
    //initializes all the items and upgrades in the shop.
    private void loadShop(){
        if(!shopInitialized){ //the shop can only be initialized once per game
            gPaneBtnShopItems.getChildren().clear();
            gPaneShopItems.getChildren().clear();
            gPaneBtnShopUpgrades.getChildren().clear();
            gPaneShopUpgrades.getChildren().clear();
            Upgrades.setUpgrades();
            shopItems = new Item[][]{{new Item("floor2", null, true), new Item("wall2", null, true), new Item("shop", null, true)},
                    {new Item("floor3", "null", true), new Item("bomb", "null", true), new Item("chest", null, true)},
                    {new Item("platform1", "null", true), new Item("grenade", "null", true), new Item("trap1", null, true)},
                    {new Item("platform2", "null", true), new Item("lavaBoots", null, true), new Item("trap2", null, true)},
                    {new Item("wall1", "null", true), new Item("healingFlask", null, true), new Item("map", null, true)}};

            for (int x = 0; x < shopItems.length; x++) { //initializes all the items in the shop
                for (int y = 0; y < shopItems[0].length; y++) {
                    shopItemsButtons[x][y] = new Button();
                    shopItemsButtons[x][y].setPrefSize(35,35);
                    shopItemsButtons[x][y].setStyle("-fx-background-radius: 0; -fx-background-color: #494545;");
                    shopItemsButtons[x][y].addEventHandler(MouseEvent.MOUSE_CLICKED, this::shopItemClicked);
                    gPaneBtnShopItems.add(shopItemsButtons[x][y],x,y);
                    if(shopItems[x][y] != null){
                        gPaneShopItems.add(shopItems[x][y].getImg(),x,y);
                    }
                }
                for (int y = 0; y < upgradesId[0].length; y++) { //initializes all the upgrades in the shop
                    shopUpgradesButtons[x][y] = new Button();
                    shopUpgradesButtons[x][y].setPrefSize(35,35);
                    shopUpgradesButtons[x][y].setStyle("-fx-background-radius: 0; -fx-background-color: #494545;");
                    shopUpgradesButtons[x][y].addEventHandler(MouseEvent.MOUSE_CLICKED, this::shopUpgradeClicked);
                    gPaneBtnShopUpgrades.add(shopUpgradesButtons[x][y],x,y);
                    if(upgradesId[x][y] != null){
                        gPaneShopUpgrades.add(Upgrades.getImageView(upgradesId[x][y]),x,y+3);
                    }
                }
            }
            //sets layout of shop
            gPaneBtnShopItems.setHgap(15);
            gPaneBtnShopItems.setVgap(10);
            gPaneBtnShopUpgrades.setHgap(15);
            gPaneBtnShopUpgrades.setVgap(10);
            gPaneShopItems.setHgap(15);
            gPaneShopItems.setVgap(10);
            gPaneShopUpgrades.setHgap(15);
            gPaneShopUpgrades.setVgap(10);
            paneShop.setLayoutX(490);
            paneShop.setLayoutY(120);
            shopInitialized = true;
        }
    }

    private Item shopItem = new Item("blank", "null",true); //this holds the item that the player wants to purchase
    private void shopItemClicked(MouseEvent event){ //a shop Item has been clicked
        shopItem = new Item("blank", "null",true);
        shopUpgrade = "null";
        int x = -1;
        int y = -1;
        for (int xTemp = 0; xTemp < shopItemsButtons.length; xTemp++) { //find the x and y coordinate of the item be purchased
            for (int yTemp = 0; yTemp < shopItemsButtons[0].length; yTemp++) {
                if(shopItemsButtons[xTemp][yTemp].equals(event.getSource())){
                    x = xTemp;
                    y = yTemp;
                }
            }
        }
        if(shopItems[x][y] != null){ //sets the GUI for purchasing the item
            shopItem = shopItems[x][y];
            lblShop2.setText(shopItem.getName());
            imgShopItem.setImage(shopItem.getImg().getImage());
            lblDefinitions.setText(shopItem.getDefinition());
            paneItemShop.setVisible(true);
            paneItemShop.setLayoutX(490);
            paneItemShop.setLayoutY(189);
            lblPrice.setLayoutY(167);
            lblPrice.setText("Cost: "+shopItem.getPrice()*Integer.parseInt(txtShop.getText())+" Gold");
            lblPrice.setVisible(true);
            lblPrice.setLayoutX(653);
            paneShop.setVisible(false);
            paneShop2.setVisible(true);
            paneShop2.setLayoutX(490);
            paneShop2.setLayoutY(120);
        }
    }

    private String shopUpgrade = "null"; //this hold the id of the upgrade the player wants to purchase
    private void shopUpgradeClicked(MouseEvent event){ //a shop Upgrade has been clicked
        shopItem = new Item("blank", "null",true);
        shopUpgrade = "null";
        int x = -1;
        int y = -1;
        for (int xTemp = 0; xTemp < shopUpgradesButtons.length; xTemp++) { //find the x and y coordinate of the upgrade be purchased
            for (int yTemp = 0; yTemp < shopUpgradesButtons[0].length; yTemp++) {
                if(shopUpgradesButtons[xTemp][yTemp].equals(event.getSource())){
                    x = xTemp;
                    y = yTemp;
                }
            }
        }
        if(upgradesId[x][y] != null){ //sets the GUI for purchasing the upgrade
            shopUpgrade = upgradesId[x][y]; lblShop2.setText(Upgrades.getName(shopUpgrade));
            imgShopItem.setImage(Upgrades.getImageView(shopUpgrade).getImage());
            lblDefinitions.setText(Upgrades.getDefinition(shopUpgrade));
            lblPrice.setLayoutY(217);
            lblPrice.setText("Cost: "+ Upgrades.getPrice(shopUpgrade)+" Emeralds");
            lblPrice.setVisible(true);
            lblPrice.setLayoutX(653);
            paneShop.setVisible(false);
            paneShop2.setVisible(true);
            paneShop2.setLayoutX(490);
            paneShop2.setLayoutY(120);
        }
    }

    @FXML
    private void handleBtnBack(){ //goes back to the main shop screen
        paneItemShop.setVisible(false);
        paneShop2.setVisible(false);
        paneShop.setVisible(true);
        lblPrice.setVisible(false);
        txtShop.setText("1");

    }

    //increases the amount being purchased by 1
    @FXML
    private void handleBtnUp(){
        txtShop.setText(Integer.toString(Integer.parseInt(txtShop.getText())+1));
    }

    //decreases the amount being purchased by 1
    @FXML
    private void handleBtnDown(){
        if(Integer.parseInt(txtShop.getText())>0) {
            txtShop.setText(Integer.toString(Integer.parseInt(txtShop.getText()) - 1));
        }
    }

    //an item or upgrade has been purchased
    @FXML
    private void handleBtnPurchase(){
        //if an item is selected and the player is able to purchase the item
        if(txtShop.getText().length()>0 && !shopItem.getId().equals("blank") && player.canAddItem(shopItem.getId()) && player.getAmount("gold") >= shopItem.getPrice()*Integer.parseInt(txtShop.getText())){
            player.addItem(shopItem.getId(),Integer.parseInt(txtShop.getText()));
            player.removeItem("gold",shopItem.getPrice()*Integer.parseInt(txtShop.getText()));
            handleBtnBack();
            updateInventory();
            //if an upgrade is selected and the player is able to purchase the upgrade
        } else if(!shopUpgrade.equals("null") && player.getAmount("emerald") >= Upgrades.getPrice(shopUpgrade)){ //this purchases an upgrade
            if(Upgrades.getCanUpgrade(shopUpgrade)){
                Upgrades.upgrades(shopUpgrade,player);
                if(shopUpgrade.equals("upTrap")){
                    socket.sendMessage("/trap:false");
                }
                player.removeItem("emerald", Upgrades.getPrice(shopUpgrade));
                updateInventory();
                updateScreen();
                handleBtnBack();
            }
        }
    }


    private InventorySlot[][] chestItems = new InventorySlot[5][5]; //the 2d array the holds the items in the chest
    private Button[][] chestButtons = new Button[5][5]; //the 2d array of buttons that goes behind the chest
    private boolean chestInitialized = false; //if the chest has been initialized or not
    //this loads the chest and updates the chest screen to items in chestItems
    private void loadChest(){
        gPaneChestBorder.getChildren().clear();
        gPaneChestBorder.setHgap(5);
        gPaneChestBorder.setVgap(5);
        gPaneChestBtn.getChildren().clear();
        gPaneChestBtn.setHgap(5);
        gPaneChestBtn.setVgap(5);
        gPaneChest.getChildren().clear();
        gPaneChest.setHgap(20);
        gPaneChest.setVgap(20);
        gPaneChestLbl.getChildren().clear();
        gPaneChestLbl.setHgap(10);
        gPaneChestLbl.setVgap(10);
        for (int x = 0; x < chestItems.length; x++) { //updates each chest slot
            for (int y = 0; y < chestItems[0].length; y++) {
                if(!chestInitialized){
                    chestItems[x][y] = new InventorySlot();
                }

                //initializes the buttons behind the chest
                chestButtons[x][y] = new Button();
                chestButtons[x][y].setPrefSize(45,45);
                chestButtons[x][y].setStyle("-fx-background-radius: 0; -fx-background-color: #494545;");
                chestButtons[x][y].addEventHandler(MouseEvent.MOUSE_CLICKED, this::chestClicked);

                gPaneChestBtn.add(chestButtons[x][y],x,y);
                gPaneChestBorder.add(chestItems[x][y].getImgBorder(), x, y);
                gPaneChest.add(chestItems[x][y].getImg(), x, y);
                gPaneChestLbl.add(chestItems[x][y].getLbl(), x, y);
            }
        }
        if(!chestInitialized){
            imgMoving.setFitWidth(movingItem.getImg().getFitWidth());
            imgMoving.setFitHeight(movingItem.getImg().getFitHeight());
            lblMoving.setPrefSize(movingItem.getLbl().getPrefWidth(),movingItem.getLbl().getPrefHeight());
            lblMoving.setStyle(movingItem.getLbl().getStyle());
        }
        chestInitialized = true;
        paneChest.setLayoutX(490);
        paneChest.setLayoutY(120);
    }

    private InventorySlot movingItem = new InventorySlot(); //this holds the items that the player is moving around
    private int movingItemX = -1; //the x coordinate of the item the player is moving around
    private int movingItemY = -1; //the y coordinate of the item the player is moving around
    //the chest has been clicked
    private void chestClicked(MouseEvent event){
        int x = 0;
        int y = 0;
        for (int tempX = 0; tempX < chestButtons.length; tempX++) { //find the coordinates of the items that was clicked in the chest
            for (int tempY = 0; tempY < chestButtons[tempX].length; tempY++) {
                if(event.getSource().equals(chestButtons[tempX][tempY])){
                    x = tempX;
                    y = tempY;
                }
            }
        }
        if(movingItem.getItem() != null){ //there is an item being held
            if(chestItems[x][y].getItem() == null || chestItems[x][y].getItem().getId().equals(movingItem.getItem().getId())){ //the spot is empty or contains the same item as is being held
                if(event.getButton().name().equals("PRIMARY") || event.getButton().name().equals("MIDDLE")){ //adds the whole stack
                    chestItems[x][y].addItem(movingItem.getItem().getId(),movingItem.getSlot().size());
                    movingItem.removeItems(movingItem.getSlot().size());
                } else if(event.getButton().name().equals("SECONDARY")){ //adds 1
                    chestItems[x][y].addItem(movingItem.getItem().getId(),1);
                    movingItem.removeItems(1);
                }
            } else{ //switches the item being moved
                InventorySlot tempItem = new InventorySlot();
                tempItem.addItem(chestItems[x][y].getItem().getId(),chestItems[x][y].getSlot().size());
                chestItems[x][y].removeItems(chestItems[x][y].getSlot().size());
                chestItems[x][y].addItem(movingItem.getItem().getId(),movingItem.getSlot().size());
                movingItem.removeItems(movingItem.getSlot().size());
                movingItem.addItem(tempItem.getItem().getId(),tempItem.getSlot().size());
            }
            imgMoving.setImage(movingItem.getImg().getImage());
            lblMoving.setText(movingItem.getLbl().getText());
            loadChest();
        } else if(chestItems[x][y].getItem() != null) {
            movingItemX = x;
            movingItemY = y;
            if(event.getButton().name().equals("PRIMARY") || event.getButton().name().equals("MIDDLE")){ //starts moving an item
                movingItem.addItem(chestItems[x][y].getItem().getId(),chestItems[x][y].getSlot().size());
                chestItems[x][y].removeItems(chestItems[x][y].getSlot().size());
            } else if(event.getButton().name().equals("SECONDARY")){ //picks up half and starts moving that
                int amount;
                if(chestItems[x][y].getSlot().size() %2 == 0){
                    amount = chestItems[x][y].getSlot().size()/2;
                } else{
                    amount = chestItems[x][y].getSlot().size()/2+1;
                }
                movingItem.addItem(chestItems[x][y].getItem().getId(),amount);
                chestItems[x][y].removeItems(amount);
            }
            imgMoving.setImage(movingItem.getImg().getImage());
            lblMoving.setText(movingItem.getLbl().getText());
            loadChest();
        }
    }


    private int numGemsLost = 0; //the number of gems the opponent has captured
    private boolean[] gemsCaptured = new boolean[]{false,false}; //the gems the player has captured
    //checks to see if something happens where the player is standing
    private void doEvent(){
        int[] coords = player.getCoords();

        if(room.getFloor()[coords[0]][coords[1]].checkEvent(player).equals("shop")){ //the player is standing on the shp[
            loadShop();
            paneShop.setVisible(true);
        } else {
            handleBtnBack();
            paneShop.setVisible(false);
        }
        if(room.getFloor()[coords[0]][coords[1]].checkEvent(player).equals("death")){ //the player is standing somewhere that kills them
            death();
        }
        if(room.getFloor()[coords[0]][coords[1]].checkEvent(player).equals("healthTrap")){ //the player is standing on a health trap
            player.setHealth(player.getHealth()-4);
            updateOpponent("/updRemove:"+("trap1")+"X"+(coords[0])+"Y"+(coords[1])+"x"+xRoom+"y"+yRoom);
            room.getFloor()[coords[0]][coords[1]].removeStructure(2);
            updateHealth();
            updateScreen();
        }
        if(room.getFloor()[coords[0]][coords[1]].checkEvent(player).equals("invTrap")){ //the player is standing on a inventory trap
            checkGems();
            player.clearInventory();
            updateOpponent("/updRemove:"+("trap2")+"X"+(coords[0])+"Y"+(coords[1])+"x"+xRoom+"y"+yRoom);
            room.getFloor()[coords[0]][coords[1]].removeStructure(2);
            updateInventory();
            updateScreen();
        }
        if(room.getFloor()[coords[0]][coords[1]].checkEvent(player).equals("onGem")){ //the player is standing on a the opponents gem
            int numberOn = Integer.parseInt(room.getFloor()[coords[0]][coords[1]].getItems()[2].getId().substring(room.getFloor()[coords[0]][coords[1]].getItems()[2].getId().length()-1));
            int moveX = 0;
            int moveY = 0;
            while(numberOn % 3 != 1){
                moveY-=1;
                numberOn = Integer.parseInt(room.getFloor()[coords[0]][coords[1]+moveY].getItems()[2].getId().substring(room.getFloor()[coords[0]][coords[1]+moveY].getItems()[2].getId().length()-1));
            }
            while(numberOn > 3){
                moveX-=1;
                numberOn = Integer.parseInt(room.getFloor()[coords[0]+moveX][coords[1]].getItems()[2].getId().substring(room.getFloor()[coords[0]+moveX][coords[1]].getItems()[2].getId().length()-1));
            }

            if(player.canAddItem(gemTarget) && !(coords[0]+moveX == 2 && gemsCaptured[0]) && !(coords[0]+moveX == 8 && gemsCaptured[1])) {
                removeObject(coords[0] + moveX, coords[1] + moveY, room);
                player.addItem(gemTarget, 1);
                updateInventory();
                updateScreen();
            }
        }
        if(room.getFloor()[coords[0]][coords[1]].checkEvent(player).equals("onHolder")){ //the player is standing on their gem holder

            int numberOn = Integer.parseInt(room.getFloor()[coords[0]][coords[1]].getItems()[2].getId().substring(room.getFloor()[coords[0]][coords[1]].getItems()[2].getId().length()-1));
            int moveX = 0;
            int moveY = 0;
            while(numberOn % 3 != 1){
                moveY-=1;
                numberOn = Integer.parseInt(room.getFloor()[coords[0]][coords[1]+moveY].getItems()[2].getId().substring(room.getFloor()[coords[0]][coords[1]+moveY].getItems()[2].getId().length()-1));
            }
            while(numberOn > 3){
                moveX-=1;
                numberOn = Integer.parseInt(room.getFloor()[coords[0]+moveX][coords[1]].getItems()[2].getId().substring(room.getFloor()[coords[0]+moveX][coords[1]].getItems()[2].getId().length()-1));
            }

            if(coords[0]+moveX == 2){
                gemsCaptured[0] = true;
            } else if(coords[0]+moveX == 8){
                gemsCaptured[1] = true;
            }
            socket.sendMessage("/captured");

            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    updateOpponent("/updPlace:"+gemTarget+((x)*3+(y)+1)+"X"+(coords[0]+moveX+x)+"Y"+(coords[1]+moveY+y)+"x"+xRoom+"y"+yRoom);
                    rooms[xRoom][yRoom].getFloor()[coords[0]+moveX+x][coords[1]+moveY+y].setStructure(gemTarget+((x)*3+(y)+1),"null");
                    rooms[xRoom][yRoom].getFloor()[coords[0]+moveX+x][coords[1]+moveY+y].getItems()[2].setImg();
                }
            }
            player.removeItem(gemTarget,1);
            updateInventory();
            updateScreen();
        }
        if(room.getFloor()[coords[0]][coords[1]].checkEvent(player).equals("onChest")){ //the player is standing on a chest
            loadChest();
            paneChest.setVisible(true);
        } else {
            paneChest.setVisible(false);
            if(movingItem.getItem() != null){
                if(movingItemY == -1){
                    player.getInventory()[movingItemX].addItem(movingItem.getItem().getId(),movingItem.getSlot().size());
                } else {
                    chestItems[movingItemX][movingItemY].addItem(movingItem.getItem().getId(),movingItem.getSlot().size());
                }
                movingItem.removeItems(movingItem.getSlot().size());
                imgMoving.setImage(movingItem.getImg().getImage());
                lblMoving.setText(movingItem.getLbl().getText());
                movingItemY = -1;
                movingItemX = -1;
            }
            updateInventory();
        }
    }

    private void removeObject(int startX,int startY, Room room){ //a 3 by 3 area of structures is being removed from layer 2
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                updateOpponent("/updRemove:"+(gemTarget+((x)*3+(y)+1))+"X"+(startX+x)+"Y"+(startY+y)+"x"+xRoom+"y"+yRoom);
                room.getFloor()[startX+x][startY+y].removeStructure(2);
            }
        }
    }

    private void addObject(int startX,int startY, String id){ //a 3 by 3 area of a structure is added to the screen
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if(room.getFloor()[startX+x][startY+y].canPlace(id)){
                    updateOpponent("/updPlace:"+id+"X"+(startX+x)+"Y"+(startY+y)+"x"+xRoom+"y"+yRoom);
                    room.getFloor()[startX+x][startY+y].setStructure(id,player.getId());
                    Item item = new Item(id, "null",false);
                    room.getFloor()[startX+x][startY+y].getItems()[item.getLayer()].setImg();
                }
            }
        }
    }

    //constantly checking if gold or emeralds should be added to players inventory, also checks the shop textField to make sure it's only integers. Also checks to see if the player is moving items inside a chest
    public void animationTimer() {
        //for some reason these have to be arrays in order to be accessed from within the animation timer.
        final long[] startGold = {System.nanoTime()};
        final long[] startEmerald = {System.nanoTime()};
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                String id = room.getFloor()[player.getCoords()[0]][player.getCoords()[1]].getItems()[2].getId();
                if (now - startGold[0] >= Upgrades.getGoldTime() * 1000000000.0) { //checks the gold generator
                    if (id.length() >= 8 && id.substring(0, 7).equals("goldGen") && player.canAddItem("gold")) {
                        player.addItem("gold", Upgrades.getGoldProduce());
                        updateInventory();
                    }
                    startGold[0] = System.nanoTime();
                }
                if (now - startEmerald[0] >= Upgrades.getEmeraldTime() * 1000000000.0) { //checks the emerald generator
                    if (id.length() >= 11 && id.substring(0, 10).equals("emeraldGen") && player.canAddItem("emerald")) {
                        player.addItem("emerald", Upgrades.getEmeraldProduce());
                        updateInventory();
                    }
                    startEmerald[0] = System.nanoTime();
                }

                String[] goodNumbers = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"}; //list of all the Integers that are allowed, the textField can only have integers.
                String shopTxt = txtShop.getText();
                for (int x = 0; x < shopTxt.length(); x++) {
                    boolean isInteger = false;
                    for (String goodNumber : goodNumbers) { //checks to see if the key pressed is allowed;
                        if (!String.valueOf(shopTxt.charAt(x)).equals(" ") && String.valueOf(shopTxt.charAt(x)).equals(goodNumber)) {
                            isInteger = true;
                            break;
                        }
                    }
                    if (!isInteger) { //if the key pressed was not allowed, it resets the textField to what it was before the key was pressed.
                        shopTxt = shopTxt.substring(0, x) + shopTxt.substring(x + 1);
                        txtShop.setText(shopTxt);
                    }
                }
                if(shopTxt.length() == 0){
                    if(!shopItem.getId().equals("blank")) { //an item was clicked, sets the shop to be ready to purchase an item
                        lblPrice.setText("Cost: 0 Gold");
                    } else if(!shopUpgrade.equals("null")){  //an update was clicked, sets the shop to be ready to purchase an update
                        lblPrice.setText("Cost: 0 Emeralds");
                    }
                } else {
                    if(!shopItem.getId().equals("blank")) { //an item was clicked, sets the shop to be ready to purchase an item
                        lblPrice.setText("Cost: " + shopItem.getPrice() * Integer.parseInt(txtShop.getText()) + " Gold");
                    } else if(!shopUpgrade.equals("null")){  //an update was clicked, sets the shop to be ready to purchase an update
                        lblPrice.setText("Cost: " + Upgrades.getPrice(shopUpgrade) * Integer.parseInt(txtShop.getText()) + " Emeralds");
                    }
                }

                if(movingItem.getItem() != null) { //the player is moving an item from a chest
                    double mouseX;
                    double mouseY;
                    //sets the items coordinates to the coordinates of the mouse
                    if(MouseInfo.getPointerInfo().getLocation().getX()>0 && MouseInfo.getPointerInfo().getLocation().getX() <= Toolkit.getDefaultToolkit().getScreenSize().getWidth()){
                        mouseX = (Screen.getPrimary().getBounds().getWidth()/Toolkit.getDefaultToolkit().getScreenSize().getWidth())*(MouseInfo.getPointerInfo().getLocation().getX() - 11) - Main.getStage().getX();
                        mouseY = (Screen.getPrimary().getBounds().getHeight()/Toolkit.getDefaultToolkit().getScreenSize().getHeight())*(MouseInfo.getPointerInfo().getLocation().getY() - 45) - Main.getStage().getY();
                    }else{
                        mouseX = MouseInfo.getPointerInfo().getLocation().getX() - 11 - Main.getStage().getX();
                        mouseY = MouseInfo.getPointerInfo().getLocation().getY() - 45 - Main.getStage().getY();
                    }
                    imgMoving.setLayoutX(mouseX-15);
                    imgMoving.setLayoutY(mouseY-15);
                    lblMoving.setLayoutX(mouseX-15+15.25);
                    lblMoving.setLayoutY(mouseY-15+6.25);
                }

            }

        }.start();
    }

    //the player died and all the necessary atributes have been reset
    private void death(){
        int[] coords = player.getCoords();
        checkGems();
        player.clearInventory();
        player.setItemSelected(0);
        updateOpponent("/updRemove:"+player2.getId()+"X"+coords[0]+"Y"+coords[1]+"x"+xRoom+"y"+yRoom);
        room.getFloor()[coords[0]][coords[1]].removeStructure(player.getId());
        updateMapRoom(xRoom,yRoom);
        xRoom = xStartR;
        yRoom = yStartR;
        room = rooms[xRoom][yRoom];
        coords[0] = 6;
        coords[1] = 6;
        updateOpponent("/updRemove:"+room.getFloor()[coords[0]][coords[1]].getItems()[3].getId()+"X"+coords[0]+"Y"+coords[1]+"x"+xRoom+"y"+yRoom);
        room.getFloor()[coords[0]][coords[1]].removeStructure(3);
        updateOpponent("/updP2X"+coords[0]+"Y"+coords[1]+"x"+xRoom+"y"+yRoom);
        room.getFloor()[coords[0]][coords[1]].setStructure(player);
        player.setHealth(player.getTotalHealth());
        updateInventory();
        updateScreen();
        if(numGemsLost == 2){
            socket.sendMessage("/gameLost");
            gameLost();
        }
    }

    //the game is over and all the necessary variables are reset
    private void reset(){
        chestInitialized = false;
        shopInitialized = false;
        numGemsLost = 0;
        gemsCaptured = new boolean[]{false,false};
        movingItem = new InventorySlot();
        movingItemX = -1;
        movingItemY = -1;
        shopItem = new Item("blank", "null",true);
        shopUpgrade = "null";
        startGame();
    }

    //checks to see if a gem should be added back to its original position
    private void checkGems(){
        Item gem = new Item(gemTarget+"1","null",false);
        for (int x = 0; x < 2; x++) {
            //if the gem is missing from its spot (the id is not equal to the gem id because the gem isn't there)
            if(player.isHolding(gemTarget) && (rooms[gem.getGemRooms()[x][0]][gem.getGemRooms()[x][1]].getFloor()[gem.getGemCoords()[x][0]][gem.getGemCoords()[x][1]].getItems()[2].getId().length() < gemTarget.length()
                    || !rooms[gem.getGemRooms()[x][0]][gem.getGemRooms()[x][1]].getFloor()[gem.getGemCoords()[x][0]][gem.getGemCoords()[x][1]].getItems()[2].getId().substring(0,gemTarget.length()).equals(gemTarget))){
                addGem(x);
            }
        }

    }

    //adds the gem to its original position
    private void addGem(int type){
        Item gem = new Item(gemTarget+"1","null",false);

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                updateOpponent("/updRemove:"+rooms[gem.getGemRooms()[type][0]][gem.getGemRooms()[type][1]].getFloor()[gem.getGemCoords()[type][0]+x][gem.getGemCoords()[type][1]+y].getItems()[3].getId()+
                        "X"+(gem.getGemCoords()[type][0]+x)+"Y"+(gem.getGemCoords()[type][1]+y)+"x"+gem.getGemRooms()[type][0]+"y"+gem.getGemRooms()[type][1]);
                updateOpponent("/updPlace:"+gemTarget+((x)*3+(y)+1)+"X"+(gem.getGemCoords()[type][0]+x)+"Y"+(gem.getGemCoords()[type][1]+y)+"x"+gem.getGemRooms()[type][0]+"y"+gem.getGemRooms()[type][1]);
                rooms[gem.getGemRooms()[type][0]][gem.getGemRooms()[type][1]].getFloor()[gem.getGemCoords()[type][0]+x][gem.getGemCoords()[type][1]+y].removeStructure(3);
                rooms[gem.getGemRooms()[type][0]][gem.getGemRooms()[type][1]].getFloor()[gem.getGemCoords()[type][0]+x][gem.getGemCoords()[type][1]+y].setStructure(gemTarget+((x)*3+(y)+1),"null");
            }
        }
        player.removeItem(gemTarget,1);
        updateMapRoom(gem.getGemRooms()[type][0],gem.getGemRooms()[type][1]);
    }

}
