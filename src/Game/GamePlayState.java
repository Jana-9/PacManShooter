package Game;

import static Game.Game.NEW_DIFFICULTY_DELAY;
import static Game.Game.AMMOS_DELAY;
import static Game.Game.ENEMY_DELAY;
import static Game.Game.BULLET_DELAY;
import static Game.Game.MENUSTATE;
import static Game.Game.START_DELAY;
import static Game.Game.Score;
import static Game.Game.ammos;
import static Game.Game.ammosCount;
import static Game.Game.bulletList;
import static Game.Game.enemyList;
import static Game.Game.startDelay;
import static Game.Game.canFire;
import static Game.Game.canSpawnEnemy;
import static Game.Game.canSpawnAmmo;
import static Game.Game.enemyPositionX;
import static Game.Game.enemyPositionY;
import static Game.Game.increaseDifficulty;
import static Game.Game.paused;
import static Game.Game.player;
import static Game.Game.smallFont;
import java.awt.Font;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import java.util.Iterator;
import java.util.Random;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;

interface GameState {

    void update(GameContainer gc, Input input, int delta, int mouseX, int mouseY) throws SlickException;

    void render(GameContainer gc, Graphics g) throws SlickException;
}

public class GamePlayState implements GameState {

    /**
     * Metodo generico per aggiornare la logica degli oggetti della scena
     *
     * @param gc {@code GameContainer} del gioco
     * @param input L'input del gioco
     * @param delta {@code delta} del gioco
     * @param mouseX Coordinata X del mouse
     * @param mouseY Coordinata Y del mouse
     * @throws SlickException
     */
    static int startPlayX = enemyPositionX.nextInt(Window.WIDTH);
    static int startPlayY = -90;

    @Override

    public void update(GameContainer gc, Input input, int delta, int mouseX, int mouseY) throws SlickException {

        startDelay -= delta;
        if (startDelay < 0) {

            canFire -= delta;
            canSpawnEnemy -= delta;
            canSpawnAmmo -= delta;
            increaseDifficulty -= delta;

            enemyList.stream().forEach((enemy) -> {
                enemy.update(player.getCoordinates(), delta);
            });

            bulletList.stream().forEach((bullet) -> {
                bullet.update(delta);
            });

            player.update(input, delta);

            if (input.isKeyPressed(Input.KEY_ESCAPE)) {
                startDelay = 300;
                Game.state = Game.PAUSESTATE;
            }

            if (player.isAlive()) {
                if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && canFire <= 0) {
                    addBullet(mouseX, mouseY);
                }
            }

            if (!player.isAlive()) {

                new Sound("Sounds/pacmanDeath.wav").play();
                gameOver(input);
            }

            if (canSpawnEnemy <= 0) {

                addEnemy();
                canSpawnEnemy = ENEMY_DELAY;
            }

            if (canSpawnAmmo <= 0) {

                if (ammosCount == 10) {
                    ammosCount = 0;
                }

                ammos[ammosCount] = new Ammo(gc);
                ammosCount++;
                canSpawnAmmo = AMMOS_DELAY;
            }

            if (increaseDifficulty <= 0) {

                if (ENEMY_DELAY > 349) {
                    ENEMY_DELAY -= 30;
                }

                increaseDifficulty = NEW_DIFFICULTY_DELAY;
            }
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {

        Iterator<Bullet> iter = bulletList.iterator();
        while (iter.hasNext()) {

            Bullet bullet = iter.next();
            bullet.render();
            if (bullet.isOutOfBounds(gc)) {
                iter.remove();
            }
        }

        Iterator<Enemy> enemyIter = enemyList.iterator();
        while (enemyIter.hasNext()) {

            Enemy enemy = enemyIter.next();
            enemy.render();
            if (enemy.isCollidingWithBullets(bulletList)) {
                enemyIter.remove();
            }
        }

        for (Ammo ammo : ammos) {
            ammo.render();
        }

        player.detectCollisionWithEnemies(enemyList);
        player.checkIfPickedUpAmmos(ammos);
        player.render(g);

    }

    public static void addBullet(int x, int y) {

        if (player.getAmmos() > 0) {

            bulletList.add(new Bullet(player.getX(), player.getY(), x, y));
            player.removeAmmo();
            Game.shootSound.play(1.0f, 0.3f);
            canFire = BULLET_DELAY;
        }
    }

    public static void addEnemy() {
        EnemyFactory enemyFactory = new EnemyFactory();
        Enemy enemy = enemyFactory.getEnemy("PlayEnemy");
        switch (new Random().nextInt(4)) {
            case 0:
                startPlayX = enemyPositionX.nextInt(Window.WIDTH);
                startPlayY = -90;
                enemyList.add(enemy);
                break;
            case 1:
                startPlayX = enemyPositionX.nextInt(Window.WIDTH);
                startPlayY = Window.HEIGHT + 90;
                enemyList.add(enemy);
                break;
            case 2:
                startPlayX = -90;
                startPlayY = enemyPositionY.nextInt(Window.HEIGHT);
                enemyList.add(enemy);
                break;
            case 3:
                startPlayX = Window.WIDTH + 90;
                startPlayY = enemyPositionY.nextInt(Window.HEIGHT);
                enemyList.add(enemy);
                break;
        }
    }

    public static void gameOver(Input input) {

        Window.clear(input);
        if (Score.checkNewHighScore()) {
            Score.saveScores();
        }
        startDelay = START_DELAY;
        canSpawnAmmo = AMMOS_DELAY;
        paused = false;
        player.reset();
        enemyList.clear();
        bulletList.clear();
        for (Ammo ammo : ammos) {
            ammo.pick();
        }
        Score.resetScore();
        Game.state = Game.GAMEOVERSTATE;
    }
}

abstract class GameStateDecorator implements GameState {

    protected GameState decoratedObject;

    public GameStateDecorator(GameState decoratedObject) {
        this.decoratedObject = decoratedObject;
    }

    @Override
    public void update(GameContainer gc, Input input, int delta, int mouseX, int mouseY) throws SlickException {
        decoratedObject.update(gc, input, delta, mouseX, mouseY);
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        decoratedObject.render(gc, g);
    }
}

class detailsDecorator extends GameStateDecorator {

    public detailsDecorator(GameState decoratedObject) {
        super(decoratedObject);
    }

    @Override
    public void update(GameContainer gc, Input input, int delta, int mouseX, int mouseY) throws SlickException {

        decoratedObject.update(gc, input, delta, mouseX, mouseY);
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        decoratedObject.render(gc, g);
        renderDetails();

    }

    private void renderDetails() {
        smallFont.drawString("Score : " + Score.getScore(), 18, 24, Color.blue);
        smallFont.drawString("Ammos : " + player.getAmmos(), 20, 50, Color.red);
        smallFont.drawCenteredString("PAUSE (ESC)", Window.HALF_WIDTH, 30, Color.green);

    }

}
