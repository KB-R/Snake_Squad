package Models;
import java.util.Random;
/**
 * 
 * @author Kurt Burton-Rowe
 * @author Maxime Ndutiye
 * @version 1.0
 * @date October 25, 2018
 * 
 * The base model for all Zombies that will appear in the game
 */

public class Zombie extends NPC implements Movable{

    protected int maxHealth;
    protected int speed;
    protected int damage;
    protected int moveTick=0;
    protected int x;
    protected int y;
    protected int[] coordinate;
    
    
    /**
     * Zombie constructor to set where it spawns on the board and how many tiles it takes up
     * @param health The amount of health that a Zombie gets
     * @param speed  The speed that the Zombie will traverse the board
     * @param dimension The dimension of the level to know the bounds of spawning
     */
    public Zombie(int health, int speed) {
    	super(health, false);
    	this.maxHealth = health;
    	this.speed = speed;
    	this.x = 9;
    	this.y = random();
    	setLocation(this.x,this.y);
    }
   
    /*This changes x coordinate of the zombie object*/
    public void move(){
    	moveTick++;
    	if(!(collision)&&(moveTick%5==0)) {
    		this.x--;
    		setLocation(this.x, this.y);
    	}
    }
    public int random() {
    	Random rand = new Random();
    	return rand.nextInt(6);
    }
	
    /**
     * @return The speed of the Zombie
     */
    @Override
    public int getVelocity() {
	return speed;
    }
	
    @Override
    public void setVelocity(int newV) {
	this.speed = newV;
    }
	
    public void setDamage(int damage) {
	this.damage=damage;
    }
	
    /**
     * @return The damage that the Zombie inflicts 
     */
     public int getDamage() {
    	 return damage;
     }
}
