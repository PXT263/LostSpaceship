import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Ship
{
    protected int wang;

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean stillalive;
    protected boolean shielded;

    protected int score;

    protected int wave;
    protected int enemiesleft;
    protected int enemieskilled;

    protected int bombs;
    protected int bombsused;
    protected int enemieskilledwithbombs;

    protected ArrayList<Laser> bullets = new ArrayList<Laser>();
    protected ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    protected ArrayList<Laser> zap = new ArrayList<Laser>();
    protected ArrayList<Powerup> arsenal = new ArrayList<Powerup>();
    protected ArrayList<Powerup> zing = new ArrayList<Powerup>();

    protected int totalBulletsFired;
    protected long timeSinceLastWave;
    protected long spawnTimer;
    protected long timeSinceBomb;

    public Ship(int width, int height)
    {
	wang = 4;

	x = width/2;
	y = height - 60;
	this.width = width;
	this.height = height;

	stillalive = true;
	score = 0;
	bombs = 1;
    }

    public Ship(int x, int y, int width, int height)
    {
	wang = 2;

	this.x = x;
	this.y = y;

        this.width = width;
	this.height = height;

	stillalive = true;
	score = 0;
	bombs = 1;
    }

    public int getX()
    {
	return this.x;
    }

    public int getY()
    {
	return this.y;
    }

    public void moveUp()
    {
	if (y - wang < 0)
	    {
		y = 0;
	    }
	else y -= wang;
    }

    public void moveDown()
    {
	if (y + wang + 64 > height)
	    {
		y = height - 64;
	    }
	else y += wang;
    }

    public void moveLeft()
    {
	if (x - wang < 0)
	    {
		x = 0;
	    }
	else x -= wang;
    }

    public void moveRight()
    {
	if (x + wang + 40 > width)
	    {
		x = width - 40;
	    }
	else x += wang;
    }

    public void shootBullet()
    {
	bullets.add(new Laser(x + 16, y - 20));
	totalBulletsFired ++;
    }

    public void moveBullets()
    {
	for (int i = 0; i < bullets.size(); i++)
	    {
		bullets.get(i).move();
		if (bullets.get(i).y <= -20)
		    {
			bullets.remove(i);
		    }
	    }
	for (int i = 0; i < zap.size(); i++)
	    {
	        zap.get(i).move();
		if (zap.get(i).y  < 0 || zap.get(i).y > 690 || zap.get(i).x < 0 || zap.get(i).x > 590)
		    {
			zap.remove(i);
		    }
	    }
    }

    public void spawnNewWave()
    {
	if (enemiesleft > 0 && System.currentTimeMillis() - timeSinceLastWave > 4000)
	    {
		if (System.currentTimeMillis() - spawnTimer > 1000)
		    {
			spawnEnemy();
			enemiesleft --;
			spawnTimer = System.currentTimeMillis();
		    }
	    }
	if (waveOver())
	    {
		timeSinceLastWave = System.currentTimeMillis();
		wave++;
		enemiesleft = 10 + (wave/5);
	    }
    }

    public void spawnEnemy()
    {
	Random rand = new Random();
	int i = rand.nextInt(2);
	if (i == 0) {i = -2;}
	else {i = 2;}
	enemies.add(new Enemy(i));
    }


    public void moveEnemies()
    {
	for (int i = 0; i < enemies.size(); i++)
	    {
		enemies.get(i).move();
		if (enemies.get(i).x > 600 || enemies.get(i).x < 0)
		    {
			enemies.remove(i);
		    }
	    }
    }

    public void enemyShoot()
    {
	for (int i = 0; i < enemies.size(); i++)
	    {
		if (System.currentTimeMillis() - enemies.get(i).previousShotTime > 1000)
		    {
			enemies.get(i).previousShotTime = System.currentTimeMillis();
			int ex = enemies.get(i).x;
			int ey = enemies.get(i).y;
			int dx = x - ex;
			int dy = y - ey;
			double factor = 0.5 + (wave * 0.05);
			double f = Math.sqrt((double)(dx*dx + dy*dy)/16);
			zap.add(new Laser(ex + 16, ey + 25,(double)(dx)/f * factor , (double)(dy) /f * factor));
		    }
	    }
    }

    public boolean waveOver()
    {
	return (enemies.isEmpty() && enemiesleft == 0);
    }

    public void spawnBombs()
    {
	if (Math.random() > 0.9995)
	    {
		arsenal.add(new Powerup());
	    }
    }

    public void moveBombs()
    {
	try{
	    for (int i = 0; i < arsenal.size(); i++)
		{
		    arsenal.get(i).move();
		    if (arsenal.get(i).y > 700)
			{
			    arsenal.remove(i);
			}
		}
	}catch(IndexOutOfBoundsException e){}
    }

    public void spawnShields()
    {
	if (Math.random() > 0.9995)
	    {
		zing.add(new Powerup());
	    }
    }

    public void moveShields()
    {
	for (int i = 0; i < zing.size(); i++)
	    {
		zing.get(i).move();
		if (zing.get(i).y > 700)
		    {
			zing.remove(i);
		    }
	    }
    }

    public void collisionDetection()
    {
	try{
	    for (int i = 0; i < zap.size(); i++)
		{
		    if ((int)zap.get(i).x - x < 20 && (int)zap.get(i).x - x > 0 && y - (int)zap.get(i).y < 0 && y - (int)zap.get(i).y > -20)
			{
			    if (shielded)
				{
				    zap.remove(i);
				    shielded = false;
				}
			    else
				{
				    stillalive = false;
				}
			}
		}
	}catch (IndexOutOfBoundsException e){}
	try{
	    for (int i = 0; i < enemies.size(); i++)
		{
		    for (int j = 0; j < bullets.size(); j++)
			{
			    if ((int)bullets.get(j).x - enemies.get(i).x < 20 && (int)bullets.get(j).x - enemies.get(i).x > 0 && enemies.get(i).y - (int)bullets.get(j).y < 0 && enemies.get(i).y - (int)bullets.get(j).y > -20)
				{
				    score += (y - enemies.get(i).y)/10;
				    bullets.remove(j);
				    enemies.remove(i);
				    enemieskilled ++;
				}
			}
		}
	}catch (IndexOutOfBoundsException e){}
	try{
	    for (int i = 0; i < arsenal.size(); i++)
		{
		    if (arsenal.get(i).x - x < 32 && arsenal.get(i).x - x > -10 && arsenal.get(i).y - y < 10 && arsenal.get(i).y - y > -32)
			{
			    arsenal.remove(i);
			    bombs++;
			    score += 30;
			} 
		}
	}catch(IndexOutOfBoundsException e){}
	try{
	    for (int i = 0; i < zing.size(); i++)
		{
		    if (zing.get(i).x - x < 32 && zing.get(i).x - x > -10 && zing.get(i).y  - y< 10 && zing.get(i).y - y > -32)
			{
			    zing.remove(i);
			    shielded = true;
			    score += 50;
			}
		}
	}catch(IndexOutOfBoundsException e){}
    }

    public void RUNEVERYTHING()
    {
	moveBullets();
	enemyShoot();
	spawnNewWave();
	moveEnemies();
	spawnBombs();
	moveBombs();
	spawnShields();
	moveShields();
	collisionDetection();
    }

    public void reset()
    {
	enemiesleft = 0;
	wave = 0;
	x = width/2 - 16;
	y = height - 80;
	stillalive = true;
	bullets.clear();
	enemies.clear();
	zap.clear();
	arsenal.clear();
	zing.clear();
	score = 0;
	enemieskilled = 0;
	totalBulletsFired = 0;
	enemieskilledwithbombs = 0;
	bombsused = 0;
	bombs = 1;
    }
}

