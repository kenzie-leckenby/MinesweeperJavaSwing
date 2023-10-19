/**
 * Author: Kenzie Leckenby
 * Date: Dec 29, 2022
 * Description:
 */

package Minesweeper;

import MineAI.BombChance;

import java.util.ArrayList;

public class Adjacency {
    public Adjacency() {}

    public boolean validBombLocation(int randLoc, int firstClick, int row) {
        // Checks if the location is already a bomb
        if (TileManager.map[randLoc].getTileVal() == 0) {
            // Checks if adjacent tiles are within the safe area of the first click
            if (randLoc == firstClick || randLoc == inBounds(firstClick, -1, row) || randLoc == inBounds(firstClick, 1, row) ||
                    randLoc == inBounds(firstClick, row - 1, row) || randLoc == inBounds(firstClick, row, row) || randLoc == inBounds(firstClick, row + 1, row) ||
                    randLoc == inBounds(firstClick, -row - 1, row) || randLoc == inBounds(firstClick, -row, row) || randLoc == inBounds(firstClick, -row + 1, row)) {
                return false;
            }
        }
        else {
            return false;
        }
        return true;
    }

    // Sets all the map values after all the bomb locations have been determined
    public void mapValues(int rows) {
        /*
         7  8  9
        -1  0  1
        -7 -8 -9
        In the situation where a row is 8 long, this is the distance in the array for every adjacent value
        */

        for (int i = 0; i < TileManager.bombs.length; i++) {
            // Calculates the middle row
            for (int j = inBounds(TileManager.bombs[i], -1, rows); j <= inBounds(TileManager.bombs[i], 1, rows); j++) {
                if (TileManager.map[j].getTileVal() != -1) {
                    TileManager.map[j].addTileVal(1);
                }
            }
            // Calculates the top row
            for (int j = inBounds(TileManager.bombs[i], rows - 1, rows); j <= inBounds(TileManager.bombs[i], rows + 1, rows); j++) {
                if (TileManager.map[j].getTileVal() != -1) {
                    TileManager.map[j].addTileVal(1);
                }
            }
            // Calculates the bottom row
            for (int j = inBounds(TileManager.bombs[i], -rows - 1, rows); j <= inBounds(TileManager.bombs[i], -rows + 1, rows); j++) {
                if (TileManager.map[j].getTileVal() != -1) {
                    TileManager.map[j].addTileVal(1);
                }
            }
        }
    }

    // Reveals adjacent zeros along with their surrounding tiles
    public void adjacentZeros(int zeroLocation) {
        int rows = TileManager.rowLength;
        for (int j = inBounds(zeroLocation, -1, rows); j <= inBounds(zeroLocation, 1, rows); j++) {
            if (TileManager.foundTiles.contains(j)) {
                continue;
            }
            TileManager.foundTiles.add(j);
            if (TileManager.map[j].getTileVal() == 0 && j != zeroLocation) {
                adjacentZeros(j);
            }
        }
        for (int j = inBounds(zeroLocation, rows - 1, rows); j <= inBounds(zeroLocation, rows + 1, rows); j++) {
            if (TileManager.foundTiles.contains(j)) {
                continue;
            }
            TileManager.foundTiles.add(j);
            if (TileManager.map[j].getTileVal() == 0 && j != zeroLocation) {
                adjacentZeros(j);
            }
        }
        for (int j = inBounds(zeroLocation, -rows - 1, rows); j <= inBounds(zeroLocation, -rows + 1, rows); j++) {
            if (TileManager.foundTiles.contains(j)) {
                continue;
            }
            TileManager.foundTiles.add(j);
            if (TileManager.map[j].getTileVal() == 0 && j != zeroLocation) {
                adjacentZeros(j);
            }
        }
    }

    public void bombChanceValues(int location) {
        int rows = TileManager.rowLength;
        ArrayList<Integer> tiles = new ArrayList<>(); // Contains all unknown tiles that are found
        ArrayList<Integer> bombsFoundOnIteration = new ArrayList<>(); // Stores the number of bombs found on this iteration
        ArrayList<Integer> nonBombsFoundOnIteration = new ArrayList<>(); // Stores the number of tiles with a value of zero found on this iteration

        for (int j = inBounds(location, -1, rows); j <= inBounds(location, 1, rows); j++) {
            if (TileManager.foundTiles.contains(j)) {
                continue;
            }
            // Checks to see if the tile is already known
            if (BombChance.bombChance[j] != null) {
                if (BombChance.bombChance[j] >= 99) {
                    bombsFoundOnIteration.add(j);
                }
                else if (BombChance.bombChance[j] == 0) {
                    nonBombsFoundOnIteration.add(j);
                }
            }
            tiles.add(j);
        }
        for (int j = inBounds(location, rows - 1, rows); j <= inBounds(location, rows + 1, rows); j++) {
            if (TileManager.foundTiles.contains(j)) {
                continue;
            }
            // Checks to see if the tile is already known
            if (BombChance.bombChance[j] != null) {
                if (BombChance.bombChance[j] >= 99) {
                    bombsFoundOnIteration.add(j);
                }
                else if (BombChance.bombChance[j] == 0) {
                    nonBombsFoundOnIteration.add(j);
                }
            }
            tiles.add(j);
        }
        for (int j = inBounds(location, -rows - 1, rows); j <= inBounds(location, -rows + 1, rows); j++) {
            if (TileManager.foundTiles.contains(j)) {
                continue;
            }
            // Checks to see if the tile is already known
            if (BombChance.bombChance[j] != null) {
                if (BombChance.bombChance[j] >= 99) {
                    bombsFoundOnIteration.add(j);
                }
                else if (BombChance.bombChance[j] == 0) {
                    nonBombsFoundOnIteration.add(j);
                }
            }
            tiles.add(j);
        }

        // Makes sure the array isn't empty before running
        if (tiles.isEmpty()) {
            return;
        }

        int knownTiles = 0;
        for (int i = 0; i < tiles.size(); i++) {
            if (BombChance.bombChance[tiles.get(i)] != null) {
                if (BombChance.bombChance[tiles.get(i)] >= 99 || BombChance.bombChance[tiles.get(i)] == 0) {
                    knownTiles += 1;
                }
            }
            if (knownTiles == tiles.size()) {
                return;
            }
        }

        // Checks if the required amount of bombs for an area has already been met
        if (TileManager.map[location].getTileVal() == bombsFoundOnIteration.size()) {
            for (int i = 0; i < tiles.size(); i++) {
                if (!bombsFoundOnIteration.contains(tiles.get(i))) {
                    BombChance.bombChance[tiles.get(i)] = 0;
                    BombChance.nonBombs.add(tiles.get(i));
                }
            }
            // Will Return since there are no more unknown tiles at this location
            return;
        }
        // Checks if the required amount of tiles with zero chance of being bombs has been met
        else if (tiles.size() - TileManager.map[location].getTileVal() == nonBombsFoundOnIteration.size()) {
            for (int i = 0; i < tiles.size(); i++) {
                if (!nonBombsFoundOnIteration.contains(tiles.get(i))) {
                    BombChance.bombChance[tiles.get(i)] = 100;
                    BombChance.foundBombs.add(tiles.get(i));
                }
            }
            // Will return since there are no more unknown tiles at this location
            return;
        }


        // Checks if the bomb is already known in a pair of tiles
        int tileSum = 0;
        for (int i = 0; i < tiles.size(); i++) {
            if (BombChance.bombChance[tiles.get(i)] != null) {
                tileSum += BombChance.bombChance[tiles.get(i)];
            }
        }

        // If tileSum is >= to the max amount of bombs around a tile then all remaining tiles must equal 0
        if (tileSum >= (100 * TileManager.map[location].getTileVal()) - TileManager.map[location].getTileVal()) {
            for (int j = 0; j < tiles.size(); j++) {
                if (BombChance.bombChance[tiles.get(j)] == null) {
                    BombChance.bombChance[tiles.get(j)] = 0;
                    BombChance.nonBombs.add(tiles.get(j));
                }
            }
            // Will return since there are no more unknown tiles at this location
            return;
        }

        // Will assign the chance of clicking on a bomb for the remaining unknown tiles
        for (int i = 0; i < tiles.size(); i++) {
            if (BombChance.bombChance[tiles.get(i)] == null) {
                BombChance.bombChance[tiles.get(i)] = (100 * (TileManager.map[location].getTileVal()) / tiles.size() - knownTiles);
                if ((100 * TileManager.map[location].getTileVal()) / tiles.size() - knownTiles >= 99) {
                    BombChance.foundBombs.add(tiles.get(i));
                    BombChance.bombChance[tiles.get(i)] = 100;
                }
                continue;
            }

            if (BombChance.bombChance[tiles.get(i)] < (100 * (TileManager.map[location].getTileVal())) / tiles.size() - knownTiles && BombChance.bombChance[tiles.get(i)] > 0) {
                BombChance.bombChance[tiles.get(i)] = (100 * (TileManager.map[location].getTileVal())) / tiles.size() - knownTiles;
                if ((100 * (TileManager.map[location].getTileVal())) / tiles.size() - knownTiles >= 99) {
                    BombChance.foundBombs.add(tiles.get(i));
                    BombChance.bombChance[tiles.get(i)] = 100;
                }
            }
        }
    }

    private int inBounds(int tileCheck, int tileOffset, int rows) {

        // This if else statement deals with an edge case where a bomb spawned in the top left corner would not be properly accounted for
        if (tileCheck + tileOffset == -1) {
            return tileCheck + tileOffset + 1;
        }
        else if (tileCheck + tileOffset < -1) {
            return tileCheck;
        }

        if (tileCheck + tileOffset == TileManager.map.length) {
            return tileCheck + tileOffset - 1;
        }
        else if (tileCheck + tileOffset >= TileManager.map.length) {
            return tileCheck;
        }

        // Checks for border cases where the tileOffset would check tiles that wrapped
        // Left Side Wrap
        if (tileCheck % rows == 0 && (tileOffset == -1 || (tileCheck + tileOffset) % rows == rows - 1)) {
            return tileCheck + tileOffset + 1;
        }
        // Right Side Wrap
        if (tileCheck % rows == rows - 1 && (tileOffset == 1 || (tileCheck + tileOffset) % rows == 0)) {
            return  tileCheck + tileOffset - 1;
        }

        return tileCheck + tileOffset;
    }
}
