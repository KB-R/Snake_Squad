package Controllers;

import java.util.ArrayList;
import java.util.Scanner;

import Controllers.CollisionDetector;
import Controllers.MoveController;
import Models.*;

/**
 * @author Maxime Ndutiye
 * @version 1.0
 * @since October 27, 2018
 * The GameController class is responsible for running the game
 * It holds all of the game objects as well as handles input from the player
 */
public class GameController implements Runnable{
    
    // ArrayList for game objects
    private CollisionDetector collisionController = new CollisionDetector();
    private MoveController moveController = new MoveController();
    private GameObjectsController goc = new GameObjectsController();

    // gameboard
    ArrayList<NPC>[][] gameBoard = new ArrayList[6][10];
    Scanner reader = new Scanner(System.in);  

    // player
    private int sunPoints = 0;
    private long sunFlowerCooldown = 0;
    private long peaShooterCooldown = 0;
    private int level = 1;

    // this will act as our clock for now
    // every turn this will be incremented
    private int timer = 0; 

    /**
     * Initialize varibales
     */
    public GameController(){
        for(int i=0;i<6;i++){
            for(int j=0; j<10; j++){
                gameBoard[i][j] = new ArrayList<NPC>();
            }
        }
    }

    /**
     * Main game loop is to be implemented here
     * Check for collisions etc.
     */
    public void run(){
        System.out.println("running main thread");

        while(true){
            printGameBoard();
            handleInput();
            // move game objects
            // check collisions

            produceSun();
            updateGameBoard();
            if(checkEndGame()){
                break;
            }
            timer++;
        }
        reader.close();
    }

    /**
     * Check to see if the game is over
     * @return boolean true if game is over
     */
    public boolean checkEndGame(){
        return false;
    }

    /**
     * Print the game grid
     */
    public void printGameBoard(){
        for(int i=0;i<6;i++){
            for(int j=0; j<10; j++){
                if (j > 0 && j < 9){
                    if (!gameBoard[i][j].isEmpty()){
                        String npcs = "";
                        for (NPC npc: gameBoard[i][j]){
                            npcs += npc.toString();
                        }

                        System.out.print("[ " + npcs + " ]");
                    }else{
                        System.out.print("[    ]");
                    }
                }else if (j == 0){
                    System.out.print("  LM  ");
                }else {
                    System.out.print("      ");
                }
            }
            System.out.println("\n");
        }
    }

    /**
     * Perform an action based on user input
     * 
     * @param input the user's input string
     */
    public void handleInput(){
        System.out.println("LEVEL: " + level + " TURN: " + timer + ", sunPoints: " + sunPoints);
        System.out.println("What would you like to do ?");
        System.out.println("buy sf x y: buy a sunflower for " + Sunflower.getCost());
        System.out.println("buy ps x y: buy a peashooter for " + NormalPea.getCost());
        System.out.println("Enter: do nothing");

        String input = reader.nextLine();
        String[] splitInput = input.split("\\s");

        switch(splitInput[0]){
            case "buy":
                buyItem(splitInput);
                break;
            default:
                System.out.println("doing nothing");
        }
    }

    public void updateGameBoard(){
        // reset gameboard
        gameBoard = new ArrayList[6][10];
        for(int i=0;i<6;i++){
            for(int j=0; j<10; j++){
                gameBoard[i][j] = new ArrayList<NPC>();
            }
        }

        for(Sunflower sf: goc.getSunflowers()){
            int[] pos = sf.getLocation();
            gameBoard[pos[0]][pos[1]].add(sf);
        }

        for(PeaShooter ps: goc.getPeaShooters()){
            int[] pos = ps.getLocation();
            gameBoard[pos[0]][pos[1]].add(ps);
        }

        for(NormalPea np: goc.getPeas()){
            int[] pos = np.getLocation();
            gameBoard[pos[0]][pos[1]].add(np);
        }

        for(Zombie zb: goc.getZombies()){
            int[] pos = zb.getLocation();
            gameBoard[pos[0]][pos[1]].add(zb);
        }
    }

    public void buyItem(String[] splitInput){
        if(splitInput.length != 3){
            System.out.println("Bad input!");
            return;
        }

        System.out.println(splitInput[0] + " " + splitInput[1] + ":" + splitInput[2] + "," + splitInput[3]);
       
        try{
            Integer xPos = Integer.parseInt(splitInput[2]);
            Integer yPos = Integer.parseInt(splitInput[3]);
            NPC item = null;

            switch (splitInput[1]){
                case "sf":
                    item = new Sunflower(xPos, yPos);
                    break;
                case "ps":
                    item = new PeaShooter(xPos, yPos, 2);
                    break;
                default:
                    break;
            }

            System.out.println(item.toString());
            if (item != null && item.getCost() <= sunPoints && xPos > 0 && xPos < 9 && yPos < 6 && yPos >= 0){
                if (gameBoard[xPos][yPos].isEmpty()){
                    switch (splitInput[1]){
                        case "sf":
                            goc.addSunflower((Sunflower)item);
                            break;
                        case "ps":
                            goc.addPeaShooter((PeaShooter)item);
                            break;
                        default:
                            break;
                    }

                    sunPoints -= item.getCost();
                }else{
                    System.out.println("Something is already in that position");
                }
            }else{
                System.out.println("Couldn't process your purchase, your input was out of bounds");
            }
        }catch (NumberFormatException nfe){
            System.out.println("illegal input arguments");
        }
    }

    /**
     * Produce sunPoints every 2 turns
     */
    private void produceSun(){
        if (timer % 2 == 0){
            sunPoints += 10;
        }
    }

    /**
     * Collect sun points from all sunflowers
     */
    private void collectSun(){

    }

    public static void main(String[] args){
        // use a thread to run the game
        Thread mainGameThread = new Thread(new GameController());
        mainGameThread.start();

        System.out.println("starting game");
    }
}
