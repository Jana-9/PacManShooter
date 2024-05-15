package Game;

import static Game.Game.titleImage;
import static Game.Game.leaderboardButton;
import static Game.Game.multiplayerButton;
import static Game.Game.playButton;
import static Game.Game.quitButton;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 * Rappresenta la scena del menu del gioco.
 * @author carloblasi
 */
 interface InputHandlerStrategy {
    void handleInput(GameContainer gc, Input input, int mouseX, int mouseY);
}

 class MouseClickHandler implements InputHandlerStrategy {
     
    @Override
    public void handleInput(GameContainer gc, Input input, int mouseX, int mouseY) {
        
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            
            if (playButton.isPressed()) {
                Window.clear(input);
                Game.openingSound.play();
                Game.state = Game.MENUPACMAN;
                
            } else if (multiplayerButton.isPressed()) {
                Window.clear(input);
                Game.state = Game.MULTIPLAYERMENUSTATE;
                Game.IPTextField.setText("");
                Game.DestinationPortTextField.setText("");
                Game.SourcePortTextField.setText("");
                
            } else if (leaderboardButton.isPressed()) {
                Window.clear(input);
                Game.state = Game.LEADERBOARDSTATE;
                
            } else if (quitButton.isPressed()) {
                System.exit(0);
                
            }
        }
    }
}

 class KeyPressHandler implements InputHandlerStrategy {
     
    @Override
    public void handleInput(GameContainer gc, Input input, int mouseX, int mouseY) {
        
        if (input.isKeyPressed(Input.KEY_ENTER)) {
            Window.clear(input);
            Game.openingSound.play();
            Game.state = Game.MENUPACMAN;
            
        } else if (input.isKeyPressed(Input.KEY_M)) {
            Window.clear(input);
            Game.state = Game.MULTIPLAYERMENUSTATE;
            Game.IPTextField.setText("");
            Game.DestinationPortTextField.setText("");
            Game.SourcePortTextField.setText("");
            
        } else if (input.isKeyPressed(Input.KEY_L)) {
            Window.clear(input);
            Game.state = Game.LEADERBOARDSTATE;
            
        } else if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            System.exit(0);
            
        }
    }
}

public class MenuState {
    
    private InputHandlerStrategy InputHandler;

    public MenuState(InputHandlerStrategy InputHandler) {
        this.InputHandler = InputHandler;
    }
    

    public void update(GameContainer gc, Input input, int delta, int mouseX, int mouseY) {
        
        InputHandler.handleInput(gc, input, mouseX, mouseY);
        
        playButton.hoverEffect();
        multiplayerButton.hoverEffect();
        leaderboardButton.hoverEffect();
        quitButton.hoverEffect();
    }

    public void render(GameContainer gc, Graphics g) {
        titleImage.drawCentered(Window.HALF_WIDTH, Window.HEIGHT/5);
        playButton.render();
        multiplayerButton.render();
        leaderboardButton.render();
        quitButton.render();
    }
}
