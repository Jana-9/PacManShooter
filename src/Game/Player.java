package Game;

import static Game.Game.mouseY;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;
import java.util.ArrayList;
import java.awt.Point;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 * Represents the player in the game, both in single player and multiplayer modes.
 * @author carloblasi
 */
public interface Player {
    public void render(Graphics g);
    public void loadImage();
    public void update(Input input, int delta);
    public void detectCollisionWithEnemies(ArrayList<Enemy> enemies) throws SlickException;
    public void detectCollisionWithBullet(ArrayList<Bullet> bullets) throws SlickException;
    public void checkIfPickedUpAmmos(Ammo[] ammos) throws SlickException;

    public void reset();

    public void removeAmmo();

    public int getAmmos();

    public boolean isAlive();

    public int getX();

    public int getY();

    public int getHealth();

    public Point getCoordinates();
}

class PacMan implements Player {
    private Image playerImage;
    private int health = 100;
    private int rotation = 0, radius = 22;
    private Point coordinates = new Point(Window.WIDTH / 2, Window.HEIGHT / 2);
    private final int speed = 324, damage = 10;
    private boolean alive = true;
    private HealthBar healthBar;
    private int ammos = 50;
    private int ID = Game.players++;

    public PacMan() {
        loadImage();
        this.healthBar = new HealthBar(0, 0);
    }

    @Override
    public void render(Graphics g) {
        if (isAlive()) {
            this.playerImage.drawCentered(this.coordinates.x, this.coordinates.y);
            this.playerImage.setRotation((float) this.rotation);
            this.healthBar.render(this.coordinates.x - 24, this.coordinates.y - 50, this.health / 2, g);
        }
    }

    @Override
    public void loadImage() {
        try {
            this.playerImage = new Image("Images/Pac Man.png");
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Input input, int delta) {
        if (this.alive) {
            this.rotation = (int) getAngleFromPoint(new Point(input.getMouseX(), input.getMouseY()), this.coordinates);
            int speed = (this.speed * delta / 1000);

            if (input.isKeyDown(Input.KEY_D))
                this.coordinates.x += speed;
            if (input.isKeyDown(Input.KEY_A))
                this.coordinates.x -= speed;
            if (input.isKeyDown(Input.KEY_W))
                this.coordinates.y -= speed;
            if (input.isKeyDown(Input.KEY_S))
                this.coordinates.y += speed;

            if (this.coordinates.y >= Window.HEIGHT - 45)
                this.coordinates.y = Window.HEIGHT - 45;
            if (this.coordinates.y <= 45)
                this.coordinates.y = 45;
            if (this.coordinates.x >= Window.WIDTH - 45)
                this.coordinates.x = Window.WIDTH - 45;
            if (this.coordinates.x <= 45)
                this.coordinates.x = 45;

            if (health == 0)
                this.alive = false;
        }
    }

    @Override
    public void detectCollisionWithEnemies(ArrayList<Enemy> enemies) throws SlickException {
        try {
            Iterator<Enemy> iter = enemies.iterator();
            while (iter.hasNext()) {
                Enemy enemy = iter.next();
                if (this.health != 0 && enemy.isAlive() &&
                        Math.sqrt(Math.pow((enemy.getX() - this.coordinates.getX()), 2) +
                                Math.pow((enemy.getY() - this.coordinates.getY()), 2)) <= this.radius + enemy.getRadius()) {
                    iter.remove();
                    this.hit();
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Error: Concurrent Modification");
        }
    }

    @Override
    public void detectCollisionWithBullet(ArrayList<Bullet> bullets) throws SlickException {
        Iterator<Bullet> iter = bullets.iterator();
        while (iter.hasNext()) {
            Bullet bullet = iter.next();
            if (this.isAlive() &&
                    Math.sqrt(Math.pow((bullet.getX() - this.coordinates.getX()), 2) +
                            Math.pow((bullet.getY() - this.coordinates.getY()), 2)) <= this.radius + bullet.getRadius()) {
                this.hit();
                iter.remove();
            }
        }
    }

    @Override
    public void checkIfPickedUpAmmos(Ammo[] ammos) throws SlickException {
        for (Ammo ammo : ammos) {
            if (this.health != 0 && !ammo.alreadyPicked() &&
                    Math.sqrt(Math.pow((ammo.getX() - this.coordinates.getX()), 2) +
                            Math.pow((ammo.getY() - this.coordinates.getY()), 2)) <= this.radius + ammo.getRadius()) {
                ammo.pick();
                new Sound("Sounds/pacmanPickedAmmo.wav").play();
                this.ammos += 50;
            }
        }
    }

    private double getAngleFromPoint(Point firstPoint, Point secondPoint) {
        double r;
        if (secondPoint.x > firstPoint.x)
            r = (Math.atan2((secondPoint.x - firstPoint.x), (firstPoint.y - secondPoint.y)) * 180 / Math.PI) + 90;
        else if (secondPoint.x < firstPoint.x)
            r = 360 - (Math.atan2((firstPoint.x - secondPoint.x), (firstPoint.y - secondPoint.y)) * 180 / Math.PI) + 90;
        else
            r = Math.atan2(0, 0) + 90;
        if (r == 90 && mouseY < this.coordinates.y)
            return 270;
        return r;
    }

    public void hit() {
        this.health -= this.damage;
    }

    public int getHealth() {
        return this.health;
    }

    public int getX() {
        return (int) this.coordinates.getX();
    }

    public int getY() {
        return (int) this.coordinates.getY();
    }

    public boolean isAlive() {
        return this.alive;
    }

    public Point getCoordinates() {
        return this.coordinates;
    }

    public int getAmmos() {
        return this.ammos;
    }

    public void removeAmmo() {
        this.ammos--;
    }

    public int getID() {
        return this.ID;
    }

    public void reset() {
        this.alive = true;
        this.health = 100;
        this.ammos = 50;
        loadImage();
        this.coordinates = new Point(Window.WIDTH / 2, Window.HEIGHT / 2);
    }
}



/**
 * Adapter class to adapt PacMan to Player interface.
 */

 class PacManAdapter implements Player {
  private MsPacMan msPacMan;

    public PacManAdapter(MsPacMan newMsPacMan) {
        msPacMan = newMsPacMan;
    }

    @Override
    public void render(Graphics g) {
        msPacMan.renderr(g);
    }

    @Override
    public void loadImage() {
        msPacMan.loadImagee();
    }

    @Override
    public void update(Input input, int delta) {
        msPacMan.updatee(input, delta);
    }

    @Override
    public void detectCollisionWithEnemies(ArrayList<Enemy> enemies) throws SlickException {
        msPacMan.detectCollisionWithEnemiess(enemies);
    }

    @Override
    public void detectCollisionWithBullet(ArrayList<Bullet> bullets) throws SlickException {
        msPacMan.detectCollisionWithBullett(bullets);
    }

    @Override
    public void checkIfPickedUpAmmos(Ammo[] ammos) throws SlickException {
        msPacMan.checkIfPickedUpAmmoss(ammos);
    }

    @Override
    public void reset() {
        msPacMan.resett();
    }

    @Override
    public void removeAmmo() {
        msPacMan.removeAmmoo();
    }

    @Override
    public int getAmmos() {
        return msPacMan.getAmmoss();
    }

    @Override
    public boolean isAlive() {
        return msPacMan.isAlivee();
    }

    @Override
    public int getX() {
        return msPacMan.ggetX();
    }

    @Override
    public int getY() {
        return msPacMan.ggetY();
    }

    @Override
    public int getHealth() {
        return msPacMan.ggetHealth();
    }

    @Override
    public Point getCoordinates() {
        return msPacMan.getCoordinatess();
    }
}
