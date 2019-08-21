package byow.Core;

import static byow.Core.MapGenerator.*;

//import byow.TileEngine.TERenderer;
//import byow.TileEngine.TETile;
//import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;


public class Position implements Serializable {

    int x;
    int y;
    int coord;

    public Position(int xPos, int yPos) {
        x = xPos;
        y = yPos;
        coord = xPos + Engine.WIDTH * yPos;
    }

    //create a new random position

    public static Position putInRandom(Random rand)  {
        // new random position between 2 and bound - 2
        int x  = Math.max(rand.nextInt(WIDTH - 2), 2);
        int y = Math.max(rand.nextInt(HEIGHT - 2), 2);
        return new Position(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != Position.class) {
            return false;
        }
        Position pO = (Position) o;
        if (pO.x == this.x && this.y == pO.y) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.x * 100 + this.y;
    }

    @Override
    public String toString() {
        return "Position (" + x + "," + y + ")";
    }

}
