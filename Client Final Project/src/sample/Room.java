package sample;

public class Room {

    private Layer[][] floor;

    private int rNumber; //the number that represents which room is to be initialized
    private boolean imagesLoaded = false;

    //initializes the necessary variables and calls the method that initializes the room
    public Room(int rNumber){

        floor = new Layer[13][13];

        this.rNumber = rNumber;
        initializeFloors();
        initializeRooms();
    }

    public boolean imagesLoaded() {
        return imagesLoaded;
    }

    public void loadImages(Character player) {
        for (Layer[] layer1: floor) {
            for (Layer layer2: layer1) {
                layer2.loadImages(player);
            }
        }
        imagesLoaded = true;
    }

    public void initializeFloors(){
        for (int x = 0; x < floor.length; x++) {
            for (int y = 0; y < floor.length; y++) {
                floor[x][y] = new Layer();
            }
        }
    }

    public Layer[][] getFloor() {
        return floor;
    }

    //this initializes the appropriate room
    private void initializeRooms(){
        setBorder();
        if(rNumber == 1){
            setRec(1,1,11,11, "floor1");
            setEntrance(3);
            setEntrance(2);
            setObject(5,5,"sapphire");
        }
        else if(rNumber == 2){
            setRec(1,1,11,11, "floor1");
            setEntrance(0);
            setEntrance(3);
            setEntrance(2);
            setObject(5,5,"goldGen");
        }
        else if(rNumber == 3){
            setEntrance(0);
            setEntrance(3);
            setRec(1,1,11,11, "void");
        }
        else if(rNumber == 4){
            setRec(1,1,11,11, "floor1");
            setEntrance(3);
            setEntrance(2);
            setObject(5,5,"chest");
        }
        else if(rNumber == 5){
            setRec(1,1,3,11, "floor1");
            setRec(9,4,8,3, "floor1");
            setRec(1,9,3,3, "floor1");
            setEntrance(0);
            setEntrance(3);
            setRec(1,4,5,8, "void");
            setRec(4,9,3,5, "void");
            setObject(1,9,"ruby");
        }
        else if(rNumber == 6){
            setRec(1,1,11,11, "floor1");
            setEntrance(1);
            setEntrance(3);
            setObject(5,8,"shop");
            setObject(2,2,"rHolder");
            setObject(8,2,"rHolder");
        }
        else if(rNumber == 7){
            setRec(1,1,7,11, "floor1");
            setEntrance(1);
            setEntrance(3);
            setEntrance(2);
            setRec(1,8,4,11, "lava");
        }
        else if(rNumber == 8){
            setRec(1,1,11,2, "floor1");
            setRec(3,1,2,7, "floor1");
            setRec(10,1,11,2, "floor1");
            setRec(1,10,2,9, "floor1");
            setRec(5,5,3,3, "floor1");
            setRec(3,3,2,5,"lava");
            setRec(8,3,5,2,"lava");
            setRec(3,5,5,2,"lava");
            setRec(5,8,2,5,"lava");
            setObject(5,5,"emeraldGen");
            for (int x = 0; x < 4; x++) {
                setEntrance(x);
            }
        }
        else if(rNumber == 9){
            setRec(1,5,7,11, "floor1");
            setEntrance(1);
            setEntrance(0);
            setEntrance(3);
            setRec(1,1,4,11, "lava");
        }
        else if(rNumber == 10){
            setRec(1,1,11,11, "floor1");
            setEntrance(1);
            setEntrance(3);
            setObject(5,2,"shop");
            setObject(2,8, "sHolder");
            setObject(8,8, "sHolder");
        }
        else if(rNumber == 11){
            setRec(1,1,11,3, "floor1");
            setRec(4,9,3,8, "floor1");
            setRec(9,1,3,3, "floor1");
            setEntrance(1);
            setEntrance(2);
            setRec(4,1,8,5, "void");
            setRec(9,4,5,3, "void");
            setObject(9,1,"sapphire");
        }
        else if(rNumber == 12){
            setRec(1,1,11,11, "floor1");
            setEntrance(1);
            setEntrance(0);
            setObject(5,5,"chest");
        }
        else if(rNumber == 13){
            setRec(1,1,11,11, "void");
            setEntrance(1);
            setEntrance(2);
        }
        else if(rNumber == 14){
            setRec(1,1,11,11, "floor1");
            setEntrance(1);
            setEntrance(0);
            setEntrance(2);
            setObject(5,5,"goldGen");
        }
        else if(rNumber == 15){
            setRec(1,1,11,11, "floor1");
            setEntrance(1);
            setEntrance(0);
            setObject(5,5,"ruby");
        }
    }

    //when called this adds a border around the room
    private void setBorder(){
        int[] xCoefficient = new int[]{1,0,-1,0};
        int[] yCoefficient = new int[]{0,1,0,-1};
        int startPos = 0;
        for(int i=0;i<4;i++){
            for (int j = 0; j < 13; j++) {
                if(i>=2){
                    startPos = 12;
                }
                floor[startPos+j*xCoefficient[i]][startPos+j*yCoefficient[i]].setStructure("border","null");
            }
        }
    }

    //when called, this adds a entrance to the room in the specified location. Pos can be 1,2,3, or 4. 1 is top wall, 2 is left wall, 3 is bottom wall, 3 is right wall
    private void setEntrance(int pos){
        int[] xCoefficient = new int[]{1,0,1,0};
        int[] yCoefficient = new int[]{0,1,0,1};
        int[] xPos = new int[]{5,0,5,12};
        int[] yPos = new int[]{0,5,12,5};
        for (int i = 0; i < 3; i++) {
            final int x = xPos[pos] + i * xCoefficient[pos];
            int y = yPos[pos] + i * yCoefficient[pos];
            floor[x][y].clear();
            floor[x][y].setStructure("floor1","null");

        }
    }

    private void setObject(int startX,int startY,String id){ //sets and object in 3 by 3 area. startX and startY are the starting coordinates. id the the id of the object being set
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                floor[startX+x][startY+y].setStructure(id+((x)*3+(y)+1),"null");
            }
        }
    }

    private void setRec(int startX, int startY, int length, int width, String itemID){//sets and object in length by width area. startX and startY are the starting coordinates. id the the id of the structure being set
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {
                floor[startX+x][startY+y].setStructure(itemID,"null");
            }
        }
    }


}