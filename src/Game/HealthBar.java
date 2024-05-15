package Game;

import static java.lang.String.valueOf;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

// Interface for different health bar states
interface HealthBarState {

    void render(int x, int y, int health, Graphics g, HealthBar healthBar);
}

// Concrete implementation of a green health bar state
class GreenHealthBarState implements HealthBarState {

    @Override
    public void render(int x , int y, int health, Graphics g, HealthBar healthBar) {
     healthBar.stateNow(x,y);
        g.setColor(Color.green);
        g.fillRect(x, y, health, 6);
        g.setColor(Color.white);

    }
}

// Concrete implementation of a blue health bar state
class OrangeHealthBarState implements HealthBarState {

    @Override
    public void render(int x, int y, int health, Graphics g, HealthBar healthBar) {
        healthBar.stateNow(x,y);
        g.setColor(Color.orange);
        g.fillRect(x, y, health, 6);
        g.setColor(Color.white);
    }
}

// Concrete implementation of a red health bar state
class RedHealthBarState implements HealthBarState {

    @Override
    public void render(int x, int y, int health, Graphics g, HealthBar healthBar) {
        healthBar.stateNow(x,y);
        g.setColor(Color.red);
        g.fillRect(x, y, health, 6);
        g.setColor(Color.white);
    }
}



// Context class that uses the state
public class HealthBar {


    private HealthBarState state;

    public HealthBar() {

        state = new GreenHealthBarState();
    }

    // Method to change the state
    public void setState(HealthBarState state) {
        this.state = state;

    }
     public HealthBarState getState() {
        return state;

    }

     public void stateNow(int x , int y){
         String state =valueOf(getState());
         
         String Nowstate="Low damage";

         if(state.contains("RedHealthBarState")){
             Nowstate="High damage !!";
             
            // Create an instance of GameFont with size
          GameFont smallFont = new GameFont(16);
	  smallFont.drawString("State : " +Nowstate , x-100, y-27, Color.white );	
          
         }        
          
	}

}
