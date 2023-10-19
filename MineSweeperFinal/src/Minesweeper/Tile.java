package Minesweeper;

/**
 * Author: Kenzie Leckenby
 * Date: Dec 28, 2022
 * Description:
 * New Minesweeper.Tile Object that simplifies the old architecture to hopefully improve efficiency
 */

public class Tile {
    public int tileVal = 0;
    public boolean markedAsFlag = false;

    public Tile() {}

    // Set Methods
    public void setTileVal(int input) {
        this.tileVal = input;
    }
    public void addTileVal(int input) {
        this.tileVal += input;
    }
    public void setFlag(boolean input) {
        this.markedAsFlag = input;
    }

    // Get Methods
    public int getTileVal() {
        return this.tileVal;
    }
    public boolean getFlag() {
        return this.markedAsFlag;
    }
}
