package Game;

import static Game.Game.menuButton;
import static Game.Game.menuCharacterTitle;
import static Game.Game.msPacManButton;
import static Game.Game.msPacManIcon;
import static Game.Game.pacManButton;
import static Game.Game.pacManIcon;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

public class MenuCharacter {

    public static void update(GameContainer gc, Input input, int delta, int mouseX, int mouseY) {
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            if (Game.pacManButton.isPressed()) {
                PacMan pacman = new PacMan();
                startGame(pacman);
            }
            if (Game.msPacManButton.isPressed()) {
                MsPacMan mspacman = new MsPacMan();
                PacManAdapter pacManAdapter = new PacManAdapter(mspacman);
                startGame(pacManAdapter);
            }
            if (menuButton.isPressed()) {
                Window.clear(input);
                Game.state = Game.MENUSTATE;
            }
        }
        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            Window.clear(input);
            Game.state = Game.MENUSTATE;
        }

        pacManButton.hoverEffect();
        msPacManButton.hoverEffect();
        menuButton.hoverEffect();
    }

    public static void render(GameContainer gc, Graphics g) {
        // Render Pac-Man and Ms. Pac-Man icons alongside the menu buttons
        pacManIcon.drawCentered( Window.HALF_WIDTH+ -300, Window.HALF_HEIGHT + 40);
        msPacManIcon.drawCentered( Window.HALF_WIDTH+250, Window.HALF_HEIGHT + 40);

        menuCharacterTitle.render();
        pacManButton.render();
        msPacManButton.render();
        menuButton.render();
    }

    private static void startGame(Player player) {
        Window.clear(Game.input);
        Game.openingSound.play();
        Game.player = player;
        Game.state = Game.GAMEPLAYSTATE;
    }

}
