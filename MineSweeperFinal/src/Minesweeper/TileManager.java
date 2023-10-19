/**
 * Author: Kenzie Leckenby
 * Date: Dec 28, 2022
 * Description:
 * The goal with this new tile manager is to cut down on clutter and make the code more efficient
 */

package Minesweeper;

import java.util.ArrayList;
import java.util.Random;

public class TileManager {

    // < Arrays to store map info >
    public static Tile[] map; // Stores tile values
    public static int[] bombs; // Stores the array location of bombs
    public static ArrayList<Integer> foundTiles; // Stores the array location of found tiles

    // < Current Game Settings >
    public static int totalBombs;
    public static int rowLength;

    public TileManager(String mapSize) {
        if (mapSize.equals("s")) {
            this.map = new Tile[98];
            this.rowLength = 14;
        }
        else if (mapSize.equals("m")) {
            this.map = new Tile[200];
            this.rowLength = 20;
        }
        else if (mapSize.equals("l")) {
            this.map = new Tile[450];
            this.rowLength = 30;
        }
        else {
            System.out.println("* Minesweeper.TileManager Constructor is invalid *");
            System.out.println("* Using a default map size *");
            this.map = new Tile[200];
            this.rowLength = 20;
        }

        // Instantiating the map array with default tiles
        for (int i = 0; i < map.length; i++) {
            this.map[i] = new Tile();
        }
        this.totalBombs = (int)(this.map.length * .20f);
    }

    public void createMap(int firstClick) {
        bombs = new int[this.totalBombs]; // Instantiates the array
        foundTiles = new ArrayList<>();
        Random rand = new Random();
        Adjacency adj = new Adjacency();

        for (int i = 0; i < this.totalBombs; i++) {
            int randLoc = rand.nextInt(map.length);
            if (adj.validBombLocation(randLoc, firstClick, this.rowLength)) {
                bombs[i] = randLoc;
                map[randLoc].setTileVal(-1);
            }
            else {
                i--;
            }
        }

        adj.mapValues(this.rowLength);
    }

    public void mapInteraction(int click) {
        Adjacency adj = new Adjacency();
        foundTiles.add(click);
        if (this.map[click].getTileVal() == 0){
            adj.adjacentZeros(click);
        }
    }

    public int getTilesLeft() {
        return (map.length - totalBombs) - foundTiles.size();
    }

    public int getFoundTileVal(int val) {
        return map[foundTiles.get(val)].getTileVal();
    }

    public boolean getIfFound(int val) {
        return foundTiles.contains(val);
    }

    public int getFoundTilePos(int val) {
        return foundTiles.get(val);
    }

    public int getTilesFound() {
        return foundTiles.size();
    }

    public int getTileVal(int position) {
        return map[position].getTileVal();
    }

    public boolean getTileFlag(int position) {
        return this.map[position].getFlag();
    }

    public void setTileFlag(int position, boolean val) {
        this.map[position].setFlag(val);
    }
}
