package byow.Core;

import byow.TileEngine.TERenderer;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;


import java.util.ArrayList;
import java.util.Random;

import java.awt.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
//import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.introcs.StdDraw;




public class Engine {

    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int BREATHS = 25;
    public static final int NUMGOALS = 5;
    TETile[][] finalWorldFrame;
    TETile[][] ogFinalWorldFrame;
    WeightedQuickUnionUF worldSet = new WeightedQuickUnionUF(WIDTH * HEIGHT);
    ArrayList<Integer> roomInts = new ArrayList<>();
    ArrayList<Character> actionHistory = new ArrayList<>();
    Position avatarStart;
    Position goalPos;
    Integer breathCapacity;
    //generator and drawer
    MapGenerator mapGen;
    MapDraw mapDraw;


    //current position
    private Position current;
    //seed


    //initialize
    private long seed;
    private Random RANDOM = new Random(seed);

    //mouse
    private int mouseX;
    private int mouseY;


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */


    public void interactWithKeyboard() {


        //initialize
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        finalWorldFrame = new TETile[WIDTH][HEIGHT];

        //menu
        menu();


    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // (!) Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

         ter.initialize(WIDTH, HEIGHT);
        input = input.toUpperCase();
        finalWorldFrame = new TETile[WIDTH][HEIGHT];
        char firstChar = Character.toUpperCase(input.charAt(0));
        String remainingString = "";

        if (firstChar == 'N') {
            if (input.equals("NS")) {
                menu();
            }
            // check for remaining string
            String[] strSplit = input.split("S", 2);
            if (strSplit.length > 1) {
                if (!(strSplit[1].equals(""))) {
                    // there are commands after S
                    remainingString = strSplit[1];
                }
            }
            seed = getSeed(input);
            RANDOM = new Random(seed);
            //start  and draw the world
            mapGen = new MapGenerator(RANDOM, worldSet);
            mapDraw = new MapDraw(finalWorldFrame, worldSet);

            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    finalWorldFrame[i][j] = Tileset.WALL;
                }
            }
            mapGen.generate();
            ArrayList<Position> allPos = mapGen.genPos;

            mapDraw.drawPositions(allPos);
            //making the locked door for the winning condition
            int numGoals = 0;
            while (numGoals < NUMGOALS) {
                int goalIndex = RANDOM.nextInt(allPos.size());
                Position goal = allPos.get(goalIndex);
                this.goalPos = goal;
                if (!(finalWorldFrame[goal.x][goal.y].equals(Tileset.WATER))) {
                    finalWorldFrame[goal.x][goal.y] = Tileset.WATER;
                    numGoals += 1;
                }
            }
            this.ogFinalWorldFrame = copyTE(finalWorldFrame);
            startPlayer();
        } else if (firstChar == 'L') {
            // check for remaining string
            String[] strSplit = input.split("L", 2);
            if (strSplit.length > 1) {
                if (!(strSplit[1].equals(""))) {
                    // there are commands after S
                    remainingString = strSplit[1];
                }
            }
            load();
            ter.renderFrame(finalWorldFrame);
        } else if (firstChar == 'R') {
            replay();
        }

        if (remainingString.equals("")) {

            startPlaying();
        } else {
            startPlaying(remainingString);
        }

        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }

    public long getSeed(String input) {
        long s;
        if (input.toLowerCase().contains("n") && input.toLowerCase().contains("s")) {
            // (!) Check that 1st char is N and last is S
            int start = input.toLowerCase().indexOf("n") + 1;
            int end = input.toLowerCase().indexOf("s");
            s = Long.parseLong(input.substring(start, end));
            return s;
        } else {
            throw new RuntimeException();
        }
    }


    private void startPlayer() {
        // choose a random starting position
        ArrayList<Position> allPos = mapGen.genPos;
        int randInt = RANDOM.nextInt(allPos.size());
        current = allPos.get(randInt);
        this.avatarStart = new Position(current.x, current.y);
        finalWorldFrame[current.x][current.y] = Tileset.AVATAR;
        ter.renderFrame(finalWorldFrame);
        this.breathCapacity = BREATHS;
    }

    private void showLore() {

        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setFont(new Font("Roman", Font.BOLD, 15));


        String text = "It was the apocalypse. the year 3102, after \n " +
                "humans ruined the earth with their carbon waste and the last remaining\n" +
                "being was the @; A very powerful two-dimensional mutation. \n " +
                "due to the catastrophe \n" +
                "very little drinkable water remained \n" +
                " and it was very hard to obtain. \n" +
                "Your goal is to obtain the scarce water \n" +
                " before running out of breath \n " +
                "and while avoiding the mountain demons which attempt to aggressively murder you \n";
        int i = 0;

        while (i <= 5) {
            for (String line : text.split("\n")) {
                StdDraw.text(WIDTH / 2, HEIGHT/2 - i, line);
                i++;

            }

    }


        StdDraw.show();
        StdDraw.setFont(new Font("Roman", Font.BOLD, 15));
}




    private void quit() {
        System.exit(0);
    }

    private void winTheGame() {

        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setFont(new Font("Roman", Font.BOLD, 75));

        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "you win bro!");
        StdDraw.show();
        StdDraw.setFont(new Font("Roman", Font.BOLD, 10));
        StdDraw.pause(1000);
        save();
        quit();
    }

    private void loseTheGame() {

        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setFont(new Font("Roman", Font.BOLD, 75));

        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "you lose bro :(");
        StdDraw.show();
        StdDraw.setFont(new Font("Roman", Font.BOLD, 10));
        StdDraw.pause(1000);

        save();
         quit();
    }


    private void menu() {

        StdDraw.clear(new Color(0, 0, 0));
        //game  title
        StdDraw.setFont(new Font("Roman", Font.BOLD, 75));
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "CS61B: THE GAME");

        //menu options
        StdDraw.setFont(new Font("Roman", Font.BOLD, 30));
        StdDraw.text(WIDTH / 2, (HEIGHT / 2 - 3), "New World(N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "Load Game(L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 8, "Quit(Q)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 10, "Replay(R)");
        StdDraw.text(WIDTH / 2, HEIGHT /2 - 12, "Background(B)");

        StdDraw.show();
        StdDraw.setFont(new Font("Roman", Font.BOLD, 14));

        while (true) {


            //Mouse interaction
            int y1 = (int)StdDraw.mouseY();
            if (StdDraw.isMousePressed()) {

                if ( y1 == HEIGHT / 2 - 3) {
                    inputSeed();
                    break;
                }else if ( y1 == HEIGHT / 2 - 5) {
                    interactWithInputString("L");
                    break;
                }else if (y1 == HEIGHT / 2 - 8) {
                    quit();
                    break;
                }else if (y1 == HEIGHT / 2 - 10) {
                    interactWithInputString("R");
                    break;
                } else if (y1 == HEIGHT /2 - 12) {
                    showLore();
                    break;
                }

                }

            if (StdDraw.hasNextKeyTyped()) {
                char h = Character.toUpperCase(StdDraw.nextKeyTyped());
                switch (h) {
                    case 'N':
                        inputSeed();
                        break;

                    case 'R':
                        interactWithInputString("R");
                        break;

                    case 'L':
                        interactWithInputString("L");
                        break;

                    case 'Q':
                        quit();
                        break;

                    case 'B':
                        showLore();
                        break;

                    default:
                        menu();
                        break;
                }
            }
        }

    }


    private TETile convertCharToTile(char c) {
        TETile conv = Tileset.NOTHING;
        switch (c) {
            case '@':
                conv = Tileset.AVATAR;
                break;
            case '#':
                conv = Tileset.WALL;
                break;
            case '·':
                conv = Tileset.FLOOR;
                break;
            case '"':
                conv = Tileset.GRASS;
                break;
            case '≈':
                conv = Tileset.WATER;
                break;
            case '❀':
                conv = Tileset.FLOWER;
                break;
            case '█':
                conv = Tileset.WATER;
                break;
            case '▢':
                conv = Tileset.UNLOCKED_DOOR;
                break;
            case '▒':
                conv = Tileset.SAND;
                break;
            case '▲':
                conv = Tileset.MOUNTAIN;
                break;
            case '♠':
                conv = Tileset.TREE;
                break;
            default:
                conv = Tileset.NOTHING;
                break;
        }
        return conv;
    }

    private TETile[][] copyTE(TETile[][] mat) {
        TETile[][] nMat = new TETile[mat.length][mat[0].length];
        for (int x = 0; x < mat.length; x++) {
            for (int y = 0; y < mat[0].length; y++) {
                nMat[x][y] = mat[x][y];
            }
        }
        return nMat;
    }

    private void replay() {
        File f = new File("./save_data.txt");
        if (f.exists()) {
            try {
                // get SavedWorld object
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                SavedWorld s = (SavedWorld) os.readObject();
                this.avatarStart = new Position(s.avatarStart.x, s.avatarStart.y);
                this.current = new Position(s.avatarStart.x, s.avatarStart.y);
                this.goalPos = s.goalPos;
                this.breathCapacity = BREATHS;
                this.ogFinalWorldFrame = convCharToTE(s.ogWorld, false);
                this.seed = s.seed;
                this.finalWorldFrame = copyTE(ogFinalWorldFrame); //this.ogFinalWorldFrame;
                // CHECK FOR MOUNTAINS
                // play back action history
                String acts = "";
                for (char c : s.actHist) {
                    if (c == 'A' || c == 'S' || c == 'W' || c == 'D') {
                        //acts += c;
                        startPlaying(Character.toString(c));
                        //System.out.println(this.breathCapacity);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
    }

    private TETile[][] convCharToTE(Character[][] charMat, boolean setAvatar) {
        TETile[][] tMat = new TETile[charMat.length][charMat[0].length];
        for (int x = 0; x < tMat.length; x++) {
            for (int y = 0; y < tMat[0].length; y++) {
                TETile converted = convertCharToTile(charMat[x][y]);
                if (converted == Tileset.AVATAR) {
                    if (setAvatar) {
                        this.current = new Position(x, y);
                    } else {
                        converted = Tileset.FLOOR;
                    }
                }
                tMat[x][y] = converted;
            }
        }
        return tMat;
    }

    private void load() {

        File f = new File("./save_data.txt");
        if (f.exists()) {
            try {
                // get SavedWorld object
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                SavedWorld s = (SavedWorld) os.readObject();
                // convert SavedWorld to our finalWorldFrame
                this.actionHistory = s.actHist;
                this.avatarStart = s.avatarStart;
                this.goalPos = s.goalPos;
                this.breathCapacity = s.breathCap;
                this.ogFinalWorldFrame = convCharToTE(s.ogWorld, false);
                this.finalWorldFrame = convCharToTE(s.saveWorld, true);
                this.seed = s.seed;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
    }


    private Character[][] convTEtoChar(TETile[][] mat) {
        Character[][] newMat = new Character[mat.length][mat[0].length];
        for (int x = 0; x < mat.length; x++) {
            for (int y = 0; y < mat[0].length; y++) {
                newMat[x][y] = mat[x][y].character();
            }
        }
        return newMat;
    }

    private void save() {

        // create and populate savedworld object
        Character[][] ogCharFrame = convTEtoChar(this.ogFinalWorldFrame);
        Character[][] finalCharFrame = convTEtoChar(this.finalWorldFrame);
        SavedWorld s = new SavedWorld(finalCharFrame, this.actionHistory, this.avatarStart,
                this.goalPos, this.breathCapacity, ogCharFrame, seed);
        //s.actHist = this.actionHistory;

        // write to file
        File f = new File("./save_data.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(s);
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }

    }

    private void inputSeed() {

        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setFont(new Font("Roman", Font.BOLD, 85));
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.text(WIDTH/2, HEIGHT /2 + 5, "Enter random seed number: ");
        StdDraw.show();
        //StdDraw.setFont(new Font("Roman", Font.BOLD, 12));
        StringBuilder builder = new StringBuilder();
        boolean validInput = true;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                // get next char
                char cha = StdDraw.nextKeyTyped();
                // make char uppercase if letter
                if (Character.isLetter(cha)) {
                    cha = Character.toUpperCase(cha);
                    if (cha == 'S') {
                        // end seed input
                        break;
                    } else {
                        // give second chance
                        menu();
                        validInput = false;
                        break;
                    }
                } else {
                    // if not letter
                    if (!(Character.isDigit(cha))) {
                        menu();
                        validInput = false;
                        break;
                    }
                }
                // append char to input string
                builder.append(cha);
                // draw seed to screen
                StdDraw.clear(new Color(0, 0, 0));
                StdDraw.setFont(new Font("Roman", Font.BOLD, 85));
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.text(WIDTH/2, HEIGHT /2 + 5, "Enter random seed number: ");
                StdDraw.text(WIDTH/2, HEIGHT /2 - 5, builder.toString());
                StdDraw.show();
            }
        }
        builder.insert(0, "N");
        builder.append("S");
        if (validInput) {
            System.out.println("VALID INPUT");
            interactWithInputString(builder.toString());
        } else {
            menu();
        }


    }


    private void status() {

        StdDraw.setFont(new Font("Roman", Font.BOLD, 20));
        if (breathCapacity <= 5) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.text(5, HEIGHT - 3, "(!) Air: " + breathCapacity + " (!)");
        } else if (breathCapacity <= 15) {
            StdDraw.setPenColor(StdDraw.YELLOW);
            StdDraw.text(5, HEIGHT - 3, "Air: " + breathCapacity);
        } else {
            StdDraw.setPenColor(StdDraw.GREEN);
            StdDraw.text(5, HEIGHT - 3, "Air: " + breathCapacity);
        }
        StdDraw.setFont((new Font("Roman", Font.BOLD, 10)));
        StdDraw.show();
        if (mouseX == (int) StdDraw.mouseX() && mouseY == (int) StdDraw.mouseY()) {
            return;
        } else {
            ter.renderFrame(finalWorldFrame);
            mouseX = (int)StdDraw.mouseX();
            mouseY = (int)StdDraw.mouseY();

            if (mouseY == HEIGHT) {
                mouseY --;

            }

            if (mouseX == WIDTH) {
                mouseX --;
            }

            StdDraw.setFont(new Font("Roman", Font.BOLD, 20));
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.text(5, HEIGHT - 1, finalWorldFrame[mouseX][mouseY].description());
            StdDraw.show();
            StdDraw.setFont((new Font("Roman", Font.BOLD, 10)));
        }

    }

    private void checkLose() {

        if (this.breathCapacity <= 0) {
            loseTheGame();
        }
        // check if enclosed
        // NEXT: end game if equals WALL or MOUNTAIN
        if (finalWorldFrame[current.x + 1][current.y].equals(Tileset.WALL)
                && finalWorldFrame[current.x - 1][current.y].equals(Tileset.WALL)
                && finalWorldFrame[current.x][current.y + 1].equals(Tileset.WALL)
                && finalWorldFrame[current.x][current.y - 1].equals(Tileset.WALL)) {
            loseTheGame();
        }

    }

    private void stepUpdate() {

        // selects random tile from finalworldframe
        // if wall or water, select new tile
        // if floor, make wall
        Random randFloor = new Random(seed);
        TETile tileChosen = Tileset.AVATAR;
        // make random floor tile a barrier
        while (!(tileChosen.equals(Tileset.FLOOR))) {
            int randX = randFloor.nextInt(finalWorldFrame.length);
            int randY = randFloor.nextInt(finalWorldFrame[0].length);
            if (finalWorldFrame[randX][randY].equals(Tileset.FLOOR)) {
                finalWorldFrame[randX][randY] = Tileset.MOUNTAIN;
                break;
            }
        }
        // decrease breath capacity
        this.breathCapacity -= 1;
        checkLose();
        //System.out.println(breathCapacity);

    }


    private void startPlaying() {
        checkLose();
        ter.renderFrame(finalWorldFrame);
        char lastc = 'n';
        while (true) {
            // display player's status
            status();
            // get key input
            if (StdDraw.hasNextKeyTyped()) {
                // PRINT ACTION HISTORY
                //System.out.println(this.actionHistory);
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                this.actionHistory.add(c);
                //System.out.println(c);
                switch (c) {
                    case 'Q':
                        //System.out.println(lastc);
                        if (lastc == ':') {
                            //System.out.println("QUIT");
                            save();
                            quit();
                        }
                        break;

                    case 'W':
                        stepUpdate();
                        if (finalWorldFrame[current.x][current.y + 1] == Tileset.WALL) {
                            continue;
                        } else if (finalWorldFrame[current.x][current.y + 1]
                                == Tileset.WATER) {
                            winTheGame();
                        } else {
                            finalWorldFrame[current.x][current.y] = Tileset.FLOOR;
                            current.y++;
                            finalWorldFrame[current.x][current.y] = Tileset.AVATAR;
                            ter.renderFrame(finalWorldFrame);
                        }
                        break;

                    case 'S':
                        stepUpdate();
                        if (finalWorldFrame[current.x][current.y - 1] == Tileset.WALL) {
                            continue;
                        } else if (finalWorldFrame[current.x][current.y - 1]
                                == Tileset.WATER) {
                            winTheGame();
                        } else {
                            finalWorldFrame[current.x][current.y] = Tileset.FLOOR;
                            current.y--;
                            finalWorldFrame[current.x][current.y] = Tileset.AVATAR;
                            ter.renderFrame(finalWorldFrame);

                        }
                        break;

                    case 'A':
                        stepUpdate();
                        if (finalWorldFrame[current.x - 1][current.y] == Tileset.WALL) {
                            continue;
                        } else if (finalWorldFrame[current.x - 1][current.y]
                                == Tileset.WATER) {
                            winTheGame();
                        } else {
                            finalWorldFrame[current.x][current.y] = Tileset.FLOOR;
                            current.x--;
                            finalWorldFrame[current.x][current.y] = Tileset.AVATAR;
                            ter.renderFrame(finalWorldFrame);
                        }
                        break;
                    case 'D':
                        stepUpdate();
                        if (finalWorldFrame[current.x + 1][current.y] == Tileset.WALL) {
                            continue;
                        } else if (finalWorldFrame[current.x + 1][current.y]
                                == Tileset.WATER) {
                            winTheGame();
                        } else {
                            finalWorldFrame[current.x][current.y] = Tileset.FLOOR;
                            current.x++;
                            finalWorldFrame[current.x][current.y] = Tileset.AVATAR;
                            ter.renderFrame(finalWorldFrame);
                        }
                        break;
                    default:
                        break;
                }
                lastc = c;
            }
        }

    }

    private void startPlaying(String remainingString) {
        checkLose();
        char lastc = 'n';
        for (int i = 0; i < remainingString.length(); i++) {
            char c = Character.toUpperCase(remainingString.charAt(i));
            this.actionHistory.add(c);
            switch (c) {
                case 'Q':
                    if (lastc == ':') {
                        save();
                        return;
                    }
                    quit();
                    break;
                case 'W':
                    stepUpdate();
                    if (finalWorldFrame[current.x][current.y + 1] == Tileset.WALL) {
                        continue;
                    } else if (finalWorldFrame[current.x][current.y + 1]
                            == Tileset.WATER) {
                        winTheGame();
                        continue;
                    } else {
                        finalWorldFrame[current.x][current.y] = Tileset.FLOOR;
                        current.y++;
                        finalWorldFrame[current.x][current.y] = Tileset.AVATAR;
                        ter.renderFrame(finalWorldFrame);
                    }
                    break;

                case 'S':
                    stepUpdate();
                    if (finalWorldFrame[current.x][current.y - 1] == Tileset.WALL) {
                        continue;
                    } else if (finalWorldFrame[current.x][current.y - 1]
                            == Tileset.WATER) {
                        winTheGame();
                        continue;

                    } else {
                        finalWorldFrame[current.x][current.y] = Tileset.FLOOR;
                        current.y--;
                        finalWorldFrame[current.x][current.y] = Tileset.AVATAR;
                        ter.renderFrame(finalWorldFrame);
                    }
                    break;
                case 'A':
                    stepUpdate();
                    if (finalWorldFrame[current.x - 1][current.y] == Tileset.WALL) {
                        continue;
                    } else if (finalWorldFrame[current.x - 1][current.y]
                            == Tileset.WATER) {
                        winTheGame();
                        continue;
                    } else {
                        finalWorldFrame[current.x][current.y] = Tileset.FLOOR;
                        current.x--;
                        finalWorldFrame[current.x][current.y] = Tileset.AVATAR;
                        ter.renderFrame(finalWorldFrame);
                    }
                    break;
                case 'D':
                    stepUpdate();
                    if (finalWorldFrame[current.x + 1][current.y] == Tileset.WALL) {
                        continue;
                    } else if (finalWorldFrame[current.x + 1][current.y]
                            == Tileset.WATER) {
                        winTheGame();
                        continue;

                    } else {
                        finalWorldFrame[current.x][current.y] = Tileset.FLOOR;
                        current.x++;
                        finalWorldFrame[current.x][current.y] = Tileset.AVATAR;
                        ter.renderFrame(finalWorldFrame);
                    }
                    break;
                default:
                    break;
            }
            lastc = c;
        }
    }


}














