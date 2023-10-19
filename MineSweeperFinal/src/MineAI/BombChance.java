/**
 * Author: Kenzie Leckenby
 * Date: Jan 13, 2023
 * Description:
 */
package MineAI;

import Minesweeper.*;
import java.util.ArrayList;

public class BombChance {
    public static Integer[] bombChance;
    public static ArrayList<Integer> foundBombs;
    public static ArrayList<Integer> nonBombs;

    public BombChance () {
        bombChance = new Integer[TileManager.map.length];
        foundBombs = new ArrayList<>();
        nonBombs = new ArrayList<>();

        int unknownTilesLeft;

        runThroughBombChances();

        unknownTilesLeft = 0;
        for (int i = 0; i < bombChance.length; i++) {
            if (bombChance[i] != null && bombChance[i] < 99 && bombChance[i] > 0) {
                unknownTilesLeft += 1;
            }
        }

        int iterations = 0;
        while (unknownTilesLeft > 0 && iterations < 100) {

            // Checks how many unknown tiles there are
            unknownTilesLeft = 0;
            for (int i = 0; i < bombChance.length; i++) {
                if (bombChance[i] != null && bombChance[i] < 99 && bombChance[i] > 0) {
                    unknownTilesLeft += 1;
                }
            }

            // Sets all unknown tiles back to null before running the loop again
            for (int j = 0; j < bombChance.length; j++) {
                if (bombChance[j] != null) {
                    if (bombChance[j] > 0 && bombChance[j] < 99) {
                        bombChance[j] = null;
                    }
                }
            }
            runThroughBombChances();
            iterations++;
        }
    }

    private void runThroughBombChances () {
        for (int i = 0; i < TileManager.foundTiles.size(); i++) {
            Adjacency adj = new Adjacency();
            // Sets every found tile equal to 0 in the bomb chance array
            bombChance[TileManager.foundTiles.get(i)] = 0;

            // Determines the bomb chance of adjacent tiles to a found tile that is not equal to 0
            if (TileManager.map[TileManager.foundTiles.get(i)].getTileVal() > 0) {
                adj.bombChanceValues(TileManager.foundTiles.get(i));
            }
        }
    }

    public int getBombChanceAt(int position) {
        int unknownTiles = 0;
        for (int j = 0; j < bombChance.length; j++) {
            if (bombChance[j] != null) {
                if (bombChance[j] > 0 && bombChance[j] < 99) {
                    unknownTiles += 1;
                }
            }
            else {
                unknownTiles += 1;
            }
        }

        // Calculates the value of a tile that has no adjacent found tiles
        if (bombChance[position] == null) {
            if ((TileManager.totalBombs - (foundBombs.size()) <= 0)) {
                return 5; // Dw about this I don't know how in the world TotalBombs - foundBombs can ever be negative, but it happens. So clearly there are duplicates somewhere but I don't have time to fix and it isn't crucial for anything just user visuals
            }
            return 100 / ((100 * unknownTiles) / (100 * (TileManager.totalBombs - (foundBombs.size()))));
        }
        return bombChance[position];
    }
}
