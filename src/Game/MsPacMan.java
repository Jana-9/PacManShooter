package Game;

import static Game.Game.mouseY;
import java.awt.Point;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *
 * @author razan
 */

public class MsPacMan {
    private Image playerImage;
    private int health = 100;
    private int rotation = 0, radius = 22;
    private Point coordinates = new Point(Window.WIDTH / 2, Window.HEIGHT / 2);
    private final int speed = 324, damage = 10;
    private boolean alive = true;
    private HealthBar healthBar;
    private int ammos = 50;
    private int ID = Game.players++;

    MsPacMan() {
        loadImagee();
        this.healthBar = new HealthBar(0, 0);    
    }
    

    public void renderr(Graphics g) {
        if (isAlivee()) {
            this.playerImage.drawCentered(this.coordinates.x, this.coordinates.y);
            this.playerImage.setRotation((float) this.rotation);
            this.healthBar.render(this.coordinates.x - 24, this.coordinates.y - 50, this.health / 2, g);
        }
    }

    public void loadImagee() {
        try {
            this.playerImage = new Image("Images/Ms. Pac Man.png");
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void updatee(Input input, int delta) {
        if (this.alive) {
            this.rotation = (int) getAngleFromPointt(new Point(input.getMouseX(), input.getMouseY()), this.coordinates);
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

    public void detectCollisionWithEnemiess(ArrayList<Enemy> enemies) throws SlickException {
        try {
            Iterator<Enemy> iter = enemies.iterator();
            while (iter.hasNext()) {
                Enemy enemy = iter.next();
                if (this.health != 0 && enemy.isAlive() &&
                        Math.sqrt(Math.pow((enemy.getX() - this.coordinates.getX()), 2) +
                                Math.pow((enemy.getY() - this.coordinates.getY()), 2)) <= this.radius + enemy.getRadius()) {
                    iter.remove();
                    this.hitt();
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Error: Concurrent Modification");
        }
    }

    public void detectCollisionWithBullett(ArrayList<Bullet> bullets) throws SlickException {
        Iterator<Bullet> iter = bullets.iterator();
        while (iter.hasNext()) {
            Bullet bullet = iter.next();
            if (this.isAlivee() &&
                    Math.sqrt(Math.pow((bullet.getX() - this.coordinates.getX()), 2) +
                            Math.pow((bullet.getY() - this.coordinates.getY()), 2)) <= this.radius + bullet.getRadius()) {
                this.hitt();
                iter.remove();
            }
        }
    }

    public void checkIfPickedUpAmmoss(Ammo[] ammos) throws SlickException {
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

    private double getAngleFromPointt(Point firstPoint, Point secondPoint) {
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

    public void hitt() {
        this.health -= this.damage;
    }

    public int ggetHealth() {
        return this.health;
    }

    public int ggetX() {
        return (int) this.coordinates.getX();
    }

    public int ggetY() {
        return (int) this.coordinates.getY();
    }

    public boolean isAlivee() {
        return this.alive;
    }

    public Point getCoordinatess() {
        return this.coordinates;
    }

    public int getAmmoss() {
        return this.ammos;
    }

    public void removeAmmoo() {
        this.ammos--;
    }

    public int ggetID() {
        return this.ID;
    }

    public void resett() {
        this.alive = true;
        this.health = 100;
        this.ammos = 50;
        loadImagee();
        this.coordinates = new Point(Window.WIDTH / 2, Window.HEIGHT / 2);
    }
}
