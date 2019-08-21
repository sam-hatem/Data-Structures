package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;
//import java.util.Random;

public class MapDraw {
    // a drawer object
    TETile[][] world;
    WeightedQuickUnionUF worldSet;

    MapDraw(TETile[][] game, WeightedQuickUnionUF worldSet) {
        this.world = game;
        this.worldSet = worldSet;
    }
    // draw a list of positions
    public void drawPositions(ArrayList<Position> floorPositions) {
        for (Position p : floorPositions) {
            //System.out.println(p.x + " " + p.y);
            world[p.x][p.y] = Tileset.FLOOR;
        }

        cleanUpWalls();

    }
    // clean up excess wall tiles
    private void cleanUpWalls() {
        for (int x = 0; x < world.length; x++) {
            for (int y = 0; y < world[0].length; y++) {
                if (!(adjFloor(x, y))) {
                    world[x][y] = Tileset.NOTHING;

                }
            }
        }

    }

    private boolean isFloor(int x, int y) {
        // returns whether a position is a floor
        if (x < 0 || x == Engine.WIDTH || y < 0 || y == Engine.HEIGHT) {
            return false;
        } else {
            return world[x][y] == Tileset.FLOOR;
        }
    }

    private boolean adjFloor(int x, int y) {
        // returns whether a position is adjacent to a floor
        if (isFloor(x, y)) {
            return true;
        } else {
            return (isFloor(x - 1, y)
                    || isFloor(x + 1, y)
                    || isFloor(x, y + 1)
                    || isFloor(x, y - 1)
                    || isFloor(x + 1, y + 1)
                    || isFloor(x - 1, y + 1)
                    || isFloor(x - 1, y - 1)
                    || isFloor(x + 1, y - 1));
        }
    }
}
