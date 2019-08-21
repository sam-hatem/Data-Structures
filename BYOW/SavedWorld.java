package byow.Core;

//import byow.TileEngine.TETile;

import java.io.Serializable;
import java.util.ArrayList;
//import java.util.HashMap;

public class SavedWorld implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    ArrayList<Character> actHist;
    Character[][] saveWorld;
    Position avatarStart;
    Position goalPos;
    Integer breathCap;
    Character[][] ogWorld;
    long seed;
    SavedWorld(Character[][] world, ArrayList<Character> aHist, Position aStart,
               Position gP, Integer bC, Character[][] ogFrame, long s) {
        /*
        this.saveWorld = new Character[world.length][world[0].length];
        for (int x = 0; x < world.length; x++) {
            for (int y = 0; y < world[0].length; y++) {
                saveWorld[x][y] = world[x][y].character();
            }
        }*/
        saveWorld = world;
        actHist = aHist;
        avatarStart = aStart;
        goalPos = gP;
        breathCap = bC;
        ogWorld = ogFrame;
        seed = s;
    }
}
