package byow.Core;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.util.ArrayList;
import java.util.Random;

public class MapGenerator {

    static final int WIDTH = Engine.WIDTH;
    static final int HEIGHT = Engine.HEIGHT;
    private static final int MINROOMNUM = 5;
    private static final int MINHLEN = 3;
    Random RANDOM;
    // List of Positions generated to be drawn later
    ArrayList<Position> genPos = new ArrayList<>();
    // Disjoint sets for connectivity
    WeightedQuickUnionUF worldSet;
    ArrayList<Integer> roomInts;
    // Hashmaps
    //HashMap<Position, Integer> posToCoord;


    //static final long SEED = 676;
    //static Random RANDOM = new Random(SEED);

    MapGenerator(Random r, WeightedQuickUnionUF worldSet) {
        this.RANDOM = r;
        this.worldSet = worldSet;
        this.roomInts = new ArrayList<>();
        //this.posToCoord = posToCoord;
    }

    // ASSUME: each Room object has a list of positions (previously validated)
    // Each Room takes RANDOM then generates its own positions and validates (with posToCoord)
    // Room will also have getPositions() which returns an arraylist of positions it occupies
    // Room does quick find unioning in its constructor
    public void generateRooms() {
        // create rooms
        int nRooms = RANDOM.nextInt(10) + MINROOMNUM;
        for (int n = 0; n < nRooms; n++) {
            Room newRoom = new Room(RANDOM, worldSet);
            ArrayList<Position> newRoomPositions = newRoom.getPositions();
            //System.out.println(newRoom);
            //System.out.println(newRoomPositions.size());
            genPos.addAll(newRoomPositions);
            int newRoomCoord = newRoomPositions.get(0).coord;
            this.roomInts.add(newRoomCoord);
        }
    }

    public static boolean validatePosition(Position p) {
        //return (!(p.x < 0 || p.y < 0 || p.x >= WIDTH || p.y >= HEIGHT));
        return (!(p.x < 2 || p.y < 2 || p.x >= (WIDTH - 2) || p.y >= (HEIGHT - 2)));
    }

    public void generateHallways() {
        //System.out.println("generateHallways");
        ArrayList<Position> hallwayPositions = new ArrayList<>();
        while (!(allRoomsConnected())) {
            //System.out.println("--New Hallway--");
            // choose initial floor space to start hallway
            int startIndex = RANDOM.nextInt(genPos.size());
            Position startPos = genPos.get(startIndex);
            //System.out.println("startPos: " + startPos.x + " " + startPos.y);
            int direction = RANDOM.nextInt(4);
            int length = Math.max(RANDOM.nextInt(WIDTH / 2), MINHLEN); // hallway at least length 3
            //System.out.println("length: " + length);
            // choose addition based on direction
            int addX = 0;
            int addY = 0;
            switch (direction) {
                case 0:
                    addX += 1;
                    break;
                case 1:
                    addY += 1;
                    break;
                case 2:
                    addX -= 1;
                    break;
                case 3:
                    addY -= 1;
                    break;
                default:
                    break;
            }
            //System.out.println("Direction: " + addX + " " + addY);
            // build hallway and union
            int oldCoord = startPos.coord;
            for (int i = 0; i < length; i++) {
                Position nextPos = new Position(startPos.x + addX, startPos.y + addY);
                int nextCoord = nextPos.coord;
                if (!(validatePosition(nextPos))) {
                    //System.out.println("!! BREAK hallway");
                    break;
                } else {
                    //System.out.println("hallway Pos: " + nextPos.x + " " + nextPos.y);
                    hallwayPositions.add(nextPos);
                    worldSet.union(nextCoord, oldCoord);
                    oldCoord = nextCoord;
                    startPos = nextPos;
                }
            }
            //System.out.println("----");
        }
        genPos.addAll(hallwayPositions);
    }

    public void generate() {
        this.generateRooms();
        this.generateHallways();
    }

    public boolean allRoomsConnected() {
        for (int i : this.roomInts) {
            for (int j : this.roomInts) {
                if (!(this.worldSet.connected(i, j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
