import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;

public class LostSpaceship extends JFrame
{
    private static int width = 600;
    private static int height = 700;
    private static long cooldown = 200;


    private boolean passCover = false;
    private boolean shipChooser = true;
    private boolean leftKey = false;
    private boolean rightKey = false;
    private boolean upKey = false;
    private boolean downKey = false;
    private boolean space = false;
    private boolean enter = false;
    private boolean pause = false;
    private boolean restart = false;
    private boolean exit = false;
    private boolean gameend = false;
    private boolean gameendpause = false;
    private boolean passcoverarm = false;
    private boolean shipchoosearm = false;
    private boolean showcredits = false;
    private boolean deployBomb = false;

    private int shipchoice = 0;

    private static long previousShotTime;
    private static long previousWaveTime;
    private static long spawnEnemyTime;

    private static long bombExplosionTime;

    private static long timeSinceLoss;
    private static double elapsedTimeSeconds;

    Image cover;
    Image spaceship, preview;
    Image background;
    Image lazer;
    Image enemylazer;
    Image enemyleft;
    Image enemyright;
    Image explosion;
    Image end;
    Image bomb;
    Image bombexplosion;
    Image shield;
    Image shieldpowerup;
    Ship fighter;

    JPanel drawing;

    public LostSpaceship()
    {
	setTitle("Lost Spaceship");
	setSize(width, height);
	setLocationRelativeTo(null);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setVisible(true);
	setFocusTraversalKeysEnabled(false);
	setFocusable(true);
	setResizable(false);
	requestFocusInWindow();
	addKeyListener(new MyKeyListener());

	timeSinceLoss = 0;

	previousShotTime = System.currentTimeMillis();

	Toolkit tkit = Toolkit.getDefaultToolkit();

	URL imageURL1 = LostSpaceship.class.getResource("data/cover.png");
	cover = tkit.getImage(imageURL1);
	URL imageURL2 = LostSpaceship.class.getResource("data/ship0.png");
	spaceship = tkit.getImage(imageURL2);
	URL imageURL3 = LostSpaceship.class.getResource("data/starfield.gif");
	background = tkit.getImage(imageURL3);
	URL imageURL4 = LostSpaceship.class.getResource("data/lazer.png");
	lazer = tkit.getImage(imageURL4);
	URL imageURL5 = LostSpaceship.class.getResource("data/enemyleft.png");
	enemyleft = tkit.getImage(imageURL5);
	URL imageURL6 = LostSpaceship.class.getResource("data/enemyright.png");
	enemyright = tkit.getImage(imageURL6);
	URL imageURL7 = LostSpaceship.class.getResource("data/enemylazer.png");
	enemylazer = tkit.getImage(imageURL7);
	URL imageURL8 = LostSpaceship.class.getResource("data/explosion.png");
	explosion = tkit.getImage(imageURL8);
	URL imageURL9 = LostSpaceship.class.getResource("data/ship0preview.png");
	preview = tkit.getImage(imageURL9);
	URL imageURL10 = LostSpaceship.class.getResource("data/end.png");
	end = tkit.getImage(imageURL10);
	URL imageURL11 = LostSpaceship.class.getResource("data/bomb.png");
	bomb = tkit.getImage(imageURL11);
	URL imageURL12 = LostSpaceship.class.getResource("data/bombexplosion.png");
	bombexplosion = tkit.getImage(imageURL12);
	URL imageURL13 = LostSpaceship.class.getResource("data/shield.png");
	shield = tkit.getImage(imageURL13);
	URL imageURL14 = LostSpaceship.class.getResource("data/shieldpowerup.png");
	shieldpowerup = tkit.getImage(imageURL14);


	fighter = new Ship(width/2 - 16, height - 80,width, height);



	drawing = new Painting();
	add(drawing);
    }

    public static void main(String[]args)
    {
	LostSpaceship g = new LostSpaceship();
	while(!g.passCover) 
	    {
		g.repaint();
		try{
		    Thread.sleep(20);
		}catch (InterruptedException e){}
	    }

	while (g.shipChooser && g.passCover)
	    {
		if (!g.passcoverarm)
		    {
			try{Thread.sleep(200);}catch(InterruptedException e){}
			g.passcoverarm = true;
		    }
		g.repaint();
		if (g.leftKey) {g.shipchoice+= 2;}
		if (g.rightKey) {g.shipchoice++;}
		if (g.enter && g.shipchoosearm) 
		    {
			g.shipChooser = false;
		    }
		g.shipchoosearm = true;
		try{
		    Thread.sleep(100);
		}catch (InterruptedException e){}
	    }

	while(!g.gameend && g.passCover)
	    {
		if (!g.fighter.stillalive) {g.gameend = true;}
		while (g.pause || !g.fighter.stillalive)
		    {
			g.repaint();
			try{
			    Thread.sleep(20);
			}catch (InterruptedException e){}
		    }
		if (g.fighter.stillalive)
		    {
			if (g.upKey) {g.fighter.moveUp();}
			if (g.downKey) {g.fighter.moveDown();}
			if (g.leftKey) {g.fighter.moveLeft();}
			if (g.rightKey) {g.fighter.moveRight();}
			if (g.deployBomb && g.fighter.bombs > 0 && System.currentTimeMillis() - g.fighter.timeSinceBomb > 500)
			    {
				bombExplosionTime = System.currentTimeMillis();
				g.fighter.bombs--;
				g.fighter.bombsused++;
				g.fighter.zap.clear();
				g.fighter.score += g.fighter.enemies.size() * 20;
				g.fighter.enemieskilledwithbombs += g.fighter.enemies.size();
				g.fighter.enemies.clear();
				g.fighter.enemiesleft = 0;
				g.fighter.timeSinceBomb = System.currentTimeMillis();
			    }
			if (g.space && (System.currentTimeMillis() - previousShotTime > cooldown))
			    {
				previousShotTime = System.currentTimeMillis();
				g.fighter.shootBullet();
			    }
		    }
		g.fighter.RUNEVERYTHING();
		g.repaint();
		elapsedTimeSeconds += 0.02;
		try{
		    Thread.sleep(20);
		}catch (InterruptedException e){}
	    }
    }


    //////////////// DRAWING CLASS ///////////////
    public class Painting extends JPanel
    {

	public void paintComponent(Graphics g)
	{
	    super.paintComponent(g);
	    if (!passCover)
		{
		    g.drawImage(cover,0,0,this);
		}
	    else
		{
		    if (shipChooser)
			{
			    g.drawImage(background,0,0,this);
			    Toolkit tkit = Toolkit.getDefaultToolkit();
			    URL imageURLz = LostSpaceship.class.getResource("data/ship" + (shipchoice % 3) + "preview.png");
			    URL imageURL0 = LostSpaceship.class.getResource("data/ship" + (shipchoice % 3) + ".png");
			    preview = tkit.getImage(imageURLz);
			    spaceship = tkit.getImage(imageURL0);
			    g.drawImage(preview, 240, 275, this);
			    g.setColor(Color.green);
			    g.drawString("Left/Right to scroll through ships", 150, 660);
			    g.drawString("Enter to select ship", 350, 660);
			}
		    else{

			if (restart)
			    {
				restart = false;
				gameend = false;
				gameendpause = false;
				fighter.reset();
				elapsedTimeSeconds = 0;
			    }
			if (gameend)
			    {
				if (!gameendpause)
				    {
					gameendpause = true;
					try{
					    Thread.sleep(1000);
					}catch (InterruptedException e){}
				    }
				g.drawImage(end,0,0,this);
				g.setColor(Color.white);
				Font myFont = new Font("Courier", Font.BOLD, 30);
				g.setFont(myFont);
				g.drawString("GOOD JOB, CADET!", 100, 100);

				String minutes = "" + (int)elapsedTimeSeconds / 60;
				if (minutes.length() == 1) {minutes = "0" + minutes;}
				String seconds = "" + (int)elapsedTimeSeconds % 60;
				if (seconds.length() == 1) {seconds = "0" + seconds;}
				g.setFont(g.getFont().deriveFont(24.0f));
				g.drawString("You survived for " + minutes + ":" + seconds, 100, 130);

				g.setFont(g.getFont().deriveFont(18.0f));
				g.drawString("Score: " + fighter.score, 100, 200);
				g.drawString("Waves completed: " + (fighter.wave - 1), 100, 250);
				g.drawString("Lasers fired: " + fighter.totalBulletsFired, 100, 280);
				g.drawString("Bombs used: " + fighter.bombsused,100,310);
				g.drawString("Laser kills: " + fighter.enemieskilled, 100, 340);
				g.drawString("Bomb kills: " + fighter.enemieskilledwithbombs,100,370);
				int accuracy = (int)(((double)(fighter.enemieskilled)/(double)(fighter.totalBulletsFired)) * 1000);

				g.drawString("Accuracy: " + ((double)(accuracy)/10) + "%", 100, 420);
				int spm = (int)((double)(fighter.score)/(elapsedTimeSeconds/60));
				g.drawString("Score per minute: " + spm, 100, 450);
				g.setFont(g.getFont().deriveFont(14.0f));
				g.drawString("R to restart", 300, 550);
				g.drawString("X to exit", 300, 575);
				if (exit)
				    {
					System.exit(0);
				    }
			    }

			else
			    {

				g.drawImage(background,0,0,this);
				if (fighter.stillalive)
				    {
					g.drawImage(spaceship,fighter.getX(),fighter.getY(),this);
					if (fighter.shielded)
					    {
						g.drawImage(shield, fighter.getX() - 9, fighter.getY() - 3, this);
					    }

					for (int i = 0; i < fighter.bullets.size(); i++)
					    {
						g.drawImage(lazer,(int)fighter.bullets.get(i).x, (int)fighter.bullets.get(i).y, this);
					    }
				    }
				if (!fighter.stillalive)
				    {
					g.drawImage(explosion,fighter.getX() - 35, fighter.getY() - 35, this);
					timeSinceLoss = System.currentTimeMillis();
				    }
				for (int i = 0; i < fighter.enemies.size(); i++)
				    {
					if (fighter.enemies.get(i).dx > 0)
					    {
						g.drawImage(enemyright, fighter.enemies.get(i).x, fighter.enemies.get(i).y, this);
					    }
					else
					    {
						g.drawImage(enemyleft, fighter.enemies.get(i).x, fighter.enemies.get(i).y, this);
					    }
				    }
				for (int i = 0; i < fighter.zap.size(); i++)
				    {
					g.drawImage(enemylazer, (int)fighter.zap.get(i).x, (int)fighter.zap.get(i).y, this);
				    }
				for (int i = 0; i < fighter.arsenal.size(); i++)
				    {
					g.drawImage(bomb, fighter.arsenal.get(i).x, fighter.arsenal.get(i).y, this);
				    }
				for (int i = 0; i < fighter.zing.size(); i++)
				    {
					g.drawImage(shieldpowerup, fighter.zing.get(i).x, fighter.zing.get(i).y, this);
				    }
				g.setColor(Color.green);
				g.drawString("Score: " + fighter.score, 0,height-30);
				g.drawString("Wave " + fighter.wave,240,height-30);
				g.drawString("Bombs: " + fighter.bombs, 330, height-30);
				String minutes = "" + (int)elapsedTimeSeconds / 60;
				if (minutes.length() == 1) {minutes = "0" + minutes;}
				String seconds = "" + (int)elapsedTimeSeconds % 60;
				if (seconds.length() == 1) {seconds = "0" + seconds;}
				g.drawString("Elapsed Time: " + minutes + ":" + seconds, 475, height - 30);
				if (pause)
				    {
					g.setFont(g.getFont().deriveFont(18.0f));
					g.drawString("PAUSED", 265, 325);
				    }
			    }
		    }
       	    
		}
	    if (System.currentTimeMillis() - bombExplosionTime < 1500)
		{
		    g.drawImage(bombexplosion, (int)(Math.random() * 500) + 50, (int)(Math.random() * 500) + 50, this);
		}
	    if (showcredits)
		{
		    g.setColor(Color.green);
		    g.drawString("Created by Patrick Tsai, January 2014", 200, 10);
		}
	}
    }
    ///////////////////////////////////////////////





    //////////////////KEY LISTENER////////////////
    private class MyKeyListener extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
	    case KeyEvent.VK_LEFT:
                leftKey = true;
                break;
	    case KeyEvent.VK_RIGHT:
                rightKey = true;
                break;
	    case KeyEvent.VK_UP:
                upKey = true;
                break;
	    case KeyEvent.VK_DOWN:
                downKey = true;
                break;
	    case KeyEvent.VK_SPACE:
		space = true;
		break;
	    case KeyEvent.VK_P:
		pause = !pause;
		break;
	    case KeyEvent.VK_R:
		restart = true;
		break;
	    case KeyEvent.VK_ENTER:
		passCover = true;
		enter = true;
		break;
	    case KeyEvent.VK_X:
		exit = true;
		break;
	    case KeyEvent.VK_C:
		showcredits = !showcredits;
		break;
	    case KeyEvent.VK_B:
		deployBomb = true;
		break;
            }
        }
        public void keyReleased(KeyEvent e){
            switch (e.getKeyCode()){
	    case KeyEvent.VK_LEFT:
		leftKey = false;
                break;
	    case KeyEvent.VK_RIGHT:
		rightKey = false;
                break;
	    case KeyEvent.VK_UP:
		upKey = false;
		break;
	    case KeyEvent.VK_DOWN:
                downKey = false;
                break;
	    case KeyEvent.VK_SPACE:
		space = false;
		break;
	    case KeyEvent.VK_ENTER:
		enter = false;
		break;
	    case KeyEvent.VK_B:
		deployBomb = false;
		break;
            }
        }
    }
    ////////////////////////////////////////////////


}
