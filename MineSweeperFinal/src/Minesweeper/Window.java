package Minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import MineAI.*;

/**
 * Author: Kenzie Leckenby
 * Date: Jan 02, 2023
 * Time: 2:15 PM
 * Description:
 */

public class Window implements ActionListener {
    String mapSize = "m";
    int mapHeight = 10;
    int mapWidth = 20;

    int AITickSpeed = 5;
    int AIIterations = 50;

    // Value Modulates according to screen size and total buttons
    int buttonScale;

    // Determines which tile art is being used
    String tileSet;

    TileManager map;
    BombChance bomb;

    JFrame frame = new JFrame("MineSweeperFinal");

    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu mapSize, gameOptions;
        JMenuItem itemGameOptions, itemAIOptions, runAI, step;
        JRadioButtonMenuItem rbMapSize;

        // Creates Menu Bar
        menuBar = new JMenuBar();

        // Menu Bar Items
        mapSize = new JMenu("Map Size");
        gameOptions = new JMenu("Game Options");
        runAI = new JMenuItem("Run AI");
        runAI.setActionCommand("runAI");
        runAI.addActionListener(this);
        step = new JMenuItem("Step");
        step.setActionCommand("step");
        step.addActionListener(this);
        menuBar.add(mapSize);
        menuBar.add(gameOptions);
        menuBar.add(runAI);
        menuBar.add(step);

        // < Map Size Menu Buttons >
        ButtonGroup mapSizeGroup = new ButtonGroup();
        rbMapSize = new JRadioButtonMenuItem("Small");
        rbMapSize.setActionCommand("ms");
        mapSizeGroup.add(rbMapSize);
        mapSize.add(rbMapSize);
        rbMapSize.addActionListener(this);

        rbMapSize = new JRadioButtonMenuItem("Medium");
        rbMapSize.setActionCommand("mm");
        rbMapSize.setSelected(true);
        mapSizeGroup.add(rbMapSize);
        mapSize.add(rbMapSize);
        rbMapSize.addActionListener(this);

        rbMapSize = new JRadioButtonMenuItem("Large");
        rbMapSize.setActionCommand("ml");
        mapSizeGroup.add(rbMapSize);
        mapSize.add(rbMapSize);
        rbMapSize.addActionListener(this);

        // < Game Option Menu Buttons >
        itemGameOptions = new JMenuItem("Generate New");
        itemGameOptions.setActionCommand("newGen");
        gameOptions.add(itemGameOptions);
        itemGameOptions.addActionListener(this);

        itemGameOptions = new JMenuItem("Give Up");
        itemGameOptions.setActionCommand("giveUp");
        gameOptions.add(itemGameOptions);
        itemGameOptions.addActionListener(this);

        // < Minesweeper.Tile Set Selector Buttons >
        // @TODO allow the ability to switch between different tile sets fluidly

        // < AI Run >
        /*runAI.setActionCommand("runAI");
        runAI.addActionListener(this);

        step.setActionCommand("step");
        step.addActionListener(this);*/

        /* @TODO create user interface to run AI that includes:
            - Amount of iterations to run the program
            - AI Speed (operations per tick)
                * choice for manual iteration
            - Run and Stop buttons
        */
        return menuBar;
    }

    public Container currentMapButtons() {
        JPanel currentMap = new JPanel();
        currentMap.setLayout(null);
        currentMap.setOpaque(true);

        if (map == null) {
            for (int i = 0; i  < this.mapWidth*this.mapHeight; i++) {
                displayTile(i, "OldTileSet", "GrassTile", currentMap);
            }
            return currentMap;
        }

        for (int i = 0; i < mapHeight * mapWidth; i++) {
            if (map.getIfFound(i)) {
                switch (map.getTileVal(i)) {
                    case -1 -> displayTile(i, "OldTileSet", "BombTile", currentMap);
                    case 0 -> displayTile(i, "OldTileSet", "DirtTile", currentMap);
                    case 1 -> displayTile(i, "OldTileSet", "OneTile", currentMap);
                    case 2 -> displayTile(i, "OldTileSet", "TwoTile", currentMap);
                    case 3 -> displayTile(i, "OldTileSet", "ThreeTile", currentMap);
                    case 4 -> displayTile(i, "OldTileSet", "FourTile", currentMap);
                    case 5 -> displayTile(i, "OldTileSet", "FiveTile", currentMap);
                    case 6 -> displayTile(i, "OldTileSet", "SixTile", currentMap);
                    case 7 -> displayTile(i, "OldTileSet", "SevenTile", currentMap);
                    case 8 -> displayTile(i, "OldTileSet", "EightTile", currentMap);
                }
            }
            else {
                if (map.getTileFlag(i)) {
                    displayTile(i, "OldTileSet", "FlagTile", currentMap);
                }
                else {
                    displayTile(i, "OldTileSet", "GrassTile", currentMap);
                }
            }
        }
        return currentMap;
    }

    public Container currentMapAI() {
        JPanel currentMap = new JPanel();
        currentMap.setLayout(null);
        currentMap.setOpaque(true);
        for (int i = 0; i < mapHeight * mapWidth; i++) {
            if (map.getIfFound(i)) {
                switch (map.getTileVal(i)) {
                    case -1 -> displayTile(i, "OldTileSet", "BombTile", currentMap);
                    case 0 -> displayTile(i, "OldTileSet", "DirtTile", currentMap);
                    case 1 -> displayTile(i, "OldTileSet", "OneTile", currentMap);
                    case 2 -> displayTile(i, "OldTileSet", "TwoTile", currentMap);
                    case 3 -> displayTile(i, "OldTileSet", "ThreeTile", currentMap);
                    case 4 -> displayTile(i, "OldTileSet", "FourTile", currentMap);
                    case 5 -> displayTile(i, "OldTileSet", "FiveTile", currentMap);
                    case 6 -> displayTile(i, "OldTileSet", "SixTile", currentMap);
                    case 7 -> displayTile(i, "OldTileSet", "SevenTile", currentMap);
                    case 8 -> displayTile(i, "OldTileSet", "EightTile", currentMap);
                }
            }
            else {
                ImageIcon icon = createImageIcon("/Minesweeper/TileGraphics/" + "OldTileSet" + "/" + "GrassTile" + ".png");
                icon = new ImageIcon(getScaledImage(icon.getImage(), buttonScale, buttonScale));
                currentMap.add(pressedTiles(icon, i));
            }
        }
        return currentMap;
    }

    public JButton tileButton (ImageIcon icon, int position) {
        JButton button = new JButton(icon);
        button.setBounds((int) ((position - mapWidth * Math.floor((double) position / mapWidth)) * buttonScale), (int) (Math.floor((double) position / mapWidth) * buttonScale), buttonScale, buttonScale);
        button.setBorderPainted(false);
        if (map == null) {
            button.setActionCommand("p" + position);
            button.addActionListener(this);
            return button;
        }
        button.setActionCommand("t" + position);
        button.addActionListener(this);
        button.setToolTipText("" + bomb.getBombChanceAt(position));
        return button;
    }

    public JLabel pressedTiles (ImageIcon icon, int position) {
        JLabel label = new JLabel(icon);
        label.setBounds((int) ((position - mapWidth * Math.floor((double) position / mapWidth)) * buttonScale), (int) (Math.floor((double) position/ mapWidth) * buttonScale), buttonScale, buttonScale);
        return label;
    }

    public void displayTile (int position, String tileSet, String tile, JPanel panel) {
        ImageIcon icon = createImageIcon("/Minesweeper/TileGraphics/" + tileSet + "/" + tile + ".png");
        icon = new ImageIcon(getScaledImage(icon.getImage(), buttonScale, buttonScale));
        if (tile.equals("GrassTile") || tile.equals("FlagTile")) {
            panel.add(tileButton(icon, position));
            return;
        }

        panel.add(pressedTiles(icon, position));
    }

    public void winPop() {
        Object[] options = {"Generate New", "Reveal Bombs"};
        int optionChose = JOptionPane.showOptionDialog(frame,
                "You Won !",
                "Win Popup",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                createImageIcon("/Minesweeper/TileGraphics/OldTileSet/FlagTile.png"),
                options,
                options[0]);
        if (optionChose == JOptionPane.YES_OPTION) {
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = (int) size.getWidth();
            screenWidth -= 64;
            buttonScale = screenWidth / this.mapWidth;
            map = null;
            frame.setContentPane(currentMapButtons());
            frame.setSize(mapWidth*buttonScale+16, mapHeight*buttonScale+60);
            SwingUtilities.updateComponentTreeUI(frame);
        }
        else if (optionChose == JOptionPane.NO_OPTION) {
            for (int i = 0; i < this.mapWidth*this.mapHeight; i++) {
                map.mapInteraction(i);
            }
            frame.setContentPane(currentMapButtons());
            SwingUtilities.updateComponentTreeUI(frame);
        }
    }

    public void lossPop() {
        Object[] options = {"Generate New", "Reveal Bombs"};
        int optionChose = JOptionPane.showOptionDialog(frame,
                "You Lost !",
                "Lost Popup",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                createImageIcon("/Minesweeper/TileGraphics/OldTileSet/BombTile.png")
                ,
                options,
                options[0]);
        if (optionChose == JOptionPane.YES_OPTION) {
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = (int) size.getWidth();
            screenWidth -= 64;
            buttonScale = screenWidth / this.mapWidth;
            map = null;
            frame.setContentPane(currentMapButtons());
            frame.setSize(mapWidth*buttonScale+16, mapHeight*buttonScale+60);
            SwingUtilities.updateComponentTreeUI(frame);
        }
        else if (optionChose == JOptionPane.NO_OPTION) {
            for (int i = 0; i < this.mapWidth*this.mapHeight; i++) {
                map.mapInteraction(i);
            }
            frame.setContentPane(currentMapButtons());
            SwingUtilities.updateComponentTreeUI(frame);
        }
    }

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Window.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            return null;
        }
    }

    private Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    private static void createAndShowGUI() {
        //Create and set up the content pane.
        Window window = new Window();
        window.frame.setJMenuBar(window.createMenuBar());

        // Checks the screen size
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) size.getWidth();
        screenWidth -= 64;
        window.buttonScale = screenWidth / window.mapWidth;
        window.frame.setContentPane(window.currentMapButtons());

        //Display the window.
        window.frame.setSize(window.mapWidth * window.buttonScale+16, window.mapHeight * window.buttonScale + 60);
        window.frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Run GUI codes in Event-Dispatching thread for thread-safety
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().charAt(0) == 'm') {
            this.mapSize = e.getActionCommand().substring(1);
            if (this.mapSize.equals("s")) {
                this.mapWidth = 14;
                this.mapHeight = 7;
            }
            if (this.mapSize.equals("m")) {
                this.mapWidth = 20;
                this.mapHeight = 10;
            }
            if (this.mapSize.equals("l")) {
                this.mapWidth = 30;
                this.mapHeight = 15;
            }
        }
        else if (e.getActionCommand().equals("newGen")) {
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = (int) size.getWidth();
            screenWidth -= 64;
            buttonScale = screenWidth / this.mapWidth;
            map = null;
            frame.setContentPane(currentMapButtons());
            frame.setSize(mapWidth*buttonScale+16, mapHeight*buttonScale+60);
            frame.revalidate();
        }
        else if (e.getActionCommand().equals("giveUp")) {
            for (int i = 0; i < this.mapWidth*this.mapHeight; i++) {
                map.mapInteraction(i);
            }
            frame.setContentPane(currentMapButtons());
            frame.revalidate();
        }

        else if (e.getActionCommand().equals("runAI")) {
            map = new TileManager(this.mapSize);
            Random rand = new Random();
            int randClick = rand.nextInt(mapWidth*mapHeight);
            map.createMap(randClick);
            map.mapInteraction(randClick);
            bomb = new BombChance();
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = (int) size.getWidth();
            screenWidth -= 64;
            buttonScale = screenWidth / this.mapWidth;
            frame.setContentPane(currentMapButtons());
            frame.setSize(mapWidth*buttonScale+16, mapHeight*buttonScale+60);
            frame.revalidate();
        }

        else if (e.getActionCommand().equals("step")) {
            if (BombChance.nonBombs.size() > 0) {
                map.mapInteraction(BombChance.nonBombs.get(0));
            }
            else {
                int lowestVal = 100;
                int bestCase = -1;
                for (int i = 0; i < BombChance.bombChance.length; i++) {
                    if (BombChance.bombChance[i] != null) {
                        if (BombChance.bombChance[i] < lowestVal && BombChance.bombChance[i] > 0) {
                            lowestVal = BombChance.bombChance[i];
                            bestCase = i;
                        }
                    }
                }
                System.out.println("I am guessing tile [" + bestCase + "] is safe, chance of bomb is " + lowestVal + "%");
                map.mapInteraction(bestCase);
            }
            bomb = new BombChance();
            frame.setContentPane(currentMapButtons());
            frame.revalidate();

        }

        else if (e.getActionCommand().charAt(0) == 'p') {
            int position = Integer.parseInt(e.getActionCommand().substring(1));
            map = new TileManager(this.mapSize);
            map.createMap(position);
            map.mapInteraction(position);
            bomb = new BombChance();
            frame.setContentPane(currentMapButtons());
            frame.revalidate();
        }
        else if (e.getActionCommand().charAt(0) == 't') {
            int position = Integer.parseInt(e.getActionCommand().substring(1));
            map.mapInteraction(position);
            bomb = new BombChance();
            frame.setContentPane(currentMapButtons());
            frame.revalidate();
            if (map.getTileVal(position) == -1) {
                lossPop();
                return;
            }
            if (map.getTilesLeft() == 0) {
                winPop();
            }
        }
    }
}