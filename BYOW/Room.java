package byow.Core;

/*import byow.Core.Engine;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.lab12.HexWorld;*/
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;
import java.util.Random;


public class Room {

    private static Random RANDOM;
    private static int WIDTH = Engine.WIDTH;
    private static int HEIGHT = Engine.HEIGHT;
    private static final int ROOMMINDIM = 2;
    WeightedQuickUnionUF worldSet;
    //Position startPos;
    int length;
    int size;
    int width;
    Position startPos;
    ArrayList<Position> roomPositions;
    //HashMap<Position, Integer> posToCoord;


    public Room(Random r, WeightedQuickUnionUF worldSet) {
        this.RANDOM = r;
        //this.posToCoord = posToCoord;
        this.roomPositions = new ArrayList<>();
        this.worldSet = worldSet;
        // room l and w must be at least 2
        this.length = RANDOM.nextInt(10) + ROOMMINDIM;
        this.width = RANDOM.nextInt(10) + ROOMMINDIM;
        this.size = this.length * this.width;
        this.generate();
        //this.roomPositions.add(Position.putInRandom(RANDOM));
    }

    public void generate() {
        this.startPos = (Position.putInRandom(RANDOM));
        //System.out.println("generating from " + this);
        /*System.out.println("ROOM GENERATION");
        System.out.println("startPos: " + startPos.x + " " + startPos.y);
        System.out.println("Room dim: " + width + " " + length);
        System.out.println("World dim: " + this.WIDTH + " " + this.HEIGHT);*/
        int startCoord = startPos.coord;
        for (int l = 0; l < length; l++) {
            int nextX = startPos.x + l;
            for (int w = 0; w < width; w++) {
                int nextY = startPos.y + w;
                Position nextPos = new Position(nextX, nextY);
                int nextCoord = nextPos.coord;
                //System.out.println("trying: " + nextPos);
                if (MapGenerator.validatePosition(nextPos)) {
                    //System.out.println("validated");
                    this.roomPositions.add(nextPos);
                    this.worldSet.union(nextCoord, startCoord);
                }
            }
        }
    }

    public ArrayList<Position> getPositions() {
        return this.roomPositions;
    }

    @Override
    public String toString() {
        return "Room (x, y): (" + this.startPos.x + "," + this.startPos.y + ")"
                + " (length,width): (" + this.length + "," + this.width + ")";
    }

}
